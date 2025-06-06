package com.meditrack.dao;

import com.meditrack.model.DetailJadwal;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailJadwalDAO {

    /** 1. Ambil semua data dari tabel detailjadwal */
    public List<DetailJadwal> getAllDetailJadwal() {
        List<DetailJadwal> list = new ArrayList<>();
        String sql = "SELECT * FROM detailjadwal";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DetailJadwal dj = new DetailJadwal(
                        rs.getInt("idDetailJadwal"),
                        rs.getInt("idJadwal"),
                        rs.getString("waktuMulai"),
                        rs.getString("waktuSelesai"),
                        rs.getString("deskripsi"),
                        rs.getString("kategori")
                );
                list.add(dj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data detailjadwal berdasarkan ID */
    public DetailJadwal getDetailJadwalById(int id) {
        String sql = "SELECT * FROM detailjadwal WHERE idDetailJadwal = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DetailJadwal(
                        rs.getInt("idDetailJadwal"),
                        rs.getInt("idJadwal"),
                        rs.getString("waktuMulai"),
                        rs.getString("waktuSelesai"),
                        rs.getString("deskripsi"),
                        rs.getString("kategori")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data detailjadwal baru (INSERT) */
    public boolean insertDetailJadwal(DetailJadwal dj) {
        String sql = "INSERT INTO detailjadwal(idJadwal, waktuMulai, waktuSelesai, deskripsi, kategori) " +
                "VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dj.getIdJadwal());
            ps.setString(2, dj.getWaktuMulai());
            ps.setString(3, dj.getWaktuSelesai());
            ps.setString(4, dj.getDeskripsi());
            ps.setString(5, dj.getKategori());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                dj.setIdDetailJadwal(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data detailjadwal (UPDATE) */
    public boolean updateDetailJadwal(DetailJadwal dj) {
        String sql = "UPDATE detailjadwal SET idJadwal = ?, waktuMulai = ?, waktuSelesai = ?, deskripsi = ?, kategori = ? " +
                "WHERE idDetailJadwal = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dj.getIdJadwal());
            ps.setString(2, dj.getWaktuMulai());
            ps.setString(3, dj.getWaktuSelesai());
            ps.setString(4, dj.getDeskripsi());
            ps.setString(5, dj.getKategori());
            ps.setInt(6, dj.getIdDetailJadwal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data detailjadwal (DELETE) */
    public boolean deleteDetailJadwal(int id) {
        String sql = "DELETE FROM detailjadwal WHERE idDetailJadwal = ?";
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