package com.meditrack;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class LandingController {

    // FXML Button References
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Button gabungButton;
    @FXML private Button learnMoreButton;
    @FXML private Button quickRecommendationButton;
    @FXML private Button quickScheduleButton;
    @FXML private Button quickConsultButton;

    // Navigation and Authentication Actions
    @FXML
    private void handleRegisterAction(ActionEvent event) {
        showInfoAlert("Registrasi", "Fitur registrasi akan segera tersedia! 🚀\nSementara ini, Anda dapat menggunakan fitur demo.");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        showInfoAlert("Login", "Fitur login akan segera tersedia! 🔐\nSementara ini, gunakan tombol 'Gabung untuk Perubahan' untuk demo.");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handleGabungAction(ActionEvent event) {
        System.out.println("🚀 Gabung button clicked. Loading main application...");
        addButtonAnimation((Button) event.getSource());
        
        // Add small delay for animation to complete
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> loadMainMenu(event)));
        timeline.play();
    }

    private void loadMainMenu(ActionEvent event) {
        try {
            URL menuFxmlUrl = getClass().getResource("/fxml/menu.fxml");

            if (menuFxmlUrl == null) {
                System.err.println("Error: Cannot find /fxml/menu.fxml");
                showErrorAlert("Navigation Error", "Tidak dapat menemukan halaman menu utama.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuFxmlUrl);
            Parent menuRoot = loader.load();
            Scene menuScene = new Scene(menuRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuScene);
            currentStage.setTitle("MediTrack - Dashboard Utama");
            currentStage.show();

            System.out.println("✅ Main menu loaded successfully!");

        } catch (IOException e) {
            System.err.println("❌ Error loading menu.fxml: " + e.getMessage());
            showErrorAlert("Loading Error", "Terjadi kesalahan saat memuat aplikasi utama.");
        }
    }

    @FXML
    private void handleLearnMoreAction(ActionEvent event) {
        showInfoAlert("Pelajari Lebih Lanjut", 
            "📚 Dokumentasi lengkap MediTrack:\n\n" +
            "• Panduan pengguna interaktif\n" +
            "• Video tutorial kesehatan\n" +
            "• FAQ dan troubleshooting\n" +
            "• Tips hidup sehat harian\n\n" +
            "Segera tersedia di portal pembelajaran kami!");
        addButtonAnimation((Button) event.getSource());
    }

    // Quick Action Handlers
    @FXML
    private void handleQuickRecommendationAction(ActionEvent event) {
        showInfoAlert("Rekomendasi Cepat", 
            "🩺 Fitur Rekomendasi AI:\n\n" +
            "• Analisis kondisi kesehatan real-time\n" +
            "• Saran obat & suplemen personal\n" +
            "• Integrasi dengan wearable device\n" +
            "• Konsultasi dengan dokter AI\n\n" +
            "Masuk ke aplikasi utama untuk mengakses fitur ini!");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handleQuickScheduleAction(ActionEvent event) {
        showInfoAlert("Smart Schedule", 
            "📅 Pengaturan Jadwal Pintar:\n\n" +
            "• Reminder otomatis konsumsi obat\n" +
            "• Sinkronisasi dengan kalender\n" +
            "• Notifikasi push real-time\n" +
            "• Tracking kepatuhan harian\n\n" +
            "Gabung sekarang untuk mulai mengatur jadwal Anda!");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handleQuickConsultAction(ActionEvent event) {
        showInfoAlert("Konsultasi Online", 
            "💬 Telehealth Premium:\n\n" +
            "• Chat 24/7 dengan dokter berpengalaman\n" +
            "• Video call konsultasi langsung\n" +
            "• Resep digital terintegrasi\n" +
            "• Riwayat konsultasi tersimpan\n\n" +
            "Hubungi dokter kapan saja melalui aplikasi!");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handlePenggunaAction(ActionEvent event) {
        System.out.println("👤 Loading user management...");
        addButtonAnimation((Button) event.getSource());
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/penggunaView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MediTrack - Kelola Pengguna");
        } catch (IOException e) {
            System.err.println("❌ Error loading penggunaView.fxml: " + e.getMessage());
            showErrorAlert("Loading Error", "Tidak dapat memuat halaman kelola pengguna.");
        }
    }

    // Interactive Effects and Animation Handlers
    @FXML
    private void handleFeatureHover(MouseEvent event) {
        if (event.getSource() instanceof VBox) {
            VBox card = (VBox) event.getSource();
            
            // Scale up animation on hover
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.play();
            
            // Enhanced shadow effect
            card.setStyle(card.getStyle() + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 8);");
        }
    }

    @FXML
    private void handleFeatureExit(MouseEvent event) {
        if (event.getSource() instanceof VBox) {
            VBox card = (VBox) event.getSource();
            
            // Scale back to normal
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
            
            // Reset shadow effect
            card.setStyle(card.getStyle().replace("; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 8);", ""));
        }
    }

    // Footer and Social Media Actions
    @FXML
    private void handleSocialMedia(ActionEvent event) {
        String platform = ((Button) event.getSource()).getText();
        String message = switch (platform) {
            case "📘" -> "Mengalihkan ke Facebook MediTrack...";
            case "📷" -> "Mengalihkan ke Instagram @meditrack_id...";
            case "🐦" -> "Mengalihkan ke Twitter @MediTrackID...";
            default -> "Mengalihkan ke media sosial...";
        };
        
        showInfoAlert("Social Media", message + "\n\nFitur integrasi media sosial segera tersedia!");
        addButtonAnimation((Button) event.getSource());
    }

    @FXML
    private void handleFooterLink(ActionEvent event) {
        String linkText = ((Button) event.getSource()).getText();
        showInfoAlert("Info", "Halaman '" + linkText + "' akan segera tersedia!\n\nTerima kasih atas minat Anda pada MediTrack. 🙏");
        addButtonAnimation((Button) event.getSource());
    }

    // Utility Methods for UI Feedback
    private void addButtonAnimation(Button button) {
        if (button != null) {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert
        alert.getDialogPane().setStyle(
            "-fx-font-family: 'Poppins'; " +
            "-fx-font-size: 14px; " +
            "-fx-background-color: white; " +
            "-fx-border-radius: 10;"
        );
        
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Terjadi Kesalahan");
        alert.setContentText(message);
        
        // Style the alert
        alert.getDialogPane().setStyle(
            "-fx-font-family: 'Poppins'; " +
            "-fx-font-size: 14px; " +
            "-fx-background-color: white; " +
            "-fx-border-radius: 10;"
        );
        
        alert.showAndWait();
    }
}
