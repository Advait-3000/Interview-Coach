# Error Handling

## Categories
- Validation errors: show field-specific guidance.
- Authentication errors: generic invalid-credentials message.
- Database connectivity errors: explain that MySQL/settings should be checked and offer retry.
- Constraint/conflict errors: show a useful message without exposing SQL.
- Unexpected errors: show a safe message and reference ID; log technical details.

## Rules
- Never swallow exceptions silently.
- Never show stack traces or SQL details in student-facing dialogs.
- Do not log passwords, password hashes, full connection URLs, or sensitive answer data.
- Use transactions and rollback on partial assessment-save failure.
- If submission fails, do not show a success result as if it were persisted. Explain whether the attempt remains recoverable.
- Log exception cause and operation context without credentials.
