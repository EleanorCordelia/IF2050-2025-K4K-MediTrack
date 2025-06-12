package com.meditrack.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingController {

    private double xOffset = 0;
    private double yOffset = 0;

    // FXML References
    @FXML private HBox headerBar;
    @FXML private ImageView heroIcon;
    @FXML private Label heroHeading;
    @FXML private Label heroDescription;
    @FXML private HBox heroButtons;
    @FXML private VBox heroImageContainer;
    @FXML private VBox featureSection;
    @FXML private HBox featureCardsContainer;
    @FXML private HBox featureCardsContainer2;

    @FXML
    public void initialize() {
        prepareForAnimation();
        playEntranceAnimation();
        addCardHoverEffects();
    }

    private void prepareForAnimation() {
        // Sembunyikan semua elemen sebelum dianimasikan
        setInitialState(headerBar, -20);
        setInitialState(heroIcon, 20);
        setInitialState(heroHeading, 20);
        setInitialState(heroDescription, 20);
        setInitialState(heroButtons, 20);
        setInitialState(heroImageContainer, 0, 0.95); // Sedikit kecil
        setInitialState(featureSection, 30);
    }

    private void setInitialState(Node node, double yTranslate) {
        setInitialState(node, yTranslate, 0);
    }

    private void setInitialState(Node node, double yTranslate, double scale) {
        if (node != null) {
            node.setOpacity(0);
            node.setTranslateY(yTranslate);
            if (scale != 0) {
                node.setScaleX(scale);
                node.setScaleY(scale);
            }
        }
    }

    private void playEntranceAnimation() {
        // Animasi untuk header
        createSlideIn(headerBar, 800, 200).play();

        // Animasi sekuensial untuk konten utama
        SequentialTransition mainSequence = new SequentialTransition(
                new PauseTransition(Duration.millis(500)),
                createSlideIn(heroIcon, 400, 0),
                createSlideIn(heroHeading, 500, 0),
                createSlideIn(heroDescription, 500, 0),
                createSlideIn(heroButtons, 500, 0)
        );

        // Animasi untuk gambar hero
        ParallelTransition imageAnimation = new ParallelTransition(
                createFadeIn(heroImageContainer, 1000),
                createScaleIn(heroImageContainer, 1000)
        );
        imageAnimation.setDelay(Duration.millis(1200));

        // Animasi untuk bagian fitur
        Transition featureSectionAnimation = createSlideIn(featureSection, 800, 1500);

        // Gabungkan dan mainkan
        mainSequence.play();
        imageAnimation.play();
        featureSectionAnimation.play();
    }

    private void addCardHoverEffects() {
        // Tambahkan efek hover ke semua kartu fitur
        addHoverEffectToCards(featureCardsContainer);
        addHoverEffectToCards(featureCardsContainer2);
    }

    private void addHoverEffectToCards(HBox container) {
        if (container == null) return;
        for (Node cardNode : container.getChildren()) {
            if (cardNode instanceof VBox) {
                VBox card = (VBox) cardNode;
                TranslateTransition liftUp = new TranslateTransition(Duration.millis(200), card);
                liftUp.setToY(-8);

                TranslateTransition dropDown = new TranslateTransition(Duration.millis(200), card);
                dropDown.setToY(0);

                DropShadow hoverShadow = new DropShadow(25, Color.rgb(37, 99, 235, 0.15));
                DropShadow defaultShadow = new DropShadow(20, Color.rgb(0, 0, 0, 0.05));
                card.setEffect(defaultShadow);

                card.setOnMouseEntered(e -> {
                    liftUp.play();
                    card.setEffect(hoverShadow);
                });
                card.setOnMouseExited(e -> {
                    dropDown.play();
                    card.setEffect(defaultShadow);
                });
            }
        }
    }

    // Helper untuk membuat animasi
    private Transition createSlideIn(Node node, int duration, int delay) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
        ft.setToValue(1);

        TranslateTransition tt = new TranslateTransition(Duration.millis(duration), node);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(node, ft, tt);
        pt.setDelay(Duration.millis(delay));
        return pt;
    }

    private Transition createFadeIn(Node node, int duration) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
        ft.setToValue(1);
        return ft;
    }

    private Transition createScaleIn(Node node, int duration) {
        ScaleTransition st = new ScaleTransition(Duration.millis(duration), node);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        return st;
    }

    // --- Logika yang sudah ada ---

    @FXML private void handleMousePressed(MouseEvent event) {
        if (headerBar == null) return;
        Stage stage = (Stage) headerBar.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML private void handleMouseDragged(MouseEvent event) {
        if (headerBar == null) return;
        Stage stage = (Stage) headerBar.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML private void handleRegisterAction(ActionEvent event) { 
        navigateToPage("/fxml/register.fxml", "MediTrack - Registrasi", event); 
    }
    
    @FXML private void handleLoginAction(ActionEvent event) { 
        navigateToPage("/fxml/login.fxml", "MediTrack - Login", event); 
    }
    
    @FXML private void handleGabungAction(ActionEvent event) { 
        System.out.println("Handle Gabung Action called - navigating to login");
        navigateToPage("/fxml/login.fxml", "MediTrack - Login", event); 
    }
    
    @FXML private void handleLearnMoreAction(ActionEvent event) { 
        showInfoAlert("Pelajari Lebih Lanjut", "Halaman detail fitur akan segera hadir!"); 
    }

    private void navigateToPage(String fxmlPath, String title, ActionEvent event) {
        try {
            System.out.println("Attempting to navigate to: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Get the stage from the event source
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            
            System.out.println("Navigation successful to: " + title);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Navigation failed: " + e.getMessage());
            showInfoAlert("Error", "Gagal memuat halaman: " + e.getMessage());
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px;");
        alert.showAndWait();
    }
}