import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

public class WikipediaTest06 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/USB_hardware");
    }

    @Test
    public void verifyUSBConnectorsSection() {
        WebElement anchor = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("Connectors")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", anchor);
        WebElement standardA = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='mw-content-text']//*[contains(.,'Standard-A')][not(*[contains(.,'Standard-A')])]")));
        Assert.assertNotNull(standardA);
        WebElement typeC = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='mw-content-text']//*[contains(.,'Type-C')][not(*[contains(.,'Type-C')])]")));
        Assert.assertNotNull(typeC);
        System.out.println("Both connectors found:");
        System.out.println(" - Standard-A");
        System.out.println(" - Type-C");
    }

    @Test
    public void verifyConnectorsSubsections() {
        List<String> subsections = Arrays.asList("Types", "Internal connectors");
        for (String subsection : subsections) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h3[contains(.,'" + subsection + "')]")));
            System.out.println("Subsection verified: " + subsection);
        }
        System.out.println("Connectors subsections verified correctly.");
    }

    @Test
    public void verifyTypesSubSubsections() {
        WebElement typesHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(.,'Types')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", typesHeader);
        List<String> subsubsections = Arrays.asList(
                "Standard connectors", "Mini connectors", "Micro connectors", "USB-C");
        for (String subsub : subsubsections) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[self::h3 or self::h4][contains(.,'" + subsub + "')]")));
            System.out.println("Sub-subsection verified: " + subsub);
        }
        System.out.println("Types sub-subsections verified correctly.");
    }

    @Test
    public void verifyCompatibilityTable() {
        wait.until(driver -> {
            List<WebElement> tables = driver.findElements(By.cssSelector("table.wikitable"));
            return tables.stream()
                    .filter(t -> { try { return t.getText().contains("USB4"); } catch (Exception e) { return false; } })
                    .findFirst().orElse(null);
        });
        List<WebElement> tables = driver.findElements(By.cssSelector("table.wikitable"));
        WebElement targetTable = tables.stream()
                .filter(t -> { try { return t.getText().contains("USB4"); } catch (Exception e) { return false; } })
                .findFirst()
                .orElseThrow(() -> new AssertionError("No wikitable containing USB4 found"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", targetTable);
        Assert.assertTrue(targetTable.getText().contains("USB4"), "Table should contain USB4");
        Assert.assertTrue(targetTable.getText().contains("3.0"), "Table should contain 3.0");
        System.out.println("Compatibility table verified correctly.");
    }
}