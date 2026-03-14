import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

public class WikipediaTest03 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/Roland_TB-303");
    }

    @Test
    public void verifyArticlePageStructure() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mw-logo-wordmark")));
        WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        Assert.assertEquals(h1.getText(), "Roland TB-303");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='search']")));
        System.out.println("Article structure verified correctly.");
    }

    @Test
    public void verifyAllContentSections() {
        List<String> sections = Arrays.asList(
                "Design and features", "Legacy", "Successors", "References");
        for (String section : sections) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(.,'" + section + "')]")));
            System.out.println("Section verified: " + section);
        }
        System.out.println("All content sections verified correctly.");
    }

    @Test
    public void verifySidebarNavigationMenu() {
        driver.findElement(By.id("vector-main-menu-dropdown-checkbox")).click();
        List<String> menuLinks = Arrays.asList(
                "Main page", "Contents", "Current events",
                "Random article", "About Wikipedia", "Contact us");
        for (String link : menuLinks) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='vector-main-menu']//a[contains(.,'" + link + "')]")));
            System.out.println("Menu link verified: " + link);
        }
        System.out.println("Sidebar navigation menu verified correctly.");
    }

    @Test
    public void verifyPageHeaderTabs() {
        List<String> headerTabs = Arrays.asList("Article", "Talk", "Read", "Edit", "View history");
        for (String tab : headerTabs) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "(//*[@id='p-views']//a[contains(.,'" + tab + "')] " +
                    "| //*[@id='p-associated-pages']//a[contains(.,'" + tab + "')])[1]")));
            System.out.println("Header tab verified: " + tab);
        }
        System.out.println("Page header tabs verified correctly.");
    }

    @Test
    public void verifyFooterLinksExist() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        List<String> footerLinks = Arrays.asList(
                "Privacy policy", "About Wikipedia", "Disclaimers", "Contact Wikipedia");
        for (String link : footerLinks) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='footer']//a[contains(.,'" + link + "')]")));
            System.out.println("Footer link verified: " + link);
        }
        System.out.println("Footer links verified correctly.");
    }

    @Test
    public void verifyTechnicalSpecificationsInInfobox() {
        WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".infobox")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", infobox);
        String text = infobox.getText();
        Assert.assertTrue(text.contains("Roland"), "Infobox should mention Roland");
        Assert.assertTrue(text.contains("1981") && text.contains("1984"),
                "Infobox should mention dates 1981-1984");
        Assert.assertTrue(text.contains("£238") || text.contains("395"),
                "Infobox should contain price info");
        Assert.assertTrue(text.contains("monophonic"), "Infobox should mention monophonic");
        Assert.assertTrue(text.contains("monotimbral"), "Infobox should mention monotimbral");
        Assert.assertTrue(text.contains("Sawtooth") || text.contains("square wave"),
                "Infobox should mention oscillator type");
        Assert.assertTrue(text.contains("low-pass") || text.contains("24 dB"),
                "Infobox should mention filter spec");
        Assert.assertTrue(text.contains("64 patterns") || text.contains("Storage"),
                "Infobox should mention storage");
        Assert.assertTrue(text.contains("16 pattern") || text.contains("Keyboard"),
                "Infobox should mention keyboard");
        System.out.println("All technical specifications verified correctly.");
    }
}