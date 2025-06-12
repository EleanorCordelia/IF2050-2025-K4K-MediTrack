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
            pengaturanButton.setOnAction(e -> navigateToPage("/fxml/manajemenpengguna.fxml", "MediTrack - Pengaturan"));
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
        navigateToPage("/fxml/landing.fxml", "MediTrack - Landing Page");
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
        System.out.println("=== DEBUG SAVE DATA START ===");

        try {
            // Get input values
            String tekananDarah = txtTekananDarah.getText().trim();
            String detakJantungStr = txtDetakJantung.getText().trim();
            String suhuTubuhStr = txtSuhuTubuh.getText().trim();
            String tingkatStres = cbTingkatStres.getValue();
            String durasiTidurStr = txtDurasiTidur.getText().trim();
            String durasiOlahragaStr = txtDurasiOlahraga.getText().trim();
            String jumlahLangkahStr = txtJumlahLangkah.getText().trim();

            System.out.println("Input values:");
            System.out.println("- Tekanan Darah: " + tekananDarah);
            System.out.println("- Detak Jantung: " + detakJantungStr);
            System.out.println("- Suhu Tubuh: " + suhuTubuhStr);
            System.out.println("- Tingkat Stres: " + tingkatStres);
            System.out.println("- Durasi Tidur: " + durasiTidurStr);
            System.out.println("- Durasi Olahraga: " + durasiOlahragaStr);
            System.out.println("- Jumlah Langkah: " + jumlahLangkahStr);
            System.out.println("- Current User ID: " + currentUserId);

            // ===== VALIDASI LENGKAP =====

            // 1. Cek apakah semua field diisi
            if (tekananDarah.isEmpty() || detakJantungStr.isEmpty() || suhuTubuhStr.isEmpty() ||
                    tingkatStres == null || durasiTidurStr.isEmpty() || durasiOlahragaStr.isEmpty() ||
                    jumlahLangkahStr.isEmpty()) {
                System.out.println("Validation failed: Empty fields");
                showValidationError("Semua field harus diisi!");
                return;
            }

            // 2. Validasi format tekanan darah (harus ada / di tengah)
            if (!tekananDarah.matches("^\\d{2,3}/\\d{2,3}$")) {
                System.out.println("Validation failed: Invalid blood pressure format");
                showValidationError("Ganti format tekanan darah menjadi (nomor)/(nomor). Contoh: 120/80 atau 180/60");
                return;
            }

            // 3. Validasi detak jantung
            int detakJantung;
            try {
                detakJantung = Integer.parseInt(detakJantungStr);
                if (detakJantung <= 0) {
                    System.out.println("Validation failed: Invalid heart rate (negative or zero)");
                    showValidationError("Ganti format detak jantung dengan angka positif. Contoh: 75");
                    return;
                }
                if (detakJantung < 30 || detakJantung > 200) {
                    System.out.println("Validation failed: Heart rate out of range");
                    showValidationError("Ganti detak jantung dengan nilai yang wajar (30-200 BPM)");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation failed: Heart rate not a number");
                showValidationError("Ganti format detak jantung dengan angka positif. Contoh: 75");
                return;
            }

            // 4. Validasi suhu tubuh - DIPERBAIKI: Bisa integer atau decimal
            double suhuTubuh;
            try {
                suhuTubuh = Double.parseDouble(suhuTubuhStr);
                if (suhuTubuh <= 0) {
                    System.out.println("Validation failed: Invalid temperature (negative or zero)");
                    showValidationError("Ganti format suhu tubuh dengan angka positif. Contoh: 36 atau 36.5");
                    return;
                }
                if (suhuTubuh < 30.0 || suhuTubuh > 45.0) {
                    System.out.println("Validation failed: Temperature out of range");
                    showValidationError("Ganti suhu tubuh dengan nilai yang wajar (30.0-45.0°C)");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation failed: Temperature not a valid number");
                showValidationError("Ganti format suhu tubuh dengan angka. Contoh: 36 atau 36.5");
                return;
            }

            // 5. Validasi durasi tidur - DIPERBAIKI: Bisa integer atau decimal
            double durasiTidur;
            try {
                durasiTidur = Double.parseDouble(durasiTidurStr);
                if (durasiTidur <= 0) {
                    System.out.println("Validation failed: Invalid sleep duration (negative or zero)");
                    showValidationError("Ganti format durasi tidur dengan angka positif. Contoh: 8 atau 7.5");
                    return;
                }
                if (durasiTidur > 24.0) {
                    System.out.println("Validation failed: Sleep duration too long");
                    showValidationError("Ganti durasi tidur dengan nilai maksimal 24 jam");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation failed: Sleep duration not a valid number");
                showValidationError("Ganti format durasi tidur dengan angka. Contoh: 8 atau 7.5");
                return;
            }

            // 6. Validasi durasi olahraga
            int durasiOlahraga;
            try {
                durasiOlahraga = Integer.parseInt(durasiOlahragaStr);
                if (durasiOlahraga <= 0) {
                    System.out.println("Validation failed: Invalid exercise duration (negative or zero)");
                    showValidationError("Ganti format durasi olahraga dengan angka menit. Contoh: 30 atau 60");
                    return;
                }
                if (durasiOlahraga > 1440) {
                    System.out.println("Validation failed: Exercise duration too long");
                    showValidationError("Ganti durasi olahraga dengan nilai maksimal 1440 menit (24 jam)");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation failed: Exercise duration not a number");
                showValidationError("Ganti format durasi olahraga dengan angka menit. Contoh: 30 atau 60");
                return;
            }

            // 7. Validasi jumlah langkah
            int jumlahLangkah;
            try {
                jumlahLangkah = Integer.parseInt(jumlahLangkahStr);
                if (jumlahLangkah <= 0) {
                    System.out.println("Validation failed: Invalid steps count (negative or zero)");
                    showValidationError("Ganti format jumlah langkah dengan angka bulat. Contoh: 8000 atau 12000");
                    return;
                }
                if (jumlahLangkah > 100000) {
                    System.out.println("Validation failed: Steps count too high");
                    showValidationError("Ganti jumlah langkah dengan nilai maksimal 100,000 langkah");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation failed: Steps count not a number");
                showValidationError("Ganti format jumlah langkah dengan angka bulat. Contoh: 8000 atau 12000");
                return;
            }

            System.out.println("All validations passed!");

            // ===== JIKA SEMUA VALIDASI BERHASIL =====

            // Test database connection first
            System.out.println("Testing database connection...");
            try {
                KondisiAktual testData = dao.getLatestByUserId(currentUserId);
                System.out.println("Database connection test: SUCCESS");
            } catch (Exception e) {
                System.out.println("Database connection test: FAILED - " + e.getMessage());
                e.printStackTrace();
                showValidationError("Masalah koneksi database. Silakan coba lagi.");
                return;
            }

            // Create new KondisiAktual object using setters (lebih aman)
            System.out.println("Creating KondisiAktual object...");
            KondisiAktual newData;

            try {
                newData = new KondisiAktual();
                newData.setUserId(currentUserId);
                newData.setTekananDarah(tekananDarah);
                newData.setDetakJantung(detakJantung);
                newData.setSuhuTubuh(suhuTubuh);
                newData.setTingkatStres(tingkatStres);
                newData.setDurasiTidur(durasiTidur);
                newData.setDurasiOlahraga(durasiOlahraga);
                newData.setJumlahLangkah(jumlahLangkah);

                System.out.println("KondisiAktual object created successfully using setters");
                System.out.println("Object details:");
                System.out.println("- User ID: " + newData.getUserId());
                System.out.println("- Tekanan Darah: " + newData.getTekananDarah());
                System.out.println("- Detak Jantung: " + newData.getDetakJantung());
                System.out.println("- Suhu Tubuh: " + newData.getSuhuTubuh());
                System.out.println("- Tingkat Stres: " + newData.getTingkatStres());
                System.out.println("- Durasi Tidur: " + newData.getDurasiTidur());
                System.out.println("- Durasi Olahraga: " + newData.getDurasiOlahraga());
                System.out.println("- Jumlah Langkah: " + newData.getJumlahLangkah());

            } catch (Exception e) {
                System.out.println("Failed to create KondisiAktual object: " + e.getMessage());
                e.printStackTrace();
                showValidationError("Gagal membuat objek data. Silakan coba lagi.");
                return;
            }

            // Validate the object before saving
            if (newData.getUserId() == null || newData.getUserId() <= 0) {
                System.out.println("ERROR: Invalid user ID in newData: " + newData.getUserId());
                showValidationError("ID pengguna tidak valid. Silakan login ulang.");
                return;
            }

            // Save to database
            System.out.println("Calling dao.save()...");
            boolean success = false;

            try {
                success = dao.save(newData);
                System.out.println("dao.save() returned: " + success);

            } catch (Exception e) {
                System.out.println("Exception during dao.save(): " + e.getMessage());
                e.printStackTrace();
                showValidationError("Terjadi kesalahan saat menyimpan: " + e.getMessage());
                return;
            }

            if (success) {
                System.out.println("Data saved successfully!");
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
                System.out.println("Save failed: dao.save() returned false");
                showValidationError("Gagal menyimpan data ke database. Silakan coba lagi.");
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception in handleSimpanData(): " + e.getMessage());
            e.printStackTrace();
            showValidationError("Terjadi kesalahan sistem: " + e.getMessage());
        }

        System.out.println("=== DEBUG SAVE DATA END ===");
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

    /**
     * Validasi format tekanan darah (sistol/diastol)
     */
    /**
     * Validasi format tekanan darah (sistol/diastol)
     */
    private boolean isValidBloodPressureFormat(String tekananDarah) {
        if (tekananDarah == null || tekananDarah.trim().isEmpty()) {
            return false;
        }

        // Pattern: angka/angka (contoh: 120/80, 180/60)
        String pattern = "^\\d{2,3}/\\d{2,3}$";
        return tekananDarah.matches(pattern);
    }

    /**
     * Validasi angka positif
     */
    private boolean isValidPositiveNumber(String value) {
        try {
            double num = Double.parseDouble(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validasi angka integer positif
     */
    private boolean isValidPositiveInteger(String value) {
        try {
            int num = Integer.parseInt(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}