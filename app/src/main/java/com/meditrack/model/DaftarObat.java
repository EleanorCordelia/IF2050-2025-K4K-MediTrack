package com.meditrack.model;

public class DaftarObat {
    private int idObat;
    private String namaObat;
    private String dosis;
    private String waktuKonsumsi;   // format 'HH:MM:SS'
    private String efekSamping;
    private String statusKonsumsi;  // 'Dikonsumsi', 'Belum dikonsumsi', atau 'Terlewat'

    // Konstruktor kosong (dibutuhkan misalnya untuk inisialisasi manual atau framework)
    public DaftarObat() { }

    // Konstruktor lengkap (digunakan misalnya pada pemetaan ResultSet)
    public DaftarObat(int idObat, String namaObat, String dosis,
                      String waktuKonsumsi, String efekSamping, String statusKonsumsi) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.dosis = dosis;
        this.waktuKonsumsi = waktuKonsumsi;
        this.efekSamping = efekSamping;
        this.statusKonsumsi = statusKonsumsi;
    }

    // Getter & Setter

    public int getIdObat() {
        return idObat;
    }

    public void setIdObat(int idObat) {
        this.idObat = idObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getWaktuKonsumsi() {
        return waktuKonsumsi;
    }

    public void setWaktuKonsumsi(String waktuKonsumsi) {
        this.waktuKonsumsi = waktuKonsumsi;
    }

    public String getEfekSamping() {
        return efekSamping;
    }

    public void setEfekSamping(String efekSamping) {
        this.efekSamping = efekSamping;
    }

    public String getStatusKonsumsi() {
        return statusKonsumsi;
    }

    public void setStatusKonsumsi(String statusKonsumsi) {
        this.statusKonsumsi = statusKonsumsi;
    }
}
