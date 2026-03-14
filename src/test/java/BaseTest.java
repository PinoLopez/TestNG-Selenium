import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    // ── ExtentReports: shared across all tests ──────────────────────────────
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // ── WebDriver: one instance per test method ──────────────────────────────
    protected WebDriver driver;
    protected WebDriverWait wait;

    // ────────────────────────────────────────────────────────────────────────
    // Suite-level setup: runs ONCE before all tests
    // ────────────────────────────────────────────────────────────────────────
    @BeforeSuite
    public void suiteSetUp() {
        // Create the output folder
        new java.io.File("test-output/SparkReport").mkdirs();

        // Configure the Spark (HTML) reporter
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/SparkReport/Index.html");
        spark.config().setReportName("TestNG-Selenium Wikipedia Tests");
        spark.config().setDocumentTitle("Selenium Test Report");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("UTF-8");
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", "Chrome (headless)");

        // Pre-warm Chrome so Selenium Manager downloads the binary before
        // any test class starts, preventing the "Chrome instance exited" race
        WebDriver warmUp = null;
        try {
            warmUp = new ChromeDriver(buildOptions());
        } finally {
            if (warmUp != null) warmUp.quit();
        }
        System.out.println("[BeforeSuite] Chrome binary ready. ExtentReports initialised.");
    }

    // ────────────────────────────────────────────────────────────────────────
    // Method-level setup: create an ExtentTest node for the current test
    // ────────────────────────────────────────────────────────────────────────
    @BeforeMethod
    public void methodSetUp(Method method) {
        // Create a named test node in the report
        ExtentTest test = extent.createTest(
                getClass().getSimpleName() + " → " + method.getName());
        extentTest.set(test);
    }

    // ────────────────────────────────────────────────────────────────────────
    // Method-level teardown: log pass/fail/skip, quit driver
    // ────────────────────────────────────────────────────────────────────────
    @AfterMethod
    public void methodTearDown(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    test.log(Status.PASS, "Test passed");
                    break;
                case ITestResult.FAILURE:
                    test.log(Status.FAIL, result.getThrowable());
                    break;
                case ITestResult.SKIP:
                    test.log(Status.SKIP, "Test skipped");
                    break;
            }
        }
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Suite-level teardown: flush the report to disk
    // ────────────────────────────────────────────────────────────────────────
    @AfterSuite
    public void suiteTearDown() {
        if (extent != null) {
            extent.flush();
            System.out.println("[AfterSuite] ExtentReports flushed to test-output/SparkReport/Index.html");
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Driver initialisation with retry (handles Chrome OS-resource race)
    // ────────────────────────────────────────────────────────────────────────
    protected void initDriver() {
        int maxAttempts = 3;
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                driver = new ChromeDriver(buildOptions());
                wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                return;
            } catch (Exception e) {
                lastException = e;
                System.out.println("[initDriver] Attempt " + attempt
                        + " failed: " + e.getMessage()
                        + ". Retrying in 2 s...");
                try { Thread.sleep(2000); } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException(
                "ChromeDriver could not be started after " + maxAttempts + " attempts.",
                lastException);
    }

    // ────────────────────────────────────────────────────────────────────────
    // Chrome flags: headless=new required for Chrome 112+ / Chrome 146
    // ────────────────────────────────────────────────────────────────────────
    private ChromeOptions buildOptions() {
         ChromeOptions options = new ChromeOptions();
           options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
          options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--disable-notifications");
    options.addArguments("--disable-infobars");
    options.addArguments("--remote-allow-origins=*");
        return options;
    }
}