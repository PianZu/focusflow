# FocusFlow - Domain Model Implementation

## Overview

This document provides details on the implementation of the core domain models for the FocusFlow application. It includes entity relationships, enums and helper methods.

## 1. **Implemented Enums**

### `TaskPriority`

Represents priority levels of a task:

- `LOW`
- `MEDIUM`
- `HIGH`

### `TaskStatus`

Represents the current status of a task:

- `OPEN`
- `PENDING`
- `IN_REVIEW`
- `CLOSED`

---

## 2. **Entity Models**

### `User`

#### Fields:

| Field         | Type          | Description           |
| ------------- | ------------- | --------------------- |
| `id`        | `Long`      | Primary Key           |
| `email`     | `String`    | User email (unique)   |
| `password`  | `String`    | Hashed password       |
| `firstName` | `String`    | User's first name     |
| `lastName`  | `String`    | User's last name      |
| `createdAt` | `Timestamp` | Account creation date |
| `lastLogin` | `Timestamp` | Last login timestamp  |

#### Relationships:

- **One-to-Many** with `Task` (A user can have multiple tasks)
- **Many-to-Many** with `Team` (A user can belong to multiple teams)

---

### `Team`

#### Fields:

| Field           | Type          | Description      |
| --------------- | ------------- | ---------------- |
| `id`          | `Long`      | Primary Key      |
| `name`        | `String`    | Team name        |
| `description` | `String`    | Team description |
| `createdAt`   | `Timestamp` | Creation date    |

#### Relationships:

- **Many-to-Many** with `User` (A team can have multiple members)
- **One-to-Many** with `Task` (A team can have multiple tasks)

---

### `Task`

#### Fields:

| Field                | Type             | Description               |
| -------------------- | ---------------- | ------------------------- |
| `id`               | `Long`         | Primary Key               |
| `title`            | `String`       | Task title                |
| `shortDescription` | `String`       | Brief summary of the task |
| `longDescription`  | `String`       | Detailed task description |
| `dueDate`          | `Date`         | Task deadline             |
| `priority`         | `TaskPriority` | Task urgency level        |
| `status`           | `TaskStatus`   | Current task status       |

#### Relationships:

- **Many-to-One** with `User` (Assigned to a user)
- **Many-to-One** with `Team` (Belongs to a team, optional)

---

## 3. **Helper methods**

For entity and task management.

### `User`

- createUser(User user): Saves a user to the database
- findUserById(Long id): Finds a user by their ID

### `Team`

- createTeam(Team team): Saves a team to the database
- findTeamByID(Long id): Find a team by ID
- addMember(Long teamId, Long userId): Add a user to a team
- removeMember(Long teamId, Long userId): Remove a user from a team

### `Task`

- createTask(Long userId, Optional `<Long>` teamId): Creates and saves a task to the database
- findTaskById(Long id): Find task by ID
- updateTaskStatus(Long taskId, TaskStatus status): Updates the task status
- updateTaskPriority(Long taskId, TaskPriority priority): Updates the task priority

**Disclaimer:** This list of methods is subject to change and expansion. These methods reflect the core functionality of the entity and task management methods and might be part of more complex methods moving forward.

---

## 4. **Implementation Details**

### **Making `Team` Optional in `Task`**

To allow tasks to be created **without a team**, we updated the `Task` entity:

```java
@ManyToOne(optional = true, fetch = FetchType.LAZY)
@JoinColumn(name = "team_id", nullable = true)
private Team team;
```
