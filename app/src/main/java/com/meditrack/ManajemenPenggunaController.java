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

    @FXML
    private ChoiceBox<String> jenisKelamin;

    @FXML
    public void initialize() {
        jenisKelamin.getItems().addAll("Pria", "Wanita", "Lainnya");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (jenisKelamin != null) {
            jenisKelamin.getItems().addAll("Pria", "Wanita", "Lainnya");
        } else {
            System.err.println("ChoiceBox 'jenisKelamin' belum terhubung (fx:id salah?).");
        }
        loadUserData();
        loadLoggedInUser();
        setupListeners();
        setupPreferensiControls();
    }


    private void loadUserData() {
        int loggedInUserId = UserSession.getUserId();
        Pengguna pengguna = new PenggunaDAO().getPenggunaById(loggedInUserId);
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

        // 🔥 Tambahkan validasi di sini
        if (loggedInUserId <= 0) {
            System.err.println("Belum ada pengguna yang login.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM pengguna WHERE idPengguna = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profileName.setText(rs.getString("nama"));
                profileAlteress.setText("Alteress"); // placeholder

                fullNameLabel.setText(rs.getString("nama"));
                emailLabel.setText(rs.getString("email"));
                phoneLabel.setText(rs.getString("no_hp"));
                dobLabel.setText(rs.getString("tanggalLahir"));
                genderLabel.setText(rs.getString("jenis_kelamin"));

                // Avatar
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

    // LoginController.java (atau yang setara)
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM pengguna WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idPengguna = rs.getInt("idPengguna");
                UserSession.setUserId(idPengguna);  // 🔥 INI PENTING!
                System.out.println("Login sukses. ID: " + idPengguna);

                // Arahkan ke halaman berikutnya
                // ...
            } else {
                System.out.println("Login gagal.");
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password salah.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        showAlert(Alert.AlertType.CONFIRMATION, "Konfirmasi Hapus Akun",
                "PERINGATAN: Apakah Anda yakin ingin menghapus akun secara permanen? Tindakan ini tidak dapat dibatalkan.")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int loggedInUserId = UserSession.getUserId();
                        PenggunaDAO dao = new PenggunaDAO();
                        if (dao.deletePengguna(loggedInUserId)) {
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Akun berhasil dihapus.");
                            // TODO: Panggil logout di sini
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus akun.");
                        }
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
            int loggedInUserId = UserSession.getUserId();
            PenggunaDAO dao = new PenggunaDAO();
            if (dao.updateUsername(loggedInUserId, newUsername)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Username berhasil diperbarui!");
                profileName.setText(newUsername);
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui username.");
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
            int loggedInUserId = UserSession.getUserId();
            Pengguna pengguna = new PenggunaDAO().getPenggunaById(loggedInUserId);
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
        System.out.println("Button 'Simpan Perubahan' diklik.");

        int loggedInUserId = UserSession.getUserId(); // ID pengguna yang login

        String nama = namaField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String tanggalLahir = (tanggalLahirPicker.getValue() != null) ? tanggalLahirPicker.getValue().toString() : "";
        String selectedJenisKelamin = (jenisKelamin.getValue() != null) ? jenisKelamin.getValue() : "";
        String tinggiBadanText = tinggiBadanField.getText();
        String beratBadanText = beratBadanField.getText();

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Nama, Email, dan Password wajib diisi.");
            return;
        }

        // 🔥 Validasi Tinggi & Berat Badan di sini
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

        try (Connection conn = DatabaseUtil.getConnection()) {
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
                loadLoggedInUser(); // untuk refresh tampilan
            } else {
                System.out.println("Gagal memperbarui informasi pengguna di database.");
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui informasi pengguna.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memperbarui pengguna: " + e.getMessage());
        }
    }

    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private TextField tinggiBadanField;
    @FXML private TextField beratBadanField;

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