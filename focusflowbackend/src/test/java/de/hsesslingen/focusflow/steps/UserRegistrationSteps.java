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

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class UserRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String confirmPassword;
    private MvcResult result;

    @Given("I am on the registration page")
    public void i_am_on_the_registration_page() {
    }

    @When("I enter {string} as the email")
    public void i_enter_email(String email) {
        this.email = email;
    }

    @And("I enter {string} as the first name")
    public void i_enter_first_name(String firstName) {
        this.firstName = firstName;
    }

    @And("I enter {string} as the last name")
    public void i_enter_last_name(String lastName) {
        this.lastName = lastName;
    }

    @And("I enter {string} as the password")
    public void i_enter_password(String password) {
        this.password = password;
    }

    @And("I confirm the password {string}")
    public void i_confirm_password(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @And("I click the {string} button")
    public void i_click_register(String buttonName) throws Exception {
        String requestBody = String.format(
            "{\"email\":\"%s\",\"password\":\"%s\",\"confirmPassword\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\"}",
            email, password, confirmPassword, firstName, lastName
        );

        result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andReturn();
    }

    @Then("I should see a success message {string}")
    public void i_should_see_message(String expectedMessage) throws Exception {
        String content = result.getResponse().getContentAsString();
        assert content.contains(expectedMessage);
    }

    @And("I should be redirected to the login page")
    public void i_should_be_redirected_to_login() throws Exception {
        assert result.getResponse().getStatus() == 302;
    }
}


