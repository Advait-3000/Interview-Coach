# Performance Requirements

- Target question bank: at least 5,000 questions on a typical student laptop.
- Add indexes for category/difficulty/active filters and user attempt history.
- Limit question result sets and history page sizes.
- Avoid loading the entire question bank into memory.
- Keep JDBC work off the JavaFX UI thread.
- Fetch only columns needed for each screen.
- Avoid calculating analytics through one query per category if one grouped query or a small number of queries is possible.
- Measure before optimizing; correctness and maintainability come first.
- Performance tests should use synthetic data and report hardware/setup.
