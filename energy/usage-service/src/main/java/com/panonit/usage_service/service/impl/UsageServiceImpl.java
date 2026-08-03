package com.panonit.usage_service.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.dto.DeviceDto;
import com.panonit.usage_service.model.DeviceEnergy;
import com.panonit.usage_service.service.DeviceService;
import com.panonit.usage_service.service.UsageService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsageServiceImpl implements UsageService {

    private final static String MEASUREMENT = "energy-usage";
    private final static String TAG = "device-id";
    private final static String FIELD = "energy-consumed";

    private final String bucket;
    private final String organization;

    private final InfluxDBClient influx;
    private final DeviceService service;

    public UsageServiceImpl(
            @Value("${influx.bucket}") String bucket,
            @Value("${influx.org}") String organization,
            InfluxDBClient influx,
            DeviceService service
    ) {
        this.bucket = bucket;
        this.organization = organization;
        this.influx = influx;
        this.service = service;
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
        log.info("aggregateDeviceEnergyUsage");

        final Instant stop = Instant.now();
        final Instant start = stop.minusSeconds(3600);

        List<FluxTable> tables = influx.getQueryApi().query(getQuery(start, stop), organization);
        Map<Long, List<DeviceEnergy>> userEnergyUsageMap = getUserEnergyUsageMap(tables);

        log.info("{} energy map aggregated", userEnergyUsageMap);
    }

    private @NonNull String getQuery(Instant start, Instant stop) {
        log.info("getQuery");

        return String.format("""
                from(bucket: "%s")
                  |> range(start: time(v: "%s"), stop: time(v: "%s"))
                  |> filter(fn: (r) => r["_measurement"] == "%s")
                  |> filter(fn: (r) => r["_field"] == "%s")
                  |> group(columns: ["%s"])
                  |> sum(column: "_value")
                """, bucket, start, stop, MEASUREMENT, FIELD, TAG);
    }

    private @NonNull Map<Long, List<DeviceEnergy>> getUserEnergyUsageMap(List<FluxTable> tables) {
        List<DeviceEnergy> array = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Long deviceId = Long.valueOf(record.getValueByKey(TAG).toString());
                Double energyConsumed = record.getValueByKey("_value") instanceof Number ? ((Number) record.getValueByKey("_value")).doubleValue() : 0.0;
                DeviceDto dto = service.getDeviceById(deviceId);

                if (dto == null || dto.userId() == null) {
                    log.warn("Device with ID {} not found", deviceId);
                    continue;
                }

                array.add(new DeviceEnergy(dto.userId(), deviceId, energyConsumed));
            }
        }

        return array.stream().collect(Collectors.groupingBy(DeviceEnergy::userId));
    }
}
