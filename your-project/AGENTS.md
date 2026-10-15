
# Agent Instructions — Interview Coach

## Mission
Build a beginner-friendly Java desktop application for interview preparation, following the product and engineering documents in `docs/`. The owner is new to Java, so explain major decisions plainly and keep the project runnable at each milestone.

## Source of truth
1. `docs/01-product/PRD.md` defines the MVP.
2. `docs/03-architecture/TECH_STACK.md` and `ARCHITECTURE.md` define technical decisions.
3. `docs/06-delivery/IMPLEMENTATION_PLAN.md` and `TASKS.md` define implementation order.
4. If documents conflict, stop and record the conflict in `DECISIONS.md`; do not silently invent a requirement.

## Required working style
- Inspect the existing repository before changing it.
- Implement one small, testable milestone at a time.
- Before coding, state the files you intend to create/change and why.
- Prefer simple, readable Java over clever abstractions.
- Keep UI, business logic, and database access in separate layers.
- Use JavaFX for the desktop UI, Maven for builds, JDBC for database access, and MySQL for persistent data.
- Use prepared statements for all SQL containing values.
- Never hardcode credentials, secrets, or personal data.
- Never claim a feature works until it has been built and tested.
- Do not replace Java/MySQL with a web stack or add AI features to the MVP.
- Add or update tests and documentation with each meaningful feature.
- Use synthetic demo data only. Never commit real student data.
- Do not run destructive database commands without explaining the effect and obtaining approval.

## Beginner-friendly output
For each milestone, explain:
1. What was built.
2. How it works in simple language.
3. How to run it.
4. How it was tested.
5. Any remaining limitations.

## Completion rules
A task is complete only when its acceptance criteria pass, the app builds, relevant tests pass, and the documentation is updated. If a tool or environment is unavailable, report the blocker rather than pretending the task succeeded.
