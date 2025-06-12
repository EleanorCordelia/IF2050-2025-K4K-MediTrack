package com.meditrack.model;

public class DaftarObat {
    private int idObat;
    private String namaObat;
    private String dosis;
    private String waktuKonsumsi;
    private String efekSamping;
    private String statusKonsumsi;
    private String deskripsi;
    private String caraKonsumsi;
    private int urutanKonsumsi;

    // Konstruktor kosong
    public DaftarObat() { }

    // Konstruktor lengkap (9 parameter)
    public DaftarObat(int idObat, String namaObat, String dosis, String waktuKonsumsi,
                      String efekSamping, String statusKonsumsi,
                      String deskripsi, String caraKonsumsi, int urutanKonsumsi) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.dosis = dosis;
        this.waktuKonsumsi = waktuKonsumsi;
        this.efekSamping = efekSamping;
        this.statusKonsumsi = statusKonsumsi;
        this.deskripsi = deskripsi;
        this.caraKonsumsi = caraKonsumsi;
        this.urutanKonsumsi = urutanKonsumsi;
    }

    // Konstruktor 6 parameter (kompatibel dengan kode lama)
    public DaftarObat(int idObat, String namaObat, String dosis, String waktuKonsumsi,
                      String efekSamping, String statusKonsumsi) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.dosis = dosis;
        this.waktuKonsumsi = waktuKonsumsi;
        this.efekSamping = efekSamping;
        this.statusKonsumsi = statusKonsumsi;
        this.deskripsi = "";
        this.caraKonsumsi = "";
        this.urutanKonsumsi = 0;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCaraKonsumsi() {
        return caraKonsumsi;
    }

    public void setCaraKonsumsi(String caraKonsumsi) {
        this.caraKonsumsi = caraKonsumsi;
    }

    public int getUrutanKonsumsi() {
        return urutanKonsumsi;
    }

    public void setUrutanKonsumsi(int urutanKonsumsi) {
        this.urutanKonsumsi = urutanKonsumsi;
    }
}
