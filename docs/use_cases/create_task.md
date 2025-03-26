# Use Case ID: UC-001

**Title: Create Task**

**Primary Actor: Registered User**

#### Stakeholders and Interests

• Registered User: Wants to create a task to organize personal or team responsibilities
efficiently
• System Owner: Wants tasks to be stored reliably, with valid metadata for filtering and
deadlines

#### Preconditions:

• The user is logged in
• The task creation interface is available
Trigger:
• The user clicks the “New Task” button in the task list view

#### Main Success Scenario:

1. User clicks “New Task”
2. System displays the task creation form
3. User enters the task title, optional description, due date, and optionally assigns a
   category or user
4. User clicks “Save”
5. System validates the input
6. System creates and stores the new task in the database
7. System refreshes the task list showing the new task
8. (If task is assigned) Assigned user receives a notification

#### Postconditions:

• A new task is stored in the system
• The task appears in the user's task list

#### Extensions (Alternate Flows):

• 5a. Invalid Input:
	o 5a1. System highlights missing or invalid fields (e.g., empty title)
	o 5a2. User corrects input and resubmits the form
• 8a. Notification Failure:
	o 8a1. System logs the failure
	o 8a2. Optionally displays a warning to the assigning user

#### Special Requirements:

• Task title is mandatory and must be between 3 and 100 characters
• Due date (if provided) must be a valid future date
• Assigned users must be part of the same team

#### Frequency of Use:

• Very frequent (users may create multiple tasks per day)
