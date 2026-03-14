import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class WikipediaTest08 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/2025_MotoGP_World_Championship");
    }

    @Test
    public void verifyPageTitle() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("firstHeading")));
        Assert.assertTrue(heading.getText().contains("2025 MotoGP World Championship"));
        System.out.println("Page title verified successfully");
    }

    @Test
    public void verifyTableOfContents() {
        WebElement toc = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#toc, .toc, [role='navigation']")));
        Assert.assertNotNull(toc);
        System.out.println("Table of contents verified successfully");
    }

    @Test
    public void verifyRidersSection() {
        WebElement ridersSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#Teams_and_riders, #Riders")));
        Assert.assertNotNull(ridersSection);
        System.out.println("Riders section verified successfully");
    }

    @Test
    public void verifyMarcMarquezInformation() {
        WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mw-content-text")));
        Assert.assertTrue(content.getText().contains("Marc Márquez"));
        System.out.println("Marc Márquez information verified successfully");
    }

    @Test
    public void verifyDefendingChampionJorgeMartin() {
        WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mw-content-text")));
        Assert.assertTrue(content.getText().contains("Jorge Martín"));
        System.out.println("Jorge Martín information verified successfully");
    }

    @Test
    public void verifyCalendarSection() {
        WebElement calendarSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("Calendar")));
        Assert.assertNotNull(calendarSection);
        System.out.println("Calendar section verified successfully");
    }

    @Test
    public void verifyResultsTables() {
        List<WebElement> tables = wait.until(driver -> {
            List<WebElement> found = driver.findElements(By.cssSelector("table.wikitable"));
            return found.isEmpty() ? null : found;
        });
        Assert.assertFalse(tables.isEmpty());
        System.out.println("Found " + tables.size() + " results tables");
    }

    @Test
    public void verifyReferenceLinks() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("References")));
        List<WebElement> refLinks = driver.findElements(
                By.cssSelector(".reflist a, .references a"));
        Assert.assertFalse(refLinks.isEmpty());
        System.out.println("Found " + refLinks.size() + " reference links");
    }

    @Test
    public void verifyMainTeams() {
        WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mw-content-text")));
        String text = content.getText();
        for (String team : new String[]{"Ducati", "Aprilia", "Honda", "Yamaha", "KTM"}) {
            Assert.assertTrue(text.contains(team), "Page should mention team: " + team);
        }
        System.out.println("Main teams information verified successfully");
    }

    @Test
    public void verifySeasonChanges() {
        WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mw-content-text")));
        Assert.assertTrue(content.getText().contains("season"));
        System.out.println("Season changes section verified successfully");
    }

    @Test
    public void takeScreenshot() throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(),
                Paths.get("motogp-2025-wikipedia.png"),
                StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot saved as motogp-2025-wikipedia.png");
    }
}