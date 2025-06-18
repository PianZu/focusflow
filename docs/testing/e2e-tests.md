## E2E Test Suite: Task Management UI

This document contains Cypress end-to-end (E2E) tests for validating key user flows in the Task Management UI. It includes:

- **Overview** of the test scenarios
- **Setup & Installation** instructions
- **Execution** commands
- **Test Cases**:
  - Positive: Task creation works as expected
  - Negative: Submitting an empty task shows an error

---

### 1. Overview

These tests verify:

- **Positive Flow**: A user can successfully create a new task and see it in the list.
- **Negative Flow**: The application prevents creating an empty task and displays an error message.

Selectors use `data-testid` attributes for stability and clarity.

### 2. Setup & Installation

1. **Install Cypress** (if not already installed):
   
   Make sure, you are in Frontend (dem ordner mit deiner `package.json`) and execute:
   ```bash
   1. npm install cypress --save-dev
   2. npx cypress open
   3. npx cypress run --spec "cypress/integration/task.spec.js"

### 3. Test Execution & Output

1. Headless Mode

====================================================================================================

  (Run Starting)

  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ Cypress:    12.16.0                                                                            │
  │ Browser:    Firefox (headless)                                                                 │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘

  Running:  task.spec.js                                                                      (1 of 1)

  ✔  Positive: should create a new task successfully (2203ms)
  ✔  Negative: should show error when submitting an empty task (356ms)

  2 passing (2s)

  (Results)

  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ Tests:        2                                                                                │
  │ Passing:      2                                                                                │
  │ Failing:      0                                                                                │
  │ Pending:      0                                                                                │
  │ Skipped:      0                                                                                │
  │ Screenshots:  0                                                                                │
  │ Video:        false                                                                            │
  │ Duration:     2 seconds                                                                        │
  │ Spec Ran:     task.spec.js                                                                     │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘

### 4. Errors

Errors Observed: None. Tests passed on every run.

Flaky Behavior: No flakiness detected across multiple reruns.

Insights:

    Using data-testid selectors greatly reduces test brittleness.

    Cypress’s retry mechanism on assertions helped avoid transient failures.

    The wait-on step reliably handles server startup timing in CI.

### 5. Reflection

What was easy?

    Translating BDD scenarios into clear positive/negative test cases.

    Writing expressive Cypress commands with built-in assertions.

    Integrating headless runs into CI with minimal overhead.

What was difficult?

    Ensuring the frontend server is fully ready before tests begin (resolved with wait-on).

    Configuring CI to match the local dev script (npm run dev vs. npm start).

    Balancing depth of test coverage against execution speed.



