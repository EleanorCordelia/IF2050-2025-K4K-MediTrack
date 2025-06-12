package com.meditrack.dao;

import com.meditrack.model.Rekomendasi;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RekomendasiDAO {

    /** 1. Ambil semua data dari tabel rekomendasi */
    public List<Rekomendasi> getAllRekomendasi() {
        List<Rekomendasi> list = new ArrayList<>();
        String sql = "SELECT * FROM rekomendasi";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rekomendasi r = new Rekomendasi(
                        rs.getInt("idRekomendasi"),
                        rs.getInt("idPengguna"),
                        rs.getInt("idObat"),
                        rs.getString("alasan"),
                        rs.getString("tanggalRekomendasi"),
                        rs.getString("statusRekomendasi")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data rekomendasi berdasarkan ID */
    public Rekomendasi getRekomendasiById(int id) {
        String sql = "SELECT * FROM rekomendasi WHERE idRekomendasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Rekomendasi(
                        rs.getInt("idRekomendasi"),
                        rs.getInt("idPengguna"),
                        rs.getInt("idObat"),
                        rs.getString("alasan"),
                        rs.getString("tanggalRekomendasi"),
                        rs.getString("statusRekomendasi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data rekomendasi baru (INSERT) */
    public boolean insertRekomendasi(Rekomendasi r) {
        String sql = "INSERT INTO rekomendasi(idPengguna, idObat, alasan, tanggalRekomendasi, statusRekomendasi) " +
                "VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getIdPengguna());
            ps.setInt(2, r.getIdObat());
            ps.setString(3, r.getAlasan());
            ps.setString(4, r.getTanggalRekomendasi());
            ps.setString(5, r.getStatusRekomendasi());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                r.setIdRekomendasi(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data rekomendasi (UPDATE) */
    public boolean updateRekomendasi(Rekomendasi r) {
        String sql = "UPDATE rekomendasi SET idPengguna = ?, idObat = ?, alasan = ?, tanggalRekomendasi = ?, statusRekomendasi = ? " +
                "WHERE idRekomendasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getIdPengguna());
            ps.setInt(2, r.getIdObat());
            ps.setString(3, r.getAlasan());
            ps.setString(4, r.getTanggalRekomendasi());
            ps.setString(5, r.getStatusRekomendasi());
            ps.setInt(6, r.getIdRekomendasi());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data rekomendasi (DELETE) */
    public boolean deleteRekomendasi(int id) {
        String sql = "DELETE FROM rekomendasi WHERE idRekomendasi = ?";
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