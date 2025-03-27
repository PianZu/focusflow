# FocusFlow Backend Setup Guide (Java + Spring Boot + PostgreSQL)

This tutorial will walk you through setting up the FocusFlow backend locally using Java, Maven, and PostgreSQL.

---

## Prerequisites

Before you start, make sure you have the following installed:

### 1. Java 21
Install Java Development Kit (JDK) version 21 from:
- https://jdk.java.net/21/

After installation, verify:
```bash
java -version
```

### 2. Maven
Install Maven from:
- https://maven.apache.org/download.cgi

Verify installation:
```bash
mvn -version
```

### 3. PostgreSQL
Download and install PostgreSQL:
- https://www.postgresql.org/download/

Make sure to:
- Set a password for the default user `postgres`
- Remember that password

### 4. pgAdmin (optional but recommended)
Use pgAdmin to create and manage databases via GUI:
- Create a database named `focusflow`
- Default username: `postgres`
- Password: the one you set during installation

---

## Clone the Repository

Use Git to clone the backend repository:
```bash
git clone https://github.com/your-username/focusflowbackend.git
cd focusflowbackend
```

---

## Run the Application

Navigate to the directory where the `pom.xml` file is located:
```bash
cd focusflowbackend
```

Start the Spring Boot application with Maven:
```bash

$env:DB_URL="jdbc:postgresql://localhost:5432/focusflow"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_password"

mvn spring-boot:run
```

The backend should now be running at:
```
http://localhost:8080
```

---

## Testing with Postman (VS Code Extension)

Install the **Postman extension** in VS Code, or use the Postman app.

### Test Endpoints

#### 1. GET All Messages
- Method: `GET`
- URL: `http://localhost:8080/messages`
- Click **Send**

You should receive a response with all the messages and a 200 OK Statuscode.

#### 2. POST New Message
- Method: `POST`
- URL: `http://localhost:8080/messages`
- Switch to the **Body** tab
- Choose **raw** and select **JSON** from the dropdown
- Enter the JSON:

```json
{
  "text": "Hello World"
}
```

- Click **Send**

You should receive a response containing the saved message with an ID and a 200 OK Statuscode.

---

## Done!

You're now up and running with the FocusFlow backend! 



