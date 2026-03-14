import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

public class WikipediaTest01 extends BaseTest {

    @BeforeMethod
    public void setUp() {
        initDriver();
        driver.get("https://en.wikipedia.org/wiki/Main_Page");
    }

    @Test
    public void verifyMainPageStructure() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".mw-logo-wordmark")));

        // FIX: "Welcome to Wikipedia" is split across child nodes on the current
        // Wikipedia layout — contains(text(),...) only matches a single text node.
        // The stable element that always contains this heading is #mp-welcome.
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#mp-welcome")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='search']")));
        System.out.println("Main structure verified correctly.");
    }

    @Test
    public void verifyAllContentSections() {
        // FIX: "From today's featured article" contains an apostrophe.
        // Embedding it inside single-quoted XPath produces invalid XPath.
        // Use XPath concat() to safely include the apostrophe.
        String todaySection = "concat('From today', \"'\", 's featured article')";

        List<String> plainSections = Arrays.asList(
                "Did you know", "In the news", "On this day");

        // Handle the apostrophe section separately with concat()
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(., " + todaySection + ")]")));
        System.out.println("Section verified: From today's featured article");

        // Plain sections have no special characters — normal XPath is fine
        for (String section : plainSections) {
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
    public void verifySearchFunctionality() {
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='search']")));
        searchBox.sendKeys("Playwright");
        searchBox.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("Playwright"));
        System.out.println("Search functionality verified correctly.");
    }

    @Test
    public void verifyAvailableLanguages() {
        WebElement langBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("p-lang-btn")));
        String text = langBtn.getText();
        Assert.assertTrue(
                text.matches(".*\\d+.*language.*") || text.matches(".*language.*"),
                "Language button should reference available languages. Actual: " + text);
        System.out.println("Language selector verified correctly.");
    }

    @Test
    public void verifyWikipediaLogoLinksToMainPage() {
        driver.get("https://en.wikipedia.org/wiki/Wikipedia:About");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".mw-logo"))).click();
        wait.until(ExpectedConditions.urlMatches(".*Main_Page$"));
        System.out.println("Logo link verified correctly.");
    }

    @Test
    public void verifySisterProjectsSection() {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight)");
        // FIX: apostrophe in "Wikipedia's sister projects" — use concat()
        String heading = "concat('Wikipedia', \"'\", 's sister projects')";
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(., " + heading + ")]")));
        List<String> projects = Arrays.asList("Commons", "Wikibooks", "Wikidata", "Wikiquote");
        for (String project : projects) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='mp-sister-content']//a[contains(.,'" + project + "')]")));
        }
        System.out.println("Sister projects section verified correctly.");
    }

    @Test
    public void verifyPageHeaderTabs() {
        List<String> headerTabs = Arrays.asList(
                "Main Page", "Talk", "Read", "View source", "View history");
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
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight)");
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
    public void verifyTodayFeaturedArticleExists() {
        WebElement featuredArticle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("mp-tfa")));
        String content = featuredArticle.getText();
        Assert.assertNotNull(content);
        Assert.assertTrue(content.length() > 100,
                "Featured article content should be > 100 chars");
        System.out.println("Featured article section verified correctly.");
    }
}