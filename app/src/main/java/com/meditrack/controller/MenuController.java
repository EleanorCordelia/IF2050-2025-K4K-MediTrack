package com.meditrack.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    // Database connection
    private Connection connection;
    private int currentUserId = 1; // Default user ID, bisa diambil dari session/login

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDatabase();
        loadUserData();
        loadBiometricData();
        loadHealthChart();
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
            SELECT tekananDarah, detakJantung, suhuTubuh, tingkatStres, waktuPencatatan
            FROM kondisiaktual 
            WHERE idPengguna = ? 
            ORDER BY waktuPencatatan DESC 
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
     * Update biometric cards with data (this would require accessing the labels in the cards)
     * Since FXML doesn't have fx:id for card labels, this is a placeholder method
     */
    private void updateBiometricCards(String tekananDarah, int detakJantung, double suhuTubuh, String tingkatStres) {
        // In a real implementation, you would need to add fx:id to the labels in the FXML
        // and update them here. For now, this serves as documentation of what data is available.

        System.out.println("Updating biometric cards:");
        System.out.println("Tekanan Darah: " + tekananDarah);
        System.out.println("Detak Jantung: " + detakJantung + " BPM");
        System.out.println("Suhu Tubuh: " + suhuTubuh + "°C");
        System.out.println("Tingkat Stress: " + tingkatStres);
    }

    /**
     * Load health chart data from database
     */
    private void loadHealthChart() {
        String query = """
            SELECT durasi_tidur, durasi_olahraga, jumlah_langkah, waktu_pencatatan
            FROM kondisiaktual 
            WHERE idPengguna = ? 
            ORDER BY waktuPencatatan DESC 
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
                String waktuPencatatan = rs.getString("waktuPencatatan");

                // Extract month from date (you might want to format this better)
                String month = extractMonthFromDate(waktuPencatatan);
                months.add(month);

                sleepSeries.getData().add(new XYChart.Data<>(month, durasiTidur));
                exerciseSeries.getData().add(new XYChart.Data<>(month, durasiOlahraga * 60)); // Convert to minutes
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
        // Implementation for navigation
    }

    private void navigateToMedicine() {
        System.out.println("Navigating to Medicine page");
        // Implementation for navigation
    }

    private void navigateToHealthReport() {
        System.out.println("Navigating to Health Report page");
        // Implementation for navigation
    }

    private void navigateToConsultation() {
        System.out.println("Navigating to Consultation page");
        // Implementation for navigation
    }

    private void navigateToSchedule() {
        System.out.println("Navigating to Schedule page");
        // Implementation for navigation
    }

    private void navigateToSettings() {
        System.out.println("Navigating to Settings page");
        // Implementation for navigation
    }

    private void handleLogout() {
        System.out.println("User logout");
        // Close database connection
        closeDatabase();
        // Navigate to login page or close application
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
                System.out.println("Average Exercise Duration: " + String.format("%.1f", avgExercise) + " hours");
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
}