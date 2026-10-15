# Database Schema Changes

## Approach
Keep versioned SQL scripts under `database/migrations/` once schema evolution begins. The initial implementation may use `database/schema.sql` for a clean local setup, but future changes must be versioned rather than editing history silently.

## Rules
1. Back up local data before a destructive migration.
2. Make migrations ordered, named, and repeatable only if explicitly designed to be idempotent.
3. Never use `DROP DATABASE` or wipe tables automatically on application startup.
4. Test migrations on a disposable database.
5. Add/update seed data separately from schema migrations.
6. Document rollback limitations; many data transformations cannot be safely reversed.
7. Keep SQL compatible with the documented MySQL version.
8. Do not run migrations against an unknown or production database without explicit approval.
