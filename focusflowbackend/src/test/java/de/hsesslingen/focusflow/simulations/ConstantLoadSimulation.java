package de.hsesslingen.focusflow.simulations;

import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.List;

public class ConstantLoadSimulation extends Simulation {

  // HTTP Configuration
  public final HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://localhost:8080/api")
    .acceptHeader("application/json")    
    .userAgentHeader("Gatling/PerformanceTest");

  // Scenario Definition
  public final ScenarioBuilder scn = scenario("Basic API Calls")
    .exec(http("Get all tasks").get("/tasks/all"))
    .pause(Duration.ofSeconds(1))
    .exec(http("Get all teams").get("/teams/all"));

  // Load Injection
  // Constant Load Example: Inject 10 users per second for 60 seconds
  public final List<OpenInjectionStep> constantLoad = List.of(
    rampUsers(10).during(Duration.ofSeconds(5)),
    constantUsersPerSec(10.0).during(Duration.ofSeconds(55)) // Use 10.0 for double parameter
  );

  // Link Scenario to Load Profile
  {  
    // Constant load test
    setUp(scn.injectOpen(
        rampUsers(10).during(Duration.ofSeconds(5)),
        constantUsersPerSec(10.0).during(Duration.ofSeconds(55))
    )).protocols(httpProtocol);
  }
}