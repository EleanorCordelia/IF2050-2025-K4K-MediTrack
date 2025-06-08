package com.meditrack.model;

public class Rekomendasi {
    private int idRekomendasi;
    private int idPengguna;
    private int idObat;
    private String alasan;
    private String tanggalRekomendasi;  // format 'YYYY-MM-DD'
    private String statusRekomendasi;   // 'Diajukan', 'Disetujui', atau 'Ditolak'

    // Tambahan properti (hasil parsing atau dari DB jika tersedia)
    private String namaObat;
    private String dosis;

    // Konstruktor kosong
    public Rekomendasi() { }

    // Konstruktor lengkap
    public Rekomendasi(int idRekomendasi, int idPengguna, int idObat,
                       String alasan, String tanggalRekomendasi, String statusRekomendasi) {
        this.idRekomendasi = idRekomendasi;
        this.idPengguna = idPengguna;
        this.idObat = idObat;
        this.alasan = alasan;
        this.tanggalRekomendasi = tanggalRekomendasi;
        this.statusRekomendasi = statusRekomendasi;

        // Parse namaObat dan dosis dari alasan jika formatnya konsisten
        parseAlasan();
    }

    /** Tambahkan parser namaObat dan dosis dari alasan (format: "Obat - Detail alasan") */
    private void parseAlasan() {
        if (this.alasan != null && this.alasan.contains(" - ")) {
            String[] parts = this.alasan.split(" - ", 2);
            this.namaObat = parts[0].trim();
            this.dosis = "1 tablet, sesudah sarapan"; // default dummy, bisa diupdate dari DB jika ada field dosis
        } else {
            this.namaObat = this.alasan;
            this.dosis = "";
        }
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
        parseAlasan();
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

    // Getter untuk namaObat
    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    // Getter untuk dosis
    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }
}
