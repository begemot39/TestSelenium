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

public class testForm {

    // Селекторы:
//    Форма - form.form_size_m.form_theme_alfa-on-white
//    Имя - [data-test-id='name'] [name='name']
//    Телефон - [data-test-id='phone'] [name='phone']
//    ЧекБокс - [data-test-id='agreement']
//    knopka - button[type='button']
//    Спан в конце - [data-test-id='order-success']
//
//    Текст - Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.
//    java -jar ./artifacts/app-order.jar

    // Негативные селекторы:
//    Имя - [data-test-id='name'] .input__sub Введены недопустимы символы или поле оставлено пустым.
//    Тест: Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы. - Недопустимые символы.
//    Тест: Поле обязательно для заполнения. - Поле пустое.
//    Телефон: [data-test-id='phone'] .input__sub
//    Текст: Поле обязательно для заполнения - Поле пустое.
//    Текст: Телефон указан неверно. Должно быть 11 цифр, например, +79012345678. - Введены некорректные символы.
//    ЧекБокс не проставлен: [role='presentation']

    private WebDriver driver;
    WebElement form;


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
        form = driver.findElement(By.cssSelector("form.form_size_m.form_theme_alfa-on-white"));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    public void positiveTestForm() {

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText.trim());
    }

    @Test
    public void negativeTestFormInvalidSymbolsInName() {

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("I2an Ivan^%");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText.trim());

    }

    @Test
    public void negativeTestFormEmptyName() {

        form.findElement(By.cssSelector("[data-test-id='name'] [name='name']")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id='phone'] [name='phone']")).sendKeys("+79999999999");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button[type='button']")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualText.trim());

    }
}