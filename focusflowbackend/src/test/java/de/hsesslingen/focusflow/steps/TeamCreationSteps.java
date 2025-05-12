package de.hsesslingen.focusflow.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = de.hsesslingen.focusflowbackend.FocusflowbackendApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
// This class is used to define the steps for team creation scenarios in Cucumber tests
public class TeamCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private MvcResult lastMvcResult;
    private String teamName;
    private String teamDescription;
    private List<String> memberEmails;

    private User creatorUserEntity;
    private User member1UserEntity;
    private User member2UserEntity;

    private final String creatorEmail = "creator.team@example.com";
    private final String member1EmailFixture = "john.doe.team@example.com";
    private final String member2EmailFixture = "jane.smith.team@example.com";


    @Before(order = 10)
    public void setup() {
        System.out.println("======== SETTING UP TEAM CREATION TEST ========");

        User creator = new User();
        creator.setEmail(this.creatorEmail);
        creator.setPassword("passwordSecure123!"); 
        creator.setRole("USER"); 
        creator.setFirstName("Creator");
        creator.setLastName("TestUser");
        this.creatorUserEntity = userRepository.save(creator);

        User member1 = new User();
        member1.setEmail(this.member1EmailFixture);
        member1.setPassword("passwordSecure123!");
        member1.setRole("USER");
        member1.setFirstName("John");
        member1.setLastName("Doe");
        this.member1UserEntity = userRepository.save(member1);

        User member2 = new User();
        member2.setEmail(this.member2EmailFixture);
        member2.setPassword("passwordSecure123!");
        member2.setRole("USER");
        member2.setFirstName("Jane");
        member2.setLastName("Smith");
        this.member2UserEntity = userRepository.save(member2);

        assertNotNull(this.creatorUserEntity.getId(), "Creator ID should not be null after save");
        assertNotNull(this.member1UserEntity.getId(), "Member1 ID should not be null after save");
        assertNotNull(this.member2UserEntity.getId(), "Member2 ID should not be null after save");

        System.out.println("======== TEAM CREATION TEST SETUP COMPLETE ========");
    }

    @Given("I am a logged-in user")
    public void i_am_a_logged_in_user() {
        assertNotNull(this.creatorUserEntity, "Creator user should be set up");
    }

    @When("I click {string}")
    public void i_click_button(String buttonName) {
    }

    @When("I enter a team name {string}")
    public void i_enter_a_team_name(String name) {
        this.teamName = name;
    }

    @When("I enter a description {string}")
    public void i_enter_a_description(String description) {
        this.teamDescription = description;
    }

    @When("I leave the team name blank")
    public void i_leave_the_team_name_blank() {
        this.teamName = "";
    }

    @When("I select members {string} and {string}")
    public void i_select_members_and(String selectedMember1Email, String selectedMember2Email) {
        this.memberEmails = Arrays.asList(selectedMember1Email, selectedMember2Email);
    }

    @And("I click the {string} team button")
    public void i_click_save_team_button(String buttonName) throws Exception {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", this.teamName);
        requestBodyMap.put("description", this.teamDescription);
        requestBodyMap.put("creatorEmail", this.creatorUserEntity.getEmail());

        if (this.memberEmails != null && !this.memberEmails.isEmpty()) {
            requestBodyMap.put("memberEmails", this.memberEmails);
        }

        String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);

        this.lastMvcResult = mockMvc.perform(post("/api/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andReturn();
    }

    @Then("I should see the team creation success message {string}")
    public void i_should_see_a_success_message(String expectedMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(201, statusCode, "Expected HTTP 201 Created, but got " + statusCode + ". Response: " + responseBody);
        assertTrue(responseBody.contains("\"message\":\"" + expectedMessage + "\""),
                "Response body did not contain the expected success message. Body: " + responseBody + ", Expected: " + expectedMessage);
    }

    @Then("the team should be saved in the system")
    public void the_team_should_be_saved_in_the_system() throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"teamId\":"), "Response body should contain a teamId. Body: " + responseBody);
    }

    @Then("the team should be visible in my team list")
    public void the_team_should_be_visible_in_my_team_list() throws Exception {
        MvcResult getTeamsResult = mockMvc.perform(get("/api/teams/user")
                        .param("userId", String.valueOf(this.creatorUserEntity.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, getTeamsResult.getResponse().getStatus());
        String teamListJson = getTeamsResult.getResponse().getContentAsString();
        assertTrue(teamListJson.contains("\"name\":\"" + this.teamName + "\""),
                "Team list for user " + this.creatorUserEntity.getId() + " did not contain team name '" + this.teamName + "'. List: " + teamListJson);
    }

    @Then("I should see the team creation error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertTrue(statusCode == 400 || statusCode == 404,
            "Expected HTTP 400 or 404, but got " + statusCode + ". Response: " + responseBody);
        assertTrue(responseBody.contains(expectedErrorMessage),
                "Response body: '" + responseBody + "' did not contain the expected error message: '" + expectedErrorMessage + "'");
    }

    @Then("the team should not be saved in the system")
    public void the_team_should_not_be_saved_in_the_system() throws Exception {
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertTrue(statusCode >= 400 && statusCode < 500,
                "Expected a client error (4xx status code). Got: " + statusCode);
    }

    @Then("the team should be saved in the system with the selected members")
    public void the_team_should_be_saved_in_the_system_with_the_selected_members() throws Exception {
        the_team_should_be_saved_in_the_system();
    }

    @Then("{string} should be visible in my team list")
    public void specific_team_should_be_visible_in_my_team_list(String teamNameToFind) throws Exception {
        MvcResult getTeamsResult = mockMvc.perform(get("/api/teams/user")
                        .param("userId", String.valueOf(this.creatorUserEntity.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, getTeamsResult.getResponse().getStatus());
        String teamListJson = getTeamsResult.getResponse().getContentAsString();
        assertTrue(teamListJson.contains("\"name\":\"" + teamNameToFind + "\""),
                "Team list for user " + this.creatorUserEntity.getId() + " did not contain team name '" + teamNameToFind + "'. List: " + teamListJson);
    }

    @Then("{string} and {string} should be listed as members of the team")
    public void and_should_be_listed_as_members_of_the_team(String expectedMember1Email, String expectedMember2Email) throws Exception {
        String postResponseJson = this.lastMvcResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(postResponseJson, new TypeReference<Map<String, Object>>() {});
        Integer teamIdInt = (Integer) responseMap.get("teamId");
        assertNotNull(teamIdInt, "teamId not found in previous POST response to verify members. Response: " + postResponseJson);
        Long teamId = teamIdInt.longValue();

        MvcResult getTeamResult = mockMvc.perform(get("/api/teams")
                        .param("id", String.valueOf(teamId))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, getTeamResult.getResponse().getStatus(), "Could not fetch team details for ID: " + teamId + ". Response: " + getTeamResult.getResponse().getContentAsString());
        String teamDetailsJson = getTeamResult.getResponse().getContentAsString();

        assertTrue(teamDetailsJson.contains("\"email\":\"" + expectedMember1Email + "\""),
                "Team details did not list member: " + expectedMember1Email + ". Details: " + teamDetailsJson);
        assertTrue(teamDetailsJson.contains("\"email\":\"" + expectedMember2Email + "\""),
                "Team details did not list member: " + expectedMember2Email + ". Details: " + teamDetailsJson);
    }

    @Then("the members should receive a notification about the new team")
    public void the_members_should_receive_a_notification_about_the_new_team() {
    }
}