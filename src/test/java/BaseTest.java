import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Runs ONCE before the entire suite.
     * Forces Selenium Manager to download the Chrome binary fully
     * before any test class tries to use it.
     */
    @BeforeSuite
    public void warmUpSeleniumManager() {
        WebDriver warmUp = null;
        try {
            warmUp = new ChromeDriver(buildOptions());
        } finally {
            if (warmUp != null) warmUp.quit();
        }
        System.out.println("[BeforeSuite] Chrome binary ready.");
    }

    /**
     * Creates a fresh ChromeDriver with up to 3 attempts, waiting 2 s between
     * each attempt. Catches the broad Exception so the class compiles cleanly
     * regardless of which Selenium sub-package the driver exception lives in.
     */
    protected void initDriver() {
        int maxAttempts = 3;
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                driver = new ChromeDriver(buildOptions());
                wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                return; // success — stop retrying
            } catch (Exception e) {
                lastException = e;
                System.out.println("[initDriver] Attempt " + attempt
                        + " failed: " + e.getMessage()
                        + ". Retrying in 2 s...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new RuntimeException(
                "ChromeDriver could not be started after " + maxAttempts + " attempts.",
                lastException);
    }

    /**
     * Single place where Chrome flags are defined.
     * --headless=new : required for Chrome 112+; the old --headless flag
     *                  causes "Chrome instance exited" on Chrome 146.
     */
    private ChromeOptions buildOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}