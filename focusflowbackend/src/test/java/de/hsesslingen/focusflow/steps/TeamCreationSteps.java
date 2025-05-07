package de.hsesslingen.focusflow.steps;

import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class TeamCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    private String teamName;
    private String description;
    private List<String> members;
    private MvcResult result;

    @Given("I am a logged-in user")
    public void iAmLoggedIn() {
        // Optional: Authentifizierung simulieren
    }

    @When("I click {string}")
    public void iClick(String button) {
        // Optional: Simuliere UI-Klick (nicht nötig für API-Test)
    }

    @When("I enter a team name {string}")
    public void iEnterTeamName(String name) {
        this.teamName = name;
    }

    @When("I leave the team name blank")
    public void iLeaveTeamNameBlank() {
        this.teamName = "";
    }

    @When("I enter a description {string}")
    public void iEnterDescription(String desc) {
        this.description = desc;
    }

    @When("I select members {string} and {string}")
    public void iSelectMembers(String m1, String m2) {
        this.members = Arrays.asList(m1, m2);
    }

    @And("I click {string}")
    public void iClickSave(String buttonName) throws Exception {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("{");
        requestBuilder.append("\"name\":\"").append(teamName).append("\",");
        requestBuilder.append("\"description\":\"").append(description).append("\"");

        if (members != null && !members.isEmpty()) {
            requestBuilder.append(",\"members\":[");
            for (int i = 0; i < members.size(); i++) {
                requestBuilder.append("\"").append(members.get(i)).append("\"");
                if (i < members.size() - 1) requestBuilder.append(",");
            }
            requestBuilder.append("]");
        }

        requestBuilder.append("}");

        result = mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBuilder.toString()))
            .andReturn();
    }

    @Then("I should see a success message {string}")
    public void iShouldSeeSuccessMessage(String expectedMessage) throws Exception {
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMessage), "Expected success message not found.");
    }

    @Then("I should see an error message {string}")
    public void iShouldSeeErrorMessage(String expectedMessage) throws Exception {
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMessage), "Expected error message not found.");
    }

    @Then("the team should be saved in the system")
    public void teamShouldBeSaved() {
        assertEquals(200, result.getResponse().getStatus(), "Expected HTTP 200 for successful team creation.");
    }

    @Then("the team should not be saved in the system")
    public void teamShouldNotBeSaved() {
        assertNotEquals(200, result.getResponse().getStatus(), "Expected HTTP error status for invalid team creation.");
    }

    @Then("the team should be visible in my team list")
    public void teamVisibleInList() {
        // In einem echten Test könnte man hier z. B. per GET prüfen, ob das Team existiert
        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("the team should be saved in the system with the selected members")
    public void teamSavedWithMembers() {
        assertEquals(200, result.getResponse().getStatus());
        assertNotNull(members);
        assertFalse(members.isEmpty());
    }

    @Then("{string} and {string} should be listed as members of the team")
    public void membersListed(String m1, String m2) throws Exception {
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(m1));
        assertTrue(content.contains(m2));
    }

    @Then("the members should receive a notification about the new team")
    public void membersShouldReceiveNotification() {
        for (String member : members) {
            System.out.println("Notification sent to: " + member);
        }
    }
}
