import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

public class WikipediaTest05 extends BaseTest {

    private static final String URL = "https://en.wikipedia.org/wiki/Roland_Juno-60";

    @BeforeMethod
    public void setUp() {
        initDriver();
    }

    @Test
    public void verifyFeaturesSection() {
        driver.get(URL);
        WebElement anchor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("Sounds_and_features")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", anchor);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'Sounds and features')] | //h3[contains(.,'Sounds and features')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'single-oscillator analog synthesizers')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'chorus effect')]")));
        System.out.println("Sounds and features section verified correctly.");
        System.out.println(" - Single-oscillator analog synthesizers mentioned");
        System.out.println(" - Chorus effect mentioned");
    }

    @Test
    public void verifyTechnicalSpecifications() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".infobox")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[contains(@class,'infobox')]//th[contains(.,'Technical specifications')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[contains(@class,'infobox')]//td[contains(.,'6 voices')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[contains(@class,'infobox')]//td[contains(.,'1 DCO per voice')]")));
        System.out.println("Technical specifications verified correctly.");
        System.out.println(" - Polyphony: 6 voices");
        System.out.println(" - Oscillator: 1 DCO per voice");
    }

    @Test
    public void verifySuccessorsSection() {
        driver.get(URL);
        WebElement anchor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("Successors")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", anchor);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'Successors')] | //h3[contains(.,'Successors')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'Roland followed up the Juno-60 with the Roland Juno-106 in 1984')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'Roland released the Juno-X in 2022')]")));
        System.out.println("Successors section verified correctly.");
        System.out.println(" - Juno-106 mentioned");
        System.out.println(" - Juno-X mentioned");
    }

    @Test
    public void verifySoftwareEmulationsSection() {
        driver.get(URL);
        WebElement anchor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("Software_emulations")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", anchor);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(.,'Software emulations')] | //h3[contains(.,'Software emulations')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(.,'TAL U-NO-62 by Togu Audio Line')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(.,'Arturia Chorus JUN-6 (2020)')]")));
        System.out.println("Software emulations section verified correctly.");
        System.out.println(" - TAL U-NO-62 mentioned");
        System.out.println(" - Arturia Chorus JUN-6 mentioned");
    }
}