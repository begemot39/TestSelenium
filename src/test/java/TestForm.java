import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestForm {


    private WebDriver driver;
    public WebElement form;


    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
        form = driver.findElement(By.cssSelector("form[method='post']"));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    public void positiveTestForm() { // Корректные данные для заполнения формы

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText.trim());
    }

    @Test
    public void negativeTestFormInvalidSymbolsInName() { // В поле Имя использованы недопустимые символы

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("I2an Ivan^%");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText.trim());

    }

    @Test
    public void negativeTestFormOnlyDashAndWhitespacesInName() { // В поле Имя использованы только дефисы и пробелы.

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("----    ");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText.trim());

    }

    @Test
    public void negativeTestFormEmptyName() { // Поле Имя пустое

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualText.trim());

    }

    @Test
    public void negativeTestFormWhitespacesInName() { // В поле Имя введены пробелы

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("      ");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualText.trim());

    }

    @Test
    public void negativeTestFormInvalidSymbolsInPhone() { // В поле Телефон некорректные символы

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("???(*9Р%;№");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText.trim());

    }

    @Test
    public void negativeTestFormPhoneEmpty() { // Поле Телефон пустое

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualText.trim());

    }

    @Test
    public void negativeTestFormPhoneNotElevenNumber() { // В поле Телефон не 11 цифр

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+799999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText.trim());

    }

    @Test
    public void negativeTestFormCheckBoxNotClick() { // ЧекБокс не нажат

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("button[type='button']")).click();

        WebElement expCheckBoxStatus = driver.findElement(By.cssSelector(".checkbox.input_invalid"));
        assertTrue(expCheckBoxStatus.isDisplayed());

    }
}