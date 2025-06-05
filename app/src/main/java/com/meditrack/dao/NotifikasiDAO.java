package com.meditrack.dao;

import com.meditrack.model.Notifikasi;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotifikasiDAO {

    /** 1. Ambil semua data dari tabel notifikasi */
    public List<Notifikasi> getAllNotifikasi() {
        List<Notifikasi> list = new ArrayList<>();
        String sql = "SELECT * FROM notifikasi";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Notifikasi n = new Notifikasi(
                        rs.getInt("idNotifikasi"),
                        rs.getInt("idPengguna"),
                        rs.getString("isi"),
                        rs.getBoolean("statusBaca"),
                        rs.getString("waktuDikirim")
                );
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari notifikasi berdasarkan ID */
    public Notifikasi getNotifikasiById(int id) {
        String sql = "SELECT * FROM notifikasi WHERE idNotifikasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Notifikasi(
                        rs.getInt("idNotifikasi"),
                        rs.getInt("idPengguna"),
                        rs.getString("isi"),
                        rs.getBoolean("statusBaca"),
                        rs.getString("waktuDikirim")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan notifikasi baru (INSERT) */
    public boolean insertNotifikasi(Notifikasi n) {
        String sql = "INSERT INTO notifikasi(idPengguna, isi, statusBaca, waktuDikirim) VALUES(?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, n.getIdPengguna());
            ps.setString(2, n.getIsi());
            ps.setBoolean(3, n.isStatusBaca());
            ps.setString(4, n.getWaktuDikirim());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                n.setIdNotifikasi(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Update notifikasi (UPDATE) */
    public boolean updateNotifikasi(Notifikasi n) {
        String sql = "UPDATE notifikasi SET idPengguna = ?, isi = ?, statusBaca = ?, waktuDikirim = ? WHERE idNotifikasi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, n.getIdPengguna());
            ps.setString(2, n.getIsi());
            ps.setBoolean(3, n.isStatusBaca());
            ps.setString(4, n.getWaktuDikirim());
            ps.setInt(5, n.getIdNotifikasi());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus notifikasi (DELETE) */
    public boolean deleteNotifikasi(int id) {
        String sql = "DELETE FROM notifikasi WHERE idNotifikasi = ?";
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