package com.meditrack;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import com.meditrack.util.DatabaseUtil;
import com.meditrack.util.UserSession;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import com.meditrack.util.SQLiteConnection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;



public class ManajemenPenggunaController implements Initializable {

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

    @FXML
    private ChoiceBox<String> jenisKelaminChoiceBox;

    @FXML
    public void initialize() {
        jenisKelaminChoiceBox.getItems().addAll("Pria", "Wanita", "Lainnya");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jenisKelaminChoiceBox.getItems().addAll("Laki-laki", "Perempuan", "Lainnya");
        loadUserData();
        loadLoggedInUser();
        setupListeners();
        setupPreferensiControls();
    }


    private void loadUserData() {
        // 🔥 Tambahan: Panggil DAO untuk ambil data user dari database (contoh id=1)
        Pengguna pengguna = new PenggunaDAO().getPenggunaById(1);
        if (pengguna != null) {
            profileName.setText(pengguna.getNama());
            profileAlteress.setText("Alteress"); // sementara placeholder
            emailLabel.setText(pengguna.getEmail());
            phoneLabel.setText(pengguna.getNoHp());
            dobLabel.setText(pengguna.getTanggalLahir());
            genderLabel.setText(pengguna.getJenisKelamin());

            // Set user avatar image
            try {
                String avatarPath = pengguna.getAvatarPath() != null ? pengguna.getAvatarPath() : "/images/carlos_fernando.jpg";
                profileAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(avatarPath))));
                Circle clip = new Circle(profileAvatar.getFitWidth() / 2, profileAvatar.getFitHeight() / 2, profileAvatar.getFitWidth() / 2);
                profileAvatar.setClip(clip);
            } catch (Exception e) {
                System.err.println("Failed to load avatar image: " + e.getMessage());
                profileAvatar.setImage(null);
            }
        } else {
            System.err.println("Pengguna tidak ditemukan di database.");
        }
    }

    // 🔥 Tambahkan method ini
    private void loadLoggedInUser() {
        int loggedInUserId = UserSession.getUserId();  // Ambil ID user yang login
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM pengguna WHERE idPengguna = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profileName.setText(rs.getString("nama"));
                profileAlteress.setText("Alteress"); // sementara placeholder

                fullNameLabel.setText(rs.getString("nama"));
                emailLabel.setText(rs.getString("email"));
                phoneLabel.setText(rs.getString("no_hp"));
                dobLabel.setText(rs.getString("tanggal_lahir"));
                genderLabel.setText(rs.getString("jenis_kelamin"));

                // Set user avatar image
                try {
                    String avatarPath = rs.getString("avatarPath");
                    if (avatarPath == null || avatarPath.isEmpty()) {
                        avatarPath = "/images/dummy.png";
                    }
                    profileAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(avatarPath))));
                    Circle clip = new Circle(profileAvatar.getFitWidth() / 2, profileAvatar.getFitHeight() / 2, profileAvatar.getFitWidth() / 2);
                    profileAvatar.setClip(clip);
                } catch (Exception e) {
                    System.err.println("Failed to load avatar image: " + e.getMessage());
                    profileAvatar.setImage(null);
                }
            } else {
                System.err.println("Pengguna login tidak ditemukan di database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data pengguna: " + e.getMessage());
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
    public boolean updatePassword(int idPengguna, String newPassword) {
        String sql = "UPDATE pengguna SET password = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsername(int idPengguna, String newUsername) {
        String sql = "UPDATE pengguna SET nama = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tambahkan field untuk TextField dan PasswordField
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;


    // Handler untuk update username
    @FXML
    private void handleUpdateUsername() {
        String newUsername = usernameField.getText();
        if (newUsername != null && !newUsername.isEmpty()) {
            Pengguna pengguna = new PenggunaDAO().getPenggunaById(1); // misal id = 1
            if (pengguna != null) {
                pengguna.setNama(newUsername);
                boolean success = new PenggunaDAO().updatePengguna(pengguna);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Username berhasil diperbarui!");
                    profileName.setText(newUsername); // update tampilan UI
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui username.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Username tidak boleh kosong.");
        }
    }

    // Handler untuk update password
    @FXML
    private void handleUpdatePassword() {
        String newPassword = passwordField.getText();
        if (newPassword != null && !newPassword.isEmpty()) {
            Pengguna pengguna = new PenggunaDAO().getPenggunaById(1); // misal id = 1
            if (pengguna != null) {
                pengguna.setPassword(newPassword);
                boolean success = new PenggunaDAO().updatePengguna(pengguna);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil diperbarui!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui password.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Password tidak boleh kosong.");
        }
    }

    @FXML
    private void handleSidebarButton(javafx.event.ActionEvent event) {
        System.out.println("Sidebar button clicked.");
        // Implementasi logika navigasi jika perlu
    }

    @FXML
    private void handleUpdateUserInfo() {
        // Logika untuk menyimpan data pengguna yang diedit
        System.out.println("Data pengguna berhasil diperbarui!");

        // Tambahkan logika validasi, update database, dan refresh tampilan jika perlu.
    }


    @FXML
    private void handleChangeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil Baru");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );


        // Tampilkan dialog untuk memilih file
        File selectedFile = fileChooser.showOpenDialog(profileAvatar.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Load dan set gambar baru
                Image newImage = new Image(selectedFile.toURI().toString());
                profileAvatar.setImage(newImage);

                // OPTIONAL: Simpan path ke database (jika diinginkan)
                // Pengguna pengguna = new PenggunaDAO().getPenggunaById(1); // contoh id 1
                // pengguna.setAvatarPath(selectedFile.getAbsolutePath());
                // new PenggunaDAO().updatePengguna(pengguna);

                System.out.println("Foto profil berhasil diubah: " + selectedFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Gagal memuat gambar baru: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat memuat gambar baru.");
            }
        }
    }

    @FXML
    private TextField avatarPathField;

    @FXML
    private void handleBrowseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Avatar");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(profileAvatar.getScene().getWindow());
        if (selectedFile != null) {
            avatarPathField.setText(selectedFile.getAbsolutePath());
        }
    }





}
