package com.meditrack.controller;

import com.meditrack.dao.KondisiAktualDAO;
import com.meditrack.model.KondisiAktual;
import com.meditrack.util.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;

// tambahkan ini:
import java.util.List;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;


public class KondisiAktualController {

    // ** TOP BAR **
    @FXML private Label lblUserName, lblUserRole;
    @FXML private Label lblLastUpdate;

    // ** BIOMETRIK CARDS **
    @FXML private Arc arcTekananDarah;
    @FXML private Label lblTekananDarahValue, lblTekananDarahStatus;

    @FXML private Arc arcDetakJantung;
    @FXML private Label lblDetakJantungValue, lblDetakJantungStatus;

    @FXML private Arc arcSuhuTubuh;
    @FXML private Label lblSuhuTubuhValue, lblSuhuTubuhStatus;

    @FXML private Arc arcStres;
    @FXML private Label lblStresValue, lblStresStatus;

    // ** PHYSICAL CARDS **
    @FXML private Arc arcTidur;
    @FXML private Label lblDurasiTidurValue, lblTidurStatus;

    @FXML private Arc arcOlahraga;
    @FXML private Label lblDurasiOlahragaValue, lblOlahragaStatus;

    @FXML private Arc arcLangkah;
    @FXML private Label lblJumlahLangkahValue, lblLangkahStatus;

    // ** INPUT FORM **
    @FXML private Button btnInputData, btnCancelInput, btnSimpanData;
    @FXML private VBox inputFormSection, validationMessages, successMessage;
    @FXML private Label lblValidationError, lblSuccessMessage;

    @FXML private TextField txtTekananDarah, txtDetakJantung, txtSuhuTubuh;
    @FXML private ComboBox<String> cbTingkatStres;
    @FXML private TextField txtDurasiTidur, txtDurasiOlahraga, txtJumlahLangkah;

    private final KondisiAktualDAO dao = new KondisiAktualDAO();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        // hide input form by default
        inputFormSection.setVisible(false);
        inputFormSection.setManaged(false);

        // load data dan tampilkan last update
        loadData();
    }

    private void loadData() {
        try {
            Integer userIdObj = Session.getCurrentUserId();
            if (userIdObj == null) {
                lblLastUpdate.setText("Terakhir diperbarui: --");
                return;
            }
            int currentUserId = userIdObj;
            List<KondisiAktual> list = dao.findByUser(currentUserId);
            if (!list.isEmpty()) {
                // ambil record terbaru
                KondisiAktual terbaru = list.get(0);
                lblLastUpdate.setText("Terakhir diperbarui: " +
                        terbaru.getTglPencatatan().toString());
            }
            // TODO: you could also bind data ini ke UI chart/card lain
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** handler tombol “Input Data Baru” */
    @FXML
    private void handleInputData(ActionEvent event) {
        inputFormSection.setVisible(true);
        inputFormSection.setManaged(true);
    }

    /** handler tombol “Batal” */
    @FXML
    private void handleCancelInput(ActionEvent event) {
        inputFormSection.setVisible(false);
        inputFormSection.setManaged(false);
    }

    /** handler tombol “Simpan Data” */
    @FXML
    private void handleSimpanData(ActionEvent event) {
        try {
            Integer userIdObj = Session.getCurrentUserId();
            if (userIdObj == null) {
                return;
            }
            KondisiAktual k = new KondisiAktual();
            k.setUserId(userIdObj);
            k.setTekananDarah(txtTekananDarah.getText());
            k.setDetakJantung(Integer.parseInt(txtDetakJantung.getText()));
            k.setSuhuTubuh(Double.parseDouble(txtSuhuTubuh.getText()));
            k.setTingkatStres(cbTingkatStres.getValue());
            k.setDurasiTidur(Double.parseDouble(txtDurasiTidur.getText()));
            k.setDurasiOlahraga(Integer.parseInt(txtDurasiOlahraga.getText()));
            k.setJumlahLangkah(Integer.parseInt(txtJumlahLangkah.getText()));
            dao.save(k);
            loadData();            // refresh last update
            handleCancelInput(null);// sembunyikan form
            // TODO: tampilkan success message
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO: tampilkan error validation
        }
    }

    /** handler dropdown profile (misal: logout, edit profil) */
    @FXML
    private void handleProfileDropdown(ActionEvent event) {
        // TODO: tampilkan ContextMenu / pilihan logout, dsb.
    }

    private void loadLatestData() {
        KondisiAktual k = dao.getLatestByUserId(Session.getCurrentUserId());
        if (k == null) {
            lblLastUpdate.setText("Terakhir diperbarui: --");
            lblTekananDarahStatus.setText("Belum ada data");
            lblDetakJantungStatus.setText("Belum ada data");
            lblSuhuTubuhStatus.setText("Belum ada data");
            lblStresStatus.setText("Belum ada data");
            lblTidurStatus.setText("Belum ada data");
            lblOlahragaStatus.setText("Belum ada data");
            lblLangkahStatus.setText("Belum ada data");
            return;
        }

        // Tanggal
        lblLastUpdate.setText("Terakhir diperbarui: " + dtf.format(k.getTglPencatatan()));

        // Tekanan Darah
        lblTekananDarahValue.setText(k.getTekananDarah());
        lblTekananDarahStatus.setText("OK");
        arcTekananDarah.setLength(-Double.parseDouble(k.getTekananDarah().split("/")[0]) / 200 * 360);

        // Detak Jantung
        lblDetakJantungValue.setText(k.getDetakJantung().toString());
        lblDetakJantungStatus.setText("OK");
        arcDetakJantung.setLength(-k.getDetakJantung() / 120.0 * 360);

        // Suhu Tubuh
        lblSuhuTubuhValue.setText(String.format("%.1f", k.getSuhuTubuh()));
        lblSuhuTubuhStatus.setText("OK");
        arcSuhuTubuh.setLength(- (k.getSuhuTubuh() - 35) / 5 * 360);

        // Stres
        lblStresValue.setText(k.getTingkatStres());
        lblStresStatus.setText("OK");
        int persenStres = switch(k.getTingkatStres()) {
            case "Rendah" -> 30;
            case "Sedang" -> 60;
            default -> 90;
        };
        arcStres.setLength(- persenStres / 100.0 * 360);

        // Durasi Tidur
        lblDurasiTidurValue.setText(String.format("%.1f", k.getDurasiTidur()));
        lblTidurStatus.setText("OK");
        arcTidur.setLength(- k.getDurasiTidur() / 12 * 360);

        // Olahraga
        lblDurasiOlahragaValue.setText(k.getDurasiOlahraga().toString());
        lblOlahragaStatus.setText("OK");
        arcOlahraga.setLength(- k.getDurasiOlahraga() / 120.0 * 360);

        // Langkah
        lblJumlahLangkahValue.setText(k.getJumlahLangkah().toString());
        lblLangkahStatus.setText("OK");
        arcLangkah.setLength(- k.getJumlahLangkah() / 10000.0 * 360);
    }

    private void showInputForm() {
        inputFormSection.setVisible(true);
        inputFormSection.setManaged(true);
    }

    private void hideInputForm() {
        inputFormSection.setVisible(false);
        inputFormSection.setManaged(false);
        validationMessages.setVisible(false);
        successMessage.setVisible(false);
    }

    private void simpanData() {
        validationMessages.setVisible(false);
        successMessage.setVisible(false);

        // Validasi
        try {
            String td      = txtTekananDarah.getText().trim();
            int dj         = Integer.parseInt(txtDetakJantung.getText().trim());
            double st      = Double.parseDouble(txtSuhuTubuh.getText().trim());
            String ts      = cbTingkatStres.getValue();
            double dtidur  = Double.parseDouble(txtDurasiTidur.getText().trim());
            int dolahr     = Integer.parseInt(txtDurasiOlahraga.getText().trim());
            int langkah    = Integer.parseInt(txtJumlahLangkah.getText().trim());

            if (td.isEmpty() || ts == null) throw new IllegalArgumentException();

            KondisiAktual k = new KondisiAktual(
                    Session.getCurrentUserId(),
                    td, dj, st, ts, dtidur, dolahr, langkah
            );
            if (dao.insert(k)) {
                hideInputForm();
                loadLatestData();
                lblSuccessMessage.setText("Data berhasil disimpan!");
                successMessage.setVisible(true);
            } else {
                throw new RuntimeException("Gagal menyimpan ke database");
            }

        } catch (Exception ex) {
            lblValidationError.setText("Periksa kembali isian Anda");
            validationMessages.setVisible(true);
        }
    }
}