# Service Interface Specification

This is a desktop application, so these are internal Java service contracts, **not HTTP endpoints**.

## AuthenticationService
- `register(RegisterRequest) -> UserSummary`
- `login(email, password) -> AuthenticatedUser`
- `logout() -> void`
- Must normalize email, verify password hash, and avoid exposing account-existence details.

## QuestionService
- `listCategories() -> List<Category>`
- `findQuestions(categoryId, difficulty, limit) -> List<Question>`
- `getQuestionForAdmin(id) -> Question`
- `createQuestion(QuestionInput, actor) -> Question`
- `updateQuestion(id, QuestionInput, actor) -> Question`
- `deactivateQuestion(id, actor) -> void`

## AssessmentService
- `startAttempt(user, selection) -> AttemptSession`
- `saveAnswer(attemptId, questionId, option) -> void` (if answers are persisted during the attempt)
- `submitAttempt(attemptId, answers) -> AttemptResult`
- Must enforce ownership, one-time submission, stable question set, and transactional persistence.

## PerformanceService
- `getHistory(userId, page, pageSize) -> List<AttemptSummary>`
- `getAttemptDetails(userId, attemptId) -> AttemptDetails`
- `getCategoryPerformance(userId) -> List<CategoryPerformance>`

## Contract rules
Use typed request/result classes rather than giant parameter lists. Validate inputs in services. Repositories should not return UI controls or show dialogs. Never treat a UI-supplied role as proof of authorization.
