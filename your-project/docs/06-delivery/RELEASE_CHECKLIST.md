# Release Checklist

## Build
- [ ] `mvn clean test` passes from a clean checkout
- [ ] Application launches on the target OS
- [ ] Java/JavaFX/Maven requirements documented
- [ ] No secrets in source, logs, or package

## Functional
- [ ] Registration and login
- [ ] Student/admin permissions
- [ ] Practice and answer feedback
- [ ] Mock assessment and confirmation
- [ ] Correct scoring including unanswered questions
- [ ] Result persistence and history
- [ ] Category analytics and sample sizes
- [ ] Admin question management
- [ ] Database-down and empty-state behavior

## Quality
- [ ] Automated tests pass
- [ ] Keyboard navigation and visible focus checked
- [ ] Window resizing checked
- [ ] No real student data in demo seed
- [ ] Known issues documented

## Demo
- [ ] Database and synthetic seed data prepared
- [ ] Demo account setup documented securely
- [ ] 3–5 minute demo flow rehearsed
- [ ] Backup made before demo
