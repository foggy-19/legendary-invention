package com.panonit.user_service.integration;

import com.panonit.user_service.dto.*;
import com.panonit.user_service.entity.User;
import com.panonit.user_service.repository.UserRepository;
import com.panonit.user_service.utils.MySqlTestcontainersBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class UserServiceIntegrationTest extends MySqlTestcontainersBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_viaRestApi_persistsAndReturnsUser() {
        // Create
        CreateUserRequestDto request = new CreateUserRequestDto("firstname", "lastname", "test@mail.com", "address 123", true, 1000.0);
        ResponseEntity<CreateUserResponseDto> createUserResponse = restTemplate.postForEntity("/api/v1/user", request, CreateUserResponseDto.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUserResponse.getBody()).isNotNull();
        assertThat(createUserResponse.getBody().id()).isNotNull();
        assertThat(createUserResponse.getBody().firstName()).isEqualTo(request.firstName());
        assertThat(createUserResponse.getBody().lastName()).isEqualTo(request.lastName());
        assertThat(createUserResponse.getBody().email()).isEqualTo(request.email());
        assertThat(createUserResponse.getBody().address()).isEqualTo(request.address());
        assertThat(createUserResponse.getBody().notifications()).isEqualTo(request.notifications());
        assertThat(createUserResponse.getBody().energyAlertingThreshold()).isEqualTo(request.energyAlertingThreshold());

        // Get
        ResponseEntity<GetUserResponseDto> getUserResponse = restTemplate.getForEntity("/api/v1/user/" + createUserResponse.getBody().id(), GetUserResponseDto.class);
        assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUserResponse.getBody()).isNotNull();
        assertThat(getUserResponse.getBody().id()).isEqualTo(createUserResponse.getBody().id());
        assertThat(getUserResponse.getBody().firstName()).isEqualTo(request.firstName());
        assertThat(getUserResponse.getBody().lastName()).isEqualTo(request.lastName());
        assertThat(getUserResponse.getBody().email()).isEqualTo(request.email());
        assertThat(getUserResponse.getBody().address()).isEqualTo(request.address());
        assertThat(getUserResponse.getBody().notifications()).isEqualTo(request.notifications());
        assertThat(getUserResponse.getBody().energyAlertingThreshold()).isEqualTo(request.energyAlertingThreshold());
    }

    @Test
    void saveUser_viaRepository_roundTripsThroughMySql() {
        // Save
        User user = new User(null, "Grace", "Hooper", "grace.it@example.com", "Compiler Way 2", false, 300.0);
        var saved = userRepository.save(user);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();

        // Load
        var retrieved = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getId()).isEqualTo(saved.getId());
        assertThat(retrieved.getFirstName()).isEqualTo(saved.getFirstName());
        assertThat(retrieved.getLastName()).isEqualTo(saved.getLastName());
        assertThat(retrieved.getEmail()).isEqualTo(saved.getEmail());
        assertThat(retrieved.getAddress()).isEqualTo(saved.getAddress());
        assertThat(retrieved.getAlerting()).isEqualTo(saved.getAlerting());
        assertThat(retrieved.getEnergyAlertingThreshold()).isEqualTo(saved.getEnergyAlertingThreshold());
    }

    @Test
    void updateUser_viaRestApi_persistsChanges() {
        // Create
        CreateUserRequestDto createRequest = new CreateUserRequestDto("Alan", "Turing", "alan.update.it@example.com", "Bletchley Park 10", true, 500.0);
        ResponseEntity<CreateUserResponseDto> createResponse = restTemplate.postForEntity("/api/v1/user", createRequest, CreateUserResponseDto.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().id()).isNotNull();

        // Update
        Long id = createResponse.getBody().id();
        UpdateUserRequestDto updateRequest = new UpdateUserRequestDto("Alan Mathison", "Turing", "alan.update.it@example.com", "Bletchley Park 19", false, 750.0);
        ResponseEntity<UpdateUserResponseDto> updateResponse = restTemplate.exchange("/api/v1/user/" + id, HttpMethod.PUT, new HttpEntity<>(updateRequest), UpdateUserResponseDto.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().id()).isEqualTo(id);
        assertThat(updateResponse.getBody().firstName()).isEqualTo(updateRequest.firstName());
        assertThat(updateResponse.getBody().lastName()).isEqualTo(updateRequest.lastName());
        assertThat(updateResponse.getBody().email()).isEqualTo(updateRequest.email());
        assertThat(updateResponse.getBody().address()).isEqualTo(updateRequest.address());
        assertThat(updateResponse.getBody().notifications()).isEqualTo(updateRequest.notifications());
        assertThat(updateResponse.getBody().energyAlertingThreshold()).isEqualTo(updateRequest.energyAlertingThreshold());

        // Get
        ResponseEntity<GetUserResponseDto> getResponse = restTemplate.getForEntity("/api/v1/user/" + id, GetUserResponseDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().id()).isEqualTo(updateResponse.getBody().id());
        assertThat(getResponse.getBody().firstName()).isEqualTo(updateResponse.getBody().firstName());
        assertThat(getResponse.getBody().lastName()).isEqualTo(updateResponse.getBody().lastName());
        assertThat(getResponse.getBody().email()).isEqualTo(updateResponse.getBody().email());
        assertThat(getResponse.getBody().address()).isEqualTo(updateResponse.getBody().address());
        assertThat(getResponse.getBody().notifications()).isEqualTo(updateResponse.getBody().notifications());
        assertThat(getResponse.getBody().energyAlertingThreshold()).isEqualTo(updateResponse.getBody().energyAlertingThreshold());
    }

    @Test
    void deleteUser_viaRestApi_removesUser() {
        // Create
        CreateUserRequestDto createRequest = new CreateUserRequestDto("Edsger", "Dijkstra", "edsger.delete.it@example.com", "Structured Programming 3", false, 300.0);
        ResponseEntity<CreateUserResponseDto> createResponse = restTemplate.postForEntity("/api/v1/user", createRequest, CreateUserResponseDto.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().id()).isNotNull();

        // Delete
        Long id = createResponse.getBody().id();
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/v1/user/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Get
        ResponseEntity<GetUserResponseDto> getResponse = restTemplate.getForEntity("/api/v1/user/" + id, GetUserResponseDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

