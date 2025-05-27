# FocusFlow Backend - Unit Testing the Domain Models

## Overview

This document outlines the unit test cases implemented for the core domain models in the FocusFlow backend project. The tests ensure proper entity behavior, validation constraints and relationships between entities.

## Test Framework

* **JUnit 5** is used for unit testing.
* **Assertions** from `org.junit.jupiter.api.Assertions` are used to verify expected outcomes.

---

## Test Cases and Scenarios

### **1. TaskTest**

**File:** `TaskTest.java`
**Description:** Tests the `Task` entity.

#### ✅ Test Cases:

* **`testTaskCreation()`**
  * Verifies object creation with valid data.
  * Checks all field values after setting them.
  * Ensures due date, priority and status are correctly assigned.
  * Validates relationships with `User` (assignee) and `Team`.

### **2. UserTest**

**File:** `UserTest.java`
**Description:** Tests the `User` entity.

#### ✅ Test Cases:

* **`testUserCreation()`**
  * Validates object creation with valid attributes.
  * Checks email, password, first name, last name and created timestamp.
* **`testUserTeamRelationShip()`**
  * Ensures a `User` can be associated with multiple `Team` objects.
  * Tests if a `User` is correctly added to a `Team`.
* **`testUserTaskRelationShip()`**
  * Checks if a `User` can be assigned multiple `Task` objects.
  * Validates correct retrieval of assigned tasks.

### **3. TeamTest**

**File:** `TeamTest.java`
**Description:** Tests the `Team` entity.

#### ✅ Test Cases:

* **`testTeamCreation()`**
  * Checks if a `Team` object is correctly initialized.
  * Validates attributes like name, description and created timestamp.
* **`testTeamMembersRelationship()`**
  * Ensures a `Team` can have multiple `User` members.
  * Checks correct assignment and retrieval of members.
* **`testTeamTasksRelationship()`**
  * Ensures a `Team` can have multiple `Task` objects.
  * Validates correct assignment and retrieval of tasks.

### **4. TaskStatusTest**

**File:** `TaskStatusTest.java`
**Description:** Tests task status transitions.

#### ✅ Test Cases:

* **`testTaskStatusTransition()`**
  * Checks if a `Task` can transition between different `TaskStatus` values.
  * Ensures that updating the status reflects the correct value.

### **5. TaskPriorityTest**

**File:** `TaskPriorityTest.java`
**Description:** Tests task priority transitions.

#### ✅ Test Cases:

* **`testTaskPriorityTransition()`**
  * Verifies that a `Task` can change its priority.
  * Ensures correct priority update.

---

## How to Run Tests

To execute all test cases, use the following command:

```sh
mvn test
```

Alternatively, if using an IDE like VS Code or IntelliJ, you can right-click the test class and select `Run Test`.

---

## Conclusion

These unit tests ensure that core domain models behave as expected, with validated data, enforced relationships and business logic integrity. Any failed test indicates a potential issue that should be addressed before deployment.
