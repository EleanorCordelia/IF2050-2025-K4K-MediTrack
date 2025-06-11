package com.meditrack;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import com.meditrack.util.SQLiteConnection;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.DatePicker;
import java.sql.Types;

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

    @FXML private ChoiceBox<String> jenisKelamin;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private TextField tinggiBadanField;
    @FXML private TextField beratBadanField;
    @FXML private TextField avatarPathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (jenisKelamin != null) {
            jenisKelamin.getItems().addAll("Pria", "Wanita", "Lainnya");
        }
        loadUserData();
        loadLoggedInUser();
        setupListeners();
        setupPreferensiControls();
    }

    private void loadUserData() {
        int loggedInUserId = UserSession.getUserId();
        if (loggedInUserId <= 0) {
            System.err.println("No user logged in.");
            return;
        }

        Pengguna pengguna = new PenggunaDAO().getPenggunaById(loggedInUserId);
        if (pengguna != null) {
            if (profileName != null) profileName.setText(pengguna.getNama());
            if (profileAlteress != null) profileAlteress.setText("Alteress");
            if (emailLabel != null) emailLabel.setText(pengguna.getEmail());
            if (phoneLabel != null) phoneLabel.setText(pengguna.getNoHp());
            if (dobLabel != null) dobLabel.setText(pengguna.getTanggalLahir());
            if (genderLabel != null) genderLabel.setText(pengguna.getJenisKelamin());

            // Set user avatar image
            if (profileAvatar != null) {
                loadAvatar(pengguna.getAvatarPath());
            }
        } else {
            System.err.println("Pengguna tidak ditemukan di database.");
        }
    }

    private void loadAvatar(String avatarPath) {
        try {
            String path = avatarPath != null ? avatarPath : "/images/carlos_fernando.jpg";
            profileAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
            Circle clip = new Circle(profileAvatar.getFitWidth() / 2, profileAvatar.getFitHeight() / 2, profileAvatar.getFitWidth() / 2);
            profileAvatar.setClip(clip);
        } catch (Exception e) {
            System.err.println("Failed to load avatar image: " + e.getMessage());
            if (profileAvatar != null) {
                profileAvatar.setImage(null);
            }
        }
    }

    public void loadLoggedInUser() {
        int loggedInUserId = UserSession.getUserId();
        if (loggedInUserId <= 0) {
            System.err.println("Belum ada pengguna yang login.");
            return;
        }

        try (Connection conn = SQLiteConnection.getConnection()) {
            String query = "SELECT * FROM pengguna WHERE idPengguna = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                updateUIFromResultSet(rs);
            } else {
                System.err.println("Pengguna login tidak ditemukan di database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data pengguna: " + e.getMessage());
        }
    }

    private void updateUIFromResultSet(ResultSet rs) throws SQLException {
        if (profileName != null) profileName.setText(rs.getString("nama"));
        if (profileAlteress != null) profileAlteress.setText("Alteress");
        if (fullNameLabel != null) fullNameLabel.setText(rs.getString("nama"));
        if (emailLabel != null) emailLabel.setText(rs.getString("email"));
        if (phoneLabel != null) phoneLabel.setText(rs.getString("noHp"));
        if (dobLabel != null) dobLabel.setText(rs.getString("tanggalLahir"));
        if (genderLabel != null) genderLabel.setText(rs.getString("jenisKelamin"));

        // Avatar
        if (profileAvatar != null) {
            loadAvatar(rs.getString("avatarPath"));
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

    // Fixed updatePassword method using consistent database connection
    public boolean updatePassword(int idPengguna, String newPassword) {
        if (idPengguna <= 0 || newPassword == null || newPassword.trim().isEmpty()) {
            System.err.println("Invalid parameters for password update");
            return false;
        }

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

    // Fixed updateUsername method using consistent database connection
    public boolean updateUsername(int idPengguna, String newUsername) {
        if (idPengguna <= 0 || newUsername == null || newUsername.trim().isEmpty()) {
            System.err.println("Invalid parameters for username update");
            return false;
        }

        String sql = "UPDATE pengguna SET nama = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername.trim());
            ps.setInt(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
        showAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi Hapus Akun",
                "PERINGATAN: Apakah Anda yakin ingin menghapus akun secara permanen? Tindakan ini tidak dapat dibatalkan.")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int loggedInUserId = UserSession.getUserId();
                        PenggunaDAO dao = new PenggunaDAO();
                        if (dao.deletePengguna(loggedInUserId)) {
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Akun berhasil dihapus.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus akun.");
                        }
                    }
                });
    }

    @FXML
    private void handleUpdateUsername() {
        if (usernameField == null) {
            System.err.println("Username field not initialized");
            return;
        }

        String newUsername = usernameField.getText();
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            int loggedInUserId = UserSession.getUserId();
            if (updateUsername(loggedInUserId, newUsername)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Username berhasil diperbarui!");
                if (profileName != null) profileName.setText(newUsername);
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui username.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Username tidak boleh kosong.");
        }
    }

    @FXML
    private void handleUpdatePassword() {
        if (passwordField == null) {
            System.err.println("Password field not initialized");
            return;
        }

        String newPassword = passwordField.getText();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            int loggedInUserId = UserSession.getUserId();
            if (updatePassword(loggedInUserId, newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil diperbarui!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui password.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Password tidak boleh kosong.");
        }
    }

    @FXML
    private void handleSidebarButton(javafx.event.ActionEvent event) {
        System.out.println("Sidebar button clicked.");
    }

    @FXML
    private void handleUpdateUserInfo() {
        System.out.println("Button 'Simpan Perubahan' diklik.");

        int loggedInUserId = UserSession.getUserId();
        if (loggedInUserId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user logged in.");
            return;
        }

        // Null checks for all fields
        String nama = namaField != null ? namaField.getText() : "";
        String email = emailField != null ? emailField.getText() : "";
        String password = passwordField != null ? passwordField.getText() : "";
        String tanggalLahir = (tanggalLahirPicker != null && tanggalLahirPicker.getValue() != null) ?
                tanggalLahirPicker.getValue().toString() : "";
        String selectedJenisKelamin = (jenisKelamin != null && jenisKelamin.getValue() != null) ?
                jenisKelamin.getValue() : "";
        String tinggiBadanText = tinggiBadanField != null ? tinggiBadanField.getText() : "";
        String beratBadanText = beratBadanField != null ? beratBadanField.getText() : "";

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Nama, Email, dan Password wajib diisi.");
            return;
        }

        Double tinggiBadan = null;
        Double beratBadan = null;
        try {
            if (!tinggiBadanText.isEmpty()) {
                tinggiBadan = Double.parseDouble(tinggiBadanText);
            }
            if (!beratBadanText.isEmpty()) {
                beratBadan = Double.parseDouble(beratBadanText);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Tinggi dan Berat Badan harus berupa angka.");
            return;
        }

        try (Connection conn = SQLiteConnection.getConnection()) {
            String sql = "UPDATE pengguna SET nama=?, email=?, password=?, tanggalLahir=?, jenisKelamin=?, tinggiBadan=?, beratBadan=? WHERE idPengguna=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, tanggalLahir);
            ps.setString(5, selectedJenisKelamin);
            if (tinggiBadan != null) ps.setDouble(6, tinggiBadan);
            else ps.setNull(6, Types.REAL);
            if (beratBadan != null) ps.setDouble(7, beratBadan);
            else ps.setNull(7, Types.REAL);
            ps.setInt(8, loggedInUserId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Informasi pengguna berhasil diperbarui di database.");
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Informasi pengguna berhasil diperbarui!");
                loadLoggedInUser();
            } else {
                System.out.println("Gagal memperbarui informasi pengguna di database.");
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui informasi pengguna.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memperbarui pengguna: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangeProfilePicture() {
        if (profileAvatar == null) {
            System.err.println("Profile avatar not initialized");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil Baru");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileAvatar.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image newImage = new Image(selectedFile.toURI().toString());
                profileAvatar.setImage(newImage);
                System.out.println("Foto profil berhasil diubah: " + selectedFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Gagal memuat gambar baru: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat memuat gambar baru.");
            }
        }
    }

    @FXML
    private void handleBrowseAvatar() {
        if (profileAvatar == null) {
            System.err.println("Profile avatar not initialized");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Avatar");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(profileAvatar.getScene().getWindow());
        if (selectedFile != null && avatarPathField != null) {
            avatarPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}