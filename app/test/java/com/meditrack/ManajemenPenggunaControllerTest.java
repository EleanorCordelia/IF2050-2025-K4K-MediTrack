package com.meditrack;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import com.meditrack.util.SQLiteConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManajemenPenggunaControllerTest {

    private ManajemenPenggunaController controller;
    private PenggunaDAO penggunaDAO;
    private static final int TEST_USER_ID = 1;

    @BeforeEach
    void setUp() {
        controller = new ManajemenPenggunaController();
        penggunaDAO = new PenggunaDAO();

        // Ensure test user exists with known data
        setupTestUser();
    }

    @AfterEach
    void tearDown() {
        // Optional: Clean up test data if needed
        // This ensures each test starts with a clean state
    }

    private void setupTestUser() {
        try (Connection conn = SQLiteConnection.getConnection()) {
            // First, delete existing test user if exists
            String deleteSQL = "DELETE FROM pengguna WHERE idPengguna = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, TEST_USER_ID);
                deleteStmt.executeUpdate();
            }

            // Insert test user
            String insertSQL = "INSERT INTO pengguna (idPengguna, nama, email, password, tanggalLahir, jenisKelamin, tinggiBadan, beratBadan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                insertStmt.setInt(1, TEST_USER_ID);
                insertStmt.setString(2, "TestUser");
                insertStmt.setString(3, "test@example.com");
                insertStmt.setString(4, "Test@123");
                insertStmt.setString(5, "2000-01-01");
                insertStmt.setString(6, "Laki-laki");
                insertStmt.setDouble(7, 170.0);
                insertStmt.setDouble(8, 60.0);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Failed to setup test user: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Test
    @Order(1)
    void testUpdatePasswordFail() {
        int invalidPenggunaId = 999; // Use a high number that definitely doesn't exist
        String newPassword = "NewP@ssw0rd!";

        boolean result = controller.updatePassword(invalidPenggunaId, newPassword);

        assertFalse(result, "Password update harus gagal jika id tidak valid.");
    }

    @Test
    @Order(2)
    void testUpdatePasswordNullId() {
        int nullId = 0;
        String newPassword = "AnotherP@ssw0rd!";

        boolean result = controller.updatePassword(nullId, newPassword);

        assertFalse(result, "Password update harus gagal jika id 0/null.");
    }

    @Test
    @Order(3)
    void testUpdatePasswordEmptyPassword() {
        int validPenggunaId = TEST_USER_ID;
        String emptyPassword = "";

        boolean result = controller.updatePassword(validPenggunaId, emptyPassword);

        assertFalse(result, "Password update harus gagal jika password kosong.");
    }

    @Test
    @Order(4)
    void testUpdatePasswordNullPassword() {
        int validPenggunaId = TEST_USER_ID;
        String nullPassword = null;

        boolean result = controller.updatePassword(validPenggunaId, nullPassword);

        assertFalse(result, "Password update harus gagal jika password null.");
    }


    @Test
    @Order(5)
    void testUpdateUsernameFail() {
        int invalidPenggunaId = 999; // Use a high number that definitely doesn't exist
        String newUsername = "invalidUsername";

        boolean result = controller.updateUsername(invalidPenggunaId, newUsername);

        assertFalse(result, "Username update harus gagal jika id tidak valid.");
    }

    @Test
    @Order(6)
    void testUpdateUsernameEmptyString() {
        int validPenggunaId = TEST_USER_ID;
        String emptyUsername = "";

        boolean result = controller.updateUsername(validPenggunaId, emptyUsername);

        assertFalse(result, "Username update harus gagal jika username kosong.");
    }

    @Test
    @Order(7)
    void testUpdateUsernameNullString() {
        int validPenggunaId = TEST_USER_ID;
        String nullUsername = null;

        boolean result = controller.updateUsername(validPenggunaId, nullUsername);

        assertFalse(result, "Username update harus gagal jika username null.");
    }

    @Test
    @Order(8)
    void testUpdateUsernameWhitespaceOnly() {
        int validPenggunaId = TEST_USER_ID;
        String whitespaceUsername = "   ";

        boolean result = controller.updateUsername(validPenggunaId, whitespaceUsername);

        assertFalse(result, "Username update harus gagal jika username hanya whitespace.");
    }
}