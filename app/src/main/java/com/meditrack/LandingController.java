package com.meditrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class LandingController {

    @FXML
    private void handleGabungAction(ActionEvent event) {
        System.out.println("Gabung button clicked. Attempting to load menu.fxml...");
        try {
            URL menuFxmlUrl = getClass().getResource("/fxml/menu.fxml");

            if (menuFxmlUrl == null) {
                System.err.println("Error: Cannot find /fxml/menu.fxml. " +
                        "Pastikan file tersebut ada di src/main/resources/fxml/");
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuFxmlUrl);
            Parent menuRoot = loader.load();

            Scene menuScene = new Scene(menuRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(menuScene);
            currentStage.setTitle("MediTrack - Menu Utama");

            currentStage.show();

            System.out.println("menu.fxml loaded successfully and scene switched.");

        } catch (IOException e) {
            System.err.println("IOException while loading menu.fxml:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during FXML loading or scene switch:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePenggunaAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/penggunaView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal membuka halaman login.");
        }
    }

    @FXML
    private void handleRegisterAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrasi - MediTrack");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal membuka halaman registrasi.");
        }
    }
}
