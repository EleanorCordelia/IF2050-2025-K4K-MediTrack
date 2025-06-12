package com.meditrack.controller; // Ensure this package declaration matches your folder structure

import com.meditrack.dao.PenggunaDAO; // Import PenggunaDAO
import com.meditrack.model.Pengguna; // Import Pengguna model
import com.meditrack.util.UserSession; // Import UserSession
import javafx.event.ActionEvent; // Import for button click events
import javafx.fxml.FXML; // Import for FXML annotations (linking UI elements)
import javafx.fxml.FXMLLoader; // Import for loading new FXML files for navigation
import javafx.scene.Parent; // Import for the root node of a scene
import javafx.scene.Scene; // Import for representing the content of a window
import javafx.scene.control.Alert; // Import for showing pop-up messages (like success/error)
import javafx.scene.control.PasswordField; // Import for password input field
import javafx.scene.control.TextField; // Import for text input field
import javafx.scene.input.MouseEvent; // Import for mouse click events (for the register link)
import javafx.stage.Stage; // Import for the application window
import java.io.IOException; // Import for handling I/O errors (like FXML loading failures)


public class LoginController {

    @FXML
    private TextField emailField; // Corresponds to fx:id="emailField" in FXML

    @FXML
    private PasswordField passwordField; // Corresponds to fx:id="passwordField" in FXML

    private final PenggunaDAO penggunaDAO = new PenggunaDAO(); // Inisialisasi DAO

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText(); // Get text from the email input field
        String password = passwordField.getText(); // Get text from the password input field

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());

        // --- Database Login Logic ---
        Pengguna user = null;
        try {
            user = penggunaDAO.getPenggunaByEmailAndPassword(email, password);
            System.out.println("Database query completed. User found: " + (user != null));
        } catch (Exception e) {
            System.err.println("Database error during login: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan database: " + e.getMessage());
            return;
        }

        if (user != null) {
            // Set user session with the logged in user's ID
            UserSession.setUserId(user.getId());
            System.out.println("Login successful for user: " + user.getNama() + " (ID: " + user.getId() + ")");
            
            // Direct navigation
            try {
                navigateToMenu(event);
            } catch (Exception e) {
                System.err.println("Navigation failed: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Gagal berpindah ke menu: " + e.getMessage());
            }
        } else {
            System.out.println("Login failed - user not found or incorrect credentials");
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau kata sandi salah. Silakan coba lagi.");
        }
    }

    @FXML
    private void handleLoginGoogleButton(ActionEvent event) {
        // TEMPORARY: Use this for testing navigation bypass
        System.out.println("=== TESTING NAVIGATION BYPASS ===");
        UserSession.setUserId(999); // Test user ID
        try {
            navigateToMenu(event);
        } catch (Exception e) {
            System.err.println("Test navigation failed: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Test Navigation Failed", "Error: " + e.getMessage());
        }
        
        // Original code commented out for testing
        // showAlert(Alert.AlertType.INFORMATION, "Login Google", "Fitur Masuk dengan Google akan diimplementasikan.");
        // TODO: Implementasi integrasi Google OAuth
    }

    @FXML
    private void handleRegisterLink(MouseEvent event) {
        System.out.println("Register FXML path: " + getClass().getResource("/fxml/register.fxml"));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent registerRoot = loader.load();

            Stage stage = (Stage) ((javafx.scene.text.Text) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(registerRoot);
            stage.setScene(scene);
            stage.setTitle("Halaman Registrasi - MediTrack");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat membuka halaman registrasi.");
        }
    }

    private void navigateToMenu(ActionEvent event) throws IOException {
        System.out.println("=== ATTEMPTING MENU NAVIGATION ===");
        System.out.println("User ID in session: " + UserSession.getUserId());
        
        // Use the same pattern as handleRegisterLink - this works
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        Parent menuRoot = loader.load();
        
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(menuRoot);
        stage.setScene(scene);
        stage.setTitle("MediTrack - Menu Utama");
        stage.show();
        
        System.out.println("=== MENU NAVIGATION SUCCESSFUL ===");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }

}
