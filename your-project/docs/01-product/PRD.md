# Product Requirements Document

## 1. Summary
Interview Coach is a Java desktop app for structured interview preparation. It uses JavaFX for screens, Java for application logic, JDBC for database access, and MySQL for persistent records.

## 2. Problem
Students often use separate websites, videos, notes, and spreadsheets for technical, aptitude, and HR preparation. Their results may not be stored in one place, making it difficult to track progress and weak areas.

## 3. Goals
- Centralize question practice and mock assessments.
- Filter questions by category and difficulty.
- Automatically score objective answers.
- Save attempts and make history visible.
- Calculate topic-wise performance from recorded answers.
- Give administrators a controlled question-management interface.

## 4. Users and permissions
- **Student:** manage own account, practise, take tests, see own results/history.
- **Administrator:** manage question bank and view basic system/question-bank information.
- No public account listing and no student-to-student access to private attempt records.

## 5. Functional requirements
- FR-01: Register with a unique email/username and password.
- FR-02: Log in and log out.
- FR-03: Browse categories and difficulty levels.
- FR-04: View a question and its answer options.
- FR-05: Submit an answer and receive feedback after submission.
- FR-06: Start a mock test from selected category/difficulty and question count.
- FR-07: Score submitted objective answers using the stored correct option.
- FR-08: Save attempt date, selected answers, score, and question-level outcomes.
- FR-09: Show previous attempts for the signed-in student.
- FR-10: Show category-wise accuracy and a clear sample size.
- FR-11: Identify weaker categories using a documented threshold and sufficient sample size.
- FR-12: Allow administrators to create, view, update, activate/deactivate, and delete questions.
- FR-13: Validate input and show understandable errors without exposing stack traces.

## 6. Non-functional requirements
- NFR-01: Core screens should remain responsive for a question bank of at least 5,000 questions on a typical student laptop.
- NFR-02: SQL values must use prepared statements.
- NFR-03: Passwords must be hashed with a maintained password-hashing library.
- NFR-04: User-facing errors must be clear; technical details belong in logs.
- NFR-05: Core scoring and service logic must be testable without launching JavaFX.
- NFR-06: Setup instructions must be reproducible from a clean machine.
- NFR-07: The app must not require paid APIs or AI services.

## 7. MVP assumptions
- Single desktop installation connected to a MySQL instance.
- Multiple-choice questions with one correct answer.
- No negative marking unless explicitly configured later; default is zero penalty.
- Assessment results are immutable snapshots once submitted.
- A student sees only their own attempts.

## 8. Out of scope
AI-based descriptive evaluation, voice interviews, resume uploads, personalized generative questions, online proctoring, mobile app, and company-specific question feeds.

## 9. Risks
- An incomplete or incorrect question bank can undermine usefulness.
- Local database setup may be difficult for a beginner; setup documentation and health checks are required.
- JavaFX configuration can vary by IDE; Maven commands are the canonical route.
- Admin bootstrap must be secure and documented; never ship a hardcoded production admin password.

## 10. Release acceptance
All criteria in `docs/01-product/ACCEPTANCE_CRITERIA.md` and quality gates must pass before calling the MVP complete.
