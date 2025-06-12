package com.meditrack.model;

public class KondisiHistoris {
    private int idHistori;
    private int idPengguna;
    private String tanggalAwal;   // format 'YYYY-MM-DD'
    private String tanggalAkhir;  // format 'YYYY-MM-DD'
    private String ringkasan;

    // Konstruktor kosong (diperlukan misalnya untuk inisialisasi manual atau framework)
    public KondisiHistoris() { }

    // Konstruktor lengkap (digunakan misalnya pada pemetaan ResultSet ke objek KondisiHistoris)
    public KondisiHistoris(int idHistori, int idPengguna, String tanggalAwal, String tanggalAkhir, String ringkasan) {
        this.idHistori = idHistori;
        this.idPengguna = idPengguna;
        this.tanggalAwal = tanggalAwal;
        this.tanggalAkhir = tanggalAkhir;
        this.ringkasan = ringkasan;
    }

    // Getter & Setter

    public int getIdHistori() {
        return idHistori;
    }

    public void setIdHistori(int idHistori) {
        this.idHistori = idHistori;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public String getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(String tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }

    public String getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setTanggalAkhir(String tanggalAkhir) {
        this.tanggalAkhir = tanggalAkhir;
    }

    public String getRingkasan() {
        return ringkasan;
    }

    public void setRingkasan(String ringkasan) {
        this.ringkasan = ringkasan;
    }
}