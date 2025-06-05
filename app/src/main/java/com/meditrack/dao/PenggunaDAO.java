package com.meditrack.dao;

import com.meditrack.model.Pengguna;
import com.meditrack.util.SQLiteConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenggunaDAO {

    public List<Pengguna> getAllPengguna() {
        List<Pengguna> list = new ArrayList<>();
        String sql = "SELECT * FROM pengguna";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pengguna p = new Pengguna(
                        rs.getInt("idPengguna"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tanggalLahir"),
                        rs.getString("jenisKelamin"),
                        rs.getObject("tinggiBadan") != null ? rs.getDouble("tinggiBadan") : null,
                        rs.getObject("beratBadan") != null ? rs.getDouble("beratBadan") : null
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertPengguna(Pengguna p) {
        String sql = "INSERT INTO pengguna(nama, email, password, tanggalLahir, jenisKelamin, tinggiBadan, beratBadan) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNama());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getPassword());
            ps.setString(4, p.getTanggalLahir());
            ps.setString(5, p.getJenisKelamin());

            if (p.getTinggiBadan() != null) ps.setDouble(6, p.getTinggiBadan());
            else ps.setNull(6, Types.REAL);

            if (p.getBeratBadan() != null) ps.setDouble(7, p.getBeratBadan());
            else ps.setNull(7, Types.REAL);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setIdPengguna(keys.getInt(1));
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePengguna(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET nama = ?, email = ?, password = ?, tanggal_lahir = ?, jenis_kelamin = ?, tinggi_badan = ?, berat_badan = ?, avatarPath = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pengguna.getNama());
            ps.setString(2, pengguna.getEmail());
            ps.setString(3, pengguna.getPassword());
            ps.setString(4, pengguna.getTanggalLahir());
            ps.setString(5, pengguna.getJenisKelamin());
            ps.setDouble(6, pengguna.getTinggiBadan());
            ps.setDouble(7, pengguna.getBeratBadan());
            ps.setString(8, pengguna.getAvatarPath());
            ps.setInt(9, pengguna.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePengguna(int idPengguna) {
        String sql = "DELETE FROM pengguna WHERE idPengguna = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPengguna);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(int idPengguna, String newPassword) {
        String sql = "UPDATE pengguna SET password = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsername(int idPengguna, String newUsername) {
        String sql = "UPDATE pengguna SET nama = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pengguna getPenggunaById(int idPengguna) {
        String sql = "SELECT * FROM pengguna WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPengguna);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Pengguna(
                        rs.getInt("idPengguna"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("tanggalLahir"),
                        rs.getString("jenisKelamin"),
                        rs.getObject("tinggiBadan") != null ? rs.getDouble("tinggiBadan") : null,
                        rs.getObject("beratBadan") != null ? rs.getDouble("beratBadan") : null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
