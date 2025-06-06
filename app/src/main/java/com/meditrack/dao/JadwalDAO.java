package com.meditrack.dao;

import com.meditrack.model.Jadwal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JadwalDAO yang bersih untuk operasi CRUD pada tabel 'jadwal'.
 * Semua metode menggunakan satu koneksi yang di-inject melalui konstruktor.
 */
public class JadwalDAO {

    private final Connection connection;
    // Formatter untuk memastikan format waktu konsisten 'HH:mm:ss'
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public JadwalDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Menyimpan objek Jadwal baru ke database.
     * Menggunakan nama kolom yang benar dan format data yang sesuai untuk SQLite.
     * @param jadwal Objek Jadwal yang akan disimpan.
     */
    public void addJadwal(Jadwal jadwal) throws SQLException {
        // Nama kolom disesuaikan dengan skema database (menggunakan underscore)
        String sql = "INSERT INTO jadwal (id_pengguna, nama_aktivitas, tanggal_mulai, waktu_mulai, tanggal_selesai, waktu_selesai, tingkat_aktivitas, catatan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Mengatur parameter PreparedStatement
            // Untuk id_pengguna, idealnya didapat dari sesi login pengguna
            pstmt.setInt(1, 1); // Contoh: Hardcode id_pengguna = 1
            pstmt.setString(2, jadwal.getNamaAktivitas());
            // Menyimpan LocalDate dan LocalTime sebagai TEXT (String)
            pstmt.setString(3, jadwal.getTanggalMulai().toString());
            pstmt.setString(4, jadwal.getWaktuMulai().format(TIME_FORMATTER));
            pstmt.setString(5, jadwal.getTanggalSelesai().toString());
            pstmt.setString(6, jadwal.getWaktuSelesai().format(TIME_FORMATTER));
            pstmt.setString(7, jadwal.getTingkatAktivitas());
            pstmt.setString(8, jadwal.getCatatan());

            pstmt.executeUpdate();
        }
    }

    /**
     * Mengambil semua jadwal dari database.
     * @return List dari objek Jadwal.
     */
    public List<Jadwal> getAllJadwal() throws SQLException {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = "SELECT * FROM jadwal";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jadwalList.add(mapResultSetToJadwal(rs));
            }
        }
        return jadwalList;
    }

    // Anda juga bisa menambahkan getById, update, dan delete dengan pola yang sama.
    // ...

    /**
     * Helper method untuk memetakan satu baris ResultSet ke objek Jadwal.
     * @param rs ResultSet yang sedang diiterasi.
     * @return Objek Jadwal yang sudah terisi data.
     * @throws SQLException jika ada error saat membaca ResultSet.
     */
    private Jadwal mapResultSetToJadwal(ResultSet rs) throws SQLException {
        int idJadwal = rs.getInt("id_jadwal");
        int idPengguna = rs.getInt("id_pengguna");
        String namaAktivitas = rs.getString("nama_aktivitas");

        // Membaca TEXT dari DB dan mengubahnya kembali menjadi LocalDate/LocalTime
        LocalDate tanggalMulai = LocalDate.parse(rs.getString("tanggal_mulai"));
        LocalTime waktuMulai = LocalTime.parse(rs.getString("waktu_mulai"), TIME_FORMATTER);
        LocalDate tanggalSelesai = LocalDate.parse(rs.getString("tanggal_selesai"));
        LocalTime waktuSelesai = LocalTime.parse(rs.getString("waktu_selesai"), TIME_FORMATTER);

        String tingkatAktivitas = rs.getString("tingkat_aktivitas");
        String catatan = rs.getString("catatan");

        return new Jadwal(idJadwal, idPengguna, namaAktivitas, tanggalMulai, waktuMulai, tanggalSelesai, waktuSelesai, tingkatAktivitas, catatan);
    }
}