package com.meditrack.dao;

import com.meditrack.model.Dokter;
import com.meditrack.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {

    /** 1. Ambil semua data dari tabel dokter */
    public List<Dokter> getAllDokter() {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter";
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dokter d = new Dokter(
                        rs.getInt("idDokter"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("spesialisasi"),
                        rs.getString("nomorSTR")
                );
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data dokter berdasarkan ID */
    public Dokter getDokterById(int id) {
        String sql = "SELECT * FROM dokter WHERE idDokter = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Dokter(
                        rs.getInt("idDokter"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("spesialisasi"),
                        rs.getString("nomorSTR")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data dokter baru (INSERT) */
    public boolean insertDokter(Dokter d) {
        String sql = "INSERT INTO dokter(nama, email, spesialisasi, nomorSTR) VALUES(?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getNama());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getSpesialisasi());
            ps.setString(4, d.getNomorSTR());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                d.setIdDokter(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 4. Perbarui data dokter (UPDATE) */
    public boolean updateDokter(Dokter d) {
        String sql = "UPDATE dokter SET nama = ?, email = ?, spesialisasi = ?, nomorSTR = ? WHERE idDokter = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getNama());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getSpesialisasi());
            ps.setString(4, d.getNomorSTR());
            ps.setInt(5, d.getIdDokter());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 5. Hapus data dokter (DELETE) */
    public boolean deleteDokter(int id) {
        String sql = "DELETE FROM dokter WHERE idDokter = ?";
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