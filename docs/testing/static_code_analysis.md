# Static Code Analysis – FocusFlow

## 1. Selected Categories and Tools

We selected the following two static analysis categories and tools for the FocusFlow application:

- **Linter**: [Checkstyle](https://checkstyle.sourceforge.io/) for Java  
- **Code Coverage Tool**: [JaCoCo](https://www.eclemma.org/jacoco/) for Java

### ✅ Why these tools?

- **Checkstyle** supports the values of FocusFlow, such as simplicity, clarity, and maintainability. Enforcing a consistent code style ensures that all developers can work in a clean and readable codebase, especially important in a collaborative environment.
  
- **JaCoCo** provides insight into the effectiveness of our unit tests. Since FocusFlow emphasizes stability and reliability through its modular architecture, code coverage is a key metric for ensuring critical functionality is tested.

---

## 2. Integration and Configuration

### ▶ Checkstyle Integration

**Steps taken:**

1. Added the Checkstyle plugin to `pom.xml`.

2. Created a `checkstyle.xml` using the Google Java Style as a base.

3. Ran it with `mvn checkstyle:check` in terminal.

### ▶ JaCoCo Integration

**Steps taken:**

1. Added the JaCoCo plugin to `pom.xml`.

2. Executed the tool using: 
    $env:DB_URL="jdbc:postgresql://localhost:5432/focusflow"
    $env:DB_USERNAME="postgres"
    $env:DB_PASSWORD="your_password"` 

    mvn clean verify
in terminal.

3. report will be accessed in `target/site/jacoco/index.html`

## 3. Results
### Checkstyle:

After running Checkstyle, the plugin reported 119 violations across multiple files.  
Common issues included:

- Usage of wildcard imports (e.g., `import xyz.*`)
- Incorrect import order or unnecessary line breaks
- Wrong indentation (expected 2 spaces according to Google Style, found 4)

We either fixed or adjusted these issues where appropriate. In some cases, we considered adjusting the rules (e.g., indentation to 4 spaces) to better match our team preferences.

### JaCoCo:

After integrating the JaCoCo Maven plugin and running the test, we currently find the test coverages of 0% in all Service Classes. 

## 4. Benefits & Trade-offs of Static Code Analysis Tools

## ✅ Benefits

- **Checkstyle** detected several small inconsistencies (e.g., indentation, import order) that were fixed immediately.  
  → This improves code readability and long-term maintainability.

- **JaCoCo** revealed that some core service classes lacked sufficient test coverage.  
  → This helped us prioritize writing more tests where needed.

- Both tools integrate seamlessly into the **Maven build process**, making them easy to include in CI/CD workflows.

---

## ⚖️ Trade-offs

- The tools introduce only a **minimal increase in build time**.

- There is a **slight learning curve** for new developers unfamiliar with static code analysis tools.








   
