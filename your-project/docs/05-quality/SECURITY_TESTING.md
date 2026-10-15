# Security Testing

Before release, verify:
- SQL-like input cannot alter query behavior.
- Public registration cannot assign ADMIN role.
- Student cannot view another user's history by changing an ID.
- Student cannot call admin question mutation services.
- Passwords are hashed and absent from logs.
- Invalid question options/correct option are rejected.
- Repeated submission does not duplicate or alter a completed result.
- Database account has only the privileges required by the app.
- `.env`, local properties, logs, and database dumps containing credentials/data are ignored.
- Failure dialogs do not expose SQL, stack traces, file paths, or credentials.

This is a student-project checklist, not a formal security certification.
