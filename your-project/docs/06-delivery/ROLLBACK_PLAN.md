# Rollback Plan

## Application rollback
Keep the previous known-good release artifact and its version notes. If a new package fails:
1. Stop the new app.
2. Restore the previous application package.
3. Check whether the database schema changed.
4. Use a database backup if data/schema incompatibility requires it.
5. Record the failure and prevent release until tests cover it.

## Database rollback
Do not assume every schema migration can be reversed. Prefer forward-fix migrations for production-like data. For the local educational project, restore a verified backup if a destructive change causes damage.

## Guardrails
- Never auto-drop or recreate a database on app startup.
- Never roll back by deleting user attempts without approval.
- Document exactly which app version and schema version are paired.
