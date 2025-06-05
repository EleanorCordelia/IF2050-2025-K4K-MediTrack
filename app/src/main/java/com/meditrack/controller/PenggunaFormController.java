package com.meditrack.controller;

import com.meditrack.model.Pengguna;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PenggunaFormController {

    @FXML private TextField tfNama;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private TextField tfTanggal;
    @FXML private ComboBox<String> cbJenisKel;
    @FXML private TextField tfTinggi;
    @FXML private TextField tfBerat;

    private Pengguna pengguna;
    private boolean isEdit = false;

    public void setPengguna(Pengguna p) {
        if (p != null) {
            this.pengguna = p;
            this.isEdit = true;

            // Fill form dengan data existing
            tfNama.setText(p.getNama());
            tfEmail.setText(p.getEmail());
            tfPassword.setText(p.getPassword());
            tfTanggal.setText(p.getTanggalLahir());
            cbJenisKel.setValue(p.getJenisKelamin());
            tfTinggi.setText(p.getTinggiBadan() != null ? p.getTinggiBadan().toString() : "");
            tfBerat.setText(p.getBeratBadan() != null ? p.getBeratBadan().toString() : "");
        } else {
            this.pengguna = new Pengguna();
            this.isEdit = false;
        }
    }

    public Pengguna getPenggunaFromForm() {
        // Validasi input
        if (tfNama.getText().trim().isEmpty() ||
                tfEmail.getText().trim().isEmpty() ||
                tfPassword.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Nama, Email, dan Password harus diisi!", ButtonType.OK);
            alert.showAndWait();
            return null;
        }

        // Set data ke objek pengguna
        pengguna.setNama(tfNama.getText().trim());
        pengguna.setEmail(tfEmail.getText().trim());
        pengguna.setPassword(tfPassword.getText().trim());
        pengguna.setTanggalLahir(tfTanggal.getText().trim());
        pengguna.setJenisKelamin(cbJenisKel.getValue());

        // Parse tinggi dan berat
        try {
            String tinggiStr = tfTinggi.getText().trim();
            if (!tinggiStr.isEmpty()) {
                pengguna.setTinggiBadan(Double.parseDouble(tinggiStr));
            } else {
                pengguna.setTinggiBadan(null);
            }
        } catch (NumberFormatException e) {
            pengguna.setTinggiBadan(null);
        }

        try {
            String beratStr = tfBerat.getText().trim();
            if (!beratStr.isEmpty()) {
                pengguna.setBeratBadan(Double.parseDouble(beratStr));
            } else {
                pengguna.setBeratBadan(null);
            }
        } catch (NumberFormatException e) {
            pengguna.setBeratBadan(null);
        }

        return pengguna;
    }
}