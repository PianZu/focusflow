# Black-Box Testing: Password Validation for FocusFlow

## Overview

FocusFlow is a web-based task management system designed for individuals and small teams. As part of
the FocusFlow user registration process, ensuring strong password security is critical.

In FocusFlow, a password must meet the following criteria (e.g., https://github.com/dgrewehse/focusflow):
	• Length Requirement: The password must be between 10 and 12 characters long.
	• Composition Requirements:
		• It must contain at least one uppercase letter.
		• It must contain at least one lowercase letter.
		• It must contain at least one special character (e.g., ! @ # $ % ^ & *).

If any of these rules are not met, the registration should reject the password. Conversely, a password is
accepted only when it meets all the specified requirements

## Equivalence Class Partitioning (ECP)

ECP helps reduce the number of test cases by categorizing input into **valid** and **invalid** partitions.

### 1. Length requirement

For an password length input field (valid range 10-12):

| Equivalence Class                     | Test Input        | Explanation             |
| ------------------------------------- | ----------------- | ----------------------- |
| Valid (length >= 10 and length =< 12) | `Abcdef!@#1`    | Boundary of valid range |
| Invalid (<10)                         | `Ab!@123`       | Clearly below minimum   |
| Invalid (>12)                         | `Abcdef!@#1234` | Exceeds maximum allowed |

### 2. Composition requirement

Adhere to the rules of password composition:

| Equivalence Class                 | Test Input     | Explanation                                         |
| --------------------------------- | -------------- | --------------------------------------------------- |
| Valid (all composition rules met) | `Abcdef!@#1` | Contains uppercase, lowercase and special character |
| Missing uppercase                 | `abcdef!@#1` | No uppercase letter                                 |
| Missing lowercase                 | `ABCDEF!@#1` | No lowercase letter                                 |
| Missing special character         | `Abcdef1234` | No special character                                |

### 3. Input type and format validity

Check the given input of the user:

| Equivalence Class      | Test Input              | Explanation                                     |
| ---------------------- | ----------------------- | ----------------------------------------------- |
| Non-string input       | `1234567890` (Number) | Invalid input type; password should be a string |
| Null input             | `null`                | No value provided                               |
| Undefined input        | `undefined`           | No input provided                               |
| Empty string           | `""`                  | No characters at all                            |
| Whitespace-only string | `"           "`       | Lacks any valid characters                      |

## Decision Table Testing

### 1. Conditions

1. Is the password length valid (between 10 or 12 characters)?
2. Does the password contain at least one uppercase letter?
3. Does the password contain at least one lowercase letter?
4. Does the password contain at least special character?

### 2. Actions

- **Accept**: If **all** conditions are met.
- **Reject**: If **any** condition is not met.

### 3. Decision Table

Decision Table Testing evaluates all condition combinations to ensure rule completeness.

| Condition                   | Rule1    | Rule2    | Rule3          | Rule4          | Rule5          | Rule6    | Rule7    |
| --------------------------- | -------- | -------- | -------------- | -------------- | -------------- | -------- | -------- |
| **Length**            | <10      | <10      | >= 10 && <= 12 | >= 10 && <= 12 | >= 10 && <= 12 | >12      | >12      |
| **Uppercase letter**  | 0        | 1        | 0              | 1              | 0              | 0        | 1        |
| **Lowercase letter**  | 0        | 1        | 0              | 1              | 1              | 0        | 1        |
| **Special character** | 0        | 1        | 0              | 1              | 1              | 0        | 1        |
| **Result**            | Rejected | Rejected | Rejected       | Accepted       | Rejected       | Rejected | Rejected |

### 4. Rule Descriptions

| Rule   | Description                                                            |
| ------ | ---------------------------------------------------------------------- |
| Rule 1 | Password is too short and missing all required components.             |
| Rule 2 | Password is too short, even though it meets all character rules.       |
| Rule 3 | Valid length, but fails all composition checks.                        |
| Rule 4 | Fully valid password: correct length and all required character types. |
| Rule 5 | Missing uppercase letter, otherwise valid.                             |
| Rule 6 | Too long and fails composition checks.                                 |
| Rule 7 | Composition valid, but length is too long — still rejected.           |
