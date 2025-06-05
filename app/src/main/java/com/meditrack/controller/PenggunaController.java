package com.meditrack.controller;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PenggunaController {

    @FXML private TableView<Pengguna> tablePengguna;
    @FXML private TableColumn<Pengguna, Integer> colId;
    @FXML private TableColumn<Pengguna, String>  colNama;
    @FXML private TableColumn<Pengguna, String>  colEmail;
    @FXML private TableColumn<Pengguna, String>  colTanggal;
    @FXML private TableColumn<Pengguna, String>  colJenisKel;
    @FXML private TableColumn<Pengguna, Double>  colTinggi;
    @FXML private TableColumn<Pengguna, Double>  colBerat;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colJenisKel.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colTinggi.setCellValueFactory(new PropertyValueFactory<>("tinggiBadan"));
        colBerat.setCellValueFactory(new PropertyValueFactory<>("beratBadan"));

        loadPenggunaTable();
    }

    private void loadPenggunaTable() {
        List<Pengguna> list = penggunaDAO.getAllPengguna();
        ObservableList<Pengguna> obsList = FXCollections.observableArrayList(list);
        tablePengguna.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadPenggunaTable();
    }

    // (Opsional) onAddPengguna(), onEditPengguna(), onDeletePengguna() dapat ditambahkan sesuai kebutuhan.
}