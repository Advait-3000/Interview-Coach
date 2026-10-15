# User Flows

## Registration
Launch app → choose Register → enter name, email, password, confirm password → validate fields → create account → show success → go to Login.

## Login
Launch app → enter email and password → authenticate → route by role to Student Dashboard or Admin Dashboard. Invalid credentials produce a generic message.

## Practice
Dashboard → Practice → choose category/difficulty → load active questions → answer one question → submit/check answer → show correctness and explanation → continue or exit → optionally save the practice session if session tracking is enabled. MVP should save completed practice sessions consistently; define the session behavior in implementation.

## Mock assessment
Dashboard → Mock Test → choose category/difficulty/count → show instructions → start → answer and navigate → review unanswered questions → confirm submission → score → persist attempt and answers → show result and category breakdown → open history.

## View history
Dashboard → History → list attempts newest first → open attempt → show score, date, category, and answer review.

## Admin question management
Admin Dashboard → Question Bank → list/search questions → Add/Edit → validate category, difficulty, options, and correct option → save → confirm success. Deactivation is preferred to deletion if a question has been used in a completed attempt.

## Failure paths
- Database unavailable: show setup/retry guidance; do not show raw stack trace.
- Session expires/app closes mid-test: do not create a false completed result. In-progress recovery is not required for MVP unless explicitly added.
- Empty question set: explain that no matching questions are available and offer another filter.
