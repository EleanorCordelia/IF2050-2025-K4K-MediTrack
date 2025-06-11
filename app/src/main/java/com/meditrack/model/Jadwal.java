package com.meditrack.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model Jadwal yang bersih dan konsisten.
 * Kelas ini mewakili satu baris data dari tabel 'jadwal' di database.
 * Versi ini disesuaikan agar kompatibel dengan TambahJadwalController.
 */
public class Jadwal {

    private int id; // Diubah dari idJadwal
    private String namaAktivitas;
    private LocalDate tanggalMulai;
    private LocalTime waktuMulai;
    private LocalDate tanggalSelesai;
    private LocalTime waktuSelesai;
    private String kategori; // Diubah dari tingkatAktivitas
    private String catatan;
    // Field idPengguna untuk sementara tidak digunakan di form ini, jadi bisa diabaikan di constructor

    /**
     * Konstruktor untuk membuat objek Jadwal baru dari UI (sebelum disimpan ke DB).
     * Cocok dengan logika 'Tambah Baru' di TambahJadwalController.
     */
    public Jadwal(String namaAktivitas, LocalDate tanggalMulai, LocalTime waktuMulai, LocalDate tanggalSelesai, LocalTime waktuSelesai, String kategori, String catatan) {
        this.namaAktivitas = namaAktivitas;
        this.tanggalMulai = tanggalMulai;
        this.waktuMulai = waktuMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.waktuSelesai = waktuSelesai;
        this.kategori = kategori;
        this.catatan = catatan;
    }

    /**
     * Konstruktor lengkap untuk membuat objek Jadwal dari data yang ada di DB.
     * Cocok dengan logika 'Edit' di TambahJadwalController.
     */
    public Jadwal(int id, String namaAktivitas, LocalDate tanggalMulai, LocalTime waktuMulai, LocalDate tanggalSelesai, LocalTime waktuSelesai, String kategori, String catatan) {
        this.id = id;
        this.namaAktivitas = namaAktivitas;
        this.tanggalMulai = tanggalMulai;
        this.waktuMulai = waktuMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.waktuSelesai = waktuSelesai;
        this.kategori = kategori;
        this.catatan = catatan;
    }

    // --- GETTERS & SETTERS ---

    public int getId() { // Diubah dari getIdJadwal
        return id;
    }

    public void setId(int id) { // Diubah dari setIdJadwal
        this.id = id;
    }

    public String getNamaAktivitas() {
        return namaAktivitas;
    }

    public void setNamaAktivitas(String namaAktivitas) {
        this.namaAktivitas = namaAktivitas;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalTime getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(LocalTime waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public LocalTime getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(LocalTime waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getKategori() { // Diubah dari getTingkatAktivitas
        return kategori;
    }

    public void setKategori(String kategori) { // Diubah dari setTingkatAktivitas
        this.kategori = kategori;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}