
# Architecture and Product Decisions

| ID | Decision | Reason | Status |
|---|---|---|---|
| D-001 | Build a desktop application, not a website | The presentation specifies Java with Swing/JavaFX and MySQL | Accepted |
| D-002 | Use JavaFX rather than Swing | JavaFX supports a modern desktop UI and clear separation of FXML/views from controllers; confirm with faculty if Swing is mandated | Provisional |
| D-003 | Use Java 21 and Maven | A current LTS Java baseline and repeatable dependency/build management | Accepted |
| D-004 | Use MySQL through JDBC | Matches the presentation and keeps database access explicit for a student project | Accepted |
| D-005 | Use layered architecture: UI, service, repository, database | Easier to test and understand than mixing SQL into UI handlers | Accepted |
| D-006 | Start with multiple-choice questions | Makes automatic scoring deterministic and manageable for an MVP | Accepted |
| D-007 | Store password hashes, never plaintext passwords | Basic credential safety | Required |
| D-008 | Keep AI, voice, resume parsing, and company-specific generation out of MVP | These are listed as future enhancements in the presentation | Accepted |
| D-009 | Use synthetic seed questions | Safe demo data; PPT examples are not a complete validated dataset | Accepted |
| D-010 | Use local deployment for the first release | Reduces operational complexity and suits a college demo | Accepted |

## Change process
Any change to an accepted decision must include the reason, trade-offs, affected documents, and migration plan if existing code/data is affected.
