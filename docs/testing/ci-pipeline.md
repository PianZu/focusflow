# CI Pipeline Documentation

This document describes the GitHub Actions CI pipeline for running Cypress E2E tests against your frontend application. It covers:

---

## Introduction

Automating your end-to-end tests in a continuous integration (CI) pipeline helps catch regressions early and ensures that every commit and pull request is validated against your UI. This guide shows how to set up a GitHub Actions workflow that:

- Checks out your code  
- Installs dependencies    
- Runs Cypress tests in Firefox  

---

## CI Pipeline Output

Run actions/checkout@v3
✔ Checked out HEAD to worktree

Run actions/setup-node@v3
✔ Node v18 installed

Run npm ci
✔ Dependencies installed

Run npm start &
> my-app@0.1.0 start
> react-scripts start

Run npx wait-on http://localhost:3000
✔ http://localhost:3000 is ready

Run npx cypress run --spec "cypress/integration/task.spec.js"

====================================================================================================

  (Run Starting)

  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ Cypress:    12.16.0                                                                            │
  │ Browser:    Firefox (headless)                                                            │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘

  Running:  task.spec.js                                                                      (1 of 1)

  ✔  Positive: should create a new task successfully (2203ms)
  ✔  Negative: should show error when submitting an empty task (356ms)

  2 passing (2s)

  (Results)

  ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
  │ Tests:        2                                                                                │
  │ Passing:      2                                                                                │
  │ Failing:      0                                                                                │
  │ Pending:      0                                                                                │
  │ Skipped:      0                                                                                │
  │ Screenshots:  0                                                                                │
  │ Video:        false                                                                            │
  │ Duration:     2 seconds                                                                        │
  │ Spec Ran:     task.spec.js                                                                     │
  └────────────────────────────────────────────────────────────────────────────────────────────────┘
