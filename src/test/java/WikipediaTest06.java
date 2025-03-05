import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WikipediaTest06 {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/home/agropecuario/geckodriver");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testHighlightElementsOnCantabriaPage() throws InterruptedException {
        driver.get("https://en.wikipedia.org/wiki/Cantabria");

        List<String> elementos = Arrays.asList("Spain", "Santander");
        List<String> colores = Arrays.asList("yellow", "cyan");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < elementos.size(); i++) {
            String elemento = elementos.get(i);
            String color = colores.get(i);

            WebElement elementoEncontrado = driver.findElement(By.xpath("//a[contains(text(), '" + elemento + "')]"));

            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", elementoEncontrado);
            Thread.sleep(1000);

            js.executeScript("arguments[0].style.backgroundColor = arguments[1]; arguments[0].style.border = '3px solid black';", elementoEncontrado, color);

            Thread.sleep(2000);
        }

        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Cantabria"));

        Thread.sleep(1000);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}