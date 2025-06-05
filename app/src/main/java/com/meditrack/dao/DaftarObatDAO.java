package com.meditrack.dao;

import com.meditrack.model.DaftarObat;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaftarObatDAO {

    /** 1. Ambil semua data dari tabel daftarobat */
    public List<DaftarObat> getAllDaftarObat() {
        List<DaftarObat> list = new ArrayList<>();
        String sql = "SELECT * FROM daftarobat";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DaftarObat o = new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi")
                );
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data obat berdasarkan ID */
    public DaftarObat getDaftarObatById(int id) {
        String sql = "SELECT * FROM daftarobat WHERE idObat = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data obat baru (INSERT) */
    public boolean insertDaftarObat(DaftarObat o) {
        String sql = "INSERT INTO daftarobat(namaObat, dosis, waktuKonsumsi, efekSamping, statusKonsumsi) " +
                "VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, o.getNamaObat());
            ps.setString(2, o.getDosis());
            ps.setString(3, o.getWaktuKonsumsi());
            ps.setString(4, o.getEfekSamping());
            ps.setString(5, o.getStatusKonsumsi());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                o.setIdObat(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data obat (UPDATE) */
    public boolean updateDaftarObat(DaftarObat o) {
        String sql = "UPDATE daftarobat SET namaObat = ?, dosis = ?, waktuKonsumsi = ?, efekSamping = ?, statusKonsumsi = ? " +
                "WHERE idObat = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, o.getNamaObat());
            ps.setString(2, o.getDosis());
            ps.setString(3, o.getWaktuKonsumsi());
            ps.setString(4, o.getEfekSamping());
            ps.setString(5, o.getStatusKonsumsi());
            ps.setInt(6, o.getIdObat());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data obat (DELETE) */
    public boolean deleteDaftarObat(int id) {
        String sql = "DELETE FROM daftarobat WHERE idObat = ?";
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