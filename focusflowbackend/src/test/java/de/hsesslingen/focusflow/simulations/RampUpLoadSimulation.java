package de.hsesslingen.focusflow.simulations;

import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.List;

public class RampUpLoadSimulation extends Simulation {

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
  // Ramp-Up Example: Ramp users from 1 per second to 20 per second over 10 minutes
   public final List<OpenInjectionStep> rampUpLoad = List.of(
     rampUsersPerSec(1.0).to(20.0).during(Duration.ofMinutes(10))
   );

  // Link Scenario to Load Profile
  {  
    // Ramp-Up load test (Ramp users from 1 to 20 per second over 5 minutes)
    setUp(scn.injectOpen(
       rampUsersPerSec(1.0).to(20.0).during(Duration.ofMinutes(5))
    )).protocols(httpProtocol);
  }
}
