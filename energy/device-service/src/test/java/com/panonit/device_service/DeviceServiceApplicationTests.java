package com.panonit.device_service;

import com.panonit.device_service.entity.Device;
import com.panonit.device_service.model.DeviceType;
import com.panonit.device_service.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class DeviceServiceApplicationTests {

    private static final int NUMBER_OF_DEVICES = 100;
    private static final int NUMBER_OF_USERS = 10;

    @Autowired
    private DeviceRepository repository;

    @Test
    void contextLoads() {
    }

    @Disabled
    @Test
    void createDevices() {
        log.info("Create devices");

        for (int i = 0; i < NUMBER_OF_DEVICES; i++) {
            var device = new Device(
                    null,
                    "Device " + i,
                    DeviceType.values()[i % DeviceType.values().length],
                    "Location " + ((i % 3) + 1),
                    (long) ((i % NUMBER_OF_USERS) + 1)
            );

            log.info("Created device {}", device.getName());
            repository.save(device);
        }

        log.info("Done");
    }
}
