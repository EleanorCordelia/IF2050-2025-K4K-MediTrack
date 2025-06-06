package com.meditrack.controller;

import com.meditrack.dao.DaftarObatDAO;
import com.meditrack.model.DaftarObat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DaftarObatController {

    @FXML private TableView<DaftarObat> tableDaftarObat;
    @FXML private TableColumn<DaftarObat, Integer> colIdObat;
    @FXML private TableColumn<DaftarObat, String>  colNamaObat;
    @FXML private TableColumn<DaftarObat, String>  colDosis;
    @FXML private TableColumn<DaftarObat, String>  colWaktuKonsumsi;
    @FXML private TableColumn<DaftarObat, String>  colEfekSamping;
    @FXML private TableColumn<DaftarObat, String>  colStatusKonsumsi;

    private final DaftarObatDAO daftarObatDAO = new DaftarObatDAO();

    @FXML
    public void initialize() {
        // Binding kolom ke properti model DaftarObat
        colIdObat.setCellValueFactory(new PropertyValueFactory<>("idObat"));
        colNamaObat.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        colDosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        colWaktuKonsumsi.setCellValueFactory(new PropertyValueFactory<>("waktuKonsumsi"));
        colEfekSamping.setCellValueFactory(new PropertyValueFactory<>("efekSamping"));
        colStatusKonsumsi.setCellValueFactory(new PropertyValueFactory<>("statusKonsumsi"));

        // Muat data awal
        loadDaftarObatTable();
    }

    private void loadDaftarObatTable() {
        List<DaftarObat> list = daftarObatDAO.getAllDaftarObat();
        ObservableList<DaftarObat> obsList = FXCollections.observableArrayList(list);
        tableDaftarObat.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadDaftarObatTable();
    }

    // Metode onAddDaftarObat(), onEditDaftarObat(), onDeleteDaftarObat() bisa ditambahkan sesuai kebutuhan
}