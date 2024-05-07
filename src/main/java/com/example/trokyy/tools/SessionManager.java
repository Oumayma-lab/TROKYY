package com.example.trokyy.tools;


import com.example.trokyy.models.Utilisateur;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static Map<String, Utilisateur> sessions = new HashMap<>();

    public static void createSession(String sessionId, Utilisateur utilisateur) {
        sessions.put(sessionId, utilisateur);
    }

    public static Utilisateur getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}

