# Testing Strategy

## Unit tests
Test scoring, category aggregation, validation, weak-area thresholds, and service rules without JavaFX or a real external database.

## Repository integration tests
Use a dedicated test schema/database and synthetic fixtures. Verify constraints, prepared queries, relationships, transactions, and mapping.

## UI/manual tests
Exercise registration, login, question filters, practice, mock test, result review, history, admin CRUD, validation, resizing, keyboard navigation, and database-down states.

## Security tests
Test SQL injection-shaped inputs, role bypass, cross-user history access, duplicate submissions, and secret leakage in logs.

## Regression
Every bug fix should include a test when practical. Run `mvn clean test` before release.

## Test data
Use fake accounts and authored sample questions. Do not use real student records or production credentials.
