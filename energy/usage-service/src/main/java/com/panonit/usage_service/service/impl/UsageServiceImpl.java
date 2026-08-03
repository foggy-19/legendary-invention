package com.panonit.usage_service.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.panonit.kafka.event.EnergyUsageEvent;
import com.panonit.usage_service.service.UsageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {

    private final static String MEASUREMENT = "energy-usage";
    private final static String TAG = "device-id";
    private final static String FIELD = "energy-consumed";

    private final String org;
    private final String bucket;
    private final InfluxDBClient influx;

    public UsageServiceImpl(
            @Value("${influx.org}") String org,
            @Value("${influx.bucket}") String bucket,
            InfluxDBClient influx
    ) {
        this.org = org;
        this.bucket = bucket;
        this.influx = influx;
    }

    @Override
    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void onEnergyUsageEvent(EnergyUsageEvent event) {
        Point point = Point.measurement(MEASUREMENT)
                .addTag(TAG, event.deviceId().toString())
                .addField(FIELD, event.consumption())
                .time(event.timestamp(), WritePrecision.MS);

        influx.getWriteApiBlocking().writePoint(bucket, org, point);
    }
}
