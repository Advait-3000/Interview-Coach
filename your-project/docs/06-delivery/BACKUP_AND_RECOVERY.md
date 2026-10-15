# Backup and Recovery

## Local project
Use Git for source code and documentation. Git is not a database backup.

## MySQL
Before schema changes or important demos, create a local dump using MySQL's supported backup utility. Store backups securely and keep credentials out of shell history where possible.

## Recovery
1. Stop the app before restoring a database snapshot.
2. Restore into the intended database after verifying the target name.
3. Confirm tables, row counts, and foreign keys.
4. Launch app and run a read-only smoke test.
5. Keep a copy of the pre-restore database until verification is complete.

## Safety
Do not run destructive restore/drop commands against an unknown database. Never upload a dump containing personal data or credentials to a public repository. For the college demo, synthetic data is preferred.
