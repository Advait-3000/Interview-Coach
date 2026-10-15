package com.example.interviewcoach.util;

import com.example.interviewcoach.model.AuthenticatedUser;

public final class SessionService {

    private static volatile AuthenticatedUser currentUser;

    private SessionService() {
    }

    public static void setCurrentUser(AuthenticatedUser user) {
        currentUser = user;
    }

    public static AuthenticatedUser getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
