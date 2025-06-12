package com.meditrack.dao;

import com.meditrack.model.KondisiHistoris;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KondisiHistorisDAO {

    /** 1. Ambil semua data dari tabel kondisihistoris */
    public List<KondisiHistoris> getAllKondisiHistoris() {
        List<KondisiHistoris> list = new ArrayList<>();
        String sql = "SELECT * FROM kondisihistoris";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KondisiHistoris kh = new KondisiHistoris(
                        rs.getInt("idHistori"),
                        rs.getInt("idPengguna"),
                        rs.getString("tanggalAwal"),
                        rs.getString("tanggalAkhir"),
                        rs.getString("ringkasan")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data kondisihistoris berdasarkan ID */
    public KondisiHistoris getKondisiHistorisById(int id) {
        String sql = "SELECT * FROM kondisihistoris WHERE idHistori = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KondisiHistoris(
                        rs.getInt("idHistori"),
                        rs.getInt("idPengguna"),
                        rs.getString("tanggalAwal"),
                        rs.getString("tanggalAkhir"),
                        rs.getString("ringkasan")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data kondisihistoris baru (INSERT) */
    public boolean insertKondisiHistoris(KondisiHistoris kh) {
        String sql = "INSERT INTO kondisihistoris(idPengguna, tanggalAwal, tanggalAkhir, ringkasan) " +
                "VALUES(?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, kh.getIdPengguna());
            ps.setString(2, kh.getTanggalAwal());
            ps.setString(3, kh.getTanggalAkhir());
            ps.setString(4, kh.getRingkasan());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                kh.setIdHistori(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data kondisihistoris (UPDATE) */
    public boolean updateKondisiHistoris(KondisiHistoris kh) {
        String sql = "UPDATE kondisihistoris SET idPengguna = ?, tanggalAwal = ?, tanggalAkhir = ?, ringkasan = ? " +
                "WHERE idHistori = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kh.getIdPengguna());
            ps.setString(2, kh.getTanggalAwal());
            ps.setString(3, kh.getTanggalAkhir());
            ps.setString(4, kh.getRingkasan());
            ps.setInt(5, kh.getIdHistori());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data kondisihistoris (DELETE) */
    public boolean deleteKondisiHistoris(int id) {
        String sql = "DELETE FROM kondisihistoris WHERE idHistori = ?";
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