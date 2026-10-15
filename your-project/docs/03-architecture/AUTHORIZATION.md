# Authorization

## Roles
- `STUDENT`
- `ADMIN`

## Permission matrix
| Action | Student | Admin |
|---|---:|---:|
| Register/login/logout | Yes | Yes |
| Practise and take tests | Yes | Optional |
| View own results/history | Yes | Yes for own attempts |
| View another student's private history | No | No by default |
| Create/edit/deactivate questions | No | Yes |
| Change account roles | No | Bootstrap/controlled admin process only |

## Enforcement
- Hide unavailable actions in the UI, but do not rely on hiding alone.
- Check role in service methods before mutations.
- Include owner checks in every attempt-detail query.
- Use typed session identity; never accept a role from a form field.
- Test unauthorized calls at service level.
