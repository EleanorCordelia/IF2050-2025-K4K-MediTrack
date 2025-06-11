package com.meditrack.dao;

import com.meditrack.model.Konsultasi;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KonsultasiDAO {

    /** 1. Ambil semua data dari tabel konsultasi */
    public List<Konsultasi> getAllKonsultasi() {
        List<Konsultasi> list = new ArrayList<>();
        String sql = "SELECT * FROM konsultasi";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Konsultasi k = new Konsultasi(
                        rs.getInt("idKonsultasi"),
                        rs.getInt("idPengguna"),
                        rs.getInt("idDokter"),
                        rs.getString("waktu"),
                        rs.getString("topik")
                );
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data konsultasi berdasarkan ID */
    public Konsultasi getKonsultasiById(int id) {
        String sql = "SELECT * FROM konsultasi WHERE idKonsultasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Konsultasi(
                        rs.getInt("idKonsultasi"),
                        rs.getInt("idPengguna"),
                        rs.getInt("idDokter"),
                        rs.getString("waktu"),
                        rs.getString("topik")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data konsultasi baru (INSERT) */
    public boolean insertKonsultasi(Konsultasi k) {
        String sql = "INSERT INTO konsultasi(idPengguna, idDokter, waktu, topik) VALUES(?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, k.getIdPengguna());
            ps.setInt(2, k.getIdDokter());
            ps.setString(3, k.getWaktu());
            ps.setString(4, k.getTopik());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                k.setIdKonsultasi(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data konsultasi (UPDATE) */
    public boolean updateKonsultasi(Konsultasi k) {
        String sql = "UPDATE konsultasi SET idPengguna = ?, idDokter = ?, waktu = ?, topik = ? WHERE idKonsultasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, k.getIdPengguna());
            ps.setInt(2, k.getIdDokter());
            ps.setString(3, k.getWaktu());
            ps.setString(4, k.getTopik());
            ps.setInt(5, k.getIdKonsultasi());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data konsultasi (DELETE) */
    public boolean deleteKonsultasi(int id) {
        String sql = "DELETE FROM konsultasi WHERE idKonsultasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}