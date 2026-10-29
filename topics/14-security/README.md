# 14 · Security best practices

## OWASP Top 10 (2021 edition; the 2025 update keeps the same themes)
| # | Risk | Prevent it by |
|---|---|---|
| A01 | Broken Access Control | deny by default; check ownership on every request (`/orders/42` belongs to *this* user?) |
| A02 | Cryptographic Failures | TLS everywhere; hash passwords with bcrypt/Argon2; never roll your own crypto |
| A03 | Injection (SQL, command, XSS) | parameterised queries (`PreparedStatement`), output encoding, input validation |
| A04 | Insecure Design | threat modelling (STRIDE) during design, abuse cases |
| A05 | Security Misconfiguration | secure defaults, no debug endpoints in prod, security headers |
| A06 | Vulnerable Components | Dependabot/Renovate, SCA scans, SBOM |
| A07 | Identification & Auth Failures | MFA, rate-limit logins, secure session handling, OAuth2/OIDC |
| A08 | Software & Data Integrity | signed artifacts, verified CI pipelines, no unsafe deserialisation |
| A09 | Logging & Monitoring Failures | log security events, alert on anomalies, never log secrets |
| A10 | SSRF | allow-list outbound hosts; block internal IP ranges |

## Secure coding checklist
- Validate input at the boundary (type, length, format); treat **all** input as untrusted.
- Use parameterised queries; never concatenate SQL.
```java
// BAD:  "SELECT * FROM users WHERE email = '" + email + "'"
// GOOD:
var ps = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
ps.setString(1, email);
```
- Principle of least privilege for users, services, DB accounts and CI tokens.
- Secrets live in a secret manager or env vars, never in git. Add secret scanning to CI.
- Defence in depth: several independent layers.
- Fail securely: errors deny access and don't leak details.
- Keep dependencies patched; pin versions; review transitive dependencies.
- Use well-known libraries (Spring Security, OWASP Java Encoder).

## Interview questions
1. How do you store passwords?
2. Explain CSRF and how SameSite cookies / tokens prevent it.
3. Authentication vs authorization? JWT pitfalls?
