package com.panonit.usage_service.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.panonit.kafka.event.AlertingEvent;
import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.dto.DeviceDto;
import com.panonit.usage_service.dto.UserDto;
import com.panonit.usage_service.model.DeviceEnergy;
import com.panonit.usage_service.service.AlertingService;
import com.panonit.usage_service.service.DeviceService;
import com.panonit.usage_service.service.UsageService;
import com.panonit.usage_service.service.UserService;
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

@Service
public class UsageServiceImpl implements UsageService {

    private static final long AGGREGATION_WINDOW_SECONDS = 3600;

    private final static String MEASUREMENT = "energy-usage";
    private final static String TAG = "device-id";
    private final static String FIELD = "energy-consumed";
    private static final String VALUE_KEY = "_value";

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
    public void aggregateAndAlert() {
        var aggregate = queryAndAggregateEnergy(Instant.now());
        var alerts = getAlertingEvents(aggregate);

        alertingService.publish(alerts);
    }

    private List<AlertingEvent> getAlertingEvents(Map<Long, Double> userEnergyUsageMap) {
        return userEnergyUsageMap.keySet().stream()
                .map(userService::getUserById)
                .filter(user -> user != null && user.notifications())
                .filter(user -> userEnergyUsageMap.get(user.id()) >= user.energyAlertingThreshold())
                .map(user -> toAlertingEvent(user, userEnergyUsageMap.get(user.id())))
                .toList();
    }

    private Map<Long, Double> queryAndAggregateEnergy(Instant timestamp) {
        Instant start = timestamp.minusSeconds(AGGREGATION_WINDOW_SECONDS);
        List<FluxTable> tables = influx.getQueryApi().query(getQuery(start, timestamp), organization);
        return getUserTotalEnergyUsage(tables);
    }

    private @NonNull Map<Long, Double> getUserTotalEnergyUsage(List<FluxTable> tables) {
        Map<Long, Double> map = new HashMap<>();

        var grouping = tables.stream()
                .flatMap(table -> table.getRecords().stream())
                .map(this::toDeviceEnergy)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(DeviceEnergy::userId));

        grouping.forEach((key, value) -> {
            Double total = value.stream().mapToDouble(DeviceEnergy::energyConsumed).sum();
            map.put(key, total);
        });

        return map;
    }

    private AlertingEvent toAlertingEvent(UserDto user, Double total) {
        return new AlertingEvent(
                user.id(),
                user.email(),
                user.energyAlertingThreshold(),
                total,
                "Energy consumption threshold exceeded!"
        );
    }

    private DeviceEnergy toDeviceEnergy(FluxRecord record) {
        Long deviceId = Long.valueOf(record.getValueByKey(TAG).toString());
        Double energyConsumed = extractEnergyValue(record.getValueByKey(VALUE_KEY));
        DeviceDto dto = deviceService.getDeviceById(deviceId);

        return (dto != null && dto.userId() != null) ? new DeviceEnergy(dto.userId(), deviceId, energyConsumed) : null;
    }

    private Double extractEnergyValue(Object value) {
        return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
    }

    private @NonNull String getQuery(Instant start, Instant stop) {
        return String.format("""
                from(bucket: "%s")
                  |> range(start: time(v: "%s"), stop: time(v: "%s"))
                  |> filter(fn: (r) => r["_measurement"] == "%s")
                  |> filter(fn: (r) => r["_field"] == "%s")
                  |> group(columns: ["%s"])
                  |> sum(column: "_value")
                """, bucket, start, stop, MEASUREMENT, FIELD, TAG);
    }
}
