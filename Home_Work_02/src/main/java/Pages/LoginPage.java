package Pages;

import Base.BasePage;
import Components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;

public class LoginPage extends BasePage {
    private final HeaderComponent header;

    private static final By EMAIL_FIELD = By.xpath("//*[@placeholder='Enter your email']");
    private static final By PASSWORD_FIELD = By.xpath("//*[@placeholder='Enter your password']");
    private static final By LOGIN_BUTTON = By.xpath("//*[@data-testid='cs-login-submit']");
    private static final By ERROR_LOGIN_MSG = By.xpath("//*[text()='Invalid email or password']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.header = new HeaderComponent(driver, wait);
    }

    public HeaderComponent header() {
        return header;
    }

    /**
     * Successful-login flow used by existing end-to-end tests.
     */
    public MyRequestPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        waitForUrlContains("/dashboard");
        return new MyRequestPage(driver, wait);
    }

    /**
     * Submits credentials and waits until either login succeeds or the login error is shown.
     */
    public LoginPage submitLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        wait.until(d -> !getCurrentPagePath().equals("/login") || hasLoginError());
        return this;
    }

    public String getErrorLoginMsg() {
        return getText(ERROR_LOGIN_MSG);
    }

    public boolean hasLoginError() {
        return isDisplayed(ERROR_LOGIN_MSG);
    }

    public boolean isOnLoginPage() {
        return "/login".equals(getCurrentPagePath());
    }

    public String getCurrentPagePath() {
        return URI.create(driver.getCurrentUrl()).getPath();
    }

    public String getPasswordFieldType() {
        return driver.findElement(PASSWORD_FIELD).getAttribute("type");
    }

    public String getEmailFieldValue() {
        return driver.findElement(EMAIL_FIELD).getAttribute("value");
    }

    public LoginPage enterEmail(String email) {
        sendKeys(EMAIL_FIELD, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(PASSWORD_FIELD, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        click(LOGIN_BUTTON);
        return this;
    }
}
