package com.example.interviewcoach.model;

import com.example.interviewcoach.model.UserRole;

public record AuthenticatedUser(
        long id,
        String fullName,
        String email,
        UserRole role
) {
}
