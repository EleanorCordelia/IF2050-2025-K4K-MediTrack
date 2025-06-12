package com.meditrack.model;

public class Notifikasi {
    private int idNotifikasi;
    private int idPengguna;
    private String isi;
    private boolean statusBaca;
    private String waktuDikirim; // format 'YYYY-MM-DD HH:MM:SS'

    // Konstruktor kosong (diperlukan misalnya untuk inisialisasi manual atau framework)
    public Notifikasi() { }

    // Konstruktor lengkap (untuk memetakan ResultSet ke objek Notifikasi)
    public Notifikasi(int idNotifikasi, int idPengguna, String isi, boolean statusBaca, String waktuDikirim) {
        this.idNotifikasi = idNotifikasi;
        this.idPengguna = idPengguna;
        this.isi = isi;
        this.statusBaca = statusBaca;
        this.waktuDikirim = waktuDikirim;
    }

    // Getter & Setter

    public int getIdNotifikasi() {
        return idNotifikasi;
    }

    public void setIdNotifikasi(int idNotifikasi) {
        this.idNotifikasi = idNotifikasi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public boolean isStatusBaca() {
        return statusBaca;
    }

    public void setStatusBaca(boolean statusBaca) {
        this.statusBaca = statusBaca;
    }

    public String getWaktuDikirim() {
        return waktuDikirim;
    }

    public void setWaktuDikirim(String waktuDikirim) {
        this.waktuDikirim = waktuDikirim;
    }
}