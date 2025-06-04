package com.meditrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL; // Pastikan import ini ada

public class LandingController {

    @FXML
    private void handleGabungAction(ActionEvent event) {
        System.out.println("Gabung button clicked. Attempting to load menu.fxml...");
        try {
            // Path ini mencari menu.fxml di src/main/resources/fxml/menu.fxml
            URL menuFxmlUrl = getClass().getResource("/fxml/menu.fxml");

            if (menuFxmlUrl == null) {
                System.err.println("Error: Cannot find /fxml/menu.fxml. " +
                        "Pastikan file tersebut ada di src/main/resources/fxml/");
                // Anda bisa menampilkan dialog error ke pengguna di sini jika diperlukan
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuFxmlUrl);
            Parent menuRoot = loader.load();

            // Opsional: Anda bisa mendapatkan controller menu jika perlu mengirim data awal
            // MenuController menuController = loader.getController();
            // menuController.initData(...); // Contoh jika ada metode initData

            Scene menuScene = new Scene(menuRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuScene);
            currentStage.setTitle("MediTrack - Menu Utama"); // Ganti judul window

            // Anda bisa menyesuaikan ukuran Stage jika perlu, misal:
            // currentStage.setWidth(1500); // Sesuaikan dengan prefWidth menu.fxml
            // currentStage.setHeight(800); // Sesuaikan dengan prefHeight menu.fxml
            // currentStage.centerOnScreen();

            currentStage.show();

            System.out.println("menu.fxml loaded successfully and scene switched.");

        } catch (IOException e) {
            System.err.println("IOException while loading menu.fxml:");
            e.printStackTrace();
            // Tampilkan dialog error ke pengguna
        } catch (Exception e) { // Menangkap exception umum lainnya
            System.err.println("An unexpected error occurred during FXML loading or scene switch:");
            e.printStackTrace();
            // Tampilkan dialog error ke pengguna
        }
    }
}