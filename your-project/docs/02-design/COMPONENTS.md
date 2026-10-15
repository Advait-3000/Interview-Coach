# UI Components

## Shared components
- `AppShell`: common navigation and current-user context.
- `PrimaryButton` / `SecondaryButton`: consistent action hierarchy.
- `FormField`: label, input, helper text, validation message.
- `QuestionCard`: prompt and metadata.
- `AnswerOption`: keyboard-operable single-choice option.
- `ProgressIndicator`: current question and total.
- `ScoreSummary`: score and correct/incorrect/unanswered counts.
- `CategoryPerformanceRow`: category, correct/total, percentage, status.
- `EmptyState`: short explanation and next action.
- `ErrorDialog`: safe message and retry/help option.
- `ConfirmationDialog`: submit/discard/deactivate confirmation.
- `LoadingIndicator`: visible busy state.
- `QuestionTable`: admin list with accessible actions.

## Implementation rules
Build reusable JavaFX controls only when repeated use justifies them. Avoid building a large design framework before the first screen works. Prefer FXML for view structure if it improves readability, with controllers kept thin.

## State rules
UI state belongs in controllers/view models; scoring and validation rules belong in services. Controllers must not contain SQL.
