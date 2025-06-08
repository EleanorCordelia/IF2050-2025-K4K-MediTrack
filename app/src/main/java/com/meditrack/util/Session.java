package com.meditrack.util;

import com.meditrack.model.Pengguna;

/**
 * Session management utility class
 */
public class Session {

    private static Pengguna currentUser;
    private static String sessionId;
    private static long loginTime;

    /**
     * Set current user session
     */
    public static void setCurrentUser(Pengguna user) {
        currentUser = user;
        sessionId = generateSessionId();
        loginTime = System.currentTimeMillis();
    }

    /**
     * Get current logged in user
     */
    public static Pengguna getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Clear current session
     */
    public static void clearSession() {
        currentUser = null;
        sessionId = null;
        loginTime = 0;
    }

    /**
     * Get session ID
     */
    public static String getSessionId() {
        return sessionId;
    }

    /**
     * Get login time
     */
    public static long getLoginTime() {
        return loginTime;
    }

    /**
     * Get session duration in minutes
     */
    public static long getSessionDuration() {
        if (loginTime == 0) return 0;
        return (System.currentTimeMillis() - loginTime) / (1000 * 60);
    }

    /**
     * Check if session is expired (24 hours)
     */
    public static boolean isSessionExpired() {
        return getSessionDuration() > 1440; // 24 hours in minutes
    }

    /**
     * Generate unique session ID
     */
    private static String generateSessionId() {
        return "SESSION_" + System.currentTimeMillis() + "_" +
                (int)(Math.random() * 1000);
    }

    /**
     * Get current user ID
     */
    public static Integer getCurrentUserId() {
        return currentUser != null ? currentUser.getIdPengguna() : null;
    }

    /**
     * Get current user name
     */
    public static String getCurrentUserName() {
        return currentUser != null ? currentUser.getNama() : "Guest";
    }

    /**
     * Get current user email
     */
    public static String getCurrentUserEmail() {
        return currentUser != null ? currentUser.getEmail() : null;
    }

    /**
     * Update current user data
     */
    public static void updateCurrentUser(Pengguna updatedUser) {
        if (currentUser != null && updatedUser != null) {
            currentUser = updatedUser;
        }
    }

    /**
     * Validate session
     */
    public static boolean validateSession() {
        return isLoggedIn() && !isSessionExpired();
    }
}