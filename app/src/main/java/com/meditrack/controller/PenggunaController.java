package com.meditrack.controller;

import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.Pengguna;
import javafx.beans.property.ReadOnlyObjectWrapper;    // ← import ini
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;                   // ← dan ini
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PenggunaController {

    @FXML private TableView<Pengguna> tablePengguna;
    @FXML private TableColumn<Pengguna, Integer> colId;
    @FXML private TableColumn<Pengguna, String> colNama;
    @FXML private TableColumn<Pengguna, String> colEmail;
    @FXML private TableColumn<Pengguna, String> colTanggal;
    @FXML private TableColumn<Pengguna, String> colJenisKel;
    @FXML private TableColumn<Pengguna, Double> colTinggi;
    @FXML private TableColumn<Pengguna, Double> colBerat;
    /** Tambahan: kolom avatarPath (string) */
    @FXML private TableColumn<Pengguna, String> colAvatarPath;
    /** Kalau mau langsung show gambarnya, gunakan ini: */
    @FXML private TableColumn<Pengguna, ImageView> colAvatarImg;

    private final PenggunaDAO penggunaDAO = new PenggunaDAO();

    @FXML
    public void initialize() {
        // Binding kolom ke properti model
        colId        .setCellValueFactory(new PropertyValueFactory<>("idPengguna"));
        colNama      .setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmail     .setCellValueFactory(new PropertyValueFactory<>("email"));
        colTanggal   .setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colJenisKel  .setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colTinggi    .setCellValueFactory(new PropertyValueFactory<>("tinggiBadan"));
        colBerat     .setCellValueFactory(new PropertyValueFactory<>("beratBadan"));
        colAvatarPath.setCellValueFactory(new PropertyValueFactory<>("avatarPath"));

        // Jika ingin menampilkan preview gambar di tabel:
        colAvatarImg.setCellValueFactory(cellData -> {
            String path = cellData.getValue().getAvatarPath();
            ImageView iv = new ImageView();
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            if (path != null && !path.isBlank()) {
                iv.setImage(new Image(path, true));
            } else {
                iv.setImage(new Image(
                        getClass().getResource("/images/avatar.png")
                                .toExternalForm()));
            }
            return new ReadOnlyObjectWrapper<>(iv);
        });

        loadPenggunaTable();
    }

    private void loadPenggunaTable() {
        List<Pengguna> list = penggunaDAO.getAllPengguna();
        ObservableList<Pengguna> obsList = FXCollections.observableArrayList(list);
        tablePengguna.setItems(obsList);
    }

    @FXML private void onRefresh() {
        loadPenggunaTable();
    }

    @FXML private void onAddPengguna() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/penggunaForm.fxml"));
            dialog.setDialogPane(loader.load());
            PenggunaFormController formController = loader.getController();
            formController.setPengguna(null); // mode tambah

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                Pengguna newP = formController.getPenggunaFromForm();
                if (newP != null) {
                    boolean sukses = penggunaDAO.insertPengguna(newP);
                    if (sukses) {
                        loadPenggunaTable();
                        showAlert("Sukses", "Data pengguna berhasil ditambahkan!", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "Gagal menyimpan data!", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onEditPengguna() {
        Pengguna selected = tablePengguna.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih baris data terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/penggunaForm.fxml"));
            dialog.setDialogPane(loader.load());
            PenggunaFormController formController = loader.getController();
            formController.setPengguna(selected); // mode edit

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                Pengguna edited = formController.getPenggunaFromForm();
                if (edited != null) {
                    boolean sukses = penggunaDAO.updatePengguna(edited);
                    if (sukses) {
                        loadPenggunaTable();
                        showAlert("Sukses", "Data pengguna berhasil diupdate!", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "Gagal mengupdate data!", Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onDeletePengguna() {
        Pengguna selected = tablePengguna.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih baris data terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus pengguna: " + selected.getNama() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            boolean sukses = penggunaDAO.deletePengguna(selected.getIdPengguna());
            if (sukses) {
                loadPenggunaTable();
                showAlert("Sukses", "Data pengguna berhasil dihapus!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Gagal menghapus data!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML private void onBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/landing.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}