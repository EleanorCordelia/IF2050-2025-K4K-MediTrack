package com.meditrack.model;

public class Rekomendasi {
    private int idRekomendasi;
    private int idPengguna;
    private int idObat;
    private String alasan;
    private String tanggalRekomendasi;  // format 'YYYY-MM-DD'
    private String statusRekomendasi;   // 'Diajukan', 'Disetujui', atau 'Ditolak'

    // Konstruktor kosong (dibutuhkan misalnya untuk inisialisasi manual atau framework)
    public Rekomendasi() { }

    // Konstruktor lengkap (misalnya untuk memetakan ResultSet ke objek Rekomendasi)
    public Rekomendasi(int idRekomendasi, int idPengguna, int idObat,
                       String alasan, String tanggalRekomendasi, String statusRekomendasi) {
        this.idRekomendasi = idRekomendasi;
        this.idPengguna = idPengguna;
        this.idObat = idObat;
        this.alasan = alasan;
        this.tanggalRekomendasi = tanggalRekomendasi;
        this.statusRekomendasi = statusRekomendasi;
    }

    // Getter & Setter

    public int getIdRekomendasi() {
        return idRekomendasi;
    }

    public void setIdRekomendasi(int idRekomendasi) {
        this.idRekomendasi = idRekomendasi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public int getIdObat() {
        return idObat;
    }

    public void setIdObat(int idObat) {
        this.idObat = idObat;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getTanggalRekomendasi() {
        return tanggalRekomendasi;
    }

    public void setTanggalRekomendasi(String tanggalRekomendasi) {
        this.tanggalRekomendasi = tanggalRekomendasi;
    }

    public String getStatusRekomendasi() {
        return statusRekomendasi;
    }

    public void setStatusRekomendasi(String statusRekomendasi) {
        this.statusRekomendasi = statusRekomendasi;
    }
}