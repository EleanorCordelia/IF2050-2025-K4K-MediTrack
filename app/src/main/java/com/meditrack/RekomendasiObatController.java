package com.meditrack;

import com.meditrack.controller.DetailRekomendasiController;
import com.meditrack.dao.RekomendasiDAO;
import com.meditrack.model.Rekomendasi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RekomendasiObatController {
    @FXML private VBox rekomendasiContainer;
    @FXML private Label userNameLabel;
    @FXML private Label pageLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;

    private final RekomendasiDAO rekomendasiDAO = new RekomendasiDAO();
    private List<Rekomendasi> rekomendasiList;
    private int currentPage = 1;
    private final int itemsPerPage = 5;
    private List<Rekomendasi> allRekomendasi = new ArrayList<>();

    @FXML
    public void initialize() {
        userNameLabel.setText("Carlos");
        loadRekomendasi();
    }

    private void loadRekomendasi() {
        allRekomendasi = rekomendasiDAO.getAllRekomendasi();
        showCurrentPage();
    }

    private void showCurrentPage() {
        rekomendasiContainer.getChildren().clear();
        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allRekomendasi.size());

        if (fromIndex >= toIndex) {
            Label placeholder = new Label("Belum ada rekomendasi saat ini.");
            placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
            rekomendasiContainer.getChildren().add(placeholder);
        } else {
            List<Rekomendasi> currentPageData = allRekomendasi.subList(fromIndex, toIndex);
            for (Rekomendasi rekom : currentPageData) {
                VBox card = createRekomendasiCard(rekom);
                rekomendasiContainer.getChildren().add(card);
            }
        }
        pageLabel.setText("Page " + currentPage);
    }

    private VBox createRekomendasiCard(Rekomendasi rekom) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-padding: 16; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");

        Label namaLabel = new Label(rekom.getAlasan());
        namaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Poppins';");

        Label statusLabel = new Label("Status: " + rekom.getStatusRekomendasi());
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");

        HBox buttonBox = new HBox(10);

        Button detailButton = new Button("Detail");
        detailButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3B82F6; -fx-underline: true;");
        detailButton.setOnAction(e -> openDetailModal(rekom));

        Button setujuiButton = new Button("Setujui");
        setujuiButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        setujuiButton.setOnAction(e -> updateStatus(rekom, "Disetujui"));

        Button tolakButton = new Button("Tidak Setujui");
        tolakButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        tolakButton.setOnAction(e -> generateUlangRekomendasi(rekom));

        buttonBox.getChildren().addAll(detailButton, setujuiButton, tolakButton);

        card.getChildren().addAll(namaLabel, statusLabel, buttonBox);

        return card;
    }

    private void openDetailModal(Rekomendasi rekom) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailRekomendasi.fxml"));
            Parent root = loader.load();

            DetailRekomendasiController controller = loader.getController();
            controller.setData(rekom);

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Detail Rekomendasi");
            modal.setScene(new Scene(root));
            modal.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(Rekomendasi rekom, String status) {
        rekom.setStatusRekomendasi(status);
        rekomendasiDAO.updateRekomendasi(rekom);
        loadRekomendasi();
    }

    private void generateUlangRekomendasi(Rekomendasi rekom) {
        Rekomendasi rekomBaru = new Rekomendasi();
        rekomBaru.setIdPengguna(rekom.getIdPengguna());
        rekomBaru.setIdObat(rekom.getIdObat());
        rekomBaru.setAlasan("Rekomendasi diperbarui berdasarkan kondisi aktual terbaru.");
        rekomBaru.setTanggalRekomendasi(rekom.getTanggalRekomendasi());
        rekomBaru.setStatusRekomendasi("Diajukan");

        rekomendasiDAO.insertRekomendasi(rekomBaru);
        updateStatus(rekom, "Ditolak");
    }

    @FXML
    private void onPrevPage() {
        if (currentPage > 1) {
            currentPage--;
            showCurrentPage();
        }
    }

    @FXML
    private void onNextPage() {
        if ((currentPage * itemsPerPage) < allRekomendasi.size()) {
            currentPage++;
            showCurrentPage();
        }
    }
}
