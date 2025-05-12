Feature: Team Creation in Focusflow
    As a registered user,
    I want to create new teams with relevant details,
    So that I can collaborate and assign tasks to team members

Scenario: Successfully create a team with valid details
    Given I am a logged-in user
    When I click "Create Team"
    When I enter a team name "Development Team"
    And I enter a description "Team responsible for software development"
    And I click the "Save" team button
    Then I should see the team creation success message "Team created successfully"
    And the team should be saved in the system
    And the team should be visible in my team list

Scenario: Create team without providing a name
    Given I am a logged-in user
    When I click "Create Team"
    And I leave the team name blank
    And I enter a description "Team responsible for software development"
    And I click the "Save" team button
    Then I should see the team creation error message "Team name is required"
    And the team should not be saved in the system

Scenario: Add team members during team creation
    Given I am a logged-in user
    When I click "Create Team"
    And I enter a team name "Development Team With Members"
    And I enter a description "Team responsible for software development with specific members"
    And I select members "john.doe.team@example.com" and "jane.smith.team@example.com"
    And I click the "Save" team button
    Then I should see the team creation success message "Team created successfully with members"
    And the team should be saved in the system with the selected members
    And "Development Team With Members" should be visible in my team list
    And "john.doe.team@example.com" and "jane.smith.team@example.com" should be listed as members of the team
    And the members should receive a notification about the new team