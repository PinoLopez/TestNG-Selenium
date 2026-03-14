import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

public class WikipediaTest02 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/Aira_Compact");
    }

    @Test
    public void verifyArticlePageStructure() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mw-logo-wordmark")));
        WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        Assert.assertEquals(h1.getText(), "Aira Compact");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='search']")));
        System.out.println("Article structure verified correctly.");
    }

    @Test
    public void verifyAllContentSections() {
        List<String> sections = Arrays.asList("Release", "Design", "Reception", "References");
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
    public void verifyModelsSubsections() {
        List<String> models = Arrays.asList(
                "T-8 Beat Machine", "J-6 Chord Synthesizer",
                "E-4 Voice Tweaker", "S-1 Tweak Synth", "P-6 Creative Sampler");
        for (String model : models) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h4[contains(.,'" + model + "')]")));
            System.out.println("Model subsection verified: " + model);
        }
        System.out.println("Models subsections verified correctly.");
    }
}