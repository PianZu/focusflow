Feature: Task Creation in Focusflow
    As a registered user,
    I want to create new tasks with relevant details,
    So that I can manage my responsbilities and
    collaborate with my team effectively

Scenario: Create a task with valid title and due date
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report" 
    And enter due date "2025-10-15"
    And I assign the task to "John Doe"
    And I click the "Save" button
    Then I should see a success message "Task created successfully"
    Then the task should be saved in the system
    And the task should be visible in my task list
    And "John Doe" should receive a notification about the new task

Scenario: Assign a task to a user outside my team
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report" 
    And enter due date "2025-10-15"
    And I assign the task to "Jane Smith"
    And I click the "Save" button
    Then I should see an error message "Cannot assign task to a user outside your team"
    And the task should not be saved in the system

Scenario: Enter a due date in the past
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "Complete project report" 
    And enter due date "2020-10-15"
    And I click the "Save" button
    Then I should see an error message "Due date cannot be in the past"
    And the task should not be saved in the system

Scenario: Enter a task title with fewer than 3 characters
    Given I am a logged-in user
    And I am on the task list view
    When I click on the "New Task" button
    And I enter a task title "ab" 
    And enter due date "2025-10-15"
    And I click the "Save" button
    Then I should see an error message "Task title must be at least 3 characters long"
    And the task should not be saved in the system

Scenario: Notification failure during task assignment
    Given I am a logged-in user
    And I create a valid task assigned to "Alex"
    And the notification service is temporarily unavailable
    When I click the "Save" button
    Then the task should be saved in the system
    And I should see a warning message "Task created, but notification failed"
    And the system should log the failure
