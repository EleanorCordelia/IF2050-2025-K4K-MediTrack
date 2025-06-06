package com.meditrack.model;

import java.time.LocalDate; // Import untuk tipe data LocalDate

public class Dokter {
    private int idDokter; // Sebaiknya gunakan Integer jika ID bisa null atau belum ter-assign
    private String nama;
    private String email;
    private String spesialisasi;
    private String nomorSTR;

    // Field tambahan yang dibutuhkan oleh DokterFormController
    private String jenisKelamin;
    private LocalDate tanggalLahir; // Menggunakan tipe data LocalDate
    private String nomorTelepon;
    private String alamat;

    // Konstruktor kosong
    public Dokter() {
    }

    // Konstruktor untuk inisialisasi sebagian field (opsional, bisa disesuaikan)
    public Dokter(int idDokter, String nama, String email, String spesialisasi, String nomorSTR) {
        this.idDokter = idDokter;
        this.nama = nama;
        this.email = email;
        this.spesialisasi = spesialisasi;
        this.nomorSTR = nomorSTR;
    }

    // Konstruktor yang lebih lengkap (opsional, bisa disesuaikan)
    public Dokter(int idDokter, String nama, String email, String spesialisasi, String nomorSTR,
                  String jenisKelamin, LocalDate tanggalLahir, String nomorTelepon, String alamat) {
        this.idDokter = idDokter;
        this.nama = nama;
        this.email = email;
        this.spesialisasi = spesialisasi;
        this.nomorSTR = nomorSTR;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.nomorTelepon = nomorTelepon;
        this.alamat = alamat;
    }

    // --- Getter & Setter ---

    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    // Pertimbangkan menggunakan Integer untuk idDokter jika ID bisa null
    // public Integer getIdDokter() { return idDokter; }
    // public void setIdDokter(Integer idDokter) { this.idDokter = idDokter; }


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

    // Getter & Setter untuk field tambahan
    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    // (Opsional) Override toString untuk representasi string yang lebih informatif
    @Override
    public String toString() {
        return "Dokter{" +
                "idDokter=" + idDokter +
                ", nama='" + nama + '\'' +
                ", spesialisasi='" + spesialisasi + '\'' +
                ", nomorSTR='" + nomorSTR + '\'' +
                ", jenisKelamin='" + jenisKelamin + '\'' +
                ", tanggalLahir=" + tanggalLahir +
                '}';
    }
}