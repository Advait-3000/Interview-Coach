
# Interview Coach

A Java desktop application that helps students prepare for technical, aptitude, and HR interviews in one place.

## What it will do
- Register and log in students.
- Let students practise question-bank questions by category and difficulty.
- Run timed or untimed mock assessments.
- Automatically score objective questions.
- Save attempts and results.
- Show score history and topic-wise strengths and weaknesses.
- Let an administrator manage questions.

The project follows the supplied college presentation. AI evaluation, voice interviews, resume-based question generation, and company-specific recommendations are **future scope**, not MVP requirements.

## Technology
- Java 21
- JavaFX desktop UI
- Maven build
- MySQL database
- JDBC for database access
- JUnit for tests

## Requirements
Install a JDK 21 distribution, Maven, and MySQL Server. Check `docs/06-delivery/LOCAL_DEVELOPMENT.md` for detailed setup.

## Current status
See `PROJECT_STATUS.md`. This repository starts as a documentation-first specification; the application has not been implemented merely by creating these documents.

## Build and run
The commands below become applicable after the Java/Maven source project is created:
```bash
mvn clean test
mvn javafx:run
```
If the JavaFX Maven plugin or application entry point has not yet been added, follow the implementation plan before running these commands.

## Documentation map
- Product requirements: `docs/01-product/`
- Design and screens: `docs/02-design/`
- Architecture and data: `docs/03-architecture/`
- Engineering standards: `docs/04-engineering/`
- Testing and quality: `docs/05-quality/`
- Build, delivery, and deployment: `docs/06-delivery/`
- AI-agent execution guidance: `docs/07-agent/`

## Important limitation
This is intended as a local desktop MVP. It is not yet a hosted multi-user service. The database and local machine must be configured before the app can persist data.
