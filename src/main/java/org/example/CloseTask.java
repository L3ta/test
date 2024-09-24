package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

public class CloseTask {
    private WebDriver driver;

    // Метод для авторизації на сайті Kanboard
    public void loginToKanboard(String username, String password, String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Додаємо headless режим
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless"); // Додаємо headless режим
            driver = new FirefoxDriver(options);
        }

        driver.get("http://localhost/kanboard"); // Заміни на реальну URL свого Kanboard

        WebElement loginField = driver.findElement(By.name("username"));
        loginField.clear();
        loginField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Затримка для завантаження
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Метод для переходу до проекту після авторизації
    public void goToProject() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement projectLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='dashboard']/div[2]/div[2]/div[2]/div[1]/span/a")));

        projectLink.click();
    }

    // Метод для натискання на конкретний елемент проекту
    public void clickOnProjectElement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement projectElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='board']/tbody/tr[2]/td[1]/div[1]/div/div[2]/div[2]/div/a/strong/i")));

        projectElement.click();
    }

    // Метод для натискання на елемент в dropdown меню
    public void clickOnDropdownElement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdownElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='dropdown']/ul/li[15]/a")));

        dropdownElement.click();
    }

    // Метод для натискання на кнопку підтвердження в модальному вікні
    public void clickOnConfirmButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='modal-confirm-button']")));

        confirmButton.click();
    }

    // Генерація випадкового заголовка
    private String generateRandomTitle() {
        SecureRandom random = new SecureRandom();
        int length = 10;
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    // Метод для створення завдання в проекті
    public void createTask() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Клік по кнопці створення завдання
        WebElement createTaskButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='board']/tbody/tr[1]/th[1]/div[2]/div/a/i")));

        createTaskButton.click();

        // Заповнення заголовка завдання рандомним текстом
        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='form-title']")));

        String randomTitle = generateRandomTitle();
        titleInput.clear();
        titleInput.sendKeys(randomTitle);

        // Натискаємо кнопку Save
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='modal-content']/form/div/div[4]/div/div/button")));

        saveButton.click();

        // Після створення завдання натискаємо на проектний елемент
        clickOnProjectElement();

        // Натискаємо на елемент в dropdown меню
        clickOnDropdownElement();

        // Натискаємо на кнопку підтвердження
        clickOnConfirmButton();
    }

    // Метод для отримання taskId після створення завдання
    public String getCreatedTaskId() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Отримуємо taskId через XPath
        WebElement taskElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/section/section/div[2]/table/tbody/tr[2]/td[1]/div[1]/div/div[2]/div[2]/div/a/strong")));

        String taskText = taskElement.getText(); // Отримуємо текст завдання, наприклад "#123"
        return taskText.replace("#", ""); // Повертаємо тільки числову частину taskId
    }

    // Метод для закриття браузера
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
