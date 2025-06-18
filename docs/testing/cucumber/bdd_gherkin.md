# BDD Test Automation

This document outlines the configuration and implementation steps undertaken to finalize the BDD (Behavior-Driven Development) acceptance tests for the FocusFlow application, as per Exercise 7.3. It also includes a reasoned decision on whether BDD tests are more appropriately executed during the unit or integration test stage of the project.

## 1. Configuration and Implementation Steps

The goal was to ensure that the BDD tests, defined in Gherkin (Exercise 7.1) and implemented with Java Step Definitions (Exercise 7.2), are detected and executed by the project's unit testing framework (JUnit via Maven).

### 1.1. Cucumber JUnit Runner (`CucumberTest.java`)

A dedicated JUnit Runner class was configured to execute Cucumber scenarios. This class acts as the entry point for JUnit to discover and run the BDD tests.

**`src/test/java/de/hsesslingen/focusflow/runner/CucumberTest.java`:**

```java
package de.hsesslingen.focusflow.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/features"},
    glue = {"de.hsesslingen.focusflow.steps"},
    plugin = {
        "pretty",
        "summary",
        "html:target/cucumber-reports/focusflow-report.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml"
    },
    monochrome = true
)
public class CucumberTest {
    // This class remains empty and serves as the JUnit entry point.
}
```

**Key `@CucumberOptions`:**

* `features`: Specifies the path to the `.feature` files containing Gherkin scenarios.
* `glue`: Defines the base Java package where Cucumber searches for Step Definition classes.
* `plugin`: Configures various reporters for test output and results, including console (`pretty`, `summary`), HTML, JSON, and JUnit XML formats.
* `monochrome`: Ensures clean console output.

### 1.2. Maven Dependencies (`pom.xml`)

The following core dependencies were included in the `pom.xml` to support Cucumber with JUnit and Spring Boot testing:

```xml
<!-- Cucumber Dependencies -->
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>${cucumber.version}</version> <!-- e.g., 7.14.0 -->
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-junit</artifactId>
    <version>${cucumber.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-spring</artifactId>
    <version>${cucumber.version}</version>
    <scope>test</scope>
</dependency>
<!-- JUnit 4 for the @RunWith(Cucumber.class) Runner -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
<!-- JUnit Vintage Engine: Crucial for running JUnit 4 based runners
     (like CucumberTest.java) when Maven Surefire defaults to or uses
     the JUnit 5 Platform Provider. -->
<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- Standard Spring Boot Test dependency for MockMvc, etc. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 1.3. Maven Surefire Plugin Configuration (`pom.xml`)

The Maven Surefire Plugin is responsible for running tests during the `test` phase. Its configuration ensures that the `CucumberTest.java` runner is included:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version> <!-- Or your current version -->
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
            <include>**/CucumberTest.java</include> <!-- Explicitly includes the runner -->
        </includes>
        <testFailureIgnore>true</testFailureIgnore> <!-- Should be 'false' for CI/CD -->
    </configuration>
</plugin>
```

### 1.4. Spring Context Configuration for Cucumber

To enable Spring Boot's testing features (like `@SpringBootTest`, `@Autowired MockMvc`) within Cucumber Step Definitions, the `@CucumberContextConfiguration` annotation must be present on exactly one class within the `glue` path. This was placed on the `UserRegistrationSteps.java` class, which also explicitly declares the main application class for `@SpringBootTest`.

**Excerpt from `UserRegistrationSteps.java`:**

```java
import de.hsesslingen.focusflowbackend.FocusflowbackendApplication; // Main application class
import io.cucumber.spring.CucumberContextConfiguration;
// ... other imports

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = FocusflowbackendApplication.class // Explicit configuration class
)
@AutoConfigureMockMvc
@CucumberContextConfiguration // Sole Spring context configurator for Cucumber
public class UserRegistrationSteps {
    // ...
}
```

Other Step Definition classes (`TeamCreationSteps.java`, `TaskCreationSteps.java`) retain `@SpringBootTest` and `@AutoConfigureMockMvc` for local dependency injection but do not carry `@CucumberContextConfiguration`.

### 1.5. Key Implementation Steps to Enable Test Execution

1. **Gherkin Features and Step Definitions:** `.feature` files were created (Exercise 7.1) and corresponding Java Step Definitions using `MockMvc` were implemented (Exercise 7.2).
2. **Runner Configuration:** The `CucumberTest.java` runner was configured as shown in section 1.1.
3. **Dependency Management:** The `junit-vintage-engine` dependency was added to the `pom.xml`. This was crucial for allowing the JUnit 4-based `CucumberTest` runner to be executed correctly by Maven Surefire, which in this project defaults to using the JUnit 5 Platform Provider.
4. **Resolving Configuration Conflicts:**
    * An initial `IllegalStateException` (Unable to find `@SpringBootConfiguration`) was resolved by explicitly specifying `classes = FocusflowbackendApplication.class` in the `@SpringBootTest` annotation of the class holding `@CucumberContextConfiguration`.
    * `DuplicateStepDefinitionException` errors were resolved by ensuring unique Gherkin phrasing and corresponding unique Java annotation patterns for similar steps across different feature contexts (e.g., differentiating "success message" for tasks, teams, and user registration). This involved modifying both the `.feature` files and the `@Then` (and other) annotations in the Step Definition classes.

### 1.6. Test Execution and Current Status

BDD tests are executed via the Maven command `mvn clean test`.
The tests are now being detected and executed. As of the last run, the output shows a number of `AssertionFailedError` (e.g., `expected: <201> but was: <404>`) and some assertion failures related to specific error messages in the User Registration feature.

* The 404 errors for Task and Team creation are **expected**, as the corresponding API endpoints (`/api/tasks`, `/api/teams`) have not yet been implemented in the main application code. These failing tests correctly indicate the "red" phase of TDD/BDD, guiding further backend development.
* The assertion failures in User Registration indicate discrepancies between the expected API responses (defined in the tests) and the actual responses from the existing `/api/user/register` endpoint. These highlight areas where either the tests need to be aligned with actual API behavior or the API behavior needs to be adjusted to meet the specified acceptance criteria.

The configured reporters (HTML, JSON, JUnit XML) provide detailed insights into these (currently failing) test results.

## 2. BDD Tests: Unit or Integration Test Stage?

For the FocusFlow project, and generally for BDD tests scenarios that verify API-level behavior or user-facing features, it is **more sensible to execute BDD tests during the integration test stage** of the project.

**Reasoning:**

1. **Scope and Nature of BDD Tests:** The BDD tests implemented for FocusFlow (e.g., user registration, team creation, task creation) are, by nature, acceptance tests or high-level integration tests. They verify the behavior of the system by interacting with its API, thus testing the collaboration of multiple components (controllers, services, repositories, etc.) rather than isolated units of code.
2. **Dependencies and Environment:** These tests require a fully initialized Spring Boot application context (`@SpringBootTest`) to function, enabling tools like `MockMvc` to simulate HTTP requests. This dependency on a significant portion of the application stack moves them beyond the scope of typical unit tests, which aim for isolation.
3. **Execution Time:** BDD tests that involve application context loading and API interactions are generally slower than granular unit tests. Unit tests should be fast to provide quick feedback during development. Grouping longer-running BDD/integration tests into a separate stage prevents them from slowing down the rapid feedback loop desired from unit tests.
4. **Test Pyramid Principle:** According to the test pyramid, a healthy test suite has a large base of fast unit tests, a smaller layer of integration tests, and an even smaller layer of end-to-end/UI tests. BDD tests verifying API features fit best into the integration test layer.
5. **Maven Build Lifecycle and Tooling:** Maven supports this separation through distinct lifecycle phases and plugins. The `maven-surefire-plugin` is conventionally used for unit tests (bound to the `test` phase), while the `maven-failsafe-plugin` is designed for integration tests (operating in phases like `pre-integration-test`, `integration-test`, `post-integration-test`). For a clean separation, BDD tests could be configured to run with Failsafe (e.g., by naming the runner class `*IT.java` or through specific Failsafe configuration).

While the BDD tests in this exercise are currently executed by the Surefire plugin, their conceptual placement is within the integration test stage. They serve as an executable specification of how the system should behave from an end-user or external API consumer's perspective, validating that implemented features meet the defined acceptance criteria.

---
