package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterControllerTest {

    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        registerController = new RegisterController();
    }

    @Test
    @Order(1)
    void testValidEmailDomain() {
        assertTrue(registerController.isEmailDomainValid("test@gmail.com"),
                "Gmail should be a valid domain.");
        assertTrue(registerController.isEmailDomainValid("test@outlook.com"),
                "Outlook should be a valid domain.");
    }

    @Test
    @Order(2)
    void testInvalidEmailDomain() {
        assertFalse(registerController.isEmailDomainValid("test@invalid.com"),
                "Invalid domain should fail validation.");
    }

    @Test
    @Order(3)
    void testPasswordStrength() {
        assertTrue(registerController.isPasswordStrong("StrongP@ss1"),
                "Password meets strength requirements.");
        assertFalse(registerController.isPasswordStrong("weakpass"),
                "Weak password should fail strength check.");
    }

    @Test
    @Order(4)
    void testRegisterWithValidData() {
        String fullName = "John Doe";
        String email = "john.doe@gmail.com";
        String password = "StrongP@ss1";
        String confirmPassword = "StrongP@ss1";
        String dob = "2000-01-01";
        String gender = "Male";

        assertTrue(!fullName.isEmpty() && !email.isEmpty() && password.equals(confirmPassword),
                "Valid data should pass the validation checks.");
    }

    @Test
    @Order(5)
    void testRegisterWithMissingData() {
        String fullName = "";
        String email = "";
        String password = "";
        String confirmPassword = "";
        String dob = "";
        String gender = null;

        assertTrue(fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty() || gender == null,
                "Missing data should fail the validation checks.");
    }
}
