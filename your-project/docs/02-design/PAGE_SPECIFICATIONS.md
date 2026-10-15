# Page Specifications

## 1. Login
Fields: email, password. Actions: Login, Create account. States: validation error, invalid credentials, database unavailable.

## 2. Registration
Fields: full name, email, password, confirm password. Actions: Create account, Back to login. Validate password confirmation and unique email.

## 3. Student dashboard
Greeting, quick actions for Practice / Mock Test / History / Performance, recent attempt summary, and a helpful first-run empty state.

## 4. Practice setup
Filters: category, difficulty, optional question count. Show matching active-question count before starting.

## 5. Question practice
Question prompt, options, progress, submit/check answer, explanation after answer, next/exit. Keep navigation predictable.

## 6. Mock-test setup/instructions
Category, difficulty, count, optional timer only if implemented. Explain scoring and unanswered-question behavior before start.

## 7. Mock-test question screen
Question index, prompt, radio options, previous/next, mark-for-review only if implemented, answered/unanswered status, progress, submit. Do not show correctness before final submission.

## 8. Results
Score percentage, correct/incorrect/unanswered totals, category breakdown with counts, review answers, return to dashboard/history.

## 9. History
Chronological list with date, mode, category, question count, and score. Selecting an item opens read-only details.

## 10. Performance
Category accuracy, sample size, recent trend if sufficient history exists, and revision suggestions based on rules. Avoid misleading charts with tiny samples.

## 11. Admin dashboard
Question count, active count, category distribution, navigation to question bank.

## 12. Admin question form/list
Search/filter, add/edit, prompt, category, difficulty, options, correct option, explanation, active status. Confirm destructive-looking operations.

## Global states
Connection failure, loading, empty results, validation errors, permission denied, and unexpected error dialog with a safe reference ID.
