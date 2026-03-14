import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class WikipediaTest04 extends BaseTest {

    private static final String URL = "https://en.wikipedia.org/wiki/Synergy_(software)";

    @BeforeMethod
    public void setUp() {
        initDriver();
    }

    @Test
    public void verifyInfoboxWebsiteLink() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(.,'Synergy')]")));
        WebElement websiteLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@href,'symless.com/synergy')]")));
        String href = websiteLink.getAttribute("href");
        Assert.assertTrue(href.contains("symless.com/synergy"),
                "Website link href should contain symless.com/synergy. Actual: " + href);
        System.out.println("Infobox website link verified correctly.");
    }

    @Test
    public void verifyDesignSection() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'Design')] | //h3[contains(.,'Design')]")));
        System.out.println("Design section verified correctly.");
    }

    @Test
    public void verifyHistorySection() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'History')] | //h3[contains(.,'History')]")));
        System.out.println("History section verified correctly.");
    }

    @Test
    public void verifyExternalLinks() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'References')] | //h2[contains(.,'External links')]")));
        List<WebElement> externalLinks = driver.findElements(
                By.cssSelector(".references a.external, .reflist a.external"));
        Assert.assertFalse(externalLinks.isEmpty(),
                "Should have at least one external reference link");
        Assert.assertNotNull(externalLinks.get(0).getAttribute("href"));
        System.out.println("External links verified correctly.");
    }
}