# Test Cases

| ID | Scenario | Expected result |
|---|---|---|
| TC-01 | Register valid student | Account created with STUDENT role and password hash |
| TC-02 | Register duplicate email with different case | Rejected as duplicate |
| TC-03 | Register blank/mismatched fields | Field validation shown; no row inserted |
| TC-04 | Login with correct credentials | Student dashboard opens |
| TC-05 | Login with incorrect password | Generic failure message |
| TC-06 | Student attempts admin service call | Access denied |
| TC-07 | Filter by category/difficulty | Only matching active questions returned |
| TC-08 | No matching questions | Clear empty state; no crash |
| TC-09 | All answers correct | Score 100% |
| TC-10 | Half answers correct | Score reflects correct/total |
| TC-11 | Some unanswered at submission | Unanswered count shown and counted incorrect |
| TC-12 | Submit attempt twice | No duplicate completed result |
| TC-13 | Database error during submission | Transaction rolls back; no false success |
| TC-14 | Student opens another user's attempt ID | Access denied/not found without leaking data |
| TC-15 | Open history with no attempts | Helpful empty state |
| TC-16 | Category has fewer than 5 answers | No definitive weak-area label |
| TC-17 | Admin creates invalid question | Save blocked with field-specific errors |
| TC-18 | Admin deactivates used question | Hidden from new sessions; old result remains viewable |
| TC-19 | App starts with MySQL stopped | Helpful connection message; no raw stack trace |
| TC-20 | Resize window and use keyboard only | Main flow remains usable |
| TC-21 | Search field contains SQL-like text | Treated as data, not executable SQL |
| TC-22 | Inspect logs after login | No plaintext password/hash logged |
