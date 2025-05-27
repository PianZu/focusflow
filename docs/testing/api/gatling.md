# FocusFlow Load Testing with Gatling

This document describes how to run performance/load tests for the FocusFlow backend using [Gatling](https://gatling.io/), integrated via Maven. We use three types of load profiles to simulate different traffic patterns.

---

## Setup Instructions

1. **Install Java (17 or later)**
   Ensure Java is installed and available in your system's PATH.
2. **Install Maven**
   Required for executing the test profiles.

   ```bash
   mvn -v
   ```

3. **Run the Spring Boot Application**
   Make sure your local server is running on `http://localhost:8080` before executing load tests.

## Running Load Tests

Each simulation can be triggered using Maven profiles defined in the `pom.xml`.

### Syntax

```bash
mvn gatling:test -P<profile-id>
```

### Available Profiles

| Profile ID                  | Load Type     | Description                                  |
| --------------------------- | ------------- | -------------------------------------------- |
| `constantLoad-simulation` | Constant Load | Simulates a steady load with a ramp-up phase |
| `rampLoad-simulation`     | Ramp-Up Load  | Gradually increases load over a time period  |
| `spikeLoad-simulation`    | Spike Load    | Simulates a sudden spike of users            |

---

## Load Test Details

### Constant Load Simulation

**Profile ID:** `constantLoad-simulation`

**Class:** `ConstantLoadSimulation`

**Behavior:**

* Ramp up to 10 users over 5 seconds.
* Maintain a constant rate of 10 users per second for 55 seconds.

**Run it:**

```bash
mvn gatling:test -PconstantLoad-simulation
```

---

### Ramp-Up Load Simulation

**Profile ID:** `rampLoad-simulation`

**Class:** `RampUpLoadSimulation`

**Behavior:**

* Gradually increase from 1 to 20 users per second over 5 minutes.

**Run it:**

```bash
mvn gatling:test -PrampLoad-simulation
```

---

### Spike Load Simulation

**Profile ID:** `spikeLoad-simulation`

**Class:** `SpikeLoadSimulation`

**Behavior:**

* Wait 10 seconds, then simulate 100 users hitting the system at once.

**Run it:**

```bash
mvn gatling:test -PrampLoad-simulation
```

---

## What the Simulations Do

Each test simulates the following user behavior:

* `GET /api/tasks/all` — Fetch all tasks.
* Pause for 1 second.
* `GET /api/teams/all` — Fetch all teams.

This basic scenario is shared across all simulations to isolate the effect of different traffic profiles.
More scenarios can be added to test further endpoints.

---

## 📂 Output

After each test, Gatling will generate an HTML report:

```bash
target/gatling/<simulation-folder>/index.html
```

Open it in a browser to view:

* Response times
* Throughput
* Error rates
* Percentiles

---

## 🧼 Clean Previous Results

Before running a new test:

```
mvn clean
```

---

## 📌 Notes

* Monitor backend CPU/RAM usage during load tests to assess performance.
