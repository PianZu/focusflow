// cypress/integration/task.spec.js

describe('Task Management UI - E2E Tests', () => {
  const baseUrl = 'http://localhost:3000';

  beforeEach(() => {
    // Reset the application state if needed
    cy.visit(baseUrl);
  });

  it('Positive: should create a new task successfully', () => {
    // Arrange
    const taskText = 'Buy groceries';

    // Act
    cy.get('[data-testid="new-task-input"]')
      .type(taskText)
      .should('have.value', taskText);

    cy.get('[data-testid="add-task-button"]').click();

    // Assert
    cy.get('[data-testid="task-list"]')
      .should('contain.text', taskText);
  });

  it('Negative: should show error when submitting an empty task', () => {
    // Act
    cy.get('[data-testid="new-task-input"]')
      .should('have.value', '');
    cy.get('[data-testid="add-task-button"]').click();

    // Assert
    cy.get('[data-testid="error-message"]')
      .should('be.visible')
      .and('contain.text', 'Task description cannot be empty');
  });
});