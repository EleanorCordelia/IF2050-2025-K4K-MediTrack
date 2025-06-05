package com.meditrack.model;

public class Dokter {
    private int idDokter;
    private String nama;
    private String email;
    private String spesialisasi;
    private String nomorSTR;

    // Konstruktor kosong (dibutuhkan, misalnya, untuk framework atau inisialisasi manual)
    public Dokter() { }

    // Konstruktor lengkap (misalnya untuk memetakan hasil ResultSet ke objek Dokter)
    public Dokter(int idDokter, String nama, String email, String spesialisasi, String nomorSTR) {
        this.idDokter = idDokter;
        this.nama = nama;
        this.email = email;
        this.spesialisasi = spesialisasi;
        this.nomorSTR = nomorSTR;
    }

    // Getter & Setter

    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public String getNomorSTR() {
        return nomorSTR;
    }

    public void setNomorSTR(String nomorSTR) {
        this.nomorSTR = nomorSTR;
    }
}