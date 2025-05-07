package de.hsesslingen.focusflow.steps;

import de.hsesslingen.focusflowbackend.FocusflowbackendApplication;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertTrue;  
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = FocusflowbackendApplication.class
)
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

    @When("I enter a {string} as the email") 
    public void i_enter_a_as_the_email(String email) { 
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


    @And("I click the {string} registration button")
    public void i_click_register_button(String buttonName) throws Exception { 
        if (!"Register".equalsIgnoreCase(buttonName)) {
            throw new IllegalArgumentException("This step is specific to the 'Register' button.");
        }

        String requestBody = String.format(
            "{\"email\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"password\":\"%s\",\"confirmPassword\":\"%s\"}",
            this.email, this.firstName, this.lastName, this.password, this.confirmPassword
        );

        this.result = mockMvc.perform(post("/api/user/register") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andReturn();
    }

    @Then("I should see the registration success message {string}") 
    public void i_should_see_registration_success_message(String expectedMessage) throws Exception {
        String content = this.result.getResponse().getContentAsString();
        int statusCode = this.result.getResponse().getStatus();
        assertEquals(302, statusCode, "Expected HTTP status 302 for successful registration redirect.");
    }

    @And("I should be redirected to the login page")
    public void i_should_be_redirected_to_login() throws Exception {
        assertEquals(302, this.result.getResponse().getStatus());
        String locationHeader = this.result.getResponse().getHeader("Location");
        assertEquals("/login", locationHeader, "Expected redirect to /login");
    }


    @Then("I should see the user registration error message {string}") 
    public void i_should_see_the_user_registration_error_message(String expectedErrorMessage) throws Exception {
        String content = this.result.getResponse().getContentAsString();
        int statusCode = this.result.getResponse().getStatus();

        assertTrue(statusCode == 400 || statusCode == 409, "Expected HTTP status 400 or 409 for registration error.");
        assertTrue(content.contains(expectedErrorMessage), "Response body should contain the error message: " + expectedErrorMessage);
    }

    @Then("I should remain on the user registration page") 
    public void i_should_remain_on_the_user_registration_page() {

        int statusCode = this.result.getResponse().getStatus();
        assertTrue(statusCode != 302, "Should not redirect on registration error.");

    }
}