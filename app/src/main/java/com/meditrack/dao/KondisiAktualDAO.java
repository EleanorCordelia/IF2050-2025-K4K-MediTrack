package com.meditrack.dao;

import com.meditrack.model.KondisiAktual;
import com.meditrack.util.SQLiteConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KondisiAktualDAO {

    /** Cari semua record utk satu user, urut DESC by tgl_pencatatan */
    public List<KondisiAktual> findByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM kondisi_aktual WHERE user_id = ? " +
                "ORDER BY tgl_pencatatan DESC";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<KondisiAktual> out = new ArrayList<>();
            while (rs.next()) {
                KondisiAktual k = new KondisiAktual();
                k.setId(rs.getInt("id"));
                k.setUserId(userId);
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
                out.add(k);
            }
            return out;
        }
    }

    /** Simpan record baru ke DB */
    public void save(KondisiAktual k) throws SQLException {
        String sql = "INSERT INTO kondisi_aktual(" +
                "user_id,tekanan_darah,detak_jantung,suhu_tubuh," +
                "tingkat_stres,durasi_tidur,durasi_olahraga,jumlah_langkah) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, k.getUserId());
            ps.setString(2, k.getTekananDarah());
            ps.setInt(3, k.getDetakJantung());
            ps.setDouble(4, k.getSuhuTubuh());
            ps.setString(5, k.getTingkatStres());
            ps.setDouble(6, k.getDurasiTidur());
            ps.setInt(7, k.getDurasiOlahraga());
            ps.setInt(8, k.getJumlahLangkah());
            ps.executeUpdate();
        }
    }

    /** Ambil entri terbaru untuk user tertentu */
    public KondisiAktual getLatestByUserId(int userId) {
        String sql = "SELECT * FROM kondisi_aktual WHERE user_id = ? ORDER BY tgl_pencatatan DESC LIMIT 1";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KondisiAktual(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("tekanan_darah"),
                        rs.getInt("detak_jantung"),
                        rs.getDouble("suhu_tubuh"),
                        rs.getString("tingkat_stres"),
                        rs.getDouble("durasi_tidur"),
                        rs.getInt("durasi_olahraga"),
                        rs.getInt("jumlah_langkah"),
                        rs.getTimestamp("tgl_pencatatan").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Simpan entri baru */
    public boolean insert(KondisiAktual k) {
        String sql = "INSERT INTO kondisi_aktual(user_id, tekanan_darah, detak_jantung, suhu_tubuh, " +
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

            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) k.setId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}