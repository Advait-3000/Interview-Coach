# Acceptance Criteria

## Accounts
- Registration rejects missing required fields and malformed email.
- Duplicate email is rejected with a clear message.
- Password is never stored or logged in plaintext.
- Invalid login does not reveal whether an email exists.
- Students cannot open admin-only operations by navigating directly to a screen.

## Questions
- Only active questions are available to students.
- A question must have a category, difficulty, prompt, at least two options, and exactly one correct option for the MVP.
- Empty options and duplicate/blank option labels are rejected.
- Editing a question does not rewrite historic attempt answers.

## Assessment and scoring
- The question set for an attempt remains stable from start to submit.
- Score equals correct answers divided by total scored questions, expressed as a percentage.
- Unanswered questions count as incorrect on submission.
- No negative marking applies unless a future approved requirement says otherwise.
- Duplicate submission cannot create duplicate completed attempts.
- Results and individual answer records are saved transactionally.

## History and analytics
- A student sees only their own history.
- Category accuracy includes the numerator and denominator, e.g. 4/10 (40%).
- An empty category displays “Not enough attempts yet,” not 0%.
- Weak-area labels use a documented threshold and minimum sample size.

## Admin
- Only administrators can mutate the question bank.
- Questions used in historic attempts are deactivated by default rather than hard-deleted.
- Validation failures preserve form inputs and explain the problem.

## Reliability
- The project builds from a clean checkout using documented commands.
- Relevant automated tests pass.
- Database outage is handled gracefully.
- No credentials are committed to source control.
