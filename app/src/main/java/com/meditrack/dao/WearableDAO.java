package com.meditrack.dao;

import com.meditrack.model.Wearable;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WearableDAO {

    /** 1. Ambil semua data dari tabel wearables */
    public List<Wearable> getAllWearables() {
        List<Wearable> list = new ArrayList<>();
        String sql = "SELECT * FROM wearables";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Wearable w = new Wearable(
                        rs.getInt("idWearable"),
                        rs.getString("tipe"),
                        rs.getString("versiFirmware"),
                        rs.getInt("idPengguna"),
                        rs.getBoolean("statusSinkron")
                );
                list.add(w);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data Wearable berdasarkan ID */
    public Wearable getWearableById(int id) {
        String sql = "SELECT * FROM wearables WHERE idWearable = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Wearable(
                        rs.getInt("idWearable"),
                        rs.getString("tipe"),
                        rs.getString("versiFirmware"),
                        rs.getInt("idPengguna"),
                        rs.getBoolean("statusSinkron")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data Wearable baru (INSERT) */
    public boolean insertWearable(Wearable w) {
        String sql = "INSERT INTO wearables(tipe, versiFirmware, idPengguna, statusSinkron) " +
                "VALUES(?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, w.getTipe());
            ps.setString(2, w.getVersiFirmware());
            ps.setInt(3, w.getIdPengguna());
            ps.setBoolean(4, w.isStatusSinkron());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                w.setIdWearable(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data Wearable (UPDATE) */
    public boolean updateWearable(Wearable w) {
        String sql = "UPDATE wearables SET tipe = ?, versiFirmware = ?, idPengguna = ?, statusSinkron = ? " +
                "WHERE idWearable = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, w.getTipe());
            ps.setString(2, w.getVersiFirmware());
            ps.setInt(3, w.getIdPengguna());
            ps.setBoolean(4, w.isStatusSinkron());
            ps.setInt(5, w.getIdWearable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data Wearable (DELETE) */
    public boolean deleteWearable(int id) {
        String sql = "DELETE FROM wearables WHERE idWearable = ?";
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