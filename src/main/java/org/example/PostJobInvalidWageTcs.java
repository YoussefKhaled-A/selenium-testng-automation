package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PostJobInvalidWageTcs {

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

        // -------------------- OPEN POST JOB PAGE (TC2) --------------------
        // For this test we open the post-job page directly (like in Katalon TC2)
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


        // (Optional) Area container click – like xpath=//div[@id='s2id_area']/ul
        try {
            driver.findElement(By.xpath("//div[@id='s2id_area']/ul")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Area dropdown not clickable, continuing...");
        }

        // Job Type and Shift – same as Katalon (job_type_2_lbl / shift_type_0_lbl)
        try {
            driver.findElement(By.id("job_type_2_lbl")).click();
        } catch (Exception e) {
            // fallback to the icon locator if lbl is not working
            driver.findElement(By.xpath("//i[@id='job_type_2_ico']/span")).click();
        }
        Thread.sleep(500);

        try {
            driver.findElement(By.xpath("//label[@id='shift_type_0_lbl']/span")).click();
        } catch (Exception e) {
            driver.findElement(By.id("shift_type_0_ico")).click();
        }
        Thread.sleep(500);

        // Education level = لا يشترط
        WebElement education = driver.findElement(By.id("education_level"));
        education.click();
        education.sendKeys("لا يشترط");
        Thread.sleep(1000);

        // Career level (click 1 then 8 like in TC2)
        try {
            driver.findElement(By.id("career_level_1_lbl")).click();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("career_level_1_lbl not found, skipping...");
        }

        try {
            driver.findElement(By.xpath("//label[@id='career_level_8_lbl']/span")).click();
        } catch (Exception e) {
            // fallback to icon like in TC1
            driver.findElement(By.id("career_level_8_ico")).click();
        }
        Thread.sleep(1000);

        // ---------- INVALID WAGE PART (MIN SALARY = 0) ----------
        WebElement minSalary = driver.findElement(By.id("min_salary"));
        minSalary.clear();
        minSalary.sendKeys("0");      // invalid wage
        Thread.sleep(500);

        WebElement maxSalary = driver.findElement(By.id("max_salary"));
        maxSalary.clear();
        maxSalary.sendKeys("3000");
        Thread.sleep(500);

        // Commission = 0 (as in your TC2)
        try {
            WebElement minCommission = driver.findElement(By.id("min_commission"));
            minCommission.clear();
            minCommission.sendKeys("0");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("min_commission not found, continuing...");
        }

        // Special needs toggle
        try {
            driver.findElement(By.id("for_special_needs_0_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
        }
        Thread.sleep(500);

        // Submit
        driver.findElement(By.id("job-post-submit")).click();
        Thread.sleep(3000);

        // -------------------- RESULT CHECK (NEGATIVE) --------------------
        // For this negative test: we EXPECT the job to *stay* on the same page
        // because min salary = 0 is invalid.
        String url = driver.getCurrentUrl();

        if (url.contains("/employer/job/post")) {
            System.out.println("✅ TC2 Passed: Invalid wage was NOT accepted (still on post page).");
        } else {
            System.out.println("❌ TC2 Failed: Form submitted and navigated away despite invalid wage.");
        }

        driver.quit();
    }
}
