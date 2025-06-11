package com.meditrack.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Optional;

/**
 * Utility class for showing various types of alerts and dialogs
 */
public class AlertUtils {

    /**
     * Show error alert
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set icon if available
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        alert.showAndWait();
    }

    /**
     * Show information alert
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        alert.showAndWait();
    }

    /**
     * Show warning alert
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        alert.showAndWait();
    }

    /**
     * Show confirmation dialog
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Show text input dialog
     */
    public static Optional<String> showTextInput(String title, String headerText, String promptText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(promptText);

        try {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        return dialog.showAndWait();
    }

    /**
     * Show success message
     */
    public static void showSuccess(String title, String message) {
        showInfo(title, message);
    }

    /**
     * Show generic alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/icon.png"));
        } catch (Exception e) {
            // Ignore if icon not found
        }

        alert.showAndWait();
    }
}