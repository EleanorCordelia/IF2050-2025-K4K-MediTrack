package com.meditrack.controller;

import com.meditrack.model.Dokter; // Pastikan Anda memiliki kelas model ini
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; // Hanya jika Anda perlu akses langsung ke tombol dari kode
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class DokterFormController {

    @FXML
    private TextField namaDokterField;

    @FXML
    private TextField spesialisasiField;

    @FXML
    private TextField nomorSTRField;

    @FXML
    private ComboBox<String> jenisKelaminComboBox;

    @FXML
    private DatePicker tanggalLahirPicker;

    @FXML
    private TextField nomorTeleponField;

    @FXML
    private TextField alamatField;

    // fx:id untuk tombol tidak wajib dideklarasikan di sini jika
    // Anda sudah menghubungkan onAction di FXML ke metode handleSimpan/handleBatal.
    // @FXML private Button simpanButton;
    // @FXML private Button batalButton;

    private Dokter dokterToProcess; // Objek dokter yang sedang diproses (baik untuk tambah maupun edit)
    private boolean isEditMode = false;
    private Dokter resultDokter = null; // Hasil dokter setelah form ditutup (null jika batal/validasi gagal)

    @FXML
    private void initialize() {
        // Inisialisasi ComboBox Jenis Kelamin
        jenisKelaminComboBox.setItems(FXCollections.observableArrayList("Laki-laki", "Perempuan"));

        // Jika Anda tidak mengatur onAction pada tombol di file FXML,
        // Anda bisa mengaturnya di sini:
        // simpanButton.setOnAction(event -> handleSimpan());
        // batalButton.setOnAction(event -> handleBatal());
    }

    /**
     * Mengisi form dengan data dokter yang ada (untuk mode edit)
     * atau menyiapkan form untuk input baru (mode tambah).
     * @param dokter Objek Dokter yang akan diedit, atau null untuk mode tambah.
     */
    public void setDokter(Dokter dokter) {
        this.resultDokter = null; // Reset hasil setiap kali form diset/dibuka
        if (dokter != null) {
            this.dokterToProcess = dokter; // Gunakan instance yang ada untuk diedit
            this.isEditMode = true;

            namaDokterField.setText(dokter.getNama());
            spesialisasiField.setText(dokter.getSpesialisasi());
            nomorSTRField.setText(dokter.getNomorSTR());
            jenisKelaminComboBox.setValue(dokter.getJenisKelamin());
            tanggalLahirPicker.setValue(dokter.getTanggalLahir());
            nomorTeleponField.setText(dokter.getNomorTelepon());
            alamatField.setText(dokter.getAlamat());
            // Field seperti ID dan Email (jika ada di model Dokter) akan tetap ada di objek dokterToProcess
            // meskipun tidak ditampilkan di form ini.
        } else {
            this.dokterToProcess = new Dokter(); // Buat instance baru untuk ditambahkan
            this.isEditMode = false;
            clearFields();
        }
    }

    private void clearFields() {
        namaDokterField.clear();
        spesialisasiField.clear();
        nomorSTRField.clear();
        jenisKelaminComboBox.getSelectionModel().clearSelection();
        jenisKelaminComboBox.setPromptText("Pilih Jenis Kelamin");
        tanggalLahirPicker.setValue(null);
        nomorTeleponField.clear();
        alamatField.clear();
    }

    /**
     * Dipanggil ketika tombol "Simpan" ditekan.
     * Pastikan tombol "Simpan" di FXML memiliki atribut: onAction="#handleSimpan"
     */
    @FXML
    private void handleSimpan() {
        String nama = namaDokterField.getText();
        String spesialisasi = spesialisasiField.getText();
        String nomorSTR = nomorSTRField.getText();
        String jenisKelamin = jenisKelaminComboBox.getValue();
        LocalDate tanggalLahir = tanggalLahirPicker.getValue();
        String nomorTelepon = nomorTeleponField.getText();
        String alamat = alamatField.getText();

        // Validasi Dasar (Contoh)
        if (!validateInput(nama, "Nama Dokter", namaDokterField) ||
                !validateInput(spesialisasi, "Spesialisasi", spesialisasiField) ||
                !validateInput(nomorSTR, "Nomor STR", nomorSTRField) ||
                jenisKelamin == null ||
                tanggalLahir == null) {
            if (jenisKelamin == null) showAlertError("Validasi Gagal", "Jenis Kelamin harus dipilih.");
            if (tanggalLahir == null) showAlertError("Validasi Gagal", "Tanggal Lahir tidak boleh kosong.");
            this.resultDokter = null; // Pastikan null jika validasi gagal
            return; // Hentikan proses jika validasi gagal
        }

        // Isi atau update objek dokterToProcess
        dokterToProcess.setNama(nama);
        dokterToProcess.setSpesialisasi(spesialisasi);
        dokterToProcess.setNomorSTR(nomorSTR);
        dokterToProcess.setJenisKelamin(jenisKelamin);
        dokterToProcess.setTanggalLahir(tanggalLahir);
        dokterToProcess.setNomorTelepon(nomorTelepon);
        dokterToProcess.setAlamat(alamat);

        // Atribut seperti idDokter akan sudah ada di dokterToProcess jika mode edit.
        // Untuk email, jika tidak ada di form, nilainya akan tetap seperti semula (jika edit)
        // atau null/default (jika tambah dan tidak di-set dari sumber lain).

        this.resultDokter = dokterToProcess; // Simpan dokter yang sudah diproses untuk diambil oleh pemanggil
        closeStage();
    }

    /**
     * Dipanggil ketika tombol "Batal" ditekan.
     * Pastikan tombol "Batal" di FXML memiliki atribut: onAction="#handleBatal"
     */
    @FXML
    private void handleBatal() {
        this.resultDokter = null; // Pastikan null jika dibatalkan
        closeStage();
    }

    /**
     * Mengembalikan objek Dokter yang telah diisi dari form setelah tombol "Simpan" ditekan
     * dan validasi berhasil. Mengembalikan null jika form dibatalkan atau validasi gagal.
     * @return Objek Dokter atau null.
     */
    public Dokter getDokterFromForm() {
        return resultDokter;
    }

    private boolean validateInput(String input, String fieldName, Node inputNode) {
        if (input == null || input.trim().isEmpty()) {
            showAlertError("Validasi Gagal", fieldName + " tidak boleh kosong.");
            if (inputNode != null) {
                inputNode.requestFocus(); // Fokuskan ke field yang error
            }
            return false;
        }
        return true;
    }

    private void closeStage() {
        // Cara aman untuk mendapatkan Stage: ambil dari salah satu node di scene
        Node node = namaDokterField; // Bisa node apa saja yang ada di scene form
        if (node != null && node.getScene() != null && node.getScene().getWindow() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlertError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}