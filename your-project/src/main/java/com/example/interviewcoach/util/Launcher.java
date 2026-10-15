package com.example.interviewcoach.util;

import com.example.interviewcoach.config.AppConfig;
import com.example.interviewcoach.controller.HomeView;
import com.example.interviewcoach.controller.LoginView;
import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.repository.UserRepository;
import com.example.interviewcoach.service.AuthService;
import javafx.stage.Stage;

public final class Launcher {

    private Launcher() {
    }

    public static void navigateToLogin(Stage stage) {
        var provider = AppConfig.getInstance().getConnectionProvider();
        var userRepository = new UserRepository(provider);
        var authService = new AuthService(userRepository);

        new LoginView().show(stage, authService);
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