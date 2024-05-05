package com.example.trokyy.tools;
import com.example.trokyy.models.Utilisateur;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SessionManager {
    private static final Map<String, SessionInfo> sessions = new HashMap<>();
    private static final Logger logger = Logger.getLogger(SessionManager.class.getName());
    private static final long SESSION_TIMEOUT_SECONDS = 300; // 5 minutes inactivity timeout

    private static class SessionInfo {
        private Utilisateur Utilisateur;
        private LocalDateTime lastActivityTime;

        public SessionInfo(Utilisateur Utilisateur) {
            this.Utilisateur = Utilisateur;
            this.lastActivityTime = LocalDateTime.now();
        }

        public Utilisateur getUtilisateur() {
            return Utilisateur;
        }

        public void updateActivityTime() {
            this.lastActivityTime = LocalDateTime.now();
        }

        public boolean isExpired() {
            return LocalDateTime.now().minusSeconds(SESSION_TIMEOUT_SECONDS).isAfter(lastActivityTime);
        }
    }

    public static void createSession(String sessionId, Utilisateur utilisateur) {
        sessions.put(sessionId, new SessionInfo(utilisateur));
        logger.info("Session created for user: " + utilisateur.getUsername());
    }

    public static Utilisateur getSession(String sessionId) {
        SessionInfo sessionInfo = sessions.get(sessionId);
        if (sessionInfo != null) {
            sessionInfo.updateActivityTime(); // Update last activity time
            logger.info("Session retrieved for user: " + sessionInfo.getUtilisateur().getUsername());
            return sessionInfo.getUtilisateur();
        }
        logger.warning("Session not found for ID: " + sessionId);
        return null;
    }

    public static void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
        logger.info("Session invalidated for session ID: " + sessionId);
    }

    public static void checkExpiredSessions() {
        logger.info("Checking for expired sessions...");
        sessions.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                logger.info("Session expired for user: " + entry.getValue().getUtilisateur().getUsername());
                return true;
            }
            return false;
        });
}
}
