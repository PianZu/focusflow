# FocusFlow Backend – Reviewing the Service Layer Tests

## Overview

This review covers the **service layer** unit tests of the FocusFlow project.  
It focuses on the `UserService`, which handles business logic related to user registration, authentication, and role assignment.  
Mocking was used to isolate the service from the repository layer using Mockito.

## Test Design Techniques Used

| Technique                   | Description                                                             |
| -------------------------- | ----------------------------------------------------------------------- |
| **Equivalence Partitioning** | Valid vs. invalid user credentials were separated                       |
| **Boundary Value Analysis**  | Edge cases for password length and email format                         |
| **Decision Table Testing**   | Logical combinations of login, registration, and role-related actions   |

## Review of Implemented Tests

| Method             | Tested Scenario                           | Result                          |
|--------------------|--------------------------------------------|---------------------------------|
| `registerUser()`   | Valid email and password                  | User successfully registered |
| `registerUser()`   | Invalid password (too short)              | Exception thrown             |
| `login()`          | Correct email and password                | Login successful             |
| `login()`          | Correct email, wrong password             | Login failed                 |
| `login()`          | Email not registered                      | Login failed                 |
| `assignRole()`     | Role changed from USER to ADMIN           | Role successfully updated    |

## Identified Missing Test Cases (Future Work)

### 1. Team Membership Management (Planned for 6.3+)

* Assigning users to teams  
* Removing users from teams  
* Verifying team-user relationships are persisted correctly

### 2. Password Hashing / Encoder Integration (Optional)

* Use a `PasswordEncoder` to hash passwords securely  
* Test:
  * `encoder.encode(rawPassword)` and `encoder.matches(...)` should work  
  * Currently uses plain text comparison (mocked)

### 3. Null / Defensive Input Handling

* Test handling of null user fields during registration/login  
* Example:
  * `registerUser(null)` → Should throw `IllegalArgumentException`

## Conclusion

The current `UserService` tests provide good coverage of the core user operations, including registration, login, and role assignment.  
Using mocking allowed the business logic to be tested in isolation, ensuring reliable behavior without relying on database interaction.  
The test suite will be extended in future iterations to include team management, password encoding, and defensive programming practices.
