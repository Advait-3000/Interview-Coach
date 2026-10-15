# Business Rules

1. Roles are `STUDENT` and `ADMIN`.
2. Email is normalized (trimmed and lowercased) before uniqueness checks.
3. Passwords are stored as strong hashes using a maintained password-hashing library.
4. Only active questions are eligible for student practice or tests.
5. MVP questions have one correct multiple-choice option.
6. Difficulty values are `EASY`, `MEDIUM`, and `HARD`; category names are controlled values or validated strings.
7. A mock attempt stores the exact question IDs presented at start, so later bank edits do not alter the attempt.
8. Each answer belongs to one attempt and one question. An attempt cannot have two answer rows for the same question.
9. Submitted attempts are immutable; corrections require a separate administrative process, not silent overwrites.
10. Percentage = `correct_count / total_question_count * 100`. If total is zero, an attempt cannot be started.
11. Unanswered questions count as incorrect at submission.
12. Weak area default rule: accuracy below 50% with at least 5 answered questions. Display the threshold and sample count; keep thresholds centralized in code/config.
13. Practice and mock tests must be distinguishable in stored attempt type.
14. The app must not present scores as professional hiring predictions.
15. Use synthetic demo users/questions. Do not add real student records without explicit authorization and a data-handling plan.
