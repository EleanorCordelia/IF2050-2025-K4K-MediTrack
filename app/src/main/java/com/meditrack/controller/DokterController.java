package com.meditrack.controller;

import com.meditrack.dao.DokterDAO;
import com.meditrack.model.Dokter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DokterController {

    @FXML private TableView<Dokter> tableDokter;
    @FXML private TableColumn<Dokter, Integer> colIdDokter;
    @FXML private TableColumn<Dokter, String>  colNamaDokter;
    @FXML private TableColumn<Dokter, String>  colEmailDokter;
    @FXML private TableColumn<Dokter, String>  colSpesialisasi;
    @FXML private TableColumn<Dokter, String>  colNomorSTR;

    private final DokterDAO dokterDAO = new DokterDAO();

    @FXML
    public void initialize() {
        colIdDokter.setCellValueFactory(new PropertyValueFactory<>("idDokter"));
        colNamaDokter.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmailDokter.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSpesialisasi.setCellValueFactory(new PropertyValueFactory<>("spesialisasi"));
        colNomorSTR.setCellValueFactory(new PropertyValueFactory<>("nomorSTR"));

        loadDokterTable();
    }

    private void loadDokterTable() {
        List<Dokter> list = dokterDAO.getAllDokter();
        ObservableList<Dokter> obsList = FXCollections.observableArrayList(list);
        tableDokter.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadDokterTable();
    }

    // (Opsional) onAddDokter(), onEditDokter(), onDeleteDokter() dapat ditambahkan sesuai kebutuhan.
}