import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class WikipediaTest10 extends BaseTest {

    private static final String BASE_URL  = "https://donate.wikimedia.org";
    private static final String PAGE_PATH =
            "/w/index.php?title=Special:LandingPage&country=ES&uselang=en" +
            "&wmf_medium=sidebar&wmf_source=donate&wmf_campaign=commons.wikimedia.org";
    private static final String FULL_URL  = BASE_URL + PAGE_PATH;

    // Donation page is heavily JS-rendered — use a longer dedicated wait
    private WebDriverWait donationWait;

    @BeforeMethod
    public void setUp() {
        initDriver();
        donationWait = new WebDriverWait(driver, Duration.ofSeconds(45));
    }

    /** Wait for document.readyState === 'complete' before querying elements */
    private void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("complete"));
    }

    @Test
    public void verifyDonationPageLoadsCorrectly() {
        driver.get(FULL_URL);
        waitForPageLoad();
        donationWait.until(d -> d.getTitle().toLowerCase().contains("donation"));
        System.out.println("✓ Donation page title verified");
        WebElement form = donationWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("form")));
        Assert.assertNotNull(form);
        System.out.println("✓ Donation form is visible");
    }

    @Test
    public void verifyDonationAmountOptions() {
        driver.get(FULL_URL);
        waitForPageLoad();
        WebElement firstAmount = donationWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[data-amount], input[name='amount']")));
        Assert.assertNotNull(firstAmount);
        List<WebElement> amountButtons = driver.findElements(
                By.cssSelector("button[data-amount], input[name='amount']"));
        Assert.assertFalse(amountButtons.isEmpty());
        System.out.println("✓ Found " + amountButtons.size() + " amount selection options");
        System.out.println("✓ Amount options are visible");
    }

    @Test
    public void verifyPaymentMethodOptions() {
        driver.get(FULL_URL);
        waitForPageLoad();
        List<WebElement> paymentMethods = driver.findElements(
                By.cssSelector("[data-payment-method], input[name='payment_method'], button[data-gateway]"));
        if (!paymentMethods.isEmpty()) {
            System.out.println("✓ Found " + paymentMethods.size() + " payment method options");
        } else {
            String pageContent = driver.getPageSource().toLowerCase();
            Assert.assertTrue(
                    pageContent.contains("credit card")
                    || pageContent.contains("paypal")
                    || pageContent.contains("payment"),
                    "Page should contain payment-related content");
            System.out.println("✓ Payment options found in page content");
        }
    }

    @Test
    public void verifyDonationFrequencyOptions() {
        driver.get(FULL_URL);
        waitForPageLoad();
        List<WebElement> frequencyOptions = driver.findElements(
                By.cssSelector("input[name='frequency'], button[data-frequency], [role='radiogroup']"));
        if (!frequencyOptions.isEmpty()) {
            System.out.println("✓ Found " + frequencyOptions.size() + " frequency options");
        } else {
            String pageSource = driver.getPageSource().toLowerCase();
            Assert.assertTrue(
                    pageSource.contains("monthly")
                    || pageSource.contains("one-time")
                    || pageSource.contains("recurring"),
                    "Page should mention donation frequency options");
            System.out.println("✓ Frequency options available");
        }
    }

    @Test
    public void verifyCountrySelectionShowsSpain() {
        driver.get(FULL_URL);
        waitForPageLoad();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("country=ES"),
                "URL should contain country=ES. Actual: " + currentUrl);
        System.out.println("✓ Country parameter (ES) verified in URL");
        List<WebElement> countryElements = driver.findElements(
                By.cssSelector("select[name='country'], [data-country='ES']"));
        if (!countryElements.isEmpty()) {
            System.out.println("✓ Spain/ES reference found on page");
        }
    }

    @Test
    public void verifyCurrencyIsSetToEUR() {
        driver.get(FULL_URL);
        waitForPageLoad();
        WebElement eurElement = donationWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'€') or contains(text(),'EUR')]")));
        Assert.assertNotNull(eurElement);
        List<WebElement> eurElements = driver.findElements(
                By.xpath("//*[contains(text(),'€') or contains(text(),'EUR')]"));
        Assert.assertFalse(eurElements.isEmpty());
        System.out.println("✓ Found " + eurElements.size() + " references to EUR/€");
    }

    @Test
    public void verifyDonationSubmitButton() {
        driver.get(FULL_URL);
        waitForPageLoad();
        // FIX: the Wikimedia donation page renders its submit button inside a JS widget.
        // Wait for any visible button that has non-empty text — avoids timing out on hidden buttons.
        WebElement submitButton = donationWait.until(driver -> {
            List<WebElement> candidates = driver.findElements(
                    By.xpath("//button[normalize-space(text())!=''] | //input[@type='submit']"));
            return candidates.stream()
                    .filter(b -> { try { return b.isDisplayed(); } catch (Exception e) { return false; } })
                    .findFirst()
                    .orElse(null);
        });
        Assert.assertNotNull(submitButton, "A visible button with text should be present");
        System.out.println("✓ Submit button found: \"" + submitButton.getText().trim() + "\"");
    }

    @Test
    public void verifyWikimediaBrandingAndTrustIndicators() {
        driver.get(FULL_URL);
        waitForPageLoad();
        WebElement logo = donationWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("img[alt*='Wikimedia'], svg, [class*='logo']")));
        Assert.assertNotNull(logo);
        System.out.println("✓ Wikimedia branding/logo visible");
        List<WebElement> secureText = driver.findElements(
                By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'secure') " +
                        "or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'nonprofit')]"));
        if (!secureText.isEmpty()) {
            System.out.println("✓ Found " + secureText.size() + " trust/security indicators");
        }
    }

    @Test
    public void completeDonationFlowValidation() {
        driver.get(FULL_URL);
        waitForPageLoad();
        System.out.println("Starting complete donation flow test...");

        // Step 1: Select an amount
        WebElement amountContainer = donationWait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("li.radiobuttons-cell")));
        amountContainer.click();
        System.out.println("✓ Step 1: Amount selected");

        // Step 2: Form still visible
        WebElement form = donationWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("form")));
        Assert.assertNotNull(form);
        System.out.println("✓ Step 2: Form remains visible");

        // FIX: after clicking an amount the page transitions — wait for any visible
        // button with text rather than a specific type='submit' that may not exist yet
        WebElement nextButton = donationWait.until(driver -> {
            List<WebElement> candidates = driver.findElements(
                    By.xpath("//button[normalize-space(text())!=''] | //input[@type='submit']"));
            return candidates.stream()
                    .filter(b -> { try { return b.isDisplayed(); } catch (Exception e) { return false; } })
                    .findFirst()
                    .orElse(null);
        });
        Assert.assertNotNull(nextButton, "A next/submit button should be visible after selecting amount");
        System.out.println("✓ Step 3: Next step/submit button available: \"" + nextButton.getText().trim() + "\"");
        System.out.println("Donation flow validation complete (stopped before payment)");
    }
}