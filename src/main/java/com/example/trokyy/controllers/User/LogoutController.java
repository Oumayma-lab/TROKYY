package com.example.trokyy.controllers.User;

import com.example.trokyy.tools.SessionManager;

public class LogoutController {

    public void logout(String sessionId) {
        SessionManager.invalidateSession(sessionId);
    }
}
