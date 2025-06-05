package com.meditrack.controller;

import com.meditrack.dao.JadwalDAO;
import com.meditrack.model.Jadwal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class JadwalController {

    @FXML private TableView<Jadwal> tableJadwal;
    @FXML private TableColumn<Jadwal, Integer> colIdJadwal;
    @FXML private TableColumn<Jadwal, Integer> colIdPengguna;
    @FXML private TableColumn<Jadwal, String>  colTanggalJadwal;
    @FXML private TableColumn<Jadwal, String>  colPrioritas;

    private final JadwalDAO jadwalDAO = new JadwalDAO();

    @FXML
    public void initialize() {
        // Binding kolom ke properti model Jadwal
        colIdJadwal.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colTanggalJadwal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colPrioritas.setCellValueFactory(new PropertyValueFactory<>("prioritas"));

        // Muat data awal
        loadJadwalTable();
    }

    private void loadJadwalTable() {
        List<Jadwal> list = jadwalDAO.getAllJadwal();
        ObservableList<Jadwal> obsList = FXCollections.observableArrayList(list);
        tableJadwal.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadJadwalTable();
    }

    // Metode onAddJadwal(), onEditJadwal(), onDeleteJadwal() bisa ditambahkan sesuai kebutuhan
}