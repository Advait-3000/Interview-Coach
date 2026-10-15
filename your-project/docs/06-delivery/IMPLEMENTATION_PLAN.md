# Implementation Plan

Implement in this order; do not ask the coding agent to generate the entire app in one uncontrolled pass.

## Phase 0 — Environment and skeleton
- Confirm Java 21, Maven, and MySQL are installed.
- Create Maven JavaFX project and verify a blank window opens.
- Add `.gitignore`, `pom.xml`, app entry point, and a build/run section.
**Exit:** clean build and blank app launches.

## Phase 1 — Database
- Create database and least-privilege application user.
- Add schema for users, categories, questions, attempts, attempt_questions, and attempt_answers.
- Add synthetic categories/questions.
- Implement connection provider and repository smoke test.
**Exit:** connection test and repository tests pass.

## Phase 2 — Domain and services
- Add models/DTOs and validation.
- Implement question repository/service.
- Implement pure scoring and analytics functions with unit tests.
**Exit:** scoring tests pass independently of UI.

## Phase 3 — Authentication
- Registration, password hashing, login/logout, in-memory session, role checks.
**Exit:** account acceptance criteria pass.

## Phase 4 — Student practice
- Dashboard, filters, question screen, feedback/explanation.
**Exit:** a student can complete a practice flow.

## Phase 5 — Mock assessment
- Stable question set, navigation, submit confirmation, transactionally persisted results.
**Exit:** assessment acceptance criteria and tests pass.

## Phase 6 — History and analytics
- History, attempt details, topic-wise accuracy, sample-size-aware weak areas.
**Exit:** users see only their own records; analytics test cases pass.

## Phase 7 — Admin
- Question list, add/edit/deactivate, validation, role enforcement.
**Exit:** admin CRUD tests pass; student access is denied.

## Phase 8 — Polish and delivery
- Accessibility, resizing, error states, package/run guide, demo data, final test pass.
**Exit:** all quality gates and release checklist pass.

## Rule
At each phase, build, test, explain, and update status before starting the next phase.
