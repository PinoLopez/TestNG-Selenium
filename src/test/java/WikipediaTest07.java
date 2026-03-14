import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

public class WikipediaTest07 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/DreamWorks_Animation");
    }

    @Test
    public void verifyArticlePageStructure() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mw-logo-wordmark")));
        WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        Assert.assertEquals(h1.getText(), "DreamWorks Animation");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='search']")));
        System.out.println("Article structure verified correctly.");
    }

    @Test
    public void verifyMainContentSections() {
        List<String> sections = Arrays.asList("History", "References");
        for (String section : sections) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h2[contains(.,'" + section + "')]")));
            System.out.println("Section verified: " + section);
        }
        System.out.println("Main content sections verified correctly.");
    }

    @Test
    public void verifySidebarNavigationMenu() {
        driver.findElement(By.id("vector-main-menu-dropdown-checkbox")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vector-main-menu")));
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
        List<String> headerTabs = Arrays.asList("Article", "Talk", "Read", "View history");
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
        WebElement footer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("footer")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", footer);
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
    public void verifyCorporateInfoboxDetails() {
        WebElement infobox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".infobox")));
        List<String> expectedDetails = Arrays.asList(
                "Glendale, California", "Universal Pictures", "Animation");
        for (String detail : expectedDetails) {
            Assert.assertTrue(infobox.getText().contains(detail),
                    "Infobox should contain: " + detail);
            System.out.println("Infobox detail verified: " + detail);
        }
        System.out.println("Corporate infobox details verified correctly.");
    }

    @Test
    public void verifyMajorFilmsMentions() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mw-content-text")));
        List<String> majorFilms = Arrays.asList(
                "Shrek", "Madagascar", "Kung Fu Panda",
                "How to Train Your Dragon", "The Boss Baby");
        for (String film : majorFilms) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='mw-content-text']//*[contains(.,'" + film + "')][1]")));
            System.out.println("Film mention verified: " + film);
        }
        System.out.println("Major films mentions verified correctly.");
    }
}