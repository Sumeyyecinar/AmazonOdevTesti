package testBase;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    public WebDriver driver;
    public WebDriverWait wait;

    @BeforeMethod
    public void setupAndLogin() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.amazon.com.tr");


        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList"))).click();


            wait.until(ExpectedConditions.elementToBeClickable(By.id("ap_email_login"))).sendKeys("smeyyecinar@gmail.com");
            driver.findElement(By.id("continue")).click();


            wait.until(ExpectedConditions.elementToBeClickable(By.id("ap_password"))).sendKeys("Otomasyon123");
            driver.findElement(By.id("signInSubmit")).click();

            WebElement profilYazisi = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList-nav-line-1")));

            System.out.println(" VAR OLAN HESAP İLE GİRİŞ BAŞARILI. Hesap: " + profilYazisi.getText());

        } catch (Exception e) {

            System.out.println("KRİTİK HATA: Giriş işlemi sırasında bir hata oluştu veya locator bulunamadı!");
        }
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}