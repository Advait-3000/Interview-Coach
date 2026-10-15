# Technology Stack

| Area | Choice | Why |
|---|---|---|
| Language | Java 21 LTS | Modern supported Java baseline |
| Desktop UI | JavaFX | Native desktop UI and reusable controls |
| View markup | FXML, optional | Separates layout from controller logic |
| Build | Maven | Repeatable dependencies and build commands |
| Database | MySQL 8.x compatible release | Matches project presentation |
| Connectivity | JDBC driver | Direct relational database access |
| Password hashing | BCrypt library or equivalent maintained password-hashing implementation | Never store plaintext passwords |
| Tests | JUnit 5 | Unit and integration tests |
| Logging | SLF4J with Logback | Consistent diagnostic logging |
| Version control | Git | Track code and documentation |

## Version policy
Use currently supported stable releases compatible with Java 21. Pin dependencies through `pom.xml`; do not invent versions or upgrade blindly. Verify compatibility when creating the project.

## Not selected for MVP
React, Node.js, Spring Boot, cloud hosting, AI APIs, vector databases, and microservices. They add complexity without serving the current presentation requirements.
