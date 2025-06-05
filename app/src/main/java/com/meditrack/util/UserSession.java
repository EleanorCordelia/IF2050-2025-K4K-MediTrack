package com.meditrack.util;

/**
 * Singleton sederhana untuk menyimpan data user yang sedang login.
 * Sementara hanya simpan ID saja.
 */
public class UserSession {

    private static int userId;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static void clear() {
        userId = 0;
    }
}
