package com.meditrack.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model Jadwal yang bersih dan konsisten.
 * Kelas ini mewakili satu baris data dari tabel 'jadwal' di database.
 */
public class Jadwal {

    private int idJadwal;
    private int idPengguna;
    private String namaAktivitas;
    private LocalDate tanggalMulai;
    private LocalTime waktuMulai;
    private LocalDate tanggalSelesai;
    private LocalTime waktuSelesai;
    private String tingkatAktivitas; // Menggantikan 'prioritas'
    private String catatan;

    /**
     * Konstruktor untuk membuat objek Jadwal baru dari UI (sebelum disimpan ke DB).
     * ID jadwal dan pengguna akan diatur oleh DAO atau logika bisnis.
     */
    public Jadwal(String namaAktivitas, LocalDate tanggalMulai, LocalTime waktuMulai, LocalDate tanggalSelesai, LocalTime waktuSelesai, String tingkatAktivitas, String catatan) {
        this.namaAktivitas = namaAktivitas;
        this.tanggalMulai = tanggalMulai;
        this.waktuMulai = waktuMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.waktuSelesai = waktuSelesai;
        this.tingkatAktivitas = tingkatAktivitas;
        this.catatan = catatan;
    }

    /**
     * Konstruktor lengkap untuk membuat objek Jadwal dari data yang ada di DB.
     */
    public Jadwal(int idJadwal, int idPengguna, String namaAktivitas, LocalDate tanggalMulai, LocalTime waktuMulai, LocalDate tanggalSelesai, LocalTime waktuSelesai, String tingkatAktivitas, String catatan) {
        this.idJadwal = idJadwal;
        this.idPengguna = idPengguna;
        this.namaAktivitas = namaAktivitas;
        this.tanggalMulai = tanggalMulai;
        this.waktuMulai = waktuMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.waktuSelesai = waktuSelesai;
        this.tingkatAktivitas = tingkatAktivitas;
        this.catatan = catatan;
    }

    // --- GETTERS ---
    public int getIdJadwal() { return idJadwal; }
    public int getIdPengguna() { return idPengguna; }
    public String getNamaAktivitas() { return namaAktivitas; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public LocalTime getWaktuMulai() { return waktuMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public LocalTime getWaktuSelesai() { return waktuSelesai; }
    public String getTingkatAktivitas() { return tingkatAktivitas; }
    public String getCatatan() { return catatan; }

    // --- SETTERS ---
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }
    public void setNamaAktivitas(String namaAktivitas) { this.namaAktivitas = namaAktivitas; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public void setWaktuMulai(LocalTime waktuMulai) { this.waktuMulai = waktuMulai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    public void setWaktuSelesai(LocalTime waktuSelesai) { this.waktuSelesai = waktuSelesai; }
    public void setTingkatAktivitas(String tingkatAktivitas) { this.tingkatAktivitas = tingkatAktivitas; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}