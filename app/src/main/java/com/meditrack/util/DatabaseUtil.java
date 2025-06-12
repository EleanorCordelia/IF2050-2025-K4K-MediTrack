package com.meditrack.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class untuk koneksi database SQLite.
 * Pastikan file DB (misalnya mediTrack.db) sudah ada.
 */
public class DatabaseUtil {

    private static final String DB_URL = "jdbc:sqlite:meditrack.db";  // Ubah nama file DB jika beda

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
