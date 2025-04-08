# Master Review Template

We will do a walkthrough through the focusflow app
[The main reason of the master review document is to provide a comprehensive overview of the project and the review process. It should include all the information about the project, the review process, the participants, the review objects, the reference documents, the checklist, and the additional notes.]

## Review Information

- **Review Number:** 1
- **Project Name:** Focus Flow
- **Project Manager:** Christian Luan
- **Quality Expert:** Maximilian Fellmann

## Review Objects

- Focus Flow introductory text, functional system requirements and specification and the code base

## Reference Documents

- functional system requirements and specification: https://github.com/dgrewe-hse/focusflow/blob/dev/docs/spec/spec.md

## Checklist

- [ ] Code Style and Formatting
- [ ] Functionality and Logic
- [ ] Error Handling
- [ ] Documentation
- [ ] Performance Considerations
- [ ] Security Considerations
- [ ] Testing Coverage
- [ ] Compliance with Standards

## Participating Reviewers and Roles

- Maximilian Fellmann, Christian Luan, William Shih

## Review Decision

- [Provide information about the review decision, including the decision made and the justification for the decision. Is the review approved or not? If not, what are the reasons for the rejection?]
- [Is a follow-up review required? If yes, what is the next step?]
- [Is a re-inspection required? If yes, what is the next step?]

## Date of Review

- **Date:** 02.04.2025

## Additional notes

- no additional notes required



# List of Findings

[The list of findings is a table that contains all the findings found during the review. It should include the following columns: No., Review Object, Finding Location, Description, Checklist/Scenario (found using checklist or a use case scenario), Found By, Severity Level (Major/Minor), Comments, Status (Open/Resolved/Rejected/Deferred/Duplicate), and Responsible Person.]

| No. | Review Object        | Finding Location | Description   | Checklist/Scenario   | Found By   | Severity Level | Comments   | Status                                      | Responsible Person   |
| --- | -------------------- | ---------------- | ------------- | -------------------- | ---------- | -------------- | ---------- | ------------------------------------------- | -------------------- |
| 1   | [File/Document/Code] | [Location]       | [Description] | [Checklist/Scenario] | [Found By] | [Major/Minor]  | [Comments] | [Open/Resolved/Rejected/Deferred/Duplicate] | [Responsible Person] |
| 2   | Flow introductory text | Flow introductory text       | to detailed for an introductory description | Documentation | Maximilian Fellmann | Minor  | no comments | Open | [Responsible Person] |
| 3   | Specsheet | Performance Requirements        | should support more current users | Performance Considerations | Maximilian Fellmann | Major  | - | Open | [Responsible Person] |
| 1   |  Specsheet | Reliability Requirements        | 99.9% uptime is not maintainable, it should be 95% | Reliability Considerations | [Found By] | Major  | - | Open | [Responsible Person] |
| 1   |  Specsheet | Security Requirements      | There is no clarity on how the login and registration should be iplementet, should be external authorization like Keycloak to handle data | Security Considerations | Maximilian Fellman | Major  | - | Open | [Responsible Person] |
| 1   |  Codebase | [Location]       | [Description] | [Checklist/Scenario] | [Found By] | [Major/Minor]  | [Comments] | [Open/Resolved/Rejected/Deferred/Duplicate] | [Responsible Person] |
| 1   | [File/Document/Code] | [Location]       | [Description] | [Checklist/Scenario] | [Found By] | [Major/Minor]  | [Comments] | [Open/Resolved/Rejected/Deferred/Duplicate] | [Responsible Person] |
| 1   | [File/Document/Code] | [Location]       | [Description] | [Checklist/Scenario] | [Found By] | [Major/Minor]  | [Comments] | [Open/Resolved/Rejected/Deferred/Duplicate] | [Responsible Person] |
| 1   | [File/Document/Code] | [Location]       | [Description] | [Checklist/Scenario] | [Found By] | [Major/Minor]  | [Comments] | [Open/Resolved/Rejected/Deferred/Duplicate] | [Responsible Person] |
