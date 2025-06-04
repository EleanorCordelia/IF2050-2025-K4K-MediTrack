package com.meditrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private DatePicker dobPicker;

    // Handle Registrasi button click
    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";
        String gender = genderComboBox.getValue();

        // Basic validation
        if (fullName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Belum Lengkap", "Silakan lengkapi semua kolom.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Kata Sandi Tidak Cocok", "Pastikan kata sandi dan konfirmasi cocok.");
            return;
        }

        if (!isEmailDomainValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Tidak Valid", "Hanya alamat email dengan domain tertentu yang diperbolehkan (mis. @gmail.com, @outlook.com).");
            return;
        }

        if (!isPasswordStrong(password)) {
            showAlert(Alert.AlertType.ERROR, "Kata Sandi Lemah",
                    "Kata sandi harus mengandung setidaknya 1 huruf besar, 1 huruf kecil, 1 angka, dan 1 simbol.");
            return;
        }


        // You could later save this user to DB or memory
        showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun berhasil dibuat untuk " + fullName + "!");
        UserStore.addUser(email, password);


        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat mengarahkan ke halaman login.");
        }

    }

    // Method to validate allowed email domains
    private boolean isEmailDomainValid(String email) {
        String[] allowedDomains = {"@gmail.com", "@outlook.com", "@yahoo.com", "@student.itb.ac.id"};
        for (String domain : allowedDomains) {
            if (email.toLowerCase().endsWith(domain)) {
                return true;
            }
        }
        return false;
    }
    // Method to ensure password is strong
    private boolean isPasswordStrong(String password) {
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSymbol = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        return hasUppercase && hasLowercase && hasDigit && hasSymbol;
    }


    // Handle "Masuk di sini" link click
    @FXML
    private void handleBackToLogin(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
            Stage stage = (Stage) ((javafx.scene.text.Text) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat kembali ke halaman login.");
        }
    }

    // Utility method for showing alert popups
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}