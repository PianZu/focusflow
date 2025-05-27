# FocusFlow Backend - Reviewing the Domain Model Tests

## Overview

This review covers the **model layer** unit tests of the FocusFlow project.
It includes an analysis of existing tests, identification of missing cases and suggestions for improvements following structured testing techniques.

## Test Design Techniques Used

| Technique                          | Description                                                           |
| ---------------------------------- | --------------------------------------------------------------------- |
| **Equivalence Partitioning** | Divided input data into valid and invalid partitions                  |
| **Boundary Value Analysis**  | Focused on boundary cases (e.g., string length for dates, passwords)  |
| **Decision Table Testing**   | Ensured logical combinations (especially status/priority transitions) |

## Review of Existing Tests

| Model Class      | Existing Tests                      | Findings                                        |
| ---------------- | ----------------------------------- | ----------------------------------------------- |
| `Task`         | Creation, attribute setting         | Correct, but could add null/invalid input cases |
| `TaskStatus`   | Status transitions                  | Basic transition works                          |
| `TaskPriority` | Priority transitions                | Works                                           |
| `Team`         | Creation, member/task relationships | Good coverage, could add null input cases       |
| `User`         | Creation, team/task relationships   | Good, but email/password validation missing     |

## Identified Missing Test Cases

### 1. Email Validation (optional future work)

* Check for a valid email format using regex
* Proposed simple tests:
  * Valid email (e.g., `test@example.com`) → Pass
  * Invalid email (e.g., `testexample.com`) → Fail

### 2. Null Handling / Defensive Testing

* Test if model handles `null` fields gracefully
* Example:
  * `Task.setTitle(null)`
  * `User.setEmail(null)`

### 3. Task Due date Validation

* Test if task dates pass the set due date
* Allows checking of tasks which have not been completed till due date
* Test:
  * `assertTrue(task.getDueDate().isBefore(LocalDateTime.now())`

## Conclusion

The current model tests cover basic functionality well. Applying structured testing methods revealed important missing edge cases. Later phases will include service and controller layer testing to achieve full test coverage of the FocusFlow backend. Tests cases for password validation or extended attribute validation will be taken into account. The model layer stability has increased by adding these tests and will increase with the future addition of test cases.
