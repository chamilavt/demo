# GitHub Copilot Instructions — `demo`

## 1. Project Overview

- This is a Spring Boot REST API application named `demo`.
- Base package: `com.chamil.demo`.
- The application currently implements a simple company/job/review domain.
- The current architecture is layered:
  - REST Controller
  - Service interface/implementation
  - Spring Data JPA Repository
  - JPA Entity
  - H2 database
- Preserve the existing package-by-feature structure (`company`, `job`, `review`) unless there is a clear architectural reason to change it.
- Make the smallest safe change required by the task. Do not perform unrelated refactoring.
- Before changing behavior, inspect the existing implementation and all affected entity relationships.

## 2. Business Domain Knowledge

### Company

`Company` represents an organization.

Current fields:
- `id`
- `name`
- `description`
- `registrationNumber` (optional, unique)
- `jobs`
- `reviews`

Relationships:
- One company has many jobs.
- One company has many reviews.

### Job

`Job` represents a job posting belonging to a company.

Current fields:
- `id`
- `title`
- `description`
- `minSalary`
- `maxSalary`
- `location`
- `company`

Relationship:
- Many jobs belong to one company.

### Review

`Review` represents a review belonging to a company.

Current fields:
- `id`
- `title`
- `description`
- `rating`
- `company`

Relationship:
- Many reviews belong to one company.

### Domain Rules

- A job associated with a company must reference a valid company.
- A review associated with a company must reference a valid company.
- Company-scoped review operations must operate only on reviews belonging to the requested company.
- Do not create orphan jobs or reviews when a company relationship is required.
- Do not blindly accept a client-supplied `company` object when the company is identified by a path variable; resolve the company from persistence and set the relationship server-side.
- Preserve JPA relationship integrity when creating, updating, or deleting child entities.
- A company's `registrationNumber` is optional, but when supplied it must be unique across all companies. Enforce uniqueness in the service layer using `CompanyRepository.existsByRegistrationNumber(...)` in addition to the database unique constraint, and return `409 Conflict` when a duplicate is submitted.

## 3. Architecture Principles

### Layering

Maintain this dependency direction:

`Controller -> Service -> Repository -> Database`

Controllers must handle HTTP concerns only. Do not put business logic or database access in controllers.

Services must contain business rules and coordinate repositories and related services.

Repositories must handle persistence and query concerns. Do not put business workflows in repositories.

### Dependency Injection

- Use constructor injection for all new dependencies.
- Prefer `private final` fields for injected dependencies.
- Do not introduce field injection with `@Autowired` in new code.
- Existing field injection may be migrated when a class is already being changed and the migration is safe.

### Entities

- JPA entities must retain a no-argument constructor.
- Avoid exposing persistence implementation details through API contracts.
- For new or significantly changed APIs, prefer request/response DTOs rather than exposing JPA entities directly.
- Be careful with bidirectional relationships because they can cause recursive JSON serialization and excessive database loading.
- Keep `@JsonIgnore` or replace it with an explicit DTO mapping when needed to prevent relationship recursion.

### Transactions

- Put transaction boundaries at the service layer.
- Use `@Transactional` for operations that require multiple related persistence changes to succeed or fail atomically.
- Avoid manually maintaining both sides of a relationship unless the mapping requires it.

### Scope Control

Do not introduce CQRS, microservices, messaging, event sourcing, MapStruct, Lombok, or other architectural frameworks unless the requirement justifies them and the change is explicitly intended.

## 4. Coding Standards

### Java

- Target Java 17.
- Follow standard Java naming conventions.
- Use `PascalCase` for classes/interfaces and `camelCase` for methods/variables.
- Use descriptive names; avoid unexplained abbreviations.
- Keep methods focused and reasonably small.
- Remove unused imports.
- Avoid wildcard imports.
- Do not leave debug statements or commented-out obsolete implementations in new/modified code.

### Null and Optional Handling

- Handle missing resources explicitly.
- Do not use `null` as an implicit API contract when an exception or `Optional`-based internal design is clearer.
- Do not use `Optional` as an entity field or method parameter.

### Exceptions

- Do not use `catch (Exception e)` as a generic control-flow mechanism.
- Do not silently convert unexpected failures into a successful response.
- Prefer specific application/domain exceptions.
- For REST APIs, use centralized exception handling (`@RestControllerAdvice`) for consistent error responses when implementing or modernizing error handling.
- Never expose stack traces, SQL statements, or internal exception details to API consumers.

### Logging

- Use a proper logging framework; do not use `System.out.println()` in application code.
- Log useful context for failures and operational troubleshooting.
- Never log passwords, tokens, API keys, or other secrets.

### Comments

- Comments should explain why something is done when the reason is not obvious.
- Do not add comments that merely restate the code.
- Keep comments accurate when code changes.

### Data Types

- Review the existing API/data model before changing field types.
- Do not change salary fields or IDs casually because existing clients/data may depend on the current representation.
- For new monetary functionality, prefer an appropriate numeric representation such as `BigDecimal` rather than floating-point arithmetic.

## 5. Technology Stack

Current stack from `pom.xml`:

- Java 17
- Spring Boot 3.2.3
- Spring Web
- Spring Data JPA
- Hibernate/Jakarta Persistence
- H2 database (runtime)
- Spring Boot Actuator
- Spring Boot Test / JUnit 5
- Maven

Use the existing Maven configuration and Maven Wrapper.

Typical commands:

```bash
./mvnw test
./mvnw clean test
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd test
mvnw.cmd clean test
mvnw.cmd spring-boot:run
```

Before adding a dependency:

1. Check whether the existing Spring Boot stack already provides the capability.
2. Check whether the requirement can be implemented without another dependency.
3. Consider maintenance, security, licensing, and transitive dependencies.
4. Keep dependency additions minimal.

Do not duplicate dependencies already present in `pom.xml`.

## 6. Security Requirements

Even though this repository is currently a simple/demo application, all new code must follow production-quality security practices.

### Secrets

Never commit or hard-code:

- Passwords
- API keys
- Access tokens
- Client secrets
- Database credentials
- Private keys

Use environment/configuration mechanisms for local development and a secure secret store such as Azure Key Vault for production deployments.

### Input Validation

- Treat all request data as untrusted.
- Validate required fields and ranges.
- Prefer Jakarta Bean Validation for request validation when adding DTOs or validation to APIs.
- Validate relationships server-side.

### Authorization

If authentication/authorization is introduced:

- Enforce authorization server-side.
- Apply least privilege.
- Do not rely on client-side access restrictions.
- Protect management/administrative endpoints.

### Actuator

The current `application.properties` exposes all Actuator endpoints with:

`management.endpoints.web.exposure.include=*`

This is acceptable only for local development. Do not treat this configuration as production-safe. Production configuration should expose only required endpoints and protect management endpoints.

### H2 Console

The H2 console is currently enabled for development. Do not assume it should be exposed in production.

## 7. API Standards

### Existing API Resources

Current API roots include:

- `/companies`
- `/jobs`
- `/companies/{companyId}/reviews`

Preserve existing routes unless the task explicitly requests an API change.

### HTTP Methods

Use standard REST semantics:

- `GET` — retrieve resources
- `POST` — create resources
- `PUT` — replace/update a resource
- `PATCH` — partial update when supported
- `DELETE` — delete a resource

### Status Codes

Use appropriate HTTP status codes:

- `200 OK` — successful retrieval/update where a response is returned
- `201 Created` — successful creation
- `204 No Content` — successful operation with no response body
- `400 Bad Request` — invalid request
- `401 Unauthorized` — missing/invalid authentication
- `403 Forbidden` — authenticated but not authorized
- `404 Not Found` — resource does not exist
- `409 Conflict` — state/business conflict
- `422 Unprocessable Entity` — use only if the API contract explicitly adopts it
- `500 Internal Server Error` — unexpected server failure

Do not return `200 OK` for failures.

### Request/Response Design

- Prefer structured JSON responses for new APIs.
- Prefer DTOs for stable public API contracts.
- Do not expose internal persistence relationships unnecessarily.
- Validation errors should be consistent and actionable.
- API error responses must not expose internal implementation details.

Example error shape for new standardized APIs:

```json
{
  "status": 404,
  "code": "RESOURCE_NOT_FOUND",
  "message": "Company was not found",
  "path": "/companies/10"
}
```

### Compatibility

- Avoid breaking existing endpoints, request formats, or response formats unless explicitly required.
- If a breaking change is required, document it and update tests/documentation.

## 8. Azure DevOps Workflow

The repository itself does not define an Azure DevOps pipeline/work-item configuration. When this project is managed through Azure DevOps, follow these conventions.

### Work Items

- Associate meaningful changes with an Azure DevOps work item.
- Work items should contain a clear description and acceptance criteria.

### Branches

Prefer work-item-oriented branch names:

```text
feature/1234-add-company-validation
bugfix/1250-fix-review-delete
hotfix/1300-fix-production-error
```

Avoid vague names such as `test`, `changes`, `fix`, or `new`.

### Commits

Keep commits focused and meaningful.

Good:

`Add validation for company creation`

Avoid:

`changes`
`fix`
`update`

### CI

At minimum, CI should validate:

1. Dependency restore/build
2. Compilation
3. Unit tests
4. Integration/API tests where applicable
5. Quality/security checks where configured
6. Packaging

Do not claim a build or test passed unless it was actually executed and passed.

## 9. Testing Standards

Every new behavior should have appropriate automated tests.

### Unit Tests

Test service/business behavior, including:

- Existing resource retrieval
- Missing resource handling
- Create operations
- Update operations
- Delete operations
- Invalid company relationships
- Business rule failures

### Controller/API Tests

Test where appropriate:

- HTTP status codes
- Request validation
- Request/response payloads
- Path variables
- Not-found behavior
- Error responses

### Repository Tests

Add repository tests for custom queries or non-trivial persistence behavior. Do not write tests merely to duplicate standard Spring Data behavior.

### Integration Tests

Use Spring Boot integration testing when multiple layers need to be validated together.

The existing `DemoApplicationTests.contextLoads()` test verifies application startup only. It is not sufficient coverage for business functionality.

### Test Naming

Prefer behavior-oriented names:

```java
shouldReturnNotFoundWhenCompanyDoesNotExist()
shouldCreateReviewForExistingCompany()
shouldNotCreateReviewForUnknownCompany()
```

Avoid generic names such as `testCompany()`.

### Test Independence

- Tests must be independent.
- Do not depend on test execution order.
- Establish required data/state within each test or controlled fixtures.
- Tests must be deterministic.

## 10. Documentation Requirements

Update documentation when behavior, API contracts, configuration, or architecture changes.

### README

The README should eventually document:

- Project purpose
- Prerequisites
- Java version
- Build/run instructions
- Test instructions
- API endpoints
- Configuration
- Database behavior

### API Documentation

For public/production APIs, document endpoints including:

- HTTP method
- Path
- Parameters
- Request body
- Response body
- Status codes
- Validation rules
- Error responses

Use OpenAPI/Swagger if adopted by the project.

### Architecture Documentation

Document significant architectural decisions, especially:

- New integration patterns
- Persistence changes
- Security/authentication decisions
- API versioning
- Major dependency changes

Do not create documentation for trivial internal changes unless requested.

## 11. Pull Request Requirements

Before considering a change PR-ready, verify:

### Code

- [ ] Code compiles.
- [ ] Only necessary files were changed.
- [ ] Existing architecture is respected.
- [ ] New dependencies are justified.
- [ ] No debug code remains.
- [ ] No secrets are committed.
- [ ] No unrelated refactoring is included.

### Testing

- [ ] Relevant tests were added or updated.
- [ ] Positive scenarios are covered.
- [ ] Negative/error scenarios are covered.
- [ ] Existing tests pass.
- [ ] The relevant Maven test command was executed.

### API

- [ ] API behavior is intentional.
- [ ] Correct HTTP status codes are used.
- [ ] Validation is present where required.
- [ ] Compatibility has been considered.
- [ ] API documentation is updated when required.

### Database

- [ ] Entity relationships are correct.
- [ ] Persistence implications have been considered.
- [ ] No accidental destructive schema change was introduced.

### PR Description

Use a concise structure such as:

```text
## Summary
What changed?

## Why
Why was the change required?

## Technical Details
Important implementation/design details.

## Testing
What was tested and with which command?

## Risks / Notes
Known limitations or follow-up work.

## Work Item
Azure DevOps work item reference.
```

## 12. Definition of Done

A change is Done when all applicable criteria are satisfied:

### Functional

- [ ] Acceptance criteria are implemented.
- [ ] Business rules are correctly enforced.
- [ ] Existing behavior is not unintentionally broken.

### Architecture

- [ ] Controller/service/repository responsibilities remain separated.
- [ ] Dependencies use constructor injection.
- [ ] No unnecessary architectural complexity was introduced.

### Code Quality

- [ ] Code follows Java 17 conventions.
- [ ] Code is readable and maintainable.
- [ ] No unused imports or debug code remain.
- [ ] Exceptions are handled intentionally.
- [ ] Logging is appropriate and does not expose secrets.

### Security

- [ ] No secrets are committed.
- [ ] External input is validated where required.
- [ ] Sensitive data is not unnecessarily exposed.
- [ ] Error responses do not leak internal details.
- [ ] Security implications have been considered.

### Testing

- [ ] Relevant unit tests exist.
- [ ] Relevant API/integration tests exist.
- [ ] Positive and negative paths are covered.
- [ ] All relevant automated tests pass.

### API

- [ ] HTTP methods and status codes are appropriate.
- [ ] Existing API contracts are preserved unless intentionally changed.
- [ ] New/changed contracts are documented.

### Documentation

- [ ] README/API/architecture documentation is updated when applicable.

### Delivery

- [ ] Azure DevOps work item is linked where applicable.
- [ ] CI checks pass.
- [ ] PR is ready for human review.

## Copilot Operating Rules

When implementing a request in this repository:

1. Inspect the relevant existing classes before proposing a design.
2. Reuse existing patterns when they are appropriate.
3. Prefer the smallest safe implementation that satisfies the requirement.
4. Do not invent business requirements.
5. Do not silently change public API behavior.
6. Do not make destructive database changes without explicit requirements.
7. Do not introduce dependencies or frameworks without justification.
8. Add/update tests for changed behavior.
9. Run the relevant Maven tests after code changes when execution is available.
10. Report important assumptions, compatibility concerns, or unresolved issues instead of hiding them.
11. If a requirement is materially ambiguous, ask for clarification rather than guessing.
12. Treat security, data integrity, maintainability, and testability as first-class requirements.
