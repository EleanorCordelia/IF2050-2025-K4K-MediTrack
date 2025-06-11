package com.meditrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Memuat font kustom
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Regular.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Medium.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-SemiBold.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), 10);

            // Memuat file FXML untuk landing page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing.fxml"));
            Parent root = loader.load();

            // Membuat Scene
            Scene scene = new Scene(root);

            // Mengatur properti window (Stage)
            primaryStage.setScene(scene);
            primaryStage.setTitle("MediTrack - Landing Page");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Gagal memuat file FXML atau resources lainnya.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Terjadi error tak terduga saat memulai aplikasi.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}