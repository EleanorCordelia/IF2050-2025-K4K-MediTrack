package com.meditrack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PenggunaController implements Initializable {

    // FXML elements from UI for Information Account section (Labels)
    @FXML private ImageView profileAvatar;
    @FXML private Label profileName;
    @FXML private Label profileAlteress;

    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label dobLabel;
    @FXML private Label genderLabel;

    // FXML elements for buttons
    @FXML private Button editProfileButton;
    @FXML private Button changePasswordButton;

    // FXML elements for Preferensi section
    @FXML private ChoiceBox<String> languageChoiceBox;
    @FXML private ToggleButton themeToggleButton;

    // FXML elements for Kelola Akun section
    @FXML private Button deactivateAccountButton;
    @FXML private Button deleteAccountButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
        setupListeners();
        setupPreferensiControls();
    }

    private void loadUserData() {
        profileName.setText("Carlos Fernando");
        profileAlteress.setText("Alteress");

        fullNameLabel.setText("Carlos Fernando");
        emailLabel.setText("carlos@email.com");
        phoneLabel.setText("+62 812-3456-7990");
        dobLabel.setText("10 April 1990");
        genderLabel.setText("Laki-laki");

        // Set user avatar image - Menggunakan carlos_fernando.jpg
        try {
            profileAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/carlos_fernando.jpg"))));
            Circle clip = new Circle(profileAvatar.getFitWidth() / 2, profileAvatar.getFitHeight() / 2, profileAvatar.getFitWidth() / 2);
            profileAvatar.setClip(clip);
        } catch (Exception e) {
            System.err.println("Failed to load Carlos Fernando avatar image: " + e.getMessage());
            profileAvatar.setImage(null); // Fallback jika gambar tidak ada
        }
    }

    private void setupListeners() {
        if (changePasswordButton != null) {
            changePasswordButton.setOnAction(event -> handleChangePassword());
        }
        if (editProfileButton != null) {
            editProfileButton.setOnAction(event -> handleEditProfile());
        }
        if (deactivateAccountButton != null) {
            deactivateAccountButton.setOnAction(event -> handleDeactivateAccount());
        }
        if (deleteAccountButton != null) {
            deleteAccountButton.setOnAction(event -> handleDeleteAccount());
        }

        if (themeToggleButton != null) {
            themeToggleButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    themeToggleButton.setText("ON");
                    showAlert(Alert.AlertType.INFORMATION, "Tema", "Tema Gelap Diaktifkan.");
                } else {
                    themeToggleButton.setText("OFF");
                    showAlert(Alert.AlertType.INFORMATION, "Tema", "Tema Terang Diaktifkan.");
                }
            });
            themeToggleButton.setText("OFF");
        }
    }

    private void setupPreferensiControls() {
        if (languageChoiceBox != null) {
            languageChoiceBox.getItems().addAll("Indonesia", "English");
            languageChoiceBox.setValue("Indonesia");
            languageChoiceBox.setOnAction(event -> {
                showAlert(Alert.AlertType.INFORMATION, "Bahasa", "Bahasa diubah ke: " + languageChoiceBox.getValue());
            });
        }
    }

    @FXML
    private void handleChangePassword() {
        showAlert(Alert.AlertType.INFORMATION, "Ubah Kata Sandi", "Fitur ubah kata sandi akan menampilkan dialog baru atau halaman terpisah.");
    }

    @FXML
    private void handleEditProfile() {
        showAlert(Alert.AlertType.INFORMATION, "Edit Informasi Akun", "Fitur edit informasi akun akan diimplementasikan di sini.");
    }

    @FXML
    private void handleDeactivateAccount() {
        showAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi", "Apakah Anda yakin ingin menonaktifkan akun sementara?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Akun dinonaktifkan sementara.");
                    }
                });
    }

    @FXML
    private void handleDeleteAccount() {
        showAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi Hapus Akun", "PERINGATAN: Apakah Anda yakin ingin menghapus akun secara permanen? Tindakan ini tidak dapat dibatalkan.")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Akun dihapus secara permanen.");
                    }
                });
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}