package com.panonit.user_service;

import com.panonit.user_service.entity.User;
import com.panonit.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Disabled
class UserServiceApplicationTests {

    private static final int NUMBER_OF_USERS = 10;

    @Autowired
    private UserRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    void createUsers() {
        log.info("Creating users");

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            User user = new User(
                    null,
                    "First " + i,
                    "LastName " + i,
                    "firstname" + i + "@example.com",
                    "Laze NAN " + i,
                    i % 2 == 0,
                    1000.0 + i
            );

            log.info("Created user {}", user.getEmail());
            repository.save(user);
        }

        log.info("Done");
    }
}
