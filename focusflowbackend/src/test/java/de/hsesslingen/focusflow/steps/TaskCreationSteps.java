package de.hsesslingen.focusflow.steps;

import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class TaskCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    private String title;
    private String dueDate;
    private String assignedUser;
    private MvcResult result;

    @Given("I am a logged-in user")
    public void loggedInUser() {
        // Authentifizierung optional simulieren
    }

    @And("I am on the task list view")
    public void onTaskListView() {
        // UI-Status nicht relevant für API-Test
    }

    @When("I click on the {string} button")
    public void clickNewTaskButton(String btn) {
        // Simuliere Klick → implizit durch POST
    }

    @When("I enter a task title {string}")
    public void enterTaskTitle(String title) {
        this.title = title;
    }

    @And("enter due date {string}")
    public void enterDueDate(String date) {
        this.dueDate = date;
    }

    @And("I assign the task to {string}")
    public void assignTask(String user) {
        this.assignedUser = user;
    }

    @And("I click the {string} button")
    public void clickSave(String button) throws Exception {
        String requestJson = String.format(
            "{\"title\":\"%s\", \"dueDate\":\"%s\", \"assignee\":\"%s\"}",
            title, dueDate, assignedUser
        );

        result = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andReturn();
    }

    @Then("I should see a success message {string}")
    public void checkSuccessMessage(String expected) throws Exception {
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(expected));
    }

    @Then("the task should be saved in the system")
    public void taskSaved() {
        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("the task should not be saved in the system")
    public void taskNotSaved() {
        assertNotEquals(200, result.getResponse().getStatus());
    }

    @Then("I should see an error message {string}")
    public void errorMessage(String expected) throws Exception {
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(expected));
    }
}

