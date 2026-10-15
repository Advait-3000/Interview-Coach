# Authentication

## Registration
1. Trim and lowercase email.
2. Validate required fields and password policy.
3. Check unique email; still handle a duplicate-key race at insert time.
4. Hash password using BCrypt or another maintained password-hashing implementation with appropriate parameters.
5. Insert user as `STUDENT`; public registration must never allow selecting `ADMIN`.
6. Do not log passwords or hashes.

## Login
1. Normalize email.
2. Retrieve active account by email.
3. Verify supplied password against stored hash.
4. Return a minimal authenticated-user object containing ID, name, and role.
5. Show a generic invalid-credentials message.

## Session
Keep the authenticated user in an in-memory session object. Clear it on logout. Never store plaintext password or long-lived secrets in session state.

## Password policy
Require a reasonable minimum length (e.g. 10 characters) and confirm-password match during registration. Avoid arbitrary complexity rules that encourage predictable passwords. Never provide password recovery that exposes stored hashes.

## Admin bootstrap
Do not ship a known admin password. Provide a documented, one-time local bootstrap process or a controlled database setup procedure for the demo. The implementation must not permit public registration as admin.
