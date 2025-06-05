package com.meditrack.controller;

import com.meditrack.dao.KondisiHistorisDAO;
import com.meditrack.model.KondisiHistoris;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class KondisiHistorisController {

    @FXML private TableView<KondisiHistoris> tableKondisiHistoris;
    @FXML private TableColumn<KondisiHistoris, Integer> colIdHistori;
    @FXML private TableColumn<KondisiHistoris, Integer> colIdPengguna;
    @FXML private TableColumn<KondisiHistoris, String>  colTanggalAwal;
    @FXML private TableColumn<KondisiHistoris, String>  colTanggalAkhir;
    @FXML private TableColumn<KondisiHistoris, String>  colRingkasan;

    private final KondisiHistorisDAO kondisiHistorisDAO = new KondisiHistorisDAO();

    @FXML
    public void initialize() {
        colIdHistori.setCellValueFactory(new PropertyValueFactory<>("idHistori"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colTanggalAwal.setCellValueFactory(new PropertyValueFactory<>("tanggalAwal"));
        colTanggalAkhir.setCellValueFactory(new PropertyValueFactory<>("tanggalAkhir"));
        colRingkasan.setCellValueFactory(new PropertyValueFactory<>("ringkasan"));

        loadKondisiHistorisTable();
    }

    private void loadKondisiHistorisTable() {
        List<KondisiHistoris> list = kondisiHistorisDAO.getAllKondisiHistoris();
        ObservableList<KondisiHistoris> obsList = FXCollections.observableArrayList(list);
        tableKondisiHistoris.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadKondisiHistorisTable();
    }

    // (Opsional) onAddKondisiHistoris(), onEditKondisiHistoris(), onDeleteKondisiHistoris() dapat ditambahkan sesuai kebutuhan.
}