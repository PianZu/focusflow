# 📘 How to Write Good Use Cases

Use cases describe how a user/actor interacts with a system to achieve a specific goal.
Writing clear and effective use cases is not as easy as it sounds, but is essential for defining software requirements and aligning stakeholders.

This guide outlines best practices and a simple structure to help you write high-quality use case descriptions.

---

## ✅ Best Practices

- **Be user-focused**: Describe the interaction from the **user’s perspective**, not the system's.
- **Use clear and simple language**: Avoid jargon; aim for clarity.
- **Be specific**: Clearly define triggers, steps, and outcomes.
- **Keep it goal-oriented**: Focus on the **user’s objective**.
- **Structure consistently**: Use a standard format across all use cases.
- **Include alternate and error flows**: Don’t just describe the happy path.
- **Name clearly**: Use a short, descriptive title starting with a verb (e.g., `Create Account`).
- **Number or ID your use cases**: This helps with traceability.

---

## ✍️ How to Write a Use Case Description Using Subject–Verb–Why

A good use case title or description should follow this simple but effective structure:

> **[User / Actor] [does something] [to achieve a goal]**

### 🔍 Structure:

- **Subject (Actor)**: *Who* is performing the action?
- **Verb (Action)**: *What* are they doing?
- **Object (Goal)**: *What* are they trying to accomplish and *why*?

### ✅ Examples:

| Actor          | Action                  | Goal / Why                             | Use Case Title                        |
|----------------|--------------------------|-----------------------------------------|----------------------------------------|
| User           | resets password          | to regain access to their account       | Reset Password                         |
| Customer       | tracks order             | to know delivery status                 | Track Order Status                     |
| Admin          | updates user roles       | to control access to features           | Manage User Permissions                |
| Guest          | browses catalog          | to explore available products           | Browse Product Catalog                 |
| Applicant      | submits job application  | to apply for a position                 | Submit Job Application                 |

### 🔧 Tips:

- **Use active voice**: "User uploads profile picture" instead of "Profile picture is uploaded".
- **Avoid technical terms**: Use domain language the user understands.
- **Be concise**: A good title is short but informative.
- **Make the goal clear**: Helps differentiate similar actions with different intentions.

---

## 🧱 Use Case Template

Below is a simple example of a use case template. You can find more templates in this folder providing a different set of details such as:

* [use_case_high_details.md](./use_case_high_details.md)
* [use_case_mid_details.md](./use_case_mid_details.md)

Example:

```text
Use Case ID: UC-001
Title: Register New User
Primary Actor: New User
Stakeholders and Interests:
  - New User: Wants to create an account to access the service
  - System Owner: Wants user data to be valid and secure

Preconditions:
  - The user has not previously registered
  - The registration page is available

Trigger:
  - The user opens the registration page and submits the registration form

Main Success Scenario:
  1. User opens the registration page
  2. User fills in the registration form
  3. User submits the form
  4. System validates the input
  5. System creates a new user account
  6. System sends a confirmation email
  7. User sees confirmation message

Postconditions:
  - A new user account is created in the system
  - User receives a confirmation email

Extensions (Alternate Flows):
  - 4a. Invalid Input:
    - 4a1. System displays error messages for incorrect fields
    - 4a2. User corrects and resubmits the form

  - 6a. Email Sending Fails:
    - 6a1. System retries or notifies the user of delay

Special Requirements:
  - Password must be at least 12 characters long
  - Email must be unique and valid

Frequency of Use:
  - Frequently (hundreds of new users per day)
```

## 🛠 Tips for Implementation

- Store your use cases in a version-controlled repository (e.g., in /docs/use-cases/)
- Review use cases regularly with stakeholders and update them as requirements evolve
- Use diagrams (e.g., UML Use Case Diagrams) to provide visual context
