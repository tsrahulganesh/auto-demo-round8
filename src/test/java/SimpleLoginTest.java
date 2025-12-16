
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class SimpleLoginTest {

    private WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");           // Headless for CI (Chrome 109+)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        // If chromedriver not on PATH, set the system property:
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        return new ChromeDriver(options);
    }

    @Test
    void loginTest() {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // 1) Open login page
            driver.get("https://bankubt.onlinebank.com/AdminHome.aspx");

            // 2) Wait for username & password fields
            WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[contains(@id,'webInputForm_txtLoginName')]")
                )
            );

            WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[contains(@id,'webInputForm_txtPassword')]")
                )
            );

            // 3) Enter credentials (consider reading from env/secrets)
            username.clear();
            username.sendKeys("Pawanadmin01");

            password.clear();
            password.sendKeys("Test@2025");

            // 4) Wait for and click login button
            WebElement loginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input[type='submit']")
                )
            );
            loginBtn.click();

            // 5) Wait for a reliable post-login signal
            // Prefer an element that only appears after successful login
            // Example locator: adjust to a real dashboard element if known
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("Dashboard"),
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@id,'Dashboard') or contains(text(),'Dashboard')]")
                )
            ));

            System.out.println("Title after login: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
