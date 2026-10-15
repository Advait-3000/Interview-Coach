
# Project Status

## Current phase
**Phase 0 — Specification and setup.** Requirements are derived from the supplied Interview Coach presentation. Documentation has been prepared; implementation is not yet verified.

## Target MVP
A JavaFX desktop app with student accounts, question practice, mock assessments, automatic scoring, attempt history, topic-wise analysis, and admin question management backed by MySQL through JDBC.

## Milestones
- [ ] M0: Install tools and create Maven JavaFX project
- [ ] M1: Configure MySQL schema and database connection
- [ ] M2: Implement repositories and seed synthetic questions
- [ ] M3: Registration, login, password hashing, and roles
- [ ] M4: Question browsing and practice session
- [ ] M5: Assessment submission and scoring
- [ ] M6: Attempt history and topic-wise performance
- [ ] M7: Admin question management
- [ ] M8: Tests, accessibility pass, packaging, and demo rehearsal

## Known decisions
- Java 21 + JavaFX + Maven + MySQL + JDBC.
- Objective multiple-choice questions are the initial question format.
- No AI, voice analysis, resume parsing, external APIs, or online deployment in MVP.
- The PPT provides sample topics and a few example questions, not a complete validated question dataset.

## Blockers / open questions
- Confirm college evaluation expectations and whether the instructor requires Swing specifically rather than JavaFX.
- Confirm whether the demo must run on multiple computers or only one local machine.
- Prepare a reviewed question bank; sample entries must be treated as illustrative only.

## Update protocol
Update this file at the end of each milestone. Include date, completed tasks, tests run, and blockers. Do not mark items complete without evidence.
