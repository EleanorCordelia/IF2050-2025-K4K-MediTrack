package com.meditrack.controller;

import com.meditrack.dao.JadwalDAO;
import com.meditrack.model.Jadwal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class JadwalController implements Initializable {

    // Sesuaikan fx:id ini dengan file FXML Anda
    @FXML private TableView<Jadwal> tableJadwal;
    @FXML private TableColumn<Jadwal, Integer> colIdJadwal;
    @FXML private TableColumn<Jadwal, String> colNamaAktivitas; // Ditambahkan untuk informasi yang lebih baik
    @FXML private TableColumn<Jadwal, LocalDate> colTanggalMulai;
    @FXML private TableColumn<Jadwal, String> colTingkatAktivitas;
    @FXML private TableColumn<Jadwal, String> colCatatan; // Ditambahkan untuk informasi yang lebih baik

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mengatur bagaimana setiap kolom mengambil data dari objek Jadwal
        // Pastikan string di dalam PropertyValueFactory("") SAMA PERSIS dengan nama variabel di Jadwal.java
        colIdJadwal.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        colNamaAktivitas.setCellValueFactory(new PropertyValueFactory<>("namaAktivitas"));
        colTanggalMulai.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));
        colTingkatAktivitas.setCellValueFactory(new PropertyValueFactory<>("tingkatAktivitas"));
        colCatatan.setCellValueFactory(new PropertyValueFactory<>("catatan"));

        // Muat data awal saat controller pertama kali dibuka
        loadJadwalTable();
    }

    /**
     * Metode ini memuat atau memuat ulang data dari database SQLite ke TableView.
     * Menggunakan try-with-resources untuk memastikan koneksi selalu ditutup.
     */
    private void loadJadwalTable() {
        // Menggunakan path relatif ke file database SQLite
        String dbURL = "jdbc:sqlite:meditrack.db";

        // try-with-resources memastikan koneksi ditutup secara otomatis
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            JadwalDAO jadwalDAO = new JadwalDAO(connection);

            // getAllJadwal() sekarang dipanggil di dalam blok try, sehingga SQLException tertangani
            List<Jadwal> list = jadwalDAO.getAllJadwal();

            ObservableList<Jadwal> obsList = FXCollections.observableArrayList(list);
            tableJadwal.setItems(obsList);

        } catch (SQLException e) {
            // Menampilkan pesan error jika gagal terhubung atau mengambil data
            System.err.println("Error saat memuat data jadwal dari SQLite: " + e.getMessage());
            e.printStackTrace();
            showAlert("Database Error", "Gagal memuat data jadwal. Pastikan file database ada dan tidak rusak.");
        }
    }

    /**
     * Dipanggil saat tombol refresh di-klik.
     */
    @FXML
    private void onRefresh() {
        System.out.println("Memuat ulang data jadwal...");
        loadJadwalTable();
    }

    /**
     * Helper method untuk menampilkan alert.
     * @param title Judul window alert.
     * @param content Pesan yang akan ditampilkan.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Anda bisa menambahkan metode lain seperti onAddJadwal(), onEditJadwal(), onDeleteJadwal() di sini.
}