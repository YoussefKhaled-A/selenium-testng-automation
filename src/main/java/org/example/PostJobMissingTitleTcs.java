package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PostJobMissingTitleTcs {

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

        // -------------------- POST JOB PAGE (TC4_MissingTitle) --------------------
        driver.get("https://forasna.com/employer/job/post");
        Thread.sleep(3000);

        // ===== Job Title (type then CLEAR → missing title) =====
        WebElement jobTitle = driver.findElement(By.id("job_title"));
        jobTitle.click();
        jobTitle.sendKeys("امين مخزن");
        Thread.sleep(1000);

        // (Like Katalon: click suggestion row if it appears)
        try {
            driver.findElement(By.xpath("//div[@id='row_job_title']/ul/li/a/strong")).click();
        } catch (Exception e) {
            System.out.println("Suggestion row not found, continuing...");
        }
        Thread.sleep(1000);

        // Now CLEAR the title to simulate "Missing Title"
        jobTitle.clear();
        Thread.sleep(1000);

        // Area dropdown (xpath=//div[@id='s2id_area']/ul)
        try {
            driver.findElement(By.xpath("//div[@id='s2id_area']/ul")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Area dropdown not clickable, continuing...");
        }

        // Job Type = job_type_2
        try {
            driver.findElement(By.xpath("//label[@id='job_type_2_lbl']/span")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//i[@id='job_type_2_ico']/span")).click();
        }
        Thread.sleep(500);

        // Shift Type = shift_type_0
        try {
            driver.findElement(By.id("shift_type_0_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.id("shift_type_0_ico")).click();
        }
        Thread.sleep(500);

        // Education level = لا يشترط
        WebElement education = driver.findElement(By.id("education_level"));
        education.click();
        education.sendKeys("لا يشترط");
        Thread.sleep(1000);

        // Career level = 8
        try {
            driver.findElement(By.id("career_level_8_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.id("career_level_8_ico")).click();
        }
        Thread.sleep(1000);

        // Salary: min 200, max 400  (from Katalon TC4)
        WebElement minSalary = driver.findElement(By.id("min_salary"));
        minSalary.clear();
        minSalary.sendKeys("200");
        Thread.sleep(500);

        WebElement maxSalary = driver.findElement(By.id("max_salary"));
        maxSalary.clear();
        maxSalary.sendKeys("400");
        Thread.sleep(500);

        // Special needs toggle
        try {
            driver.findElement(By.id("for_special_needs_0_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
        }
        Thread.sleep(500);

        // Submit (with MISSING TITLE)
        driver.findElement(By.id("job-post-submit")).click();
        Thread.sleep(3000);

        // Optionally click "Save as draft" like in Katalon
        try {
            driver.findElement(By.id("save_as_draft")).click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("save_as_draft button not found, continuing...");
        }

        // -------------------- RESULT CHECK (NEGATIVE) --------------------
        // Expectation: Missing title → job should not be fully posted.
        String url = driver.getCurrentUrl();

        if (url.contains("/employer/job/post")) {
            System.out.println("✅ TC4 Passed: Missing job title was NOT accepted (still on post/draft flow).");
        } else {
            System.out.println("❌ TC4 Failed: Form navigated away even though title is missing.");
        }

        driver.quit();
    }
}
