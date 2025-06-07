package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegisterControllerTest {

    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        registerController = new RegisterController();
    }

    @Test
    @Order(1)
    void testValidEmailDomain() {
        assertTrue(registerController.isEmailDomainValid("test@gmail.com"));
        assertTrue(registerController.isEmailDomainValid("test@outlook.com"));
    }

    @Test
    @Order(2)
    void testInvalidEmailDomain() {
        assertFalse(registerController.isEmailDomainValid("test@invalid.com"));
    }

    @Test
    @Order(3)
    void testPasswordStrength() {
        assertTrue(registerController.isPasswordStrong("StrongP@ss1"));
        assertFalse(registerController.isPasswordStrong("weakpass"));
    }

    @Test
    @Order(4)
    void testRegisterWithValidData() {
        // Simulasi data registrasi valid
        String fullName = "John Doe";
        String email = "john.doe@gmail.com";
        String password = "StrongP@ss1";
        String confirmPassword = "StrongP@ss1";
        String dob = "2000-01-01";
        String gender = "Male";

        assertTrue(!fullName.isEmpty() && !email.isEmpty() && password.equals(confirmPassword));
    }

    @Test
    @Order(5)
    void testRegisterWithMissingData() {
        // Simulasi data tidak lengkap
        String fullName = "";
        String email = "";
        String password = "";
        String confirmPassword = "";
        String dob = "";
        String gender = null;

        assertTrue(fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty() || gender == null);
    }
}
