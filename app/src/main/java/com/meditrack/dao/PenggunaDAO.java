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
        String sql = "UPDATE pengguna SET nama = ?, email = ?, password = ?, tanggalLahir = ?, jenisKelamin = ?, tinggiBadan = ?, beratBadan = ? WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pengguna.getNama());
            ps.setString(2, pengguna.getEmail());
            ps.setString(3, pengguna.getPassword());
            ps.setString(4, pengguna.getTanggalLahir());
            ps.setString(5, pengguna.getJenisKelamin());
            if (pengguna.getTinggiBadan() != null) {
                ps.setDouble(6, pengguna.getTinggiBadan());
            } else {
                ps.setNull(6, Types.REAL);
            }
            if (pengguna.getBeratBadan() != null) {
                ps.setDouble(7, pengguna.getBeratBadan());
            } else {
                ps.setNull(7, Types.REAL);
            }
            ps.setInt(8, pengguna.getIdPengguna());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePengguna(int idPengguna) {
        String selectSql = "SELECT * FROM pengguna WHERE idPengguna = ?";
        String deleteSql = "DELETE FROM pengguna WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement selectPs = conn.prepareStatement(selectSql);
             PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
            selectPs.setInt(1, idPengguna);
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                deletePs.setInt(1, idPengguna);
                int affectedRows = deletePs.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Pengguna berhasil dihapus dengan ID: " + idPengguna);
                    return true;
                } else {
                    System.out.println("Gagal menghapus pengguna dengan ID: " + idPengguna);
                    return false;
                }
            } else {
                System.out.println("Pengguna dengan ID " + idPengguna + " tidak ditemukan.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan saat menghapus pengguna: " + e.getMessage());
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

    public Pengguna getPenggunaByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM pengguna WHERE email = ? AND password = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
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
