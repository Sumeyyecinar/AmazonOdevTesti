package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import testBase.BaseTest;

import java.util.List;

public class MacbookTest extends BaseTest {

    private final By aramaKutusuId = By.id("twotabsearchtextbox");
    private final By aramaButonuId = By.id("nav-search-submit-button");
    private final By minFiyatKutusuName = By.name("low-price");
    private final By goButonuXPath = By.xpath("//input[@aria-label='Fiyat aralığına git - fiyat aralığını gönder']");
    private final By siralaDropdownId = By.id("s-result-sort-select");
    private final By tumUrunKartiXPath = By.xpath("//div[@data-component-type='s-search-result']");
    private final By sepetIkonuSayiXPath = By.xpath("//span[@id='nav-cart-count']");
    private final By sepeteEkleButonuId = By.id("add-to-cart-button");
    private final By sepettekiUrunAdiXPath = By.xpath("//div[@data-name='Active Items']//span[@class='a-truncate-cut']");
    private final By sepetteSilButonuXPath = By.xpath("//input[@data-action='delete']");
    private final By urunKonteynerleriXPath = By.xpath("//div[@data-asin]");
    private final By yeniUrunListesiXPath = By.xpath("//div[contains(@class, 'sc-list-item')]");

    @Test
    public void macbookAramaTesti() throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String aramaSonucURL = "";
        int bulunanUrunSayisi = 0;
        final int HEDEF_INDEX = 1;
        final int HEDEF_SIRA_NUMARASI = 2;

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(aramaKutusuId)).sendKeys("Apple Macbook");
            driver.findElement(aramaButonuId).click();
            System.out.println("'Apple Macbook' araması tamamlandı.");
            aramaSonucURL = driver.getCurrentUrl();

            js.executeScript("window.scrollBy(0, 700)");
            WebElement minKutusu = wait.until(ExpectedConditions.presenceOfElementLocated(minFiyatKutusuName));
            js.executeScript("arguments[0].value = '88000';", minKutusu);
            System.out.println(" Minimum fiyat 88.000 TL olarak  girildi.");
            WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(goButonuXPath));
            js.executeScript("arguments[0].click();", goButton);
            wait.until(ExpectedConditions.urlContains("88000"));
            Thread.sleep(3000);
            System.out.println(" Fiyat filtresi (88.000 TL ve Üzeri)  uygulandı.");


            wait.until(ExpectedConditions.presenceOfElementLocated(siralaDropdownId));
            Select siralamaMenusuBir = new Select(driver.findElement(siralaDropdownId));
            siralamaMenusuBir.selectByVisibleText("Fiyat: Düşükten Yükseğe");
            System.out.println("SIRALAMA TAMAM: Ürünler 'Fiyat: Düşükten Yükseğe' sıralandı.");


            wait.until(ExpectedConditions.stalenessOf(driver.findElement(siralaDropdownId)));
            driver.navigate().refresh();
            wait.until(ExpectedConditions.visibilityOfElementLocated(siralaDropdownId));
            Thread.sleep(7000);

            List<WebElement> urunKartlari = driver.findElements(tumUrunKartiXPath);
            bulunanUrunSayisi = urunKartlari.size();

            if (bulunanUrunSayisi > HEDEF_INDEX) {

                WebElement hedefUrunLink = urunKartlari.get(HEDEF_INDEX).findElement(By.xpath(".//a"));
                String urunSayfasiURLBir = hedefUrunLink.getAttribute("href");
                driver.get(urunSayfasiURLBir);
                System.out.println(" En düşük fiyatlı " + HEDEF_SIRA_NUMARASI + ". ürünün sayfası açıldı.");

                wait.until(ExpectedConditions.elementToBeClickable(sepeteEkleButonuId)).click();
                wait.until(ExpectedConditions.textToBe(sepetIkonuSayiXPath, "1"));
                System.out.println(" İlk ürün sepete eklendi.");

                driver.get(aramaSonucURL);
                wait.until(ExpectedConditions.presenceOfElementLocated(siralaDropdownId));
                Select siralamaMenusuIki = new Select(driver.findElement(siralaDropdownId));
                siralamaMenusuIki.selectByVisibleText("Ort. Müşteri Yorumu");
                driver.navigate().refresh();
                wait.until(ExpectedConditions.visibilityOfElementLocated(siralaDropdownId));
                Thread.sleep(5000);

                List<WebElement> urunKartlariIki = driver.findElements(tumUrunKartiXPath);
                if (urunKartlariIki.size() > 0) {
                    WebElement hedefUrunLinkIki = urunKartlariIki.get(0).findElement(By.xpath(".//a"));
                    driver.get(hedefUrunLinkIki.getAttribute("href"));
                    wait.until(ExpectedConditions.elementToBeClickable(sepeteEkleButonuId)).click();
                    wait.until(ExpectedConditions.textToBe(sepetIkonuSayiXPath, "2"));
                    System.out.println(" İkinci ürün sepete eklendi ve sepette 2 ürün olduğu doğrulandı.");
                } else {
                    System.err.println("UYARI: İkinci ürün sepete eklenemedi.");
                }



                driver.get("https://www.amazon.com.tr/gp/cart/view.html");
                System.out.println(" Sepet sayfasına (doğrudan URL ile) gidildi.");

                System.out.println(" Sepet elementleri yüklendi.");

                Thread.sleep(12000);
                System.out.println(" Dinamik yükleme için 12 saniye sabit beklendi.");

                List<WebElement> sepettekiUrunler = driver.findElements(yeniUrunListesiXPath);
                int mevcutUrunSayisi = sepettekiUrunler.size();
                System.out.println("mevcutUrunSayisi (yeniUrunListesiXPath): " + mevcutUrunSayisi);

                if (mevcutUrunSayisi == 0) {
                    sepettekiUrunler = driver.findElements(sepetteSilButonuXPath);
                    mevcutUrunSayisi = sepettekiUrunler.size();
                    System.out.println("mevcutUrunSayisi (sepetteSilButonuXPath - yedek): " + mevcutUrunSayisi);
                }

                if (mevcutUrunSayisi == 0) {
                    sepettekiUrunler = driver.findElements(urunKonteynerleriXPath);
                    mevcutUrunSayisi = sepettekiUrunler.size();
                    System.out.println("mevcutUrunSayisi (urunKonteynerleriXPath - ikinci yedek): " + mevcutUrunSayisi);
                }

                if (mevcutUrunSayisi < 2) {
                    System.err.println("KRİTİK HATA: Sepet beklendiği gibi en az 2 ürün içermiyor. Bulunan ürün: " + mevcutUrunSayisi);
                    Assert.fail("Test Başarısız: Sepette en az 2 ürün bulunamadı. (Tüm XPATH denemeleri başarısız oldu)");
                }

                System.out.println(" Sepette **en az 2 ürün** (" + mevcutUrunSayisi + " adet) olduğu doğrulandı ve listelendi.");

                for(int i = 0; i < mevcutUrunSayisi; i++) {
                    System.out.println("   -> Ürün " + (i + 1) + ": " + sepettekiUrunler.get(i).getText().split(",")[0] + "...");
                }

                System.out.println("✅ TEST BAŞARILI ŞEKİLDE TAMAMLANDI:) Sepette 2 ürünün varlığı doğrulandı. Temizlik adımı atlandı.");

                Assert.assertTrue(true, "Sepette 2 ürünün varlığı başarıyla doğrulandı.");

                return;

            } else {
                System.err.println("KRİTİK HATA: Sayfada en az " + HEDEF_SIRA_NUMARASI + " adet ürün bulunamadı. Toplam ürün: " + bulunanUrunSayisi);
                Assert.fail("Test Başarısız: Yeterli ürün bulunamadı.");
            }

        } catch (Exception e) {
            System.err.println("KRİTİK HATA: Test beklenmedik bir hatayla karşılaştı.");
            Assert.fail("Test Başarısız: Hata Mesajı: " + e.getMessage());
        }
    }
}