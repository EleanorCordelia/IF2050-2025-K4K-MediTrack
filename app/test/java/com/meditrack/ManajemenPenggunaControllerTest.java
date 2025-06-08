package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManajemenPenggunaControllerTest {

    private ManajemenPenggunaController controller;

    @BeforeEach
    void setUp() {
        controller = new ManajemenPenggunaController();
    }

    @Test
    @Order(1)
    void testUpdatePasswordSuccess() {
        int validUserId = 1;
        String newPassword = "NewP@ssw0rd!";
        boolean result = controller.updatePassword(validUserId, newPassword);
        assertTrue(result, "Password harus berhasil diupdate jika id valid.");
    }

    @Test
    @Order(2)
    void testUpdatePasswordFail() {
        int invalidUserId = -1;
        String newPassword = "NewP@ssw0rd!";
        boolean result = controller.updatePassword(invalidUserId, newPassword);
        assertFalse(result, "Password update harus gagal jika id tidak valid.");
    }

    @Test
    @Order(3)
    void testUpdatePasswordNullId() {
        int nullId = 0;  // asumsi id 0 tidak valid
        String newPassword = "AnotherP@ssw0rd!";
        boolean result = controller.updatePassword(nullId, newPassword);
        assertFalse(result, "Password update harus gagal jika id 0/null.");
    }

    @Test
    @Order(4)
    void testUpdateUsernameSuccess() {
        int validUserId = 1;
        String newUsername = "newUsername";
        boolean result = controller.updateUsername(validUserId, newUsername);
        assertTrue(result, "Username harus berhasil diupdate jika id valid.");
    }

    @Test
    @Order(5)
    void testUpdateUsernameFail() {
        int invalidUserId = -1;
        String newUsername = "invalidUsername";
        boolean result = controller.updateUsername(invalidUserId, newUsername);
        assertFalse(result, "Username update harus gagal jika id tidak valid.");
    }

    @Test
    @Order(6)
    void testUpdateUsernameEmptyString() {
        int validUserId = 1;
        String emptyUsername = "";
        boolean result = controller.updateUsername(validUserId, emptyUsername);
        assertFalse(result, "Username update harus gagal jika username kosong.");
    }

}
