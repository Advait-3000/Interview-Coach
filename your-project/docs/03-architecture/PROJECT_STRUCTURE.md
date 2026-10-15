# Proposed Source Structure

```text
interview-coach/
├── pom.xml
├── README.md
├── .gitignore
├── .env.example
├── database/
│   ├── schema.sql
│   └── seed.sql
├── src/
│   ├── main/
│   │   ├── java/<package-path>/
│   │   │   ├── App.java
│   │   │   ├── config/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   │   ├── util/
│   │   │   └── validation/
│   │   └── resources/
│   │       ├── fxml/
│   │       ├── css/
│   │       └── images/
│   └── test/
│       ├── java/<package-path>/
│       └── resources/
└── docs/
```

Replace `<package-path>` with a real lowercase Java package, e.g. `com.example.interviewcoach`. Keep names consistent across files.

## Rules
- Do not put all classes in one file.
- Avoid a generic `Utils` class becoming a dumping ground.
- Keep SQL in repositories or versioned SQL files.
- Keep secrets and machine-specific configuration out of Git.
- Put test fixtures in test resources, not production seed data.
