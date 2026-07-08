package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class PostJobTcs {

    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // -------------------- LOGIN --------------------
        driver.get("https://forasna.com/");
        Thread.sleep(2000);

        driver.findElement(By.xpath("//a[@onclick=\"gtagFN('Top Nav', 'Login','Homepage - Desktop');\"]")).click();
        Thread.sleep(2000);

        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("kamoh22341@mucate.com");
        Thread.sleep(1000);

        driver.findElement(By.id("password")).sendKeys("Youseif200");
        Thread.sleep(1000);

        driver.findElement(By.id("btn-submit")).click();
        Thread.sleep(4000);

        // -------------------- DASHBOARD --------------------
        driver.get("https://forasna.com/employer/dashboard");
        Thread.sleep(3000);

        // Click "Add New Job"
        WebElement addJob = driver.findElement(By.xpath("//div[@class='add-new-job-btn-wrapper']/a"));
        addJob.click();
        Thread.sleep(3000);

        // -------------------- POST JOB PAGE --------------------
        driver.get("https://forasna.com/employer/job/post");
        Thread.sleep(3000);

        // Job Title
        WebElement jobTitle = driver.findElement(By.id("job_title"));
        jobTitle.click();
        jobTitle.sendKeys("امين مخزن");
        Thread.sleep(1000);

        // Click suggested title if visible
        try {
            driver.findElement(By.linkText("امين مخزن (يشترط الخبرة)")).click();
        } catch (Exception e) {
            System.out.println("Suggestion not visible, continuing...");
        }

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


        // Education level
        WebElement education = driver.findElement(By.id("education_level"));
        education.click();
        education.sendKeys("لا يشترط");
        Thread.sleep(1000);

        // Career level
        driver.findElement(By.id("career_level_8_ico")).click();
        Thread.sleep(1000);

        // Salary
        WebElement minSalary = driver.findElement(By.id("min_salary"));
        minSalary.clear();
        minSalary.sendKeys("1000");
        Thread.sleep(500);

        WebElement maxSalary = driver.findElement(By.id("max_salary"));
        maxSalary.clear();
        maxSalary.sendKeys("3000");
        Thread.sleep(500);

        // Job Type and Shift
        driver.findElement(By.xpath("//i[@id='job_type_2_ico']/span")).click();
        Thread.sleep(500);
        driver.findElement(By.id("shift_type_0_ico")).click();
        Thread.sleep(500);

        // Special needs toggle
        driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
        Thread.sleep(500);

        // Submit
        driver.findElement(By.id("job-post-submit")).click();
        Thread.sleep(3000);

        // -------------------- RESULT CHECK --------------------
        String url = driver.getCurrentUrl();
        if (url.contains("pending") || url.contains("success")) {
            System.out.println("✅ Test Passed: Job posted successfully.");
        } else {
            System.out.println("❌ Test Failed: Job not posted.");
        }

        driver.quit();
    }
}
