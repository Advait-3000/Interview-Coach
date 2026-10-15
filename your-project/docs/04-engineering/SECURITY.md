# Security Requirements

## Credentials and secrets
- Keep DB username/password in local environment variables or an ignored local config file.
- Commit `.env.example` with placeholders only; add `.env` and local config to `.gitignore`.
- Never embed production DB credentials in the JAR or source code.
- Passwords must be hashed, not encrypted reversibly or stored as plaintext.

## Database
- Use least-privilege DB account for the app; schema setup may use a separate privileged account.
- Use prepared statements for all dynamic SQL.
- Validate role, category, difficulty, option selection, limits, and IDs.
- Enforce ownership for student attempt reads and writes.
- Do not allow SQL entered by an admin as a feature.

## Application
- Default registration role is STUDENT.
- Guard admin operations in service layer.
- Avoid sensitive data in logs and error messages.
- Avoid storing unnecessary personal data.
- Do not collect real student information for the demo unless required and authorized.

## Threat model
Protect against SQL injection, privilege escalation, accidental cross-user result access, weak credential storage, accidental secret commits, and tampered assessment submissions.

## Limitations
A local desktop app connected directly to MySQL is suitable for a controlled college demo but is not an ideal architecture for a publicly distributed product. Do not expose MySQL directly to the internet. A future multi-user release should introduce a secured backend API.
