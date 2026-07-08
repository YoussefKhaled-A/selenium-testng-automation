package StepDefinitions;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.Select;

public class TC1Steps {

    private WebDriver driver;
    private String finalUrl;

    @Given("employer is logged in")
    public void employer_is_logged_in() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://forasna.com/");
        Thread.sleep(2000);

        driver.findElement(By.xpath("//a[@onclick=\"gtagFN('Top Nav', 'Login','Homepage - Desktop');\"]")).click();
        Thread.sleep(2000);

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("kamoh22341@mucate.com\n");
        Thread.sleep(1000);

        driver.findElement(By.id("password")).sendKeys("Youseif200");
        Thread.sleep(1000);

        driver.findElement(By.id("btn-submit")).click();
        Thread.sleep(4000);
    }

    @When("employer posts a valid job")
    public void employer_posts_a_valid_job() throws InterruptedException {
        driver.navigate().to("https://forasna.com/employer/dashboard");
        Thread.sleep(3000);

        driver.findElement(By.xpath("//div[@class='add-new-job-btn-wrapper']/a")).click();
        Thread.sleep(3000);

        driver.navigate().to("https://forasna.com/employer/job/post");
        Thread.sleep(3000);

        WebElement jobTitle = driver.findElement(By.id("job_title"));
        jobTitle.click();
        jobTitle.sendKeys("امين مخزن");
        Thread.sleep(1000);

        try { driver.findElement(By.linkText("امين مخزن (يشترط الخبرة)")).click(); } catch (Exception e) {}
        Thread.sleep(1500);

        // Main Work Field (select by sending text then ENTER)
        WebElement mainWorkEl = driver.findElement(By.id("main_work_field_id"));
        Select mainWork = new Select(mainWorkEl);
        mainWork.selectByVisibleText("تكنولوجيا معلومات واتصالات");
        Thread.sleep(1000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_sub_work_field_id")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_sub_work_field_id"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop') and contains(@class,'select2-drop-active')]//div[contains(text(),'برمجة')]")
        )).click();


        // Number of vacancies
        WebElement vacancies = driver.findElement(By.id("num_of_vacancies"));
        vacancies.clear();
        vacancies.sendKeys("3");
        Thread.sleep(1000);


        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Select countrySel = new Select(driver.findElement(By.id("country")));
        countrySel.selectByVisibleText("مصر");
        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_governorate")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_governorate"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='القاهرة']")
        )).click();

        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_area")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_area"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='الزيتون']")
        )).click();

        Thread.sleep(500);
        WebElement education = driver.findElement(By.id("education_level"));
        education.click();
        education.sendKeys("لا يشترط");
        Thread.sleep(1000);

        driver.findElement(By.id("career_level_8_ico")).click();
        Thread.sleep(1000);

        WebElement minSalary = driver.findElement(By.id("min_salary"));
        minSalary.clear();
        minSalary.sendKeys("1000");
        Thread.sleep(500);

        WebElement maxSalary = driver.findElement(By.id("max_salary"));
        maxSalary.clear();
        maxSalary.sendKeys("3000");
        Thread.sleep(500);

        driver.findElement(By.xpath("//i[@id='job_type_2_ico']/span")).click();
        Thread.sleep(500);

        driver.findElement(By.id("shift_type_0_ico")).click();
        Thread.sleep(500);

        driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
        Thread.sleep(500);

        driver.findElement(By.id("job-post-submit")).click();
        Thread.sleep(3000);

        finalUrl = driver.getCurrentUrl();
    }

    @Then("job should be posted successfully")
    public void job_should_be_posted_successfully() {
        Assert.assertTrue(finalUrl.contains("pending") || finalUrl.contains("success"));
        if (driver != null) driver.quit();
    }
}
