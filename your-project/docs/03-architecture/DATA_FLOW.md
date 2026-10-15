# Data Flow

## Login
User submits form → controller validates basic form state → `AuthService` verifies credentials against `UserRepository` → password hash check → authenticated user/role returned → UI opens role-appropriate dashboard.

## Fetch questions
Student chooses filters → controller calls `QuestionService` → repository queries active questions using prepared statements → mapped questions returned → UI displays them.

## Submit assessment
UI sends attempt ID and selected answers → `AssessmentService` validates attempt ownership/state → computes score from the stable question set → repository persists attempt summary and answers in one transaction → service returns result DTO → result screen renders saved result.

## Performance
Student opens performance → `PerformanceService` loads completed attempts/answer rows → groups by category → calculates correct/answered counts and percentage → UI displays values with sample size.

## Admin update
Admin form → controller validates required fields → authorization/service layer checks role → repository inserts/updates/deactivates question → success state returned.

## Data ownership
Every student history query must include the authenticated user's ID. Never trust a user ID supplied by the UI without checking it against the session.
