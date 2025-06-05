package com.meditrack.model;

public class Konsultasi {
    private int idKonsultasi;
    private int idPengguna;
    private int idDokter;
    private String waktu;  // format 'YYYY-MM-DD HH:MM:SS'
    private String topik;

    // Konstruktor kosong (diperlukan misalnya untuk inisialisasi manual atau framework)
    public Konsultasi() { }

    // Konstruktor lengkap (digunakan misalnya pada pemetaan ResultSet)
    public Konsultasi(int idKonsultasi, int idPengguna, int idDokter, String waktu, String topik) {
        this.idKonsultasi = idKonsultasi;
        this.idPengguna = idPengguna;
        this.idDokter = idDokter;
        this.waktu = waktu;
        this.topik = topik;
    }

    // Getter & Setter

    public int getIdKonsultasi() {
        return idKonsultasi;
    }

    public void setIdKonsultasi(int idKonsultasi) {
        this.idKonsultasi = idKonsultasi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTopik() {
        return topik;
    }

    public void setTopik(String topik) {
        this.topik = topik;
    }
}