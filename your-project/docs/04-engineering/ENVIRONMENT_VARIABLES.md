# Environment Variables

Use these names as the project convention; implementation may load them via environment variables or an ignored local properties file.

| Variable | Required | Example | Notes |
|---|---|---|---|
| `DB_HOST` | Yes | `localhost` | Do not assume remote access |
| `DB_PORT` | Yes | `3306` | Standard MySQL port |
| `DB_NAME` | Yes | `interview_coach` | Create through setup SQL |
| `DB_USER` | Yes | `interview_coach_app` | Least-privilege app account |
| `DB_PASSWORD` | Yes | placeholder | Never commit real value |
| `APP_LOG_LEVEL` | No | `INFO` | Default should avoid verbose secrets |
| `APP_SEED_DEMO_DATA` | No | `false` | Explicitly opt in to local synthetic seed data |

## Example
Create a local `.env` or properties file based on `.env.example`. Keep it out of Git. The app must validate missing settings and give a helpful error without printing the password or complete JDBC URL.

## Rules
- Never commit credentials.
- Never use a real production database for automated tests.
- Document the actual configuration-loading mechanism in code once implemented.
