package com.meditrack.controller;

import com.meditrack.dao.WearableDAO;
import com.meditrack.model.Wearable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class WearableController {

    @FXML private TableView<Wearable> tableWearables;
    @FXML private TableColumn<Wearable, Integer>    colIdWearable;
    @FXML private TableColumn<Wearable, String>     colTipe;
    @FXML private TableColumn<Wearable, String>     colVersiFirmware;
    @FXML private TableColumn<Wearable, Integer>    colIdPengguna;
    @FXML private TableColumn<Wearable, Boolean>    colStatusSinkron;

    private final WearableDAO wearableDAO = new WearableDAO();

    @FXML
    public void initialize() {
        // Binding kolom ke properti model Wearable
        colIdWearable.setCellValueFactory(new PropertyValueFactory<>("idWearable"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colVersiFirmware.setCellValueFactory(new PropertyValueFactory<>("versiFirmware"));
        colIdPengguna.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colStatusSinkron.setCellValueFactory(new PropertyValueFactory<>("statusSinkron"));

        // Muat data awal
        loadWearablesTable();
    }

    private void loadWearablesTable() {
        List<Wearable> list = wearableDAO.getAllWearables();
        ObservableList<Wearable> obsList = FXCollections.observableArrayList(list);
        tableWearables.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadWearablesTable();
    }

    // Metode onAddWearable(), onEditWearable(), onDeleteWearable() bisa ditambahkan sesuai kebutuhan
}