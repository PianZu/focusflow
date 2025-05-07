package de.hsesslingen.focusflow.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/features"},
    glue = {"de.hsesslingen.focusflow.steps"},
    plugin = {"pretty",  "summary","html:target/cucumber-reports"},
    monochrome = true
)
public class CucumberTest {
}