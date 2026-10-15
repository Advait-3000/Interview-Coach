# Context Management

## Always load
- `AGENTS.md`
- `PROJECT_STATUS.md`
- `DECISIONS.md`
- The current task and relevant acceptance criteria
- Relevant architecture and security documents

## Keep context small
Do not read every document for every small task. Load the relevant domain documents and follow links to adjacent requirements.

## Preserve continuity
At session end update `PROJECT_STATUS.md` with milestone, files changed, tests run, exact outcomes, blockers, and next task. Add durable architectural choices to `DECISIONS.md`, not only chat history.

## Conflict handling
If source code and documentation disagree, inspect Git history/current implementation and report the mismatch. Do not blindly rewrite working code to match stale prose or silently change the specification.
