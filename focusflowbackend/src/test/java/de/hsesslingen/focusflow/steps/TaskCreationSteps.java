package de.hsesslingen.focusflow.steps; // Stelle sicher, dass das Paket korrekt ist

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
//@CucumberContextConfiguration
public class TaskCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult lastMvcResult;
    private String taskTitle;
    private String dueDate;
    private String assigneeName;
    private boolean simulateNotificationFailure = false;


    @Given("I am on the task list view")
    public void i_am_on_the_task_list_view() {
    }

    @When("I click on the {string} button")
    public void i_click_on_the_button(String buttonName) {
        if ("New Task".equalsIgnoreCase(buttonName)) {
            // No action needed, symbolic for UI preparation
        } else if (!"Save".equalsIgnoreCase(buttonName)){
             throw new IllegalArgumentException("Button click for '" + buttonName + "' not handled in this step for tasks.");
        }
    }

    @When("I enter a task title {string}")
    public void i_enter_a_task_title(String title) {
        this.taskTitle = title;
    }

    @When("enter due date {string}")
    public void enter_due_date(String date) {
        this.dueDate = date;
    }

    @When("I assign the task to {string}")
    public void i_assign_the_task_to(String userName) {
        this.assigneeName = userName;
    }

    @And("I click the {string} button") // This step will specifically handle the "Save" button for tasks
    public void i_click_the_save_task_button(String buttonName) throws Exception {
        if (!"Save".equalsIgnoreCase(buttonName)) {
            throw new IllegalArgumentException("This step is for the 'Save' button during task creation.");
        }

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("title", this.taskTitle);
        requestBodyMap.put("dueDate", this.dueDate);
        if (this.assigneeName != null) {
            requestBodyMap.put("assigneeName", this.assigneeName);
        }
        if (this.simulateNotificationFailure) {
            requestBodyMap.put("simulateNotificationFailure", true); // For backend to pick up if designed so
        }


        String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);

        this.lastMvcResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andReturn();
        this.simulateNotificationFailure = false; // Reset for next scenario
    }

    @Then("I should see the task creation success message {string}")
    public void i_should_see_a_success_message_task(String expectedMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(201, statusCode);
        assertTrue(responseBody.contains("\"message\":\"" + expectedMessage + "\""));
    }

    @Then("the task should be saved in the system")
    public void the_task_should_be_saved_in_the_system() throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"taskId\":"));
    }

    @Then("the task should be visible in my task list")
    public void the_task_should_be_visible_in_my_task_list() throws Exception {
        MvcResult getTasksResult = mockMvc.perform(get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, getTasksResult.getResponse().getStatus());
        String taskListJson = getTasksResult.getResponse().getContentAsString();
        assertTrue(taskListJson.contains("\"title\":\"" + this.taskTitle + "\""));
    }

    @Then("{string} should receive a notification about the new task")
    public void should_receive_a_notification_about_the_new_task(String userName) {
    }

    @Then("I should see the task creation error message {string}")
    public void i_should_see_an_error_message_task(String expectedErrorMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(400, statusCode);
        assertTrue(responseBody.contains("\"error\":\"" + expectedErrorMessage + "\""));
    }

    @Then("the task should not be saved in the system")
    public void the_task_should_not_be_saved_in_the_system() throws Exception {
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertTrue(statusCode >= 400 && statusCode < 500);
    }

    @Given("I create a valid task assigned to {string}")
    public void i_create_a_valid_task_assigned_to(String userName) {
        this.taskTitle = "Valid Task for " + userName;
        this.dueDate = "2099-12-31";
        this.assigneeName = userName;
    }

    @Given("the notification service is temporarily unavailable")
    public void the_notification_service_is_temporarily_unavailable() {
        this.simulateNotificationFailure = true;
    }

    @Then("I should see the task creation warning message {string}")
    public void i_should_see_a_warning_message(String warningMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(201, statusCode);
        assertTrue(responseBody.contains("\"warning\":\"" + warningMessage + "\""));
        assertTrue(responseBody.contains("\"taskId\":"));
    }

    @Then("the system should log the failure")
    public void the_system_should_log_the_failure() {
    }
}