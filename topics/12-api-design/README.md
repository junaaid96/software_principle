# 12 · API design best practices

**Code:** [`ApiDesign.java`](../../src/main/java/dev/junaidul/principles/api/ApiDesign.java) (idempotency store, cursor pagination, problem details) · **Tests:** [`ApiDesignTest`](../../src/test/java/dev/junaidul/principles/api/ApiDesignTest.java)

## REST resource design
- Nouns, plural, hierarchical: `GET /v1/orders/42/items`.
- HTTP verbs carry the action: `GET` read, `POST` create, `PUT` replace, `PATCH` partial update, `DELETE` remove.
- Correct status codes: `200 OK`, `201 Created` (+ `Location`), `204 No Content`, `400` bad input, `401` unauthenticated, `403` forbidden, `404`, `409` conflict, `422` validation, `429` rate-limited, `5xx` server faults.
- Consistent naming (`camelCase` JSON), ISO-8601 UTC timestamps, money as decimal string or minor units plus currency.

## Idempotency
`GET`, `PUT`, `DELETE` are idempotent by definition. Make `POST` safe to retry with an **`Idempotency-Key`** header: store the first response per key and return it on retries (see `IdempotencyStore`). Essential for payments.

## Pagination
| Style | Pros | Cons |
|---|---|---|
| Offset (`?page=3&size=20`) | simple, jump to page | slow on big tables; items shift on insert |
| Cursor / keyset (`?after=abc&limit=20`) | fast, stable | no random page access |
Always cap `limit`, and return `nextCursor` (or a `Link` header).

## Error model
Use **RFC 9457 Problem Details** (`application/problem+json`):
```json
{ "type": "https://example.com/problems/validation", "title": "Validation failed",
  "status": 422, "detail": "email is invalid", "instance": "/v1/users" }
```
Never leak stack traces.

## Versioning
URI (`/v1`), header (`Accept: application/vnd.app.v2+json`) or query parameter. Additive changes (new optional fields) are non-breaking; removing/renaming fields is breaking. Publish a deprecation timeline (`Deprecation` / `Sunset` headers).

## Also
- Describe APIs with **OpenAPI**; generate docs and clients.
- Filtering/sorting: `?status=PAID&sort=-createdAt`.
- Rate limiting with `429` + `Retry-After`.
- Use `ETag`/`If-None-Match` for caching and `If-Match` for optimistic concurrency.
- Consider gRPC for internal high-throughput calls; GraphQL when clients need flexible queries.

## Interview questions
1. PUT vs PATCH? Is PATCH idempotent?
2. How would you design a retry-safe payment API?
3. Offset vs cursor pagination?
