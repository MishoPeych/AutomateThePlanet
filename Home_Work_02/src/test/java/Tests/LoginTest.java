package Tests;

import Base.BaseTest;
import Pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginTest extends BaseTest {

    private static final String VALID_PASSWORD = "password123";

    @ParameterizedTest(name = "{0}: {1} lands on {2}")
    @CsvSource({
            "Citizen,john@example.com,/dashboard"
    })
    void validLogin_opensExpectedPage(String role, String email, String expectedPath) {
        LoginPage loginPage = new LoginPage(driver, wait)
                .submitLogin(email, VALID_PASSWORD);

        Assertions.assertEquals(
                expectedPath,
                loginPage.getCurrentPagePath(),
                role + " user should land on the expected page after login"
        );
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(
            delimiter = '|',
            value = {
                    "wrong password|john@example.com|wrong-password",
                    "unknown user|unknown@example.com|password123",
                    "empty email|''|password123"
            }
    )
    void invalidLoginData_staysOnLoginPage(
            String scenario,
            String email,
            String password
    ) {
        LoginPage loginPage = new LoginPage(driver, wait);

        if (email.isBlank()) {
            loginPage.enterPassword(password).clickLoginButton();

            Assertions.assertTrue(
                    loginPage.isOnLoginPage(),
                    "Login with an empty email should be rejected"
            );
            Assertions.assertEquals(
                    "",
                    loginPage.getEmailFieldValue(),
                    "Email should remain empty when the form is submitted without an email"
            );
            return;
        }

        loginPage.submitLogin(email, password);

        Assertions.assertTrue(
                loginPage.isOnLoginPage(),
                scenario + " should not navigate away from the login page"
        );
        Assertions.assertEquals(
                "Invalid email or password",
                loginPage.getErrorLoginMsg(),
                scenario + " should display the expected login error"
        );
    }

    @Test
    void passwordField_isMasked() {
        LoginPage loginPage = new LoginPage(driver, wait);

        Assertions.assertEquals(
                "password",
                loginPage.getPasswordFieldType(),
                "Password field should mask the entered password"
        );
    }

    @Test
    void invalidPassword_retainsEnteredEmail() {
        String email = "john@example.com";
        LoginPage loginPage = new LoginPage(driver, wait)
                .submitLogin(email, "wrong-password");

        Assertions.assertTrue(
                loginPage.isOnLoginPage(),
                "Invalid password should keep the user on the login page"
        );
        Assertions.assertEquals(
                email,
                loginPage.getEmailFieldValue(),
                "Email should remain in the field after a failed login"
        );
    }
}
