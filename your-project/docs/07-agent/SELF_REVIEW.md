# Self-Review Checklist

Before reporting completion, verify:
- [ ] Did I follow the correct task and acceptance criteria?
- [ ] Did I inspect the existing code before editing?
- [ ] Did I keep JavaFX controllers free of SQL/business logic?
- [ ] Did I use prepared statements and close JDBC resources?
- [ ] Are authentication, role, and ownership checks enforced in services?
- [ ] Are multi-row saves transactional?
- [ ] Are errors useful but safe?
- [ ] Did I add/update tests?
- [ ] Did I run tests and report exact outcomes?
- [ ] Did I avoid secrets, real user data, and destructive changes?
- [ ] Did I update documentation and project status?
- [ ] Is the next task clear?

If any critical item is unchecked, state it explicitly and do not call the milestone complete.
