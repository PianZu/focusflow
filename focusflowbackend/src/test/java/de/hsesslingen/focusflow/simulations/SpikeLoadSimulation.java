package de.hsesslingen.focusflow.simulations;

import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.List;

public class SpikeLoadSimulation extends Simulation {

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
  // Spike Load Example: Hit with 100 users simultaneously after a short wait
  public final List<OpenInjectionStep> spikeLoad = List.of(
     nothingFor(Duration.ofSeconds(10)),
     atOnceUsers(100)
   );

  // Link Scenario to Load Profile
  {  
    // Spike Load test (Hit with 100 users simultaneously after a short wait)
    setUp(scn.injectOpen(
     nothingFor(Duration.ofSeconds(10)),
     atOnceUsers(100) 
    )).protocols(httpProtocol);
  }
}
