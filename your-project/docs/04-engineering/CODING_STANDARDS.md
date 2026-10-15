# Coding Standards

- Use Java naming conventions: `UpperCamelCase` classes, `lowerCamelCase` methods/variables, uppercase constants.
- One main class per file; package names lowercase.
- Prefer small methods with descriptive names and explicit types.
- Use immutable DTOs/records where appropriate and compatible with the selected Java version.
- Use `try-with-resources` for JDBC resources.
- Never concatenate user input into SQL.
- Avoid static global mutable state except narrowly justified application configuration.
- Keep JavaFX controllers thin; no SQL or complex scoring inside event handlers.
- Document non-obvious business rules, not every obvious line.
- Use meaningful exceptions and avoid catching `Exception` merely to ignore it.
- Format consistently and remove unused imports and debug prints.
- Do not commit IDE-specific clutter unless the team intentionally standardizes it.
