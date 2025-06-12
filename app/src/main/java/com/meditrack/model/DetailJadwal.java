package com.meditrack.model;

public class DetailJadwal {
    private int idDetailJadwal;
    private int idJadwal;
    private String waktuMulai;   // format 'HH:MM:SS'
    private String waktuSelesai; // format 'HH:MM:SS'
    private String deskripsi;
    private String kategori;     // 'Medis', 'Konsultasi', 'Teknis', 'Kesehatan', atau 'Rapat'

    // Konstruktor kosong (diperlukan misalnya untuk inisialisasi manual atau framework)
    public DetailJadwal() { }

    // Konstruktor lengkap (misalnya untuk memetakan ResultSet ke objek DetailJadwal)
    public DetailJadwal(
            int idDetailJadwal,
            int idJadwal,
            String waktuMulai,
            String waktuSelesai,
            String deskripsi,
            String kategori
    ) {
        this.idDetailJadwal = idDetailJadwal;
        this.idJadwal = idJadwal;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
    }

    // Getter & Setter

    public int getIdDetailJadwal() {
        return idDetailJadwal;
    }

    public void setIdDetailJadwal(int idDetailJadwal) {
        this.idDetailJadwal = idDetailJadwal;
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public String getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(String waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
