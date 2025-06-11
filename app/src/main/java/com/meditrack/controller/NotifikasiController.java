package com.meditrack.controller;

import com.meditrack.dao.NotifikasiDAO;
import com.meditrack.model.Notifikasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class NotifikasiController {

    @FXML private TableView<Notifikasi> tableNotifikasi;
    @FXML private TableColumn<Notifikasi, Integer> colIdNotifikasi;
    @FXML private TableColumn<Notifikasi, Integer> colIdPengguna;
    @FXML private TableColumn<Notifikasi, String>  colIsi;
    @FXML private TableColumn<Notifikasi, Boolean> colStatusBaca;
    @FXML private TableColumn<Notifikasi, String>  colWaktuDikirim;

    private final NotifikasiDAO notifikasiDAO = new NotifikasiDAO();

    @FXML
    public void initialize() {
        colIdNotifikasi.setCellValueFactory(new PropertyValueFactory<>("idNotifikasi"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colIsi.setCellValueFactory(new PropertyValueFactory<>("isi"));
        colStatusBaca.setCellValueFactory(new PropertyValueFactory<>("statusBaca"));
        colWaktuDikirim.setCellValueFactory(new PropertyValueFactory<>("waktuDikirim"));

        loadNotifikasiTable();
    }

    private void loadNotifikasiTable() {
        List<Notifikasi> list = notifikasiDAO.getAllNotifikasi();
        ObservableList<Notifikasi> obsList = FXCollections.observableArrayList(list);
        tableNotifikasi.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadNotifikasiTable();
    }

    // (Opsional) onAddNotifikasi(), onEditNotifikasi(), onDeleteNotifikasi() dapat ditambahkan sesuai kebutuhan.
}