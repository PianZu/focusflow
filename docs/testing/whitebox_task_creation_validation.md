# ⚪️ White-Box Testing: `createTask(...)` in FocusFlow

## Overview

FocusFlow is a web-based task management system designed for individuals and small teams.  
As part of the task creation process, ensuring proper validation logic for each input field is critical to maintaining consistency and data integrity.

In FocusFlow, a task must satisfy several conditions during creation:

• Title Requirements: Must be present and between 1 and 100 characters long.  
• Short Description: Must be present and no longer than 200 characters.  
• Due Date: Must be provided and set in the future.  
• Priority: Must be set and valid.  
• Assignment Logic: A task can be assigned to either a user or a team — not both at the same time.  
• Entity Validation: Creator, Assignee, Team, and Tags must exist in the database.

If any of these rules are violated, task creation should be rejected. A task is created successfully only when **all conditions are met**.

## Logic Analysis

Below are the most important **conditions** in the method:

| # | Check | Description |
|--|---------|--------------|
| 1 | `title == null || title.trim().isEmpty()` | Title must not be null or empty |
| 2 | `title.length() < 1 || > 100` | Title must be between 1 and 100 characters long |
| 3 | `shortDescription == null || shortDescription.trim().isEmpty()` | Short description must not be null or empty |
| 4 | `shortDescription.length() > 200` | Short description must not exceed 200 characters |
| 5 | `dueDate == null` | Due date must not be null |
| 6 | `dueDate.isBefore(LocalDateTime.now())` | Due date must be in the future |
| 7 | `priority == null` | Priority is a required field |
| 8 | `createdById == null` | Creator ID is a required field |
| 9 | `assigneeId != null && teamId != null` | Only one of assignee or team can be set, not both |
| 10 | `createdBy` exists in the DB? | Must be present in the database |
| 11 | `assignee` exists in the DB? | Only checked if ID is provided |
| 12 | `team` exists in the DB? | Only checked if ID is provided |
| 13 | All `tagIds` exist in DB? | Optional; if provided, each must exist |

## White-Box Test Cases

| Test ID | Description | Input Combination | Expected Result |
|--------|-------------|-------------------|------------------|
| WB1 | Valid Task | All fields are valid | Task is created |
| WB2 | Title missing | `title = null` | Exception: "Title is required" |
| WB3 | Title too long | `title.length() = 101` | Exception: "Title must be between 1 and 100 characters" |
| WB4 | Short description missing | `shortDescription = null` | Exception: "Short description is required" |
| WB5 | Short description too long | `shortDescription.length() = 201` | Exception: "Short description must be at most 200 characters" |
| WB6 | Due date missing | `dueDate = null` | Exception: "Due date is required" |
| WB7 | Due date in the past | `dueDate = now().minusDays(1)` | Exception: "Due date must be in the future" |
| WB8 | Priority missing | `priority = null` | Exception: "Priority is required" |
| WB9 | Creator ID missing | `createdById = null` | Exception: "Created by user ID is required" |
| WB10 | Both assignee and team set | Both IDs ≠ null | Exception: "A task cannot be assigned to both..." |
| WB11 | Creator not found | UUID not in DB | Exception: "Created by user not found" |
| WB12 | Assignee not found | UUID not in DB | Exception: "Assignee not found" |
| WB13 | Team not found | UUID not in DB | Exception: "Team not found" |
| WB14 | Invalid tag ID | `tagIds` includes an unknown ID | Exception: "Tag with ID ... not found" |
| WB15 | Only team set | Only `teamId ≠ null` | Task is created |
| WB16 | Only assignee set | Only `assigneeId ≠ null` | Task is created |
| WB17 | No tags provided | `tagIds = null` | Task is created |
| WB18 | Empty tag set provided | `tagIds = empty set` | Task is created |

## Sample Test Case Description (WB4)

```
Test ID: WB4  
Description: Verifies that the system responds correctly when no short description is provided.  
Input: shortDescription = null  
Expected Result: Exception "Short description is required" is thrown.  
Reason: Fails the required field validation.
```
