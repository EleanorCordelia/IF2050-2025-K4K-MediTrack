package com.meditrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
<<<<<<< HEAD
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("MediTrack - Landing Page");
=======
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // !!! load semua variant Poppins, kedua weight nya (size argumen cuma dummy)
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Regular.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Medium.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-SemiBold.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), 10);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/menu.fxml")
        );
        Parent root = loader.load();
        // pakai angka yang sesuai dengan fxml prefWidth/prefHeight
        Scene scene = new Scene(root, 1020, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MediTrack - Menu");
>>>>>>> 7f199c692950a45a0f07577d1d371c4ece1f7015
        primaryStage.show();
    }

    public static void main(String[] args) {
<<<<<<< HEAD
        launch(args);
    }
}
=======
        launch();
    }
}
>>>>>>> 7f199c692950a45a0f07577d1d371c4ece1f7015
