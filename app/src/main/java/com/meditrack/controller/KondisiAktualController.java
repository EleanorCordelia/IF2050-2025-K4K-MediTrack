package com.meditrack.controller;

import com.meditrack.dao.KondisiAktualDAO;
import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.KondisiAktual;
import com.meditrack.model.Pengguna;
import com.meditrack.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class KondisiAktualController {

    // ** TOP BAR **
    @FXML private Label lblUserName, lblUserRole;
    @FXML private Label lblLastUpdate;

    // ** BIOMETRIK CARDS **
    @FXML private Label lblTekananDarahValue, lblTekananDarahStatus;
    @FXML private ProgressBar pbTekananDarah;

    @FXML private Label lblDetakJantungValue, lblDetakJantungStatus;
    @FXML private ProgressBar pbDetakJantung;

    @FXML private Label lblSuhuTubuhValue, lblSuhuTubuhStatus;
    @FXML private ProgressBar pbSuhuTubuh;

    @FXML private Label lblStresValue, lblStresStatus;
    @FXML private ProgressBar pbStres;

    // ** PHYSICAL CARDS **
    @FXML private Label lblDurasiTidurValue, lblTidurStatus;
    @FXML private ProgressBar pbDurasiTidur;

    @FXML private Label lblDurasiOlahragaValue, lblOlahragaStatus;
    @FXML private ProgressBar pbOlahraga;

    @FXML private Label lblJumlahLangkahValue, lblLangkahStatus;
    @FXML private ProgressBar pbLangkah;

    // ** INPUT FORM **
    @FXML private Button btnInputData, btnCancelInput, btnSimpanData;
    @FXML private VBox inputFormSection, validationMessages, successMessage;
    @FXML private Label lblValidationError, lblSuccessMessage;

    @FXML private TextField txtTekananDarah, txtDetakJantung, txtSuhuTubuh;
    @FXML private ComboBox<String> cbTingkatStres;
    @FXML private TextField txtDurasiTidur, txtDurasiOlahraga, txtJumlahLangkah;

    // Navigation buttons
    @FXML private Button menuButton;
    @FXML private Button rekomendasiButton;
    @FXML private Button obatButton;
    @FXML private Button laporanButton;
    @FXML private Button konsultasiButton;
    @FXML private Button jadwalButton;
    @FXML private Button pengaturanButton;
    @FXML private Button logoutButton;

    private final KondisiAktualDAO dao = new KondisiAktualDAO();
    private final PenggunaDAO penggunaDAO = new PenggunaDAO();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private int currentUserId;

    @FXML
    public void initialize() {
        if (cbTingkatStres.getItems().isEmpty()) {
            cbTingkatStres.getItems().addAll("Rendah", "Sedang", "Tinggi");
        }

        // Hide input form by default
        hideInputForm();

        // Initialize user profile info
        initializeUserProfile();
        
        // Setup navigation buttons
        setupNavigationButtons();

        // Load initial data when page loads
        loadLatestData();
    }

    /**
     * Initialize user profile information in top bar
     */
    private void initializeUserProfile() {
        try {
            currentUserId = UserSession.getUserId();
            if (currentUserId == 0) {
                currentUserId = 1; // Default fallback
            }
            
            Pengguna user = penggunaDAO.getPenggunaById(currentUserId);
            if (user != null) {
                lblUserName.setText(user.getNama());
            } else {
                lblUserName.setText("User");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblUserName.setText("User");
        }
    }

    /**
     * Load data terbaru dari database dan update UI
     */
    private void loadLatestData() {
        try {
            if (currentUserId == 0) {
                setNoDataState();
                return;
            }

            KondisiAktual latestData = dao.getLatestByUserId(currentUserId);
            if (latestData == null) {
                setNoDataState();
                return;
            }

            // Update UI dengan data terbaru
            updateUIWithData(latestData);

        } catch (Exception ex) {
            ex.printStackTrace();
            setNoDataState();
        }
    }
    
    /**
     * Setup navigation button handlers
     */
    private void setupNavigationButtons() {
        if (menuButton != null) {
            menuButton.setOnAction(e -> navigateToPage("/fxml/menu.fxml", "MediTrack - Menu"));
        }
        if (rekomendasiButton != null) {
            rekomendasiButton.setOnAction(e -> navigateToPage("/fxml/rekomendasiObat.fxml", "MediTrack - Rekomendasi"));
        }
        if (obatButton != null) {
            obatButton.setOnAction(e -> navigateToPage("/fxml/daftarObatView.fxml", "MediTrack - Obat"));
        }
        if (konsultasiButton != null) {
            konsultasiButton.setOnAction(e -> navigateToPage("/fxml/konsultasiView.fxml", "MediTrack - Konsultasi"));
        }
        if (jadwalButton != null) {
            jadwalButton.setOnAction(e -> navigateToPage("/fxml/jadwalPengguna.fxml", "MediTrack - Jadwal"));
        }
        if (pengaturanButton != null) {
            pengaturanButton.setOnAction(e -> navigateToPage("/fxml/pengaturanView.fxml", "MediTrack - Pengaturan"));
        }
        if (logoutButton != null) {
            logoutButton.setOnAction(e -> handleLogout());
        }
    }
    
    /**
     * Navigate to a different page
     */
    private void navigateToPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat halaman: " + e.getMessage());
        }
    }
    
    /**
     * Handle logout
     */
    private void handleLogout() {
        UserSession.clear();
        navigateToPage("/fxml/login.fxml", "MediTrack - Login");
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Set UI state ketika belum ada data
     */
    private void setNoDataState() {
        lblLastUpdate.setText("Terakhir diperbarui: --");
        resetAllCards();
    }

    /**
     * Reset semua card ke state default
     */
    private void resetAllCards() {
        // Biometrik cards
        if (lblTekananDarahValue != null) lblTekananDarahValue.setText("--/--");
        if (lblTekananDarahStatus != null) lblTekananDarahStatus.setText("Memuat data...");
        if (pbTekananDarah != null) pbTekananDarah.setProgress(0);

        if (lblDetakJantungValue != null) lblDetakJantungValue.setText("--");
        if (lblDetakJantungStatus != null) lblDetakJantungStatus.setText("Memuat data...");
        if (pbDetakJantung != null) pbDetakJantung.setProgress(0);

        if (lblSuhuTubuhValue != null) lblSuhuTubuhValue.setText("--");
        if (lblSuhuTubuhStatus != null) lblSuhuTubuhStatus.setText("Memuat data...");
        if (pbSuhuTubuh != null) pbSuhuTubuh.setProgress(0);

        if (lblStresValue != null) lblStresValue.setText("--");
        if (lblStresStatus != null) lblStresStatus.setText("Memuat data...");
        if (pbStres != null) pbStres.setProgress(0);

        // Physical health cards
        if (lblDurasiTidurValue != null) lblDurasiTidurValue.setText("--");
        if (lblTidurStatus != null) lblTidurStatus.setText("Memuat data...");
        if (pbDurasiTidur != null) pbDurasiTidur.setProgress(0);

        if (lblDurasiOlahragaValue != null) lblDurasiOlahragaValue.setText("--");
        if (lblOlahragaStatus != null) lblOlahragaStatus.setText("Memuat data...");
        if (pbOlahraga != null) pbOlahraga.setProgress(0);

        if (lblJumlahLangkahValue != null) lblJumlahLangkahValue.setText("--");
        if (lblLangkahStatus != null) lblLangkahStatus.setText("Memuat data...");
        if (pbLangkah != null) pbLangkah.setProgress(0);
    }

    /**
     * Update UI dengan data dari database
     */
    private void updateUIWithData(KondisiAktual data) {
        // Update timestamp
        if (data.getTglPencatatan() != null) {
            lblLastUpdate.setText("Terakhir diperbarui: " + data.getTglPencatatan().format(dtf));
        }

        // Update biometric values
        if (data.getTekananDarah() != null && lblTekananDarahValue != null) {
            lblTekananDarahValue.setText(data.getTekananDarah());
            updateBiometricStatus(lblTekananDarahStatus, pbTekananDarah, "Tekanan Darah", data.getTekananDarah());
        }

        if (data.getDetakJantung() != null && lblDetakJantungValue != null) {
            lblDetakJantungValue.setText(String.valueOf(data.getDetakJantung()));
            updateHeartRateStatus(lblDetakJantungStatus, pbDetakJantung, data.getDetakJantung());
        }

        if (data.getSuhuTubuh() != null && lblSuhuTubuhValue != null) {
            lblSuhuTubuhValue.setText(String.format("%.1f", data.getSuhuTubuh()));
            updateTemperatureStatus(lblSuhuTubuhStatus, pbSuhuTubuh, data.getSuhuTubuh());
        }

        if (data.getTingkatStres() != null && lblStresValue != null) {
            lblStresValue.setText(data.getTingkatStres());
            updateStressStatus(lblStresStatus, pbStres, data.getTingkatStres());
        }

        // Update physical health values
        if (data.getDurasiTidur() != null && lblDurasiTidurValue != null) {
            lblDurasiTidurValue.setText(String.format("%.1f", data.getDurasiTidur()));
            updateSleepStatus(lblTidurStatus, pbDurasiTidur, data.getDurasiTidur());
        }

        if (data.getDurasiOlahraga() != null && lblDurasiOlahragaValue != null) {
            lblDurasiOlahragaValue.setText(String.valueOf(data.getDurasiOlahraga()));
            updateExerciseStatus(lblOlahragaStatus, pbOlahraga, data.getDurasiOlahraga());
        }

        if (data.getJumlahLangkah() != null && lblJumlahLangkahValue != null) {
            lblJumlahLangkahValue.setText(String.valueOf(data.getJumlahLangkah()));
            updateStepsStatus(lblLangkahStatus, pbLangkah, data.getJumlahLangkah());
        }
    }

    // Helper methods for status updates
    private void updateBiometricStatus(Label statusLabel, ProgressBar progressBar, String type, String value) {
        if (statusLabel != null) {
            statusLabel.setText("Normal");
        }
        if (progressBar != null) {
            progressBar.setProgress(0.7);
        }
    }

    private void updateHeartRateStatus(Label statusLabel, ProgressBar progressBar, Integer heartRate) {
        if (statusLabel != null && heartRate != null) {
            if (heartRate < 60) {
                statusLabel.setText("Rendah");
                if (progressBar != null) progressBar.setProgress(0.3);
            } else if (heartRate > 100) {
                statusLabel.setText("Tinggi");
                if (progressBar != null) progressBar.setProgress(0.9);
            } else {
                statusLabel.setText("Normal");
                if (progressBar != null) progressBar.setProgress(0.6);
            }
        }
    }

    private void updateTemperatureStatus(Label statusLabel, ProgressBar progressBar, Double temperature) {
        if (statusLabel != null && temperature != null) {
            if (temperature < 36.0) {
                statusLabel.setText("Rendah");
                if (progressBar != null) progressBar.setProgress(0.3);
            } else if (temperature > 37.5) {
                statusLabel.setText("Demam");
                if (progressBar != null) progressBar.setProgress(0.9);
            } else {
                statusLabel.setText("Normal");
                if (progressBar != null) progressBar.setProgress(0.6);
            }
        }
    }

    private void updateStressStatus(Label statusLabel, ProgressBar progressBar, String stressLevel) {
        if (statusLabel != null && stressLevel != null) {
            statusLabel.setText(stressLevel);
            if (progressBar != null) {
                switch (stressLevel.toLowerCase()) {
                    case "rendah":
                        progressBar.setProgress(0.3);
                        break;
                    case "sedang":
                        progressBar.setProgress(0.6);
                        break;
                    case "tinggi":
                        progressBar.setProgress(0.9);
                        break;
                    default:
                        progressBar.setProgress(0.5);
                }
            }
        }
    }

    private void updateSleepStatus(Label statusLabel, ProgressBar progressBar, Double sleepHours) {
        if (statusLabel != null && sleepHours != null) {
            if (sleepHours < 6) {
                statusLabel.setText("Kurang");
                if (progressBar != null) progressBar.setProgress(0.3);
            } else if (sleepHours > 9) {
                statusLabel.setText("Berlebih");
                if (progressBar != null) progressBar.setProgress(0.9);
            } else {
                statusLabel.setText("Cukup");
                if (progressBar != null) progressBar.setProgress(0.8);
            }
        }
    }

    private void updateExerciseStatus(Label statusLabel, ProgressBar progressBar, Integer exerciseMinutes) {
        if (statusLabel != null && exerciseMinutes != null) {
            if (exerciseMinutes < 30) {
                statusLabel.setText("Kurang");
                if (progressBar != null) progressBar.setProgress(0.4);
            } else if (exerciseMinutes >= 60) {
                statusLabel.setText("Sangat Baik");
                if (progressBar != null) progressBar.setProgress(1.0);
            } else {
                statusLabel.setText("Baik");
                if (progressBar != null) progressBar.setProgress(0.7);
            }
        }
    }

    private void updateStepsStatus(Label statusLabel, ProgressBar progressBar, Integer steps) {
        if (statusLabel != null && steps != null) {
            if (steps < 5000) {
                statusLabel.setText("Kurang Aktif");
                if (progressBar != null) progressBar.setProgress(0.3);
            } else if (steps >= 10000) {
                statusLabel.setText("Sangat Aktif");
                if (progressBar != null) progressBar.setProgress(1.0);
            } else {
                statusLabel.setText("Aktif");
                if (progressBar != null) progressBar.setProgress(0.6);
            }
        }
    }

    // Form handling methods
    @FXML
    private void handleInputData() {
        showInputForm();
    }

    @FXML
    private void handleCancelInput() {
        hideInputForm();
        clearInputFields();
    }

    @FXML
    private void handleSimpanData() {
        try {
            // Validate and parse input
            String tekananDarah = txtTekananDarah.getText().trim();
            String detakJantungStr = txtDetakJantung.getText().trim();
            String suhuTubuhStr = txtSuhuTubuh.getText().trim();
            String tingkatStres = cbTingkatStres.getValue();
            String durasiTidurStr = txtDurasiTidur.getText().trim();
            String durasiOlahragaStr = txtDurasiOlahraga.getText().trim();
            String jumlahLangkahStr = txtJumlahLangkah.getText().trim();

            // Basic validation
            if (tekananDarah.isEmpty() || detakJantungStr.isEmpty() || suhuTubuhStr.isEmpty() ||
                tingkatStres == null || durasiTidurStr.isEmpty() || durasiOlahragaStr.isEmpty() ||
                jumlahLangkahStr.isEmpty()) {
                showValidationError("Semua field harus diisi!");
                return;
            }

            // Parse numeric values
            Integer detakJantung = Integer.parseInt(detakJantungStr);
            Double suhuTubuh = Double.parseDouble(suhuTubuhStr);
            Double durasiTidur = Double.parseDouble(durasiTidurStr);
            Integer durasiOlahraga = Integer.parseInt(durasiOlahragaStr);
            Integer jumlahLangkah = Integer.parseInt(jumlahLangkahStr);

            // Create new KondisiAktual object
            KondisiAktual newData = new KondisiAktual(
                currentUserId, tekananDarah, detakJantung, suhuTubuh,
                tingkatStres, durasiTidur, durasiOlahraga, jumlahLangkah
            );

            // Save to database
            boolean success = dao.save(newData);
            
            if (success) {
                hideInputForm();
                clearInputFields();
                loadLatestData(); // Refresh displayed data
                
                // Show success message
                if (lblSuccessMessage != null && successMessage != null) {
                    lblSuccessMessage.setText("Data berhasil disimpan!");
                    successMessage.setVisible(true);

                    // Hide success message after 3 seconds
                    javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                            new javafx.animation.KeyFrame(
                                    javafx.util.Duration.seconds(3),
                                    e -> successMessage.setVisible(false)
                            )
                    );
                    timeline.play();
                }
            } else {
                throw new RuntimeException("Gagal menyimpan ke database");
            }

        } catch (NumberFormatException e) {
            showValidationError("Format angka tidak valid. Periksa kembali input Anda.");
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showValidationError("Terjadi kesalahan saat menyimpan data. Coba lagi.");
        }
    }

    private void showInputForm() {
        if (inputFormSection != null) {
            inputFormSection.setVisible(true);
            inputFormSection.setManaged(true);
        }
        hideValidationMessages();
    }

    private void hideInputForm() {
        if (inputFormSection != null) {
            inputFormSection.setVisible(false);
            inputFormSection.setManaged(false);
        }
        hideValidationMessages();
    }

    private void clearInputFields() {
        if (txtTekananDarah != null) txtTekananDarah.clear();
        if (txtDetakJantung != null) txtDetakJantung.clear();
        if (txtSuhuTubuh != null) txtSuhuTubuh.clear();
        if (cbTingkatStres != null) cbTingkatStres.setValue(null);
        if (txtDurasiTidur != null) txtDurasiTidur.clear();
        if (txtDurasiOlahraga != null) txtDurasiOlahraga.clear();
        if (txtJumlahLangkah != null) txtJumlahLangkah.clear();
    }

    private void hideValidationMessages() {
        if (validationMessages != null) {
            validationMessages.setVisible(false);
        }
        if (successMessage != null) {
            successMessage.setVisible(false);
        }
    }

    @FXML
    private void handleProfileDropdown() {
        // Handle profile dropdown functionality
        System.out.println("Profile dropdown clicked");
    }

    /**
     * Tampilkan pesan error validasi
     */
    private void showValidationError(String message) {
        if (lblValidationError != null && validationMessages != null) {
            lblValidationError.setText(message);
            validationMessages.setVisible(true);
        }
    }
}