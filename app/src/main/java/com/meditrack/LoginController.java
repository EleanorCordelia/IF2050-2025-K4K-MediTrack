package com.meditrack; // Ensure this package declaration matches your folder structure

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
import java.util.Objects; // Import for utility methods like Objects.requireNonNull



public class LoginController {

    // These fields are linked to the fx:id attributes in your login.fxml
    // @FXML annotation tells JavaFX to inject the corresponding UI component from FXML
    @FXML
    private TextField emailField; // Corresponds to fx:id="emailField" in FXML

    @FXML
    private PasswordField passwordField; // Corresponds to fx:id="passwordField" in FXML

    // Note: loginButton doesn't strictly need fx:id in the controller
    // if its only purpose is to trigger an onAction method,
    // but having it can be useful for later manipulation if needed.
    // @FXML
    // private Button loginButton;

    // This method is called when the "Masuk" button (loginButton) is clicked
    // The onAction="#handleLoginButton" in FXML points to this method
    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText(); // Get text from the email input field
        String password = passwordField.getText(); // Get text from the password input field

        // --- Simple Login Logic (for demonstration) ---
        // In a real application, you would connect to a database or authentication service here
        if (UserStore.isValidUser(email, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + email + "!");
            // TODO: Arahkan ke Dashboard atau halaman utama setelah login berhasil
            // Example: navigateToDashboard(event); // Uncomment and implement this when dashboard is ready
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau kata sandi salah. Silakan coba lagi.");
        }
    }

    // This method is called when the "Masuk dengan Google" button (loginGoogleButton) is clicked
    // The onAction="#handleLoginGoogleButton" in FXML points to this method
    @FXML
    private void handleLoginGoogleButton(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Login Google", "Fitur Masuk dengan Google akan diimplementasikan.");
        // TODO: Implementasi integrasi Google OAuth
    }

    @FXML
    private void handleRegisterLink(MouseEvent event) {
        System.out.println("Register FXML path: " + getClass().getResource("/fxml/register.fxml"));

        try {
            // Load the FXML file for the registration page
            // Parent registerRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/register.fxml")));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent registerRoot = loader.load();

            // Get the current window (stage) from the event source
            Stage stage = (Stage) ((javafx.scene.text.Text) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(registerRoot);
            stage.setScene(scene);
            stage.setTitle("Halaman Registrasi - MediTrack");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat membuka halaman registrasi.");
        }

    }

    // Helper method to display a simple pop-up alert message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message); // The actual message content
        alert.showAndWait(); // Display the alert and wait for user interaction
    }
    /*
    // --- Example Navigation Methods (uncomment and modify as needed) -
    // These methods show how to load a new FXML scene and set it on the current stage (window)

    private void navigateToDashboard(ActionEvent event) {
        try {
            // Load the FXML file for the dashboard
            Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/dashboard.fxml")));
            // Get the current window (stage) from the event source (e.g., the button that was clicked)
            Stage stage = (Stage)((javafx.scene.control.Button)event.getSource()).getScene().getWindow();
            // Create a new scene with the dashboard content
            Scene scene = new Scene(dashboardRoot);
            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show(); // Show the updated window
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if FXML loading fails
            showAlert(Alert.AlertType.ERROR, "Navigasi Gagal", "Tidak dapat memuat halaman dashboard.");
        }
    }

    private void navigateToRegister(MouseEvent event) {
        try {
            // Load the FXML file for the registration page
            Parent registerRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/register.fxml")));
            // Get the current window (stage) from the event source (e.g., the Text element that was clicked)
            Stage stage = (Stage)((javafx.scene.text.Text)event.getSource()).getScene().getWindow();
            // Create a new scene with the registration content
            Scene scene = new Scene(registerRoot);
            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show(); // Show the updated window
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigasi Gagal", "Tidak dapat memuat halaman registrasi.");
        }
    }
    */
}