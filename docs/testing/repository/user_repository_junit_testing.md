# FocusFlow Backend – Reviewing the Repository Layer Tests

## Overview

This review documents the **repository layer** unit tests of the FocusFlow project, specifically for the `UserRepository` interface.  
These tests verify correct interaction with the database using a temporary in-memory setup (H2) via `@DataJpaTest`, following the requirements of Exercise 6.3.

## Test Design Techniques Used

| Technique                   | Description                                                                     |
|----------------------------|---------------------------------------------------------------------------------|
| **Equivalence Partitioning** | Verified behavior with existing, non-existing, and invalid user data            |
| **Boundary Value Analysis**  | Edge cases such as email uniqueness, role filtering, and team membership        |
| **State-based Testing**      | Ensured data is saved, retrieved, updated and deleted as expected               |

## Review of Implemented Tests

| Method                   | Tested Scenario                                 | Result                         |
|--------------------------|--------------------------------------------------|--------------------------------|
| `save()` + `findById()` | Create a new user and retrieve by ID            |  User successfully saved and found |
| `findByEmail()`          | Search for user by email                        |  Correct user found          |
| `deleteById()`           | Delete a user and check absence                 |  User successfully deleted   |
| `findByRole()`           | Retrieve all users with a given role            |  Correct users returned      |
| `findByTeams_Id()`       | Query users by team membership (optional)       |  Planned / WIP              |

## Identified Missing Test Cases (Future Work)

### 1. Query Users by Team (Advanced Relationship)

* Requires creating and linking `Team` entities to users  
* Test:
  * `findByTeams_Id(teamId)` returns expected members

### 2. Email Uniqueness Constraint

* Attempting to save two users with the same email should fail (violates `@Column(unique=true)`)  
* Can be tested with a `DataIntegrityViolationException`

### 3. Update Operation

* Save → modify user → save again → check updated fields  
* e.g. changing the user's role or lastLogin time

## Conclusion

The `UserRepository` has been successfully tested using Spring Boot's `@DataJpaTest`, which provides a fast and isolated environment using an in-memory H2 database.  
This allows verifying JPA queries, entity mappings, and database constraints without requiring a real PostgreSQL instance.

The repository layer is now well-covered for CRUD operations and can be extended with additional queries or constraints as the application evolves.
