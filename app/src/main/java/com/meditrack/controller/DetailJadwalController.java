package com.meditrack.controller;

import com.meditrack.dao.DetailJadwalDAO;
import com.meditrack.model.DetailJadwal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DetailJadwalController {

    @FXML private TableView<DetailJadwal> tableDetailJadwal;
    @FXML private TableColumn<DetailJadwal, Integer> colIdDetail;
    @FXML private TableColumn<DetailJadwal, Integer> colIdJadwal;
    @FXML private TableColumn<DetailJadwal, String>  colWaktuMulai;
    @FXML private TableColumn<DetailJadwal, String>  colWaktuSelesai;
    @FXML private TableColumn<DetailJadwal, String>  colDeskripsi;
    @FXML private TableColumn<DetailJadwal, String>  colKategori;

    private final DetailJadwalDAO detailJadwalDAO = new DetailJadwalDAO();

    @FXML
    public void initialize() {
        // Binding kolom ke properti model DetailJadwal
        colIdDetail.setCellValueFactory(new PropertyValueFactory<>("idDetailJadwal"));
        colIdJadwal.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        colWaktuMulai.setCellValueFactory(new PropertyValueFactory<>("waktuMulai"));
        colWaktuSelesai.setCellValueFactory(new PropertyValueFactory<>("waktuSelesai"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        // Muat data awal
        loadDetailJadwalTable();
    }

    private void loadDetailJadwalTable() {
        List<DetailJadwal> list = detailJadwalDAO.getAllDetailJadwal();
        ObservableList<DetailJadwal> obsList = FXCollections.observableArrayList(list);
        tableDetailJadwal.setItems(obsList);
    }

    @FXML
    private void onRefresh() {
        loadDetailJadwalTable();
    }

    // Metode onAddDetailJadwal(), onEditDetailJadwal(), onDeleteDetailJadwal() bisa ditambahkan sesuai kebutuhan
}