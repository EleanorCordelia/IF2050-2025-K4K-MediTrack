package com.meditrack;

import java.util.HashMap;
import java.util.Map;


public class UserStore {
    // Simulasi database: email → password
    private static final Map<String, String> userDatabase = new HashMap<>();

    public static void addUser(String email, String password) {
        userDatabase.put(email, password);
    }

    public static boolean isValidUser(String email, String password) {
        return userDatabase.containsKey(email) && userDatabase.get(email).equals(password);
    }
}