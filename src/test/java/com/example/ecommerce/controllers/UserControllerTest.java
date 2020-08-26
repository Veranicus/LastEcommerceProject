package com.example.ecommerce.controllers;


import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {


    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() throws Exception {
//        When is stub
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    void findById() {
    }

    @Test
    void findByUserName() {
    }

    @Test
    void createUser() {
    }
}