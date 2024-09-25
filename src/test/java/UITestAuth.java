import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class UITestAuth {

    private WebDriver driver;

    @BeforeClass
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        // Налаштування WebDriver для вибраного браузера в безголовому режимі
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            //options.addArguments("--headless"); // Запуск у безголовому режимі
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            //options.addArguments("--headless"); // Запуск у безголовому режимі
            driver = new FirefoxDriver(options);
        }
        driver.get("https://192.168.10.173/kanboard"); // Заміни на реальну URL свого Kanboard
    }

    @Test
    public void positiveLoginTest() {
        Assert.assertTrue(loginToKanboard("admin", "admin"), "Позитивний тест: Авторизація не вдалася.");
    }

    @Test
    public void negativeLoginTestWrongUser() {
        Assert.assertFalse(loginToKanboard("wrongUser", "admin"), "Негативний тест 1: Авторизація повинна не пройти.");
    }

    @Test
    public void negativeLoginTestWrongPassword() {
        Assert.assertFalse(loginToKanboard("admin", "wrongPass"), "Негативний тест 2: Авторизація повинна не пройти.");
    }

    @Test
    public void negativeLoginTestEmptyFields() {
        Assert.assertFalse(loginToKanboard("", ""), "Негативний тест 3: Авторизація повинна не пройти.");
    }

    @AfterClass
    public void tearDown() {
        // Закриваємо браузер
        if (driver != null) {
            driver.quit();
        }
    }

    // Метод для авторизації з доданими затримками
    public boolean loginToKanboard(String username, String password) {
        try {
            // Додаємо затримку перед пошуком елементів
            //Thread.sleep(1000);

            // Знаходимо поле для введення логіна
            WebElement loginField = driver.findElement(By.name("username"));
            loginField.clear(); // Очищуємо поле перед введенням
            loginField.sendKeys(username);

            // Затримка перед введенням пароля
            //Thread.sleep(1000);

            // Знаходимо поле для введення пароля
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.clear(); // Очищуємо поле перед введенням
            passwordField.sendKeys(password);

            // Затримка перед натисканням кнопки входу
            //Thread.sleep(1000);

            // Натискаємо на кнопку входу
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();

            // Перевіряємо, чи успішно виконано вхід
            return driver.getTitle().contains("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
