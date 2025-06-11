package com.meditrack.controller;

import com.meditrack.model.Rekomendasi;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DetailRekomendasiController {
    @FXML private Text detailText;

    public void setData(Rekomendasi rekom) {
        detailText.setText(
                "ID: " + rekom.getIdRekomendasi() + "\n" +
                        "Alasan: " + rekom.getAlasan() + "\n" +
                        "Status: " + rekom.getStatusRekomendasi() + "\n" +
                        "Tanggal: " + rekom.getTanggalRekomendasi()
        );
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) detailText.getScene().getWindow();
        stage.close();
    }
}
