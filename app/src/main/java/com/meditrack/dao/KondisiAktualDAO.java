package com.meditrack.dao;

import com.meditrack.model.KondisiAktual;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KondisiAktualDAO {

    /** 1. Ambil semua data dari tabel kondisiaktual */
    public List<KondisiAktual> getAllKondisiAktual() {
        List<KondisiAktual> list = new ArrayList<>();
        String sql = "SELECT * FROM kondisiaktual";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KondisiAktual ka = new KondisiAktual(
                        rs.getInt("idKondisi"),
                        rs.getInt("idPengguna"),
                        rs.getString("tekananDarah"),
                        rs.getInt("detakJantung"),
                        rs.getDouble("suhuTubuh"),
                        rs.getString("waktuPencatatan")
                );
                list.add(ka);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data kondisiaktual berdasarkan ID */
    public KondisiAktual getKondisiAktualById(int id) {
        String sql = "SELECT * FROM kondisiaktual WHERE idKondisi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KondisiAktual(
                        rs.getInt("idKondisi"),
                        rs.getInt("idPengguna"),
                        rs.getString("tekananDarah"),
                        rs.getInt("detakJantung"),
                        rs.getDouble("suhuTubuh"),
                        rs.getString("waktuPencatatan")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data kondisiaktual baru (INSERT) */
    public boolean insertKondisiAktual(KondisiAktual ka) {
        String sql = "INSERT INTO kondisiaktual(idPengguna, tekananDarah, detakJantung, suhuTubuh, waktuPencatatan) " +
                "VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ka.getIdPengguna());
            ps.setString(2, ka.getTekananDarah());
            ps.setInt(3, ka.getDetakJantung());
            ps.setDouble(4, ka.getSuhuTubuh());
            ps.setString(5, ka.getWaktuPencatatan());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                ka.setIdKondisi(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data kondisiaktual (UPDATE) */
    public boolean updateKondisiAktual(KondisiAktual ka) {
        String sql = "UPDATE kondisiaktual SET idPengguna = ?, tekananDarah = ?, detakJantung = ?, suhuTubuh = ?, waktuPencatatan = ? " +
                "WHERE idKondisi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ka.getIdPengguna());
            ps.setString(2, ka.getTekananDarah());
            ps.setInt(3, ka.getDetakJantung());
            ps.setDouble(4, ka.getSuhuTubuh());
            ps.setString(5, ka.getWaktuPencatatan());
            ps.setInt(6, ka.getIdKondisi());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data kondisiaktual (DELETE) */
    public boolean deleteKondisiAktual(int id) {
        String sql = "DELETE FROM kondisiaktual WHERE idKondisi = ?";
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