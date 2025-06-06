package com.meditrack.controller;

import com.meditrack.dao.KonsultasiDAO;
import com.meditrack.model.Konsultasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class KonsultasiController {

    @FXML private TableView<Konsultasi> tableKonsultasi;
    @FXML private TableColumn<Konsultasi, Integer> colIdKonsultasi;
    @FXML private TableColumn<Konsultasi, Integer> colIdPengguna;
    @FXML private TableColumn<Konsultasi, Integer> colIdDokter;
    @FXML private TableColumn<Konsultasi, String>  colWaktu;
    @FXML private TableColumn<Konsultasi, String>  colTopik;

    private final KonsultasiDAO konsultasiDAO = new KonsultasiDAO();

    @FXML
    public void initialize() {
        colIdKonsultasi.setCellValueFactory(new PropertyValueFactory<>("idKonsultasi"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colIdDokter.setCellValueFactory(new PropertyValueFactory<>("idDokter"));
        colWaktu.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        colTopik.setCellValueFactory(new PropertyValueFactory<>("topik"));

        loadKonsultasiTable();
    }

    private void loadKonsultasiTable() {
        List<Konsultasi> list = konsultasiDAO.getAllKonsultasi();
        ObservableList<Konsultasi> obsList = FXCollections.observableArrayList(list);
        tableKonsultasi.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadKonsultasiTable();
    }

    // (Opsional) onAddKonsultasi(), onEditKonsultasi(), onDeleteKonsultasi() dapat ditambahkan sesuai kebutuhan.
}