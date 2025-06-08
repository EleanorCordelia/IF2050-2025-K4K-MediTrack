package com.meditrack;  // pastikan ini sesuai struktur paketmu

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import com.meditrack.util.Session;               // ✏️ import Session helper
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Pengguna user = penggunaDAO.getPenggunaByEmailAndPassword(email, password);
        if (user != null) {
            // ✏️ simpan user ke session
            Session.setCurrentUser(user);

            showAlert(Alert.AlertType.INFORMATION, "Login Berhasil",
                    "Selamat datang, " + user.getNama() + "!");

            // ✏️ navigasi ke halaman “Data Aktual”
            try {
                Parent root = FXMLLoader.load(
                        getClass().getResource("/fxml/kondisiAktual.fxml")
                );
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                        .getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("MediTrack - Data Aktual");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal",
                        "Tidak dapat membuka halaman utama.");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal",
                    "Email atau kata sandi salah. Silakan coba lagi.");
        }
    }

    @FXML
    private void handleLoginGoogleButton(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION,
                "Login Google", "Fitur Masuk dengan Google akan diimplementasikan.");
    }

    @FXML
    private void handleRegisterLink(MouseEvent event) {
        try {
            Parent registerRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/register.fxml")
            );
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
            stage.setTitle("Halaman Registrasi - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal",
                    "Tidak dapat membuka halaman registrasi.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}