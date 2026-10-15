# Integrations

## Required
- MySQL server
- MySQL Connector/J through Maven
- JavaFX runtime/modules compatible with the selected JDK
- Password hashing library
- Logging and testing libraries

## Not required in MVP
No OpenAI/Gemini API, external interview-question API, email provider, cloud storage, analytics SDK, or web service.

## Integration principles
- Pin dependencies in Maven and review licenses.
- Do not scrape external websites for questions.
- Keep database configuration external to source code.
- If a future external API is added, document purpose, data sent, credentials, failure behavior, cost, and privacy implications before implementation.
