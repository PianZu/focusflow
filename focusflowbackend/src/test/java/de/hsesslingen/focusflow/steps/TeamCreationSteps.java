package de.hsesslingen.focusflow.steps;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
//@CucumberContextConfiguration
public class TeamCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult lastMvcResult;
    private String teamName;
    private String teamDescription;
    private List<String> memberUsernames;

    @Given("I am a logged-in user")
    public void i_am_a_logged_in_user() {
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
    public void i_select_members_and(String member1, String member2) {
        this.memberUsernames = Arrays.asList(member1, member2);
    }

    @And("I click the {string} team button")
    public void i_click_save_team_button(String buttonName) throws Exception {
        if (!"Save".equalsIgnoreCase(buttonName)) {
            throw new IllegalArgumentException("Unsupported button: " + buttonName);
        }

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", this.teamName);
        requestBodyMap.put("description", this.teamDescription);

        if (this.memberUsernames != null && !this.memberUsernames.isEmpty()) {
            requestBodyMap.put("memberUsernames", this.memberUsernames);
        }

        String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);

        this.lastMvcResult = mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andReturn();
    }

    @Then("I should see the team creation success message {string}")
    public void i_should_see_a_success_message(String expectedMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(201, statusCode);
        assertTrue(responseBody.contains("\"message\":\"" + expectedMessage + "\""));
    }

    @Then("the team should be saved in the system")
    public void the_team_should_be_saved_in_the_system() throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"teamId\":"));
    }

    @Then("the team should be visible in my team list")
    public void the_team_should_be_visible_in_my_team_list() throws Exception {
        MvcResult getTeamsResult = mockMvc.perform(get("/api/teams")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, getTeamsResult.getResponse().getStatus());
        String teamListJson = getTeamsResult.getResponse().getContentAsString();
        assertTrue(teamListJson.contains("\"name\":\"" + this.teamName + "\""));
    }

    @Then("I should see the team creation error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessage) throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertEquals(400, statusCode);
        assertTrue(responseBody.contains("\"error\":\"" + expectedErrorMessage + "\""));
    }

    @Then("the team should not be saved in the system")
    public void the_team_should_not_be_saved_in_the_system() throws Exception {
        int statusCode = this.lastMvcResult.getResponse().getStatus();
        assertTrue(statusCode >= 400 && statusCode < 500);
    }

    @Then("I should see a success message {string} with members")
    public void i_should_see_a_success_message_with_members(String expectedMessage) throws Exception {
        i_should_see_a_success_message(expectedMessage);
    }

    @Then("the team should be saved in the system with the selected members")
    public void the_team_should_be_saved_in_the_system_with_the_selected_members() throws Exception {
        String responseBody = this.lastMvcResult.getResponse().getContentAsString();
        assertTrue(responseBody.contains("\"teamId\":"));
    }

    @Then("{string} should be visible in my team list")
    public void specific_team_should_be_visible_in_my_team_list(String teamNameToFind) throws Exception {
        MvcResult getTeamsResult = mockMvc.perform(get("/api/teams")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, getTeamsResult.getResponse().getStatus());
        String teamListJson = getTeamsResult.getResponse().getContentAsString();
        assertTrue(teamListJson.contains("\"name\":\"" + teamNameToFind + "\""));
    }

    @Then("{string} and {string} should be listed as members of the team")
    public void and_should_be_listed_as_members_of_the_team(String member1Username, String member2Username) throws Exception {
        String postResponseJson = this.lastMvcResult.getResponse().getContentAsString();
        String teamIdStr = "";
        if (postResponseJson.contains("\"teamId\":")) {
            teamIdStr = postResponseJson.split("\"teamId\":")[1].split(",")[0].replaceAll("[^0-9]", "");
        }
        if (teamIdStr.isEmpty()){
             fail("teamId not found in previous response to verify members.");
        }
        Long teamId = Long.parseLong(teamIdStr);

        MvcResult getTeamResult = mockMvc.perform(get("/api/teams/" + teamId)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, getTeamResult.getResponse().getStatus());
        String teamDetailsJson = getTeamResult.getResponse().getContentAsString();

        assertTrue(teamDetailsJson.contains("\"username\":\"" + member1Username + "\""));
        assertTrue(teamDetailsJson.contains("\"username\":\"" + member2Username + "\""));
    }

    @Then("the members should receive a notification about the new team")
    public void the_members_should_receive_a_notification_about_the_new_team() {
    }
}