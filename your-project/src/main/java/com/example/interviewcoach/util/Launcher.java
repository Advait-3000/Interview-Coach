package com.example.interviewcoach.util;

import com.example.interviewcoach.controller.HomeController;
import com.example.interviewcoach.controller.HomeView;
import com.example.interviewcoach.controller.LoginController;
import com.example.interviewcoach.controller.LoginView;
import com.example.interviewcoach.model.UserRole;
import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.util.SessionService;
import javafx.stage.Stage;

public final class Launcher {

    private Launcher() {
    }

    public static void navigateToLogin(Stage stage) {
        new LoginView().show(stage);
    }

    public static void navigateToDashboard(AuthenticatedUser user, Stage stage) {
        if (user == null) {
            navigateToLogin(stage);
            return;
        }

        SessionService.setCurrentUser(user);

        switch (user.role()) {
            case ADMIN -> HomeView.navigateToAdminDashboard(stage, user);
            case STUDENT -> HomeView.navigateToStudentDashboard(stage, user);
            default -> {
                SessionService.clear();
                navigateToLogin(stage);
            }
        }
    }

    public static void logout(Stage stage) {
        SessionService.clear();
        navigateToLogin(stage);
    }
}
