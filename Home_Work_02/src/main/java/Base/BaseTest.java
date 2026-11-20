package Base;

import Components.HeaderComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BaseTest extends BasePage {

    private static final By USER_MENU_BUTTON =
            By.cssSelector("[data-testid='cs-user-menu-button']");

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.navigate().to(BASE_URL);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver == null) {
            return;
        }

        try {
            List<WebElement> userMenuButtons = driver.findElements(USER_MENU_BUTTON);
            if (!userMenuButtons.isEmpty() && userMenuButtons.get(0).isDisplayed()) {
                new HeaderComponent(driver, wait).logout();
            }
        } finally {
            driver.quit();
        }
    }
}
