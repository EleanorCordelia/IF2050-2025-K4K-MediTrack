package com.meditrack.controller;

import com.meditrack.dao.RekomendasiDAO;
import com.meditrack.model.Rekomendasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RekomendasiController {

    @FXML private TableView<Rekomendasi> tableRekomendasi;
    @FXML private TableColumn<Rekomendasi, Integer> colIdRekomendasi;
    @FXML private TableColumn<Rekomendasi, Integer> colIdPengguna;
    @FXML private TableColumn<Rekomendasi, Integer> colIdObat;
    @FXML private TableColumn<Rekomendasi, String>  colAlasan;
    @FXML private TableColumn<Rekomendasi, String>  colTanggalRekomendasi;
    @FXML private TableColumn<Rekomendasi, String>  colStatusRekomendasi;

    private final RekomendasiDAO rekomendasiDAO = new RekomendasiDAO();

    @FXML
    public void initialize() {
        colIdRekomendasi.setCellValueFactory(new PropertyValueFactory<>("idRekomendasi"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colIdObat.setCellValueFactory(new PropertyValueFactory<>("idObat"));
        colAlasan.setCellValueFactory(new PropertyValueFactory<>("alasan"));
        colTanggalRekomendasi.setCellValueFactory(new PropertyValueFactory<>("tanggalRekomendasi"));
        colStatusRekomendasi.setCellValueFactory(new PropertyValueFactory<>("statusRekomendasi"));

        loadRekomendasiTable();
    }

    private void loadRekomendasiTable() {
        List<Rekomendasi> list = rekomendasiDAO.getAllRekomendasi();
        ObservableList<Rekomendasi> obsList = FXCollections.observableArrayList(list);
        tableRekomendasi.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadRekomendasiTable();
    }

    // (Opsional) onAddRekomendasi(), onEditRekomendasi(), onDeleteRekomendasi() dapat ditambahkan sesuai kebutuhan.
}