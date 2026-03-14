import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

public class WikipediaTest09 extends BaseTest {

    @DataProvider(name = "mediaWikiSites")
    public Object[][] mediaWikiSitesProvider() {
        return new Object[][] {
                {"Wikipedia",         "https://en.wikipedia.org/wiki/Main_Page"},
                {"Wikimedia Commons", "https://commons.wikimedia.org/wiki/Main_Page"},
                {"Wikidata",          "https://www.wikidata.org/wiki/Wikidata:Main_Page"},
                {"Wikivoyage",        "https://en.wikivoyage.org/wiki/Main_Page"},
                {"Wiktionary",        "https://en.wiktionary.org/wiki/Wiktionary:Main_Page"},
                {"MediaWiki",         "https://www.mediawiki.org/wiki/MediaWiki"},
                {"Wikiversity",       "https://en.wikiversity.org/wiki/Wikiversity:Main_Page"},
                {"Wikinews",          "https://en.wikinews.org/wiki/Main_Page"},
        };
    }

    @BeforeMethod
    public void setUp() {
        initDriver();
    }

    @Test(dataProvider = "mediaWikiSites")
    public void verifyMediaWikiSiteStructureAndLinks(String siteName, String url) {
        System.out.println("\n=== Verifying: " + siteName + " ===");
        driver.get(url);

        // Verify page loaded with a title
        wait.until(driver -> !driver.getTitle().isEmpty());
        System.out.println("Page title: " + driver.getTitle());

        // Verify MediaWiki generator meta tag
        List<WebElement> generators = driver.findElements(
                By.cssSelector("meta[name='generator']"));
        Assert.assertFalse(generators.isEmpty(),
                siteName + ": Should have a generator meta tag");
        String generatorContent = generators.get(0).getAttribute("content");
        Assert.assertTrue(generatorContent != null && generatorContent.contains("MediaWiki"),
                siteName + ": Generator should be MediaWiki. Actual: " + generatorContent);
        System.out.println("Generator: " + generatorContent);

        // Verify main navigation
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#mw-navigation, nav, #p-navigation")));
        System.out.println("✓ Main navigation visible");

        // Verify main content
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#mw-content-text, #content, .mw-body")));
        System.out.println("✓ Main content visible");

        // FIX: collect all /wiki/ links then filter out those with empty visible text
        // (icon-only anchors like logo links have no text and caused the original failure)
        List<WebElement> allLinks = driver.findElements(
                By.cssSelector("#mw-content-text a[href*='/wiki/'], #content a[href*='/wiki/']"));

        List<WebElement> internalLinks = allLinks.stream()
                .filter(a -> {
                    try {
                        String t = a.getText().trim();
                        String h = a.getAttribute("href");
                        return !t.isEmpty() && h != null && h.contains("/wiki/");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        Assert.assertFalse(internalLinks.isEmpty(),
                siteName + ": Should have internal /wiki/ links with visible text");
        System.out.println("Internal links with text found: " + internalLinks.size());

        // Spot-check up to 5 links
        int linksToCheck = Math.min(internalLinks.size(), 5);
        for (int i = 0; i < linksToCheck; i++) {
            WebElement link = internalLinks.get(i);
            String linkText = link.getText().trim();
            String href = link.getAttribute("href");
            Assert.assertTrue(linkText.length() > 0, "Link text should not be empty");
            Assert.assertTrue(href != null && href.contains("/wiki/"),
                    "Link href should contain /wiki/");
            System.out.println("  Link #" + (i + 1) + ": \"" + linkText + "\" → " + href);
        }

        // Verify footer with MediaWiki link
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("footer, #footer")));
        List<WebElement> poweredBy = driver.findElements(
                By.cssSelector("a[href*='mediawiki.org']"));
        Assert.assertFalse(poweredBy.isEmpty(),
                siteName + ": Should have a link to mediawiki.org in footer");
        System.out.println("✓ Footer with MediaWiki link found");
        System.out.println("\n✓ " + siteName + " verified successfully\n");
    }

    @Test
    public void quickVerificationOfMultipleMediaWikiSites() {
        System.out.println("\n=== Quick verification of all sites ===");
        Object[][] sites = mediaWikiSitesProvider();
        int successful = 0;

        for (Object[] siteData : sites) {
            String siteName = (String) siteData[0];
            String url = (String) siteData[1];
            try {
                driver.get(url);
                List<WebElement> generators = driver.findElements(
                        By.cssSelector("meta[name='generator']"));
                boolean isMediaWiki = !generators.isEmpty()
                        && generators.get(0).getAttribute("content") != null
                        && generators.get(0).getAttribute("content").contains("MediaWiki");
                if (isMediaWiki) {
                    successful++;
                    System.out.println("✓ " + siteName + ": MediaWiki detected");
                } else {
                    System.out.println("✗ " + siteName + ": MediaWiki not detected");
                }
            } catch (Exception e) {
                System.out.println("✗ " + siteName + ": Error - " + e.getMessage());
            }
        }

        System.out.println("\n=== SUMMARY ===");
        System.out.println("Sites verified: " + sites.length);
        System.out.println("MediaWiki sites confirmed: " + successful);
        System.out.printf("Success rate: %.1f%%%n", (successful * 100.0 / sites.length));
        Assert.assertTrue((double) successful / sites.length > 0.8,
                "At least 80% of sites should be confirmed as MediaWiki");
    }
}