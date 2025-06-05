package com.meditrack.dao;

import com.meditrack.model.Jadwal;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {

    /** 1. Ambil semua data dari tabel jadwal */
    public List<Jadwal> getAllJadwal() {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jadwal j = new Jadwal(
                        rs.getInt("idJadwal"),
                        rs.getInt("idPengguna"),
                        rs.getString("tanggal"),
                        rs.getString("prioritas")
                );
                list.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data jadwal berdasarkan ID */
    public Jadwal getJadwalById(int id) {
        String sql = "SELECT * FROM jadwal WHERE idJadwal = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Jadwal(
                        rs.getInt("idJadwal"),
                        rs.getInt("idPengguna"),
                        rs.getString("tanggal"),
                        rs.getString("prioritas")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data jadwal baru (INSERT) */
    public boolean insertJadwal(Jadwal j) {
        String sql = "INSERT INTO jadwal(idPengguna, tanggal, prioritas) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, j.getIdPengguna());
            ps.setString(2, j.getTanggal());
            ps.setString(3, j.getPrioritas());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                j.setIdJadwal(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data jadwal (UPDATE) */
    public boolean updateJadwal(Jadwal j) {
        String sql = "UPDATE jadwal SET idPengguna = ?, tanggal = ?, prioritas = ? WHERE idJadwal = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, j.getIdPengguna());
            ps.setString(2, j.getTanggal());
            ps.setString(3, j.getPrioritas());
            ps.setInt(4, j.getIdJadwal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data jadwal (DELETE) */
    public boolean deleteJadwal(int id) {
        String sql = "DELETE FROM jadwal WHERE idJadwal = ?";
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