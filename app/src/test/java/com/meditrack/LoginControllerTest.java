package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginControllerTest {

    private final String testEmail = "user@example.com";
    private final String testPassword = "password123";

    @BeforeEach
    void setUp() {
        UserStore.addUser(testEmail, testPassword);
    }

    @Test
    @Order(1)
    void testLoginWithValidCredentials() {
        assertTrue(UserStore.isValidUser(testEmail, testPassword));
    }

    @Test
    @Order(2)
    void testLoginWithInvalidPassword() {
        assertFalse(UserStore.isValidUser(testEmail, "wrongpassword"));
    }

    @Test
    @Order(3)
    void testLoginWithUnknownEmail() {
        assertFalse(UserStore.isValidUser("unknown@example.com", "somepassword"));
    }
}
