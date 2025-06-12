package com.meditrack.dao;

import com.meditrack.model.Jadwal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JadwalDAO yang bersih untuk operasi CRUD pada tabel 'jadwal'.
 * Semua metode menggunakan satu koneksi yang di-inject melalui konstruktor.
 * Sudah disesuaikan dengan model Jadwal dan controller yang ada.
 */
public class JadwalDAO {

    private final Connection connection;

    public JadwalDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Menyimpan objek Jadwal baru ke database.
     * @param jadwal Objek Jadwal yang akan disimpan.
     */
    public void addJadwal(Jadwal jadwal) throws SQLException {
        String sql = "INSERT INTO jadwal (id_pengguna, nama_aktivitas, tanggal_mulai, jam_mulai, tanggal_selesai, jam_selesai, kategori, catatan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, 1);  // user ID sementara (kalau ada login ganti sesuai session)
            pstmt.setString(2, jadwal.getNamaAktivitas());
            pstmt.setString(3, jadwal.getTanggalMulai().toString());
            pstmt.setString(4, jadwal.getWaktuMulai().toString());
            pstmt.setString(5, jadwal.getTanggalSelesai().toString());
            pstmt.setString(6, jadwal.getWaktuSelesai().toString());
            pstmt.setString(7, jadwal.getKategori());
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

    /**
     * Mengambil semua jadwal pada tanggal tertentu.
     * @param date Tanggal yang dicari.
     * @return List dari objek Jadwal pada tanggal tersebut.
     */
    public List<Jadwal> getJadwalByDate(LocalDate date) throws SQLException {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = "SELECT * FROM jadwal WHERE tanggal_mulai = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jadwalList.add(mapResultSetToJadwal(rs));
                }
            }
        }
        return jadwalList;
    }

    // ✅ DITAMBAHKAN: Method yang dibutuhkan oleh JadwalPenggunaController
    /**
     * Mengambil satu jadwal berdasarkan ID uniknya.
     * @param id ID dari jadwal yang akan dicari.
     * @return Optional berisi objek Jadwal jika ditemukan, atau Optional kosong jika tidak.
     */
    public Optional<Jadwal> getJadwalById(int id) throws SQLException {
        String sql = "SELECT * FROM jadwal WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToJadwal(rs));
                }
            }
        }
        return Optional.empty();
    }

    // ✅ DITAMBAHKAN: Method yang dibutuhkan oleh JadwalPenggunaController
    /**
     * Memeriksa apakah ada jadwal pada tanggal tertentu.
     * @param date Tanggal yang akan diperiksa.
     * @return true jika ada jadwal, false jika tidak.
     */
    public boolean hasJadwalOnDate(LocalDate date) throws SQLException {
        String sql = "SELECT 1 FROM jadwal WHERE tanggal_mulai = ? LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Mengupdate data jadwal yang ada di database.
     * @param jadwal Objek Jadwal yang berisi data baru dan ID yang akan diupdate.
     */
    public void updateJadwal(Jadwal jadwal) throws SQLException {
        String sql = "UPDATE jadwal SET nama_aktivitas = ?, tanggal_mulai = ?, jam_mulai = ?, " +
                "tanggal_selesai = ?, jam_selesai = ?, kategori = ?, catatan = ? " +
                "WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, jadwal.getNamaAktivitas());
            pstmt.setString(2, jadwal.getTanggalMulai().toString());
            pstmt.setString(3, jadwal.getWaktuMulai().toString());
            pstmt.setString(4, jadwal.getTanggalSelesai().toString());
            pstmt.setString(5, jadwal.getWaktuSelesai().toString());
            pstmt.setString(6, jadwal.getKategori());
            pstmt.setString(7, jadwal.getCatatan());
            pstmt.setInt(8, jadwal.getId());
            pstmt.executeUpdate();
        }
    }


    /**
     * Menghapus jadwal dari database berdasarkan ID-nya.
     *
     * @param id ID dari jadwal yang akan dihapus.
     * @return
     */
    public boolean deleteJadwal(int id) {
        try {
            System.out.println("Deleting jadwal ID: " + id);

            // Simple delete tanpa foreign key check
            String sql = "DELETE FROM jadwal WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int affected = ps.executeUpdate();

                System.out.println("Delete affected rows: " + affected);
                return affected > 0;
            }

        } catch (SQLException e) {
            System.err.println("Delete error: " + e.getMessage());

            // Jika error foreign key, coba dengan disable dulu
            try {
                connection.createStatement().execute("PRAGMA foreign_keys = OFF");

                String sql = "DELETE FROM jadwal WHERE id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int affected = ps.executeUpdate();

                    connection.createStatement().execute("PRAGMA foreign_keys = ON");

                    return affected > 0;
                }

            } catch (SQLException e2) {
                System.err.println("Fallback delete also failed: " + e2.getMessage());
                return false;
            }
        }
    }

    /**
     * Helper method untuk memetakan satu baris ResultSet ke objek Jadwal.
     * @param rs ResultSet yang sedang di-iterasi.
     * @return Objek Jadwal yang sudah terisi data.
     */
    private Jadwal mapResultSetToJadwal(ResultSet rs) throws SQLException {
        // Nama kolom harus sama persis dengan yang ada di tabel database Anda
        int id = rs.getInt("id");
        String namaAktivitas = rs.getString("nama_aktivitas");
        LocalDate tanggalMulai = LocalDate.parse(rs.getString("tanggal_mulai"));
        LocalTime waktuMulai = LocalTime.parse(rs.getString("jam_mulai"));
        LocalTime waktuSelesai = LocalTime.parse(rs.getString("jam_selesai"));
        LocalDate tanggalSelesai = LocalDate.parse(rs.getString("tanggal_selesai"));
        String kategori = rs.getString("kategori");
        String catatan = rs.getString("catatan");

        // Menggunakan constructor yang sesuai (tanpa id_pengguna)
        return new Jadwal(id, namaAktivitas, tanggalMulai, waktuMulai, tanggalSelesai, waktuSelesai, kategori, catatan);
    }

    public void debugDatabaseStructure() {
        try {
            System.out.println("=== DEBUG DATABASE STRUCTURE ===");
            // Cek struktur tabel jadwal
            ResultSet rs = connection.getMetaData().getColumns(null, null, "jadwal", null);
            System.out.println("Kolom tabel jadwal:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + ")");
            }

            // Cek data yang ada
            Statement stmt = connection.createStatement();
            ResultSet dataRs = stmt.executeQuery("SELECT * FROM jadwal LIMIT 5");
            System.out.println("\nSample data jadwal:");
            while (dataRs.next()) {
                System.out.println("ID: " + dataRs.getInt(1) + ", Nama: " + dataRs.getString(2));
            }
            System.out.println("================================");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}