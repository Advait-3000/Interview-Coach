# Database Schema

Use InnoDB and foreign keys. Store timestamps consistently; use database UTC timestamps where possible and display local time in the UI.

## `users`
- `id` BIGINT primary key, generated
- `full_name` VARCHAR(120), not null
- `email` VARCHAR(254), not null, unique, normalized lowercase
- `password_hash` VARCHAR(255), not null
- `role` ENUM or validated VARCHAR (`STUDENT`, `ADMIN`)
- `created_at`, `updated_at` timestamps
- `is_active` boolean, default true

## `categories`
- `id` BIGINT primary key
- `name` VARCHAR(80), unique, not null
- `description` VARCHAR(255), nullable
- `is_active` boolean

## `questions`
- `id` BIGINT primary key
- `category_id` foreign key to categories
- `prompt` TEXT, not null
- `difficulty` validated value (`EASY`, `MEDIUM`, `HARD`)
- `option_a`, `option_b`, `option_c`, `option_d` TEXT (MVP uses four options)
- `correct_option` CHAR(1), one of A/B/C/D
- `explanation` TEXT, nullable
- `is_active` boolean, default true
- `created_by` nullable FK to users
- `created_at`, `updated_at` timestamps

## `attempts`
- `id` BIGINT primary key
- `user_id` FK to users
- `attempt_type` validated value (`PRACTICE`, `MOCK_TEST`)
- `started_at`, `submitted_at`
- `status` (`IN_PROGRESS`, `COMPLETED`, `ABANDONED`)
- `question_count`, `correct_count`, `score_percent`
- Optional category/difficulty filter snapshot fields for reporting

## `attempt_questions`
- `attempt_id` FK to attempts
- `question_id` FK to questions
- `question_order` integer
- primary key `(attempt_id, question_id)` or `(attempt_id, question_order)` plus unique constraints appropriate to the implementation
- Store a snapshot of prompt/options/correct answer if historical review must survive later edits. Prefer this snapshot for stable history.

## `attempt_answers`
- `attempt_id` FK to attempts
- `question_id` FK to questions
- `selected_option` CHAR(1), nullable for unanswered
- `is_correct` boolean
- `answered_at` timestamp, nullable
- unique `(attempt_id, question_id)`
- FK to the corresponding attempt-question record

## Indexes
- Unique `users.email`
- `questions(category_id, difficulty, is_active)`
- `attempts(user_id, submitted_at)`
- `attempt_answers(attempt_id)`

## Integrity notes
- Use transactions when creating an attempt and its question snapshot.
- Use a transaction when finalizing an attempt and inserting answers.
- Do not hard-delete questions used in attempts; deactivate them.
- Enforce constraints in SQL and validate in Java.
- Finalize exact SQL types and snapshot fields in `database/schema.sql` during implementation.
