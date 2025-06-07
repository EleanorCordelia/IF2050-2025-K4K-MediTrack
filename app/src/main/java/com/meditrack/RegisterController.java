package com.meditrack;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
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
    @FXML private TextField usernameField; // optional (belum dipakai di DAO)
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private DatePicker dobPicker;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO(); // DAO untuk connect DB

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
                password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty() || gender == null) {
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

        // Simpan ke database menggunakan PenggunaDAO
        Pengguna newUser = new Pengguna();
        newUser.setNama(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password); // plaintext, dev only! Sebaiknya di-hash.
        newUser.setTanggalLahir(dob);
        newUser.setJenisKelamin(gender);

        boolean success = penggunaDAO.insertPengguna(newUser);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun berhasil dibuat untuk " + fullName + "!");
            navigateToLogin(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Terjadi kesalahan saat menyimpan data pengguna.");
        }
    }

    /**
     * Handler untuk tombol Registrasi dengan Google.
     * Implementasi logika untuk registrasi via Google akan ada di sini.
     * Untuk saat ini, hanya menampilkan pesan placeholder.
     * @param event ActionEvent yang terjadi
     */
    @FXML
    private void handleRegisterGoogle(ActionEvent event) {
        System.out.println("Tombol Registrasi dengan Google diklik!");
        showAlert(Alert.AlertType.INFORMATION, "Fitur Dalam Pengembangan", "Registrasi dengan Google akan segera hadir!");
        // TODO: Implementasikan logika untuk registrasi dengan Google
        // Misalnya, membuka browser untuk autentikasi OAuth, dll.
    }

    public boolean isEmailDomainValid(String email) {
        String[] allowedDomains = {"@gmail.com", "@outlook.com", "@yahoo.com", "@student.itb.ac.id"};
        for (String domain : allowedDomains) {
            if (email.toLowerCase().endsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordStrong(String password) {
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        // Pastikan simbol Anda tidak mengandung karakter khusus regex tanpa di-escape
        boolean hasSymbol = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        return hasUppercase && hasLowercase && hasDigit && hasSymbol;
    }

    @FXML
    private void handleBackToLogin(MouseEvent event) {
        try {
            // Mendapatkan sumber event dan meng-cast ke Node untuk mendapatkan Scene dan Window
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat kembali ke halaman login.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File FXML Tidak Ditemukan", "Pastikan path ke login.fxml sudah benar.");
        }
    }

    private void navigateToLogin(ActionEvent event) {
        try {
            // Mendapatkan sumber event dan meng-cast ke Node untuk mendapatkan Scene dan Window
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat mengarahkan ke halaman login.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File FXML Tidak Ditemukan", "Pastikan path ke login.fxml sudah benar.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Tidak menampilkan header text
        alert.setContentText(message);
        alert.showAndWait();
    }
}
