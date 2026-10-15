# Dependency Policy

- Use Maven and declare every third-party dependency in `pom.xml`.
- Prefer maintained, widely used libraries with compatible licenses.
- Check Java 21 and JavaFX compatibility before adding/upgrading libraries.
- Avoid duplicate libraries serving the same purpose.
- Do not add frameworks solely for convenience if they obscure Java basics.
- Pin versions; avoid dynamic version ranges.
- Review security advisories before releases and update deliberately.
- Never download and execute arbitrary JAR files from untrusted sources.
- Document why each non-obvious dependency exists.
