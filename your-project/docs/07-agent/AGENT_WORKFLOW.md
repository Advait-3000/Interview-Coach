# Agent Workflow

1. Read relevant specifications and current status.
2. Inspect existing source and Git diff before changing anything.
3. Identify one small task and its acceptance criteria.
4. Explain intended changes and dependencies.
5. Implement the smallest coherent increment.
6. Run formatting/build/tests that are available.
7. Inspect the diff for secrets, accidental deletions, architecture violations, and generated junk.
8. Update docs/status/changelog where relevant.
9. Report evidence and remaining gaps.
10. Stop at the milestone boundary; do not continue into unrelated features without checking the plan.

## When blocked
Ask one concise question if a decision genuinely affects correctness. Otherwise choose the simplest reversible option, document it in `DECISIONS.md`, and proceed. Do not invent credentials, question-bank facts, or successful test results.
