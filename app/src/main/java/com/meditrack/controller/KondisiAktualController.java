package com.meditrack.controller;

import com.meditrack.dao.KondisiAktualDAO;
import com.meditrack.model.KondisiAktual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class KondisiAktualController {

    @FXML private TableView<KondisiAktual> tableKondisiAktual;
    @FXML private TableColumn<KondisiAktual, Integer> colIdKondisi;
    @FXML private TableColumn<KondisiAktual, Integer> colIdPengguna;
    @FXML private TableColumn<KondisiAktual, String>  colTekananDarah;
    @FXML private TableColumn<KondisiAktual, Integer> colDetakJantung;
    @FXML private TableColumn<KondisiAktual, Double>  colSuhuTubuh;
    @FXML private TableColumn<KondisiAktual, String>  colWaktuPencatatan;

    private final KondisiAktualDAO kondisiAktualDAO = new KondisiAktualDAO();

    @FXML
    public void initialize() {
        colIdKondisi.setCellValueFactory(new PropertyValueFactory<>("idKondisi"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colTekananDarah.setCellValueFactory(new PropertyValueFactory<>("tekananDarah"));
        colDetakJantung.setCellValueFactory(new PropertyValueFactory<>("detakJantung"));
        colSuhuTubuh.setCellValueFactory(new PropertyValueFactory<>("suhuTubuh"));
        colWaktuPencatatan.setCellValueFactory(new PropertyValueFactory<>("waktuPencatatan"));

        loadKondisiAktualTable();
    }

    private void loadKondisiAktualTable() {
        List<KondisiAktual> list = kondisiAktualDAO.getAllKondisiAktual();
        ObservableList<KondisiAktual> obsList = FXCollections.observableArrayList(list);
        tableKondisiAktual.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadKondisiAktualTable();
    }

    // (Opsional) onAddKondisiAktual(), onEditKondisiAktual(), onDeleteKondisiAktual() dapat ditambahkan sesuai kebutuhan.
}