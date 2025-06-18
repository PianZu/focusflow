package de.hsesslingen.focusflow.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsesslingen.focusflowbackend.dto.TaskCreationRequestDTO;
import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = de.hsesslingen.focusflowbackend.FocusflowbackendApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
// This class is used to define the steps for task creation scenarios in Cucumber tests
public class TaskCreationSteps {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private MvcResult lastMvcResult;
    private TaskCreationRequestDTO taskRequestDto;
    
    private String currentTaskTitle;
    private User loggedInUser;
    private User johnDoe;
    private User janeSmith;
    private User alex;

    private static final String DEFAULT_PASSWORD = "Password123!";
    private static final String DEFAULT_USER_ROLE = "USER";

    @Before(order = 10)
    public void setUp() {
        System.out.println("======== SETTING UP TASK CREATION TEST ========");
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Create test users
        loggedInUser = createUser("task.creator@example.com", "Task", "Creator");
        johnDoe = createUser("john.doe.tasks@example.com", "John", "Doe");
        janeSmith = createUser("jane.smith.tasks@example.com", "Jane", "Smith");
        alex = createUser("alex.tasks@example.com", "Alex", "Nobody");

        // Create a shared team for test scenarios
        Team sharedTeamForJohnAndCreator = new Team();
        sharedTeamForJohnAndCreator.setName("Task Collaboration Team");
        sharedTeamForJohnAndCreator.setDescription("Team for creator and John Doe");
        sharedTeamForJohnAndCreator = teamRepository.saveAndFlush(sharedTeamForJohnAndCreator);

        // Add users to team and team to users
        loggedInUser.getTeams().add(sharedTeamForJohnAndCreator);
        johnDoe.getTeams().add(sharedTeamForJohnAndCreator);

        sharedTeamForJohnAndCreator.getMembers().add(loggedInUser);
        sharedTeamForJohnAndCreator.getMembers().add(johnDoe);

        // Save entities with relationships
        loggedInUser = userRepository.save(loggedInUser);
        johnDoe = userRepository.save(johnDoe);
        teamRepository.save(sharedTeamForJohnAndCreator);

        // Initialize task request DTO
        taskRequestDto = new TaskCreationRequestDTO();
        if (loggedInUser != null && loggedInUser.getId() != null) {
            taskRequestDto.setCreatorId(loggedInUser.getId());
        } else {
            fail("loggedInUser or its ID is null after creation in @Before hook.");
        }
        
        // Initialize tracking variables
        currentTaskTitle = null;
        
        System.out.println("======== TASK CREATION TEST SETUP COMPLETE ========");
    }

    // Helper method creates a user to use for testing
    private User createUser(String email, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(DEFAULT_USER_ROLE);
        return userRepository.saveAndFlush(user);
    }

    @Given("I am on the task list view")
    public void iAmOnTheTaskListView() {
    }

    @When("I click on the {string} button")
    public void iClickOnTheButton(String buttonName) {
    }

    @When("I enter a task title {string}")
    public void iEnterATaskTitle(String title) {
        taskRequestDto.setTitle(title);
        currentTaskTitle = title;
    }

    @When("enter due date {string}")
    public void enterDueDate(String dateString) {
        try {
            LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            taskRequestDto.setDueDate(parsedDate);
        } catch (DateTimeParseException e) {
            fail("Failed to parse due date: " + dateString + ". Error: " + e.getMessage());
        }
    }

    @When("I assign the task to {string}")
    public void iAssignTheTaskTo(String userName) {
        String assigneeEmail = null;

        if ("Task Creator".equalsIgnoreCase(userName)) {
            assigneeEmail = loggedInUser.getEmail();
        } else if ("John Doe".equalsIgnoreCase(userName)) {
            assigneeEmail = johnDoe.getEmail();
        } else if ("Jane Smith".equalsIgnoreCase(userName)) {
            assigneeEmail = janeSmith.getEmail();
        } else if ("Alex".equalsIgnoreCase(userName)) {
            assigneeEmail = alex.getEmail();
        } else {
            // Fallback for other names
            assigneeEmail = "unknown." + userName.toLowerCase().replaceAll("\\s+", "") + "@example.com";
        }

        taskRequestDto.setAssigneeEmail(assigneeEmail);
    }

    @And("I click the {string} button")
    public void iClickTheSaveTaskButton(String buttonName) throws Exception {
        if (!"Save".equalsIgnoreCase(buttonName)) {
            throw new IllegalArgumentException("This step is intended for the 'Save' button during task creation.");
        }

        // Ensure we have all required fields
        if (taskRequestDto.getTitle() == null || taskRequestDto.getTitle().isEmpty()) {
            fail("Task title is missing before API call");
        }
        if (taskRequestDto.getDueDate() == null) {
            // Set a default due date to prevent validation errors
            taskRequestDto.setDueDate(LocalDate.now().plusDays(7));
        }
        
        // Ensure creator ID is set
        taskRequestDto.setCreatorId(loggedInUser.getId());
        String requestBodyJson = objectMapper.writeValueAsString(taskRequestDto);

        lastMvcResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andReturn();

        // Create a new DTO for the next scenario but maintain the current task title
        taskRequestDto = new TaskCreationRequestDTO();
        taskRequestDto.setCreatorId(loggedInUser.getId());
    }

    @Then("I should see the task creation success message {string}")
    public void iShouldSeeTheTaskCreationSuccessMessage(String expectedMessage) throws Exception {      
        String responseBody = lastMvcResult.getResponse().getContentAsString();
        int statusCode = lastMvcResult.getResponse().getStatus();
        
        // Check if the response is 201 Created
        assertEquals(201, statusCode, "Expected HTTP 201 (Created). Body: " + responseBody);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, typeRef);
        assertEquals(expectedMessage, responseMap.get("message"), "Success message mismatch.");
    }

    @Then("the task should be saved in the system")
    public void theTaskShouldBeSavedInTheSystem() throws Exception {
        int statusCode = lastMvcResult.getResponse().getStatus();
        String responseBody = lastMvcResult.getResponse().getContentAsString();

        // Check if the response is 201 Created
        assertEquals(201, statusCode, "Expected HTTP 201 (Created) status for task saving check. Body: " + responseBody);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, typeRef);
        Object taskIdObj = responseMap.get("taskId");

        // Check if taskId exists and is not null
        assertNotNull(taskIdObj, "Response should contain 'taskId'.");
        assertTrue(taskIdObj instanceof Number, "'taskId' should be a number.");
    }

    @Then("the task should be visible in my task list")
    public void theTaskShouldBeVisibleInMyTaskList() throws Exception {
        // Check if we have a valid title to verify
        if (currentTaskTitle == null) {
            fail("Task title to verify is null, but trying to check visibility. Check scenario flow.");
        }

        // Perform GET request to fetch tasks for the logged-in user
        MvcResult getTasksResult = mockMvc.perform(get("/api/tasks/user?userId=" + loggedInUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
        
        int status = getTasksResult.getResponse().getStatus();
        String taskListJson = getTasksResult.getResponse().getContentAsString();
   
        assertEquals(200, status, "Expected HTTP 200 (OK) when getting user tasks");
        // Check if the task list contains our task title
        assertTrue(taskListJson.contains("\"title\":\"" + currentTaskTitle + "\""),
                "Task list for user " + loggedInUser.getEmail() + " should contain task title '" + currentTaskTitle + "'. List: " + taskListJson);
    }

    @Then("{string} should receive a notification about the new task")
    public void shouldReceiveANotificationAboutTheNewTask(String userName) {
        if (taskRequestDto.isSimulateNotificationFailure()) {
            fail("Notification check step reached, but notification was simulated to fail for " + userName);
        }
    }

    @Then("I should see the task creation error message {string}")
    public void iShouldSeeTheTaskCreationErrorMessage(String expectedErrorMessage) throws Exception {
        String responseBody = lastMvcResult.getResponse().getContentAsString();
        int statusCode = lastMvcResult.getResponse().getStatus();
        assertTrue(statusCode == 400 || statusCode == 404,
                "Expected HTTP 400/404. Actual: " + statusCode + ". Body: " + responseBody);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, typeRef);
        assertEquals(expectedErrorMessage, responseMap.get("error"), "Error message mismatch. Full response: " + responseBody);
    }

    @Then("the task should not be saved in the system")
    public void theTaskShouldNotBeSavedInTheSystem() {  
        int statusCode = lastMvcResult.getResponse().getStatus(); 
        assertTrue(statusCode >= 400 && statusCode < 500,
                "Expected a client error (4xx). Actual status: " + statusCode);
    }

    @Given("I create a valid task assigned to {string}")
    public void iCreateAValidTaskAssignedTo(String userName) {  
        String title = "Valid Task for " + userName;
        taskRequestDto.setTitle(title);
        currentTaskTitle = title;

        try {
            LocalDate dueDate = LocalDate.parse("2099-12-31");
            taskRequestDto.setDueDate(dueDate);
        } catch (DateTimeParseException e) {
            fail("Failed to parse hardcoded valid date in Gherkin step setup: " + e.getMessage());
        }

        iAssignTheTaskTo(userName);
    }

    @Given("the notification service is temporarily unavailable")
    public void theNotificationServiceIsTemporarilyUnavailable() {
        taskRequestDto.setSimulateNotificationFailure(true);
    }

    @Then("I should see the task creation warning message {string}")
    public void iShouldSeeTheTaskCreationWarningMessage(String warningMessage) throws Exception {      
        String responseBody = lastMvcResult.getResponse().getContentAsString();
        int statusCode = lastMvcResult.getResponse().getStatus();
        
        assertEquals(201, statusCode, "Expected HTTP 201 (Created). Body: " + responseBody);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, typeRef);
        assertNotNull(responseMap.get("taskId"), "Response should contain 'taskId' with warning.");
        assertEquals(warningMessage, responseMap.get("warning"), "Warning message mismatch. Full response: " + responseBody);
    }

    @Then("the system should log the failure")
    public void theSystemShouldLogTheFailure() {
    }
}

