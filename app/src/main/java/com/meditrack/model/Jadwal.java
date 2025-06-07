package com.meditrack.model;

public class Jadwal {
    private int idJadwal;
    private int idPengguna;
    private String tanggal;   // disimpan sebagai 'YYYY-MM-DD'
    private String prioritas; // 'Tinggi', 'Sedang', atau 'Rendah'

    // Konstruktor kosong (diperlukan misalnya untuk inisialisasi manual atau framework)
    public Jadwal() { }

    // Konstruktor lengkap (misalnya untuk memetakan ResultSet ke objek Jadwal)
    public Jadwal(int idJadwal, int idPengguna, String tanggal, String prioritas) {
        this.idJadwal = idJadwal;
        this.idPengguna = idPengguna;
        this.tanggal = tanggal;
        this.prioritas = prioritas;
    }


    // Getter & Setter

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }
}