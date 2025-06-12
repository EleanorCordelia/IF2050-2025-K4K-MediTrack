package com.meditrack.controller;

import com.meditrack.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    // FXML Components - Sidebar
    @FXML private VBox sidebar;
    @FXML private Button menuButton;
    @FXML private Button rekomendasiButton;
    @FXML private Button obatButton;
    @FXML private Button laporanButton;
    @FXML private Button konsultasiButton;
    @FXML private Button jadwalButton;
    @FXML private Button pengaturanButton;
    @FXML private Button keluarButton;

    // FXML Components - Top Bar
    @FXML private ImageView ivProfile;
    @FXML private Label lblUserName;
    @FXML private Button btnProfileDropdown;

    // FXML Components - Main Content
    @FXML private Button btnGenerate;
    @FXML private LineChart<String, Number> healthChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    // FXML Components - Biometric Data
    @FXML private Label bloodPressureValue;
    @FXML private Label heartRateValue;
    @FXML private Label bodyTempValue;
    @FXML private Label stressLevelValue;
    @FXML private Label heroUserName;

    // Database connection
    private Connection connection;
    private int currentUserId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get current user ID from session
        currentUserId = UserSession.getUserId();
        if (currentUserId == 0) {
            currentUserId = 1; // Default fallback
        }
        
        initializeDatabase();
        loadUserData();
        loadBiometricData();
        loadHealthChart();
        loadTodaySchedule();
        setupEventHandlers();
    }

    /**
     * Initialize database connection
     */
    private void initializeDatabase() {
        try {
            // Sesuaikan dengan path database Anda
            String url = "jdbc:sqlite:meditrack.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Database connected successfully");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load user data from database
     */
    private void loadUserData() {
        String query = "SELECT nama, avatar_path FROM pengguna WHERE idPengguna = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nama = rs.getString("nama");
                String avatarPath = rs.getString("avatar_path");

                // Update UI with user data
                lblUserName.setText(nama);

                // Load user avatar if exists
                if (avatarPath != null && !avatarPath.isEmpty()) {
                    try {
                        Image avatar = new Image(getClass().getResourceAsStream(avatarPath));
                        ivProfile.setImage(avatar);
                    } catch (Exception e) {
                        System.err.println("Failed to load avatar: " + e.getMessage());
                        // Set default avatar
                        setDefaultAvatar(nama);
                    }
                } else {
                    setDefaultAvatar(nama);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to load user data: " + e.getMessage());
            e.printStackTrace();
            // Set default values
            lblUserName.setText("User");
            setDefaultAvatar("User");
        }
    }

    /**
     * Set default avatar with user initial
     */
    private void setDefaultAvatar(String nama) {
        // This would require creating a text-based avatar or using a default image
        try {
            Image defaultAvatar = new Image(getClass().getResourceAsStream("/images/user.png"));
            ivProfile.setImage(defaultAvatar);
        } catch (Exception e) {
            System.err.println("Failed to load default avatar");
        }
    }

    /**
     * Load latest biometric data for display in cards
     */
    private void loadBiometricData() {
        String query = """
            SELECT tekananDarah, detakJantung, suhuTubuh, tingkatStres, tglPencatatan
            FROM kondisiaktual 
            WHERE idPengguna = ? 
            ORDER BY tglPencatatan DESC 
            LIMIT 1
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String tekananDarah = rs.getString("tekananDarah");
                int detakJantung = rs.getInt("detakJantung");
                double suhuTubuh = rs.getDouble("suhuTubuh");
                String tingkatStres = rs.getString("tingkatStres");

                // Update biometric cards with real data
                updateBiometricCards(tekananDarah, detakJantung, suhuTubuh, tingkatStres);

                System.out.println("Biometric data loaded successfully");
            } else {
                System.out.println("No biometric data found for user");
                // Use default/placeholder values
                updateBiometricCards("120/80", 110, 36.5, "Tinggi");
            }
        } catch (SQLException e) {
            System.err.println("Failed to load biometric data: " + e.getMessage());
            e.printStackTrace();
            // Use default values in case of error
            updateBiometricCards("120/80", 110, 36.5, "Tinggi");
        }
    }

    /**
     * Update biometric cards with data
     */
    private void updateBiometricCards(String tekananDarah, int detakJantung, double suhuTubuh, String tingkatStres) {
        try {
            if (bloodPressureValue != null) {
                bloodPressureValue.setText(tekananDarah);
            }
            if (heartRateValue != null) {
                heartRateValue.setText(String.valueOf(detakJantung));
            }
            if (bodyTempValue != null) {
                bodyTempValue.setText(String.valueOf(suhuTubuh));
            }
            if (stressLevelValue != null) {
                stressLevelValue.setText(tingkatStres);
            }
            
            // Update hero section username
            if (heroUserName != null && lblUserName != null) {
                heroUserName.setText(lblUserName.getText());
            }
            
            System.out.println("Biometric cards updated successfully");
        } catch (Exception e) {
            System.err.println("Failed to update biometric cards: " + e.getMessage());
        }
    }

    /**
     * Load health chart data from database
     */
    private void loadHealthChart() {
        String query = """
            SELECT durasiTidur, durasiOlahraga, jumlahLangkah, tglPencatatan
            FROM kondisiaktual 
            WHERE idPengguna = ? 
            ORDER BY tglPencatatan DESC 
            LIMIT 12
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            // Create series for the chart
            XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
            sleepSeries.setName("Durasi Tidur");

            XYChart.Series<String, Number> exerciseSeries = new XYChart.Series<>();
            exerciseSeries.setName("Durasi Olahraga");

            XYChart.Series<String, Number> stepsSeries = new XYChart.Series<>();
            stepsSeries.setName("Jumlah Langkah");

            // Populate chart data
            ObservableList<String> months = FXCollections.observableArrayList();

            while (rs.next()) {
                double durasiTidur = rs.getDouble("durasiTidur");
                double durasiOlahraga = rs.getDouble("durasiOlahraga");
                int jumlahLangkah = rs.getInt("jumlahLangkah");
                Timestamp tglPencatatan = rs.getTimestamp("tglPencatatan");

                // Extract month from date
                String month = extractMonthFromDate(tglPencatatan.toString());
                months.add(month);

                sleepSeries.getData().add(new XYChart.Data<>(month, durasiTidur));
                exerciseSeries.getData().add(new XYChart.Data<>(month, durasiOlahraga)); // Already in minutes
                stepsSeries.getData().add(new XYChart.Data<>(month, jumlahLangkah / 100)); // Scale down for visibility
            }

            // Update chart
            healthChart.getData().clear();
            healthChart.getData().addAll(sleepSeries, exerciseSeries, stepsSeries);

            System.out.println("Health chart data loaded successfully");

        } catch (SQLException e) {
            System.err.println("Failed to load health chart data: " + e.getMessage());
            e.printStackTrace();
            // Load default/sample data
            loadSampleChartData();
        }
    }

    /**
     * Load sample chart data when database data is not available
     */
    private void loadSampleChartData() {
        XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
        sleepSeries.setName("Durasi Tidur");

        XYChart.Series<String, Number> exerciseSeries = new XYChart.Series<>();
        exerciseSeries.setName("Durasi Olahraga");

        XYChart.Series<String, Number> stepsSeries = new XYChart.Series<>();
        stepsSeries.setName("Jumlah Langkah");

        String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agt", "Sep", "Okt", "Nov", "Des"};

        for (String month : months) {
            sleepSeries.getData().add(new XYChart.Data<>(month, Math.random() * 200 + 100));
            exerciseSeries.getData().add(new XYChart.Data<>(month, Math.random() * 150 + 50));
            stepsSeries.getData().add(new XYChart.Data<>(month, Math.random() * 100 + 200));
        }

        healthChart.getData().clear();
        healthChart.getData().addAll(sleepSeries, exerciseSeries, stepsSeries);
    }

    /**
     * Extract month from date string
     */
    private String extractMonthFromDate(String dateString) {
        try {
            // Assuming date format is YYYY-MM-DD or similar
            String[] parts = dateString.split("-");
            if (parts.length >= 2) {
                int monthNum = Integer.parseInt(parts[1]);
                String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                        "Jul", "Agt", "Sep", "Okt", "Nov", "Des"};
                return monthNames[monthNum - 1];
            }
        } catch (Exception e) {
            System.err.println("Failed to parse date: " + dateString);
        }
        return "Unknown";
    }

    /**
     * Setup event handlers for buttons
     */
    private void setupEventHandlers() {
        // Navigation buttons
        rekomendasiButton.setOnAction(e -> navigateToRecommendation());
        obatButton.setOnAction(e -> navigateToMedicine());
        laporanButton.setOnAction(e -> navigateToHealthReport());
        konsultasiButton.setOnAction(e -> navigateToConsultation());
        jadwalButton.setOnAction(e -> navigateToSchedule());
        pengaturanButton.setOnAction(e -> navigateToSettings());
        keluarButton.setOnAction(e -> handleLogout());

        // Main action button
        btnGenerate.setOnAction(e -> handleGenerate());

        // Profile dropdown
        btnProfileDropdown.setOnAction(e -> handleProfileDropdown());
    }

    // Event handler methods
    @FXML
    private void handleGenerate() {
        System.out.println("Generate button clicked - Navigate to recommendation page");
        // Navigate to recommendation/generation page
        navigateToRecommendation();
    }

    @FXML
    private void handleProfileDropdown() {
        System.out.println("Profile dropdown clicked");
        // Show profile dropdown menu
        // Implementation depends on your UI framework choice
    }

    // Navigation methods
    private void navigateToRecommendation() {
        System.out.println("Navigating to Recommendation page");
        navigateToPage("/fxml/rekomendasiObat.fxml", "MediTrack - Rekomendasi");
    }

    private void navigateToMedicine() {
        System.out.println("Navigating to Medicine page");
        navigateToPage("/fxml/daftarObatView.fxml", "MediTrack - Obat");
    }

    private void navigateToHealthReport() {
        System.out.println("Navigating to Health Report page");
        navigateToPage("/fxml/kondisiAktual.fxml", "MediTrack - Laporan Kesehatan");
    }

    private void navigateToConsultation() {
        System.out.println("Navigating to Consultation page");
        navigateToPage("/fxml/konsultasiView.fxml", "MediTrack - Konsultasi");
    }

    private void navigateToSchedule() {
        System.out.println("Navigating to Schedule page");
        navigateToPage("/fxml/jadwalPengguna.fxml", "MediTrack - Jadwal");
    }

    private void navigateToSettings() {
        System.out.println("Navigating to User Management page");
        try {
            // Coba berbagai kemungkinan path
            navigateToPage("/fxml/manajemenpengguna.fxml", "MediTrack - Manajemen Pengguna");
        } catch (Exception e1) {
            try {
                navigateToPage("/manajemenpengguna.fxml", "MediTrack - Manajemen Pengguna");
            } catch (Exception e2) {
                try {
                    navigateToPage("manajemenpengguna.fxml", "MediTrack - Manajemen Pengguna");
                } catch (Exception e3) {
                    showAlert("Error", "File manajemenpengguna.fxml tidak ditemukan");
                    e3.printStackTrace();
                }
            }
        }
    }

    private void handleLogout() {
        System.out.println("User logout");
        // Clear user session
        UserSession.clear();
        // Close database connection
        closeDatabase();
        // Navigate to login page
        navigateToPage("/fxml/landing.fxml", "MediTrack - Landing Page");
    }

    /**
     * Get user statistics for dashboard
     */
    public void getUserStatistics() {
        String query = """
            SELECT 
                COUNT(*) as total_records,
                AVG(detakJantung) as avg_heart_rate,
                AVG(suhuTubuh) as avg_temperature,
                AVG(durasiTidur) as avg_sleep,
                AVG(durasiOlahraga) as avg_exercise,
                AVG(jumlahLangkah) as avg_steps
            FROM kondisiaktual 
            WHERE idPengguna = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalRecords = rs.getInt("total_records");
                double avgHeartRate = rs.getDouble("avg_heart_rate");
                double avgTemperature = rs.getDouble("avg_temperature");
                double avgSleep = rs.getDouble("avg_sleep");
                double avgExercise = rs.getDouble("avg_exercise");
                double avgSteps = rs.getDouble("avg_steps");

                System.out.println("=== User Health Statistics ===");
                System.out.println("Total Records: " + totalRecords);
                System.out.println("Average Heart Rate: " + String.format("%.1f", avgHeartRate) + " BPM");
                System.out.println("Average Temperature: " + String.format("%.1f", avgTemperature) + "°C");
                System.out.println("Average Sleep Duration: " + String.format("%.1f", avgSleep) + " hours");
                System.out.println("Average Exercise Duration: " + String.format("%.1f", avgExercise) + " minutes");
                System.out.println("Average Steps: " + String.format("%.0f", avgSteps) + " steps");
            }
        } catch (SQLException e) {
            System.err.println("Failed to get user statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Close database connection
     */
    private void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    /**
     * Cleanup method called when controller is destroyed
     */
    public void cleanup() {
        closeDatabase();
    }

    // Getters and setters
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        // Reload data for new user
        loadUserData();
        loadBiometricData();
        loadHealthChart();
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
    
    /**
     * Load today's schedule data from database
     */
    private void loadTodaySchedule() {
        String query = """
            SELECT namaAktivitas, waktuMulai, waktuSelesai, kategori
            FROM jadwal 
            WHERE idPengguna = ? AND tanggalMulai = CURRENT_DATE
            ORDER BY waktuMulai ASC 
            LIMIT 3
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            int scheduleCount = 0;
            while (rs.next() && scheduleCount < 3) {
                String namaAktivitas = rs.getString("namaAktivitas");
                String waktuMulai = rs.getString("waktuMulai");
                String waktuSelesai = rs.getString("waktuSelesai");
                String kategori = rs.getString("kategori");
                
                System.out.println("Schedule " + (scheduleCount + 1) + ": " + namaAktivitas + " (" + waktuMulai + " - " + waktuSelesai + ")");
                scheduleCount++;
            }

            if (scheduleCount == 0) {
                System.out.println("No schedule found for today");
            } else {
                System.out.println("Today's schedule loaded successfully");
            }
        } catch (SQLException e) {
            System.err.println("Failed to load today's schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to a different page
     */
    private void navigateToPage(String fxmlPath, String title) {
        try {
            System.out.println("Attempting to navigate to: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Get the current stage
            Stage stage = (Stage) sidebar.getScene().getWindow();
            
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            
            System.out.println("Navigation successful to: " + title);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Navigation failed: " + e.getMessage());
            showAlert("Error", "Gagal memuat halaman: " + e.getMessage());
        }
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px;");
        alert.showAndWait();
    }
    
    /**
     * Handle sidebar button clicks
     */
    @FXML
    private void handleSidebarButton(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();

        if (sourceButton == rekomendasiButton) {
            navigateToRecommendation();
        } else if (sourceButton == obatButton) {
            navigateToMedicine();
        } else if (sourceButton == laporanButton) {
            navigateToHealthReport();
        } else if (sourceButton == konsultasiButton) {
            navigateToConsultation();
        } else if (sourceButton == jadwalButton) {
            navigateToSchedule();
        } else if (sourceButton == pengaturanButton) {
            // Langsung ke manajemen pengguna
            navigateToPage("/fxml/manajamenpengguna.fxml", "MediTrack - Manajemen Pengguna");
        } else if (sourceButton == keluarButton) {
            handleLogout();
        }
    }


}