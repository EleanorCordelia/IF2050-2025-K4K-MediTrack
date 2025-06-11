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
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
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
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 3. Masukkan data obat baru (INSERT) */
    public boolean insertDaftarObat(DaftarObat o) {
        String sql = "INSERT INTO daftarobat(namaObat, dosis, waktuKonsumsi, efekSamping, statusKonsumsi, deskripsi, caraKonsumsi, urutan_konsumsi) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, o.getNamaObat());
            ps.setString(2, o.getDosis());
            ps.setString(3, o.getWaktuKonsumsi());
            ps.setString(4, o.getEfekSamping());
            ps.setString(5, o.getStatusKonsumsi());
            ps.setString(6, o.getDeskripsi());
            ps.setString(7, o.getCaraKonsumsi());
            ps.setInt(8, o.getUrutanKonsumsi());

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
        String sql = "UPDATE daftarobat SET namaObat = ?, dosis = ?, waktuKonsumsi = ?, efekSamping = ?, statusKonsumsi = ?, deskripsi = ?, caraKonsumsi = ?, urutan_konsumsi = ? " +
                "WHERE idObat = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, o.getNamaObat());
            ps.setString(2, o.getDosis());
            ps.setString(3, o.getWaktuKonsumsi());
            ps.setString(4, o.getEfekSamping());
            ps.setString(5, o.getStatusKonsumsi());
            ps.setString(6, o.getDeskripsi());
            ps.setString(7, o.getCaraKonsumsi());
            ps.setInt(8, o.getUrutanKonsumsi());
            ps.setInt(9, o.getIdObat());

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

    public DaftarObat getObatById(int idObat) {
        String sql = "SELECT * FROM daftarobat WHERE idObat = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idObat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DaftarObat getRandomObat() {
        String sql = "SELECT * FROM daftarobat ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DaftarObat> getAllObat() {
        List<DaftarObat> listObat = new ArrayList<>();
        String query = "SELECT * FROM daftarobat";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DaftarObat obat = new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
                );
                listObat.add(obat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listObat;
    }

    /** 6. Cari data obat berdasarkan keyword */
    public List<DaftarObat> cariObatByKeyword(String keyword) {
        List<DaftarObat> result = new ArrayList<>();
        String sql = "SELECT * FROM daftarobat WHERE LOWER(namaObat) LIKE ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DaftarObat obat = new DaftarObat(
                        rs.getInt("idObat"),
                        rs.getString("namaObat"),
                        rs.getString("dosis"),
                        rs.getString("waktuKonsumsi"),
                        rs.getString("efekSamping"),
                        rs.getString("statusKonsumsi"),
                        rs.getString("deskripsi"),
                        rs.getString("caraKonsumsi"),
                        rs.getInt("urutan_konsumsi")
                );
                result.add(obat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
