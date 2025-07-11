Feature: Task Creation in Focusflow
    As a registered user,
    I want to create new tasks with relevant details,
    So that I can manage my responsbilities and
    collaborate with my team effectively

Scenario: Create a task with valid title and due date (assigned to self)
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report"
    And enter due date "2025-10-15"
    And I assign the task to "Task Creator"
    And I click the "Save" button
    Then I should see the task creation success message "Task created successfully"
    Then the task should be saved in the system
    And the task should be visible in my task list

Scenario: Assign a task to a user outside my team
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report" 
    And enter due date "2025-10-15"
    And I assign the task to "Jane Smith"
    And I click the "Save" button
    Then I should see the task creation error message "Cannot assign task to a user outside your team"
    And the task should not be saved in the system

Scenario: Enter a due date in the past
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report" 
    And enter due date "2020-10-15"
    And I click the "Save" button
    Then I should see the task creation error message "Due date cannot be in the past"
    And the task should not be saved in the system

Scenario: Enter a task title with fewer than 3 characters
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "ab" 
    And enter due date "2025-10-15"
    And I click the "Save" button
    Then I should see the task creation error message "Task title must be at least 3 characters long"
    And the task should not be saved in the system

Scenario: Notification failure during task assignment
    Given I am a logged-in user
    And I create a valid task assigned to "John Doe"
    And the notification service is temporarily unavailable
    When I click the "Save" button
    Then the task should be saved in the system
    Then I should see the task creation warning message "Task created, but notification failed"
    And the system should log the failure

