package com.panonit.usage_service.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.panonit.kafka.event.AlertingEvent;
import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.dto.DeviceDto;
import com.panonit.usage_service.dto.UsageDto;
import com.panonit.usage_service.model.Device;
import com.panonit.usage_service.model.DeviceEnergy;
import com.panonit.usage_service.service.AlertingService;
import com.panonit.usage_service.service.DeviceService;
import com.panonit.usage_service.service.UsageService;
import com.panonit.usage_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsageServiceImpl implements UsageService {

    private static final String TAG = "device-id";
    private static final String FIELD = "energy-consumed";
    private static final String MEASUREMENT = "energy-usage";
    private static final String VALUE = "_value";

    private static final int HOUR_SECONDS = 3600;

    private final String bucket;
    private final String organization;
    private final InfluxDBClient influx;
    private final UserService userService;
    private final DeviceService deviceService;
    private final AlertingService alertingService;

    public UsageServiceImpl(
            @Value("${influx.bucket}") String bucket,
            @Value("${influx.org}") String organization,
            InfluxDBClient influx,
            UserService userService,
            DeviceService deviceService,
            AlertingService alertingService
    ) {
        this.bucket = bucket;
        this.organization = organization;
        this.influx = influx;
        this.userService = userService;
        this.deviceService = deviceService;
        this.alertingService = alertingService;
    }

    @Override
    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void onEnergyUsageEvent(EnergyUsageEvent event) {
        Point point = Point.measurement(MEASUREMENT)
                .addTag(TAG, event.deviceId().toString())
                .addField(FIELD, event.consumption())
                .time(event.timestamp(), WritePrecision.MS);

        influx.getWriteApiBlocking().writePoint(bucket, organization, point);
    }

    @Scheduled(cron = "0 * * * * *")
    public void aggregateDeviceEnergyUsage() {
        var deviceEnergies = enrichWithUserIds(queryDeviceEnergyForHour());

        var userEnergyMap = deviceEnergies.stream()
                .collect(Collectors.groupingBy(DeviceEnergy::getUserId, Collectors.summingDouble(DeviceEnergy::getEnergyConsumed)));

        checkAndPublishAlerts(userEnergyMap);
    }

    private List<DeviceEnergy> queryDeviceEnergyForHour() {
        var fluxQuery = getFluxQuery();

        return influx.getQueryApi().query(fluxQuery, organization).stream()
                .flatMap(table -> table.getRecords().stream())
                .map(this::toDeviceEnergy)
                .filter(Objects::nonNull)
                .toList();
    }

    private DeviceEnergy toDeviceEnergy(FluxRecord record) {
        var deviceIdStr = (String) record.getValueByKey(TAG);
        if (deviceIdStr == null) {
            return null;
        }

        var energyConsumed = extractDouble(record.getValueByKey(VALUE));

        return DeviceEnergy.builder()
                .deviceId(Long.valueOf(deviceIdStr))
                .energyConsumed(energyConsumed)
                .build();
    }

    private List<DeviceEnergy> enrichWithUserIds(List<DeviceEnergy> deviceEnergies) {
        deviceEnergies.forEach(de -> {
            try {
                var device = deviceService.getDeviceById(de.getDeviceId());
                if (device != null && device.userId() != null) {
                    de.setUserId(device.userId());
                }
            } catch (Exception e) {
                log.error("Failed to fetch device {}: {}", de.getDeviceId(), e.getMessage());
            }
        });

        return deviceEnergies.stream().filter(de -> de.getUserId() != null).toList();
    }

    private void checkAndPublishAlerts(Map<Long, Double> userEnergyMap) {
        userEnergyMap.forEach((userId, totalConsumption) -> {
            try {
                var user = userService.getUserById(userId);
                if (user == null || user.id() == null || !user.notifications()) {
                    return;
                }

                var threshold = user.energyAlertingThreshold();
                if (totalConsumption > threshold) {
                    alertingService.publish(new AlertingEvent(userId, user.email(), threshold, totalConsumption, "Energy consumption threshold exceeded!"));
                }
            } catch (Exception e) {
                log.error("Failed to user {}: {}", userId, e.getMessage());
            }
        });
    }


    @Override
    public UsageDto getXDaysUsageForUser(Long userId, int days) {
        var devices = deviceService.getAllDevicesForUser(userId).stream()
                .map(this::toDevice)
                .toList();

        if (devices.isEmpty()) {
            return UsageDto.builder()
                    .userId(userId)
                    .devices(null)
                    .build();
        }

        try {
            var energyMap = queryDeviceEnergyForDays(devices, days);
            var resultDevices = devices.stream()
                    .peek(d -> d.setEnergyConsumed(energyMap.getOrDefault(d.getId(), 0.0)))
                    .map(this::toDeviceDto)
                    .toList();

            log.info("Aggregated energy consumption for userId {}: {}", userId, energyMap);
            return UsageDto.builder()
                    .userId(userId)
                    .devices(resultDevices)
                    .build();
        } catch (Exception e) {
            return UsageDto.builder()
                    .userId(userId)
                    .devices(null)
                    .build();
        }
    }

    private Device toDevice(DeviceDto dto) {
        return Device.builder()
                .id(dto.id())
                .name(dto.name())
                .type(dto.type())
                .location(dto.location())
                .userId(dto.userId())
                .build();
    }

    private DeviceDto toDeviceDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .name(device.getName())
                .type(device.getType())
                .location(device.getLocation())
                .userId(device.getUserId())
                .energyConsumed(device.getEnergyConsumed())
                .build();
    }

    private Map<Long, Double> queryDeviceEnergyForDays(List<Device> devices, int days) {
        var fluxQuery = getFluxQuery(days, getDeviceFilter(devices));

        var aggregatedMap = new HashMap<Long, Double>();
        influx.getQueryApi().query(fluxQuery, organization).stream()
                .flatMap(table -> table.getRecords().stream())
                .forEach(record -> {
                    try {
                        var deviceIdStr = record.getValueByKey(TAG);
                        if (deviceIdStr != null) {
                            var deviceId = Long.parseLong(deviceIdStr.toString());
                            var energy = extractDouble(record.getValueByKey(VALUE));
                            aggregatedMap.merge(deviceId, energy, Double::sum);
                        }
                    } catch (NumberFormatException e) {
                        log.error("Failed to parse deviceId from flux record: {}", record.getValueByKey(TAG));
                    }
                });

        return aggregatedMap;
    }

    private @NonNull String getFluxQuery() {
        var end = Instant.now();
        var start = end.minusSeconds(HOUR_SECONDS);

        return String.format("""
                from(bucket: "%s")
                  |> range(start: time(v: "%s"), stop: time(v: "%s"))
                  |> filter(fn: (r) => r["_measurement"] == "%s")
                  |> filter(fn: (r) => r["_field"] == "%s")
                  |> group(columns: ["%s"])
                  |> sum(column: "%s")
                """, bucket, start, end, MEASUREMENT, FIELD, TAG, VALUE);
    }

    private @NonNull String getFluxQuery(long days, String deviceIdFilter) {
        val end = Instant.now();
        var start = end.minusSeconds(days * 24 * HOUR_SECONDS);

        return String.format("""
                from(bucket: "%s")
                  |> range(start: time(v: "%s"), stop: time(v: "%s"))
                  |> filter(fn: (r) => r["_measurement"] == "%s")
                  |> filter(fn: (r) => r["_field"] == "%s")
                  |> filter(fn: (r) => %s)
                  |> group(columns: ["%s"])
                  |> sum(column: "%s")
                """, bucket, start, end, MEASUREMENT, FIELD, deviceIdFilter, TAG, VALUE);
    }

    private String getDeviceFilter(List<Device> devices) {
        return devices.stream()
                .map(Device::getId)
                .filter(Objects::nonNull)
                .map(id -> String.format("r[\"%s\"] == \"%s\"", TAG, id))
                .collect(Collectors.joining(" or "));
    }

    private Double extractDouble(Object value) {
        return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
    }
}
