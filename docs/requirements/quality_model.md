# FocusFlow – Quality Model (ISO 25010)

Based on the initial description and functional requirements of the FocusFlow application, the following four ISO 25010 quality attributes were identified as the most relevant. Each quality aspect is defined using the 3-step quality model approach discussed in the lecture: Abstract, Specific, and Measurable.


## 1. Functional Suitability

**Abstract:**  
The system must deliver complete and appropriate functionality that meets the user’s task management needs.

**Specific:**  
FocusFlow should allow users to create, organize, and track tasks in a way that reflects their priorities and workflow. All core features (task creation, categorization, assignment, status tracking) must be correctly implemented and aligned with user expectations.

**Measurable:**  

- All task-related core functions (creation, status updates, grouping) are implemented and verified.  
- 95% of test cases related to task management pass successfully.  
- User acceptance tests confirm that essential task workflows are achievable without workarounds.


## 2. Performance Efficiency

**Abstract:**  
The system should operate efficiently, with acceptable response times and resource usage under expected conditions.

**Specific:**  
FocusFlow should load task views and allow task interactions (create/edit/assign) quickly, even with a moderate number of concurrent users or tasks.

**Measurable:**  

- Average response time for core operations (e.g., loading dashboard, creating a task) < 1000 ms.  
- System supports up to 100 active tasks per user with no performance degradation.  


## 3. Usability

**Abstract:**  
The user interface must be easy to understand and operate, supporting users in achieving their goals efficiently and with satisfaction.

**Specific:**  
The system should provide a clean and intuitive interface that requires little to no onboarding. Features should be self-explanatory, and workflows should follow logical patterns.

**Measurable:**  

- Users can complete a task creation and assignment flow in under 2 minutes on average.


## 4. Reliability

**Abstract:**  
The system must perform its intended functions consistently and be available when needed.

**Specific:**  
FocusFlow should remain available and consistent during normal use and recover gracefully from errors (e.g., interrupted connections or system crashes).

**Measurable:**  

- System uptime ≥ 90% over a 1-day period.  
- No data loss after simulated crash scenarios; 90% of tasks recoverable.  
- Fault tolerance verified through test cases simulating unexpected behavior.


## Ensuring Testability

To guarantee the testability of FocusFlow during development, the following measures should be implemented:

1. **Modular Architecture**  
   - Separate components for UI, logic, and storage to allow isolated testing.

2. **Automated Testing**  
   - Implement unit, integration, and end-to-end tests with coverage >80%.  
   - Use CI pipelines to run tests on every commit.

3. **Mocking and Stubbing**  
   - Use mock services for external dependencies (e.g., notifications, storage) to test error handling.

4. **Static Code Analysis**  
   - Integrate tools like ESLint or SonarQube to catch bugs and enforce consistency early.

5. **Logging and Traceability**  
   - Implement structured logging to enable debugging and test result analysis.

These practices ensure that quality criteria can be verified efficiently and that testing remains an integral part of the development process.
