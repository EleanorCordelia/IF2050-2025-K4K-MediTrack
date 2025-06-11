package com.meditrack.dao;

import com.meditrack.model.KondisiAktual;
import com.meditrack.util.SQLiteConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KondisiAktualDAO {

<<<<<<< HEAD
    /** Cari semua record untuk satu user, urut DESC by tgl_pencatatan */
    public List<KondisiAktual> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM kondisi_aktual WHERE user_id = ? " +
                "ORDER BY tgl_pencatatan DESC";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<KondisiAktual> out = new ArrayList<>();

            while (rs.next()) {
                KondisiAktual k = mapResultSetToKondisiAktual(rs);
                out.add(k);
            }
            return out;
        }
    }

    /** Ambil entri terbaru untuk user tertentu */
    public KondisiAktual getLatestByUserId(Integer userId) {
        if (userId == null) return null;

        String sql = "SELECT * FROM kondisi_aktual WHERE user_id = ? " +
                "ORDER BY tgl_pencatatan DESC LIMIT 1";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKondisiAktual(rs);
=======
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
                        rs.getString("tingkatStres"),
                        rs.getDouble("durasiOlahraga"),
                        rs.getDouble("jumlahLangkah"),
                        rs.getString("waktuPencatatan")
                );
                list.add(ka);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 2. Cari data kondisiaktual berdasarkan ID pengguna */
    public List<KondisiAktual> getKondisiAktualByUserId(int userId) {
        List<KondisiAktual> list = new ArrayList<>();
        String sql = "SELECT * FROM kondisiaktual WHERE idPengguna = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KondisiAktual ka = new KondisiAktual(
                        rs.getInt("idKondisi"),
                        rs.getInt("idPengguna"),
                        rs.getString("tekananDarah"),
                        rs.getInt("detakJantung"),
                        rs.getDouble("suhuTubuh"),
                        rs.getString("tingkatStres"),
                        rs.getDouble("durasiOlahraga"),
                        rs.getDouble("jumlahLangkah"),
                        rs.getString("waktuPencatatan")
                );
                list.add(ka);
>>>>>>> rekomendasi
            }
        } catch (SQLException e) {
            System.err.println("Error getting latest data for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

<<<<<<< HEAD
    /** Simpan entri baru - gunakan ini untuk insert data baru */
    public boolean insert(KondisiAktual k) {
        if (k == null || k.getUserId() == null) return false;

        String sql = "INSERT INTO kondisi_aktual(" +
                "user_id, tekanan_darah, detak_jantung, suhu_tubuh, " +
                "tingkat_stres, durasi_tidur, durasi_olahraga, jumlah_langkah, tgl_pencatatan) " +
                "VALUES(?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, k.getUserId());
            ps.setString(2, k.getTekananDarah());
            ps.setInt(3, k.getDetakJantung());
            ps.setDouble(4, k.getSuhuTubuh());
            ps.setString(5, k.getTingkatStres());
            ps.setDouble(6, k.getDurasiTidur());
            ps.setInt(7, k.getDurasiOlahraga());
            ps.setInt(8, k.getJumlahLangkah());
=======
    /** 3. Masukkan data kondisiaktual baru (INSERT) */
    public boolean insertKondisiAktual(KondisiAktual ka) {
        String sql = "INSERT INTO kondisiaktual(idPengguna, tekananDarah, detakJantung, suhuTubuh, tingkatStres, durasiOlahraga, jumlahLangkah, waktuPencatatan) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ka.getIdPengguna());
            ps.setString(2, ka.getTekananDarah());
            ps.setInt(3, ka.getDetakJantung());
            ps.setDouble(4, ka.getSuhuTubuh());
            ps.setString(5, ka.getTingkatStres());
            ps.setDouble(6, ka.getDurasiOlahraga());
            ps.setDouble(7, ka.getJumlahLangkah());
            ps.setString(8, ka.getWaktuPencatatan());
>>>>>>> rekomendasi

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            // Set ID yang baru dibuat
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    k.setId(rs.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error inserting kondisi aktual: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

<<<<<<< HEAD
    /**
     * Method save() yang konsisten dengan controller
     * Ini akan selalu insert record baru (tidak update)
     */
    public boolean save(KondisiAktual k) {
        return insert(k);
    }

    /**
     * Method save() yang throws SQLException untuk kompatibilitas
     * dengan controller lama
     */
    public void save(KondisiAktual k, boolean throwException) throws SQLException {
        if (!insert(k)) {
            throw new SQLException("Failed to save KondisiAktual data");
        }
    }

    /** Update entri yang sudah ada berdasarkan ID */
    public boolean update(KondisiAktual k) {
        if (k == null || k.getId() == null) return false;

        String sql = "UPDATE kondisi_aktual SET " +
                "tekanan_darah = ?, detak_jantung = ?, suhu_tubuh = ?, " +
                "tingkat_stres = ?, durasi_tidur = ?, durasi_olahraga = ?, " +
                "jumlah_langkah = ?, tgl_pencatatan = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, k.getTekananDarah());
            ps.setInt(2, k.getDetakJantung());
            ps.setDouble(3, k.getSuhuTubuh());
            ps.setString(4, k.getTingkatStres());
            ps.setDouble(5, k.getDurasiTidur());
            ps.setInt(6, k.getDurasiOlahraga());
            ps.setInt(7, k.getJumlahLangkah());
            ps.setInt(8, k.getId());

            int affected = ps.executeUpdate();
            return affected > 0;
=======
    /** 4. Perbarui data kondisiaktual (UPDATE) */
    public boolean updateKondisiAktual(KondisiAktual ka) {
        String sql = "UPDATE kondisiaktual SET idPengguna = ?, tekananDarah = ?, detakJantung = ?, suhuTubuh = ?, " +
                "tingkatStres = ?, durasiOlahraga = ?, jumlahLangkah = ?, waktuPencatatan = ? " +
                "WHERE idKondisi = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ka.getIdPengguna());
            ps.setString(2, ka.getTekananDarah());
            ps.setInt(3, ka.getDetakJantung());
            ps.setDouble(4, ka.getSuhuTubuh());
            ps.setString(5, ka.getTingkatStres());
            ps.setDouble(6, ka.getDurasiOlahraga());
            ps.setDouble(7, ka.getJumlahLangkah());
            ps.setString(8, ka.getWaktuPencatatan());
            ps.setInt(9, ka.getIdKondisi());
>>>>>>> rekomendasi

        } catch (SQLException e) {
            System.err.println("Error updating kondisi aktual: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /** Hapus entri berdasarkan ID */
    public boolean delete(int id) {
        String sql = "DELETE FROM kondisi_aktual WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting kondisi aktual: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
<<<<<<< HEAD

    /** Hapus semua entri untuk user tertentu */
    public boolean deleteByUserId(int userId) {
        String sql = "DELETE FROM kondisi_aktual WHERE user_id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting kondisi aktual by user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /** Get data dalam rentang tanggal tertentu */
    public List<KondisiAktual> findByUserAndDateRange(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM kondisi_aktual WHERE user_id = ? " +
                "AND tgl_pencatatan BETWEEN ? AND ? " +
                "ORDER BY tgl_pencatatan DESC";

        List<KondisiAktual> results = new ArrayList<>();

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(startDate));
            ps.setTimestamp(3, Timestamp.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToKondisiAktual(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding by date range: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    /** Hitung jumlah record untuk user tertentu */
    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM kondisi_aktual WHERE user_id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting records: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /** Helper method untuk mapping ResultSet ke KondisiAktual */
    private KondisiAktual mapResultSetToKondisiAktual(ResultSet rs) throws SQLException {
        KondisiAktual k = new KondisiAktual();
        k.setId(rs.getInt("id"));
        k.setUserId(rs.getInt("user_id"));
        k.setTekananDarah(rs.getString("tekanan_darah"));
        k.setDetakJantung(rs.getInt("detak_jantung"));
        k.setSuhuTubuh(rs.getDouble("suhu_tubuh"));
        k.setTingkatStres(rs.getString("tingkat_stres"));
        k.setDurasiTidur(rs.getDouble("durasi_tidur"));
        k.setDurasiOlahraga(rs.getInt("durasi_olahraga"));
        k.setJumlahLangkah(rs.getInt("jumlah_langkah"));

        Timestamp ts = rs.getTimestamp("tgl_pencatatan");
        if (ts != null) {
            k.setTglPencatatan(ts.toLocalDateTime());
        }

        return k;
    }

    /**
     * Check apakah user sudah punya data hari ini
     * Berguna untuk validasi atau logic tertentu
     */
    public boolean hasDataToday(int userId) {
        String sql = "SELECT COUNT(*) FROM kondisi_aktual WHERE user_id = ? " +
                "AND DATE(tgl_pencatatan) = DATE('now')";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking today's data: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
=======
}
>>>>>>> rekomendasi
