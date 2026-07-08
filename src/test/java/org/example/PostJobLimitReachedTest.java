package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class PostJobLimitReachedTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // ====== Test Data (same account + same flow) ======
    private final String baseUrl = "https://forasna.com/";
    private final String email = "kamoh22341@mucate.com";
    private final String password = "Youseif200";
    private final String postJobUrl = "https://forasna.com/employer/job/post";

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // -------------------- LOGIN --------------------
        driver.get(baseUrl);

        By loginBtn = By.xpath("//a[@onclick=\"gtagFN('Top Nav', 'Login','Homepage - Desktop');\"]");
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();

        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailInput.click();
        emailInput.sendKeys(email);

        WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passInput.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-submit"))).click();

        // Ensure employer area loaded
        wait.until(ExpectedConditions.urlContains("/employer/"));

        // -------------------- GO TO POST JOB PAGE --------------------
        driver.get("https://forasna.com/employer/dashboard");
        wait.until(ExpectedConditions.urlContains("/employer/dashboard"));

        // Click "Add New Job" (like your script)
        By addNewJobBtn = By.xpath("//div[@class='add-new-job-btn-wrapper']/a");
        wait.until(ExpectedConditions.elementToBeClickable(addNewJobBtn)).click();

        // Open post job page directly (stable)
        driver.get(postJobUrl);
        wait.until(ExpectedConditions.urlContains("/employer/job/post"));

        // Wait for form to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("job_title")));
    }

    @Test
    public void PostJobLimitReachedTest() throws InterruptedException {

        // -------------------- POST JOB (Limit Reached – Negative) --------------------

        // Job Title
        WebElement jobTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("job_title")));
        jobTitle.click();
        jobTitle.sendKeys("امين مخزن");

        // Click suggested title if visible
        try {
            driver.findElement(By.linkText("امين مخزن (يشترط الخبرة)")).click();
        } catch (Exception ignored) {}

        // Main Work Field
        Select mainWork = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main_work_field_id"))));
        mainWork.selectByVisibleText("تكنولوجيا معلومات واتصالات");

        // Sub Work Field (Select2)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_sub_work_field_id")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_sub_work_field_id"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop') and contains(@class,'select2-drop-active')]//div[contains(text(),'برمجة')]")
        )).click();

        // Number of vacancies = 3
        WebElement vacancies = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("num_of_vacancies")));
        vacancies.click();
        vacancies.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        vacancies.sendKeys(Keys.BACK_SPACE);
        vacancies.sendKeys("3");

        // Country = مصر
        Select countrySel = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("country"))));
        countrySel.selectByVisibleText("مصر");

        // Governorate (Select2) = القاهرة
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_governorate")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_governorate"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='القاهرة']")
        )).click();

        // Area (Select2) = الزيتون
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_area")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_area"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='الزيتون']")
        )).click();

        // Job Type = job_type_2
        try {
            driver.findElement(By.id("job_type_2_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//i[@id='job_type_2_ico']/span")).click();
        }

        // Shift Type = shift_type_0
        try {
            driver.findElement(By.id("shift_type_0_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.id("shift_type_0_ico")).click();
        }

        // Education level = لا يشترط
        WebElement education = wait.until(ExpectedConditions.elementToBeClickable(By.id("education_level")));
        education.click();
        education.sendKeys("لا يشترط");

        // Career level = 8
        try {
            driver.findElement(By.id("career_level_8_lbl")).click();
        } catch (Exception e) {
            driver.findElement(By.id("career_level_8_ico")).click();
        }

        // Salary: min 100, max 300
        WebElement minSalary = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("min_salary")));
        minSalary.click();
        minSalary.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        minSalary.sendKeys(Keys.BACK_SPACE);
        minSalary.sendKeys("100");

        WebElement maxSalary = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("max_salary")));
        maxSalary.click();
        maxSalary.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        maxSalary.sendKeys(Keys.BACK_SPACE);
        maxSalary.sendKeys("300");

        // Special needs toggle (off)
        try {
            driver.findElement(By.xpath("//label[@id='for_special_needs_0_lbl']/span")).click();
        } catch (Exception e) {
            driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
        }

        // Submit
        String beforeSubmitUrl = driver.getCurrentUrl();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("job-post-submit"))).click();

        // Give validation/message time (negative case often stays same page)
        Thread.sleep(2000);

        // -------------------- ASSERTION (NEGATIVE – LIMIT REACHED) --------------------
        // Precondition: account already reached max active jobs.
        // Expectation: system should NOT activate/post new job => still on post page OR shows validation message.
        String afterSubmitUrl = driver.getCurrentUrl();

        Assert.assertTrue(
                afterSubmitUrl.contains("/employer/job/post"),
                "TC Limit Reached failed: Job seems posted/navigated away despite supposed limit. " +
                        "Before: " + beforeSubmitUrl + " | After: " + afterSubmitUrl
        );

        // (Optional) Flexible check: any visible error/alert message on the page
        boolean hasAnyError = driver.findElements(By.cssSelector(".alert, .error, .help-block, .text-danger, .invalid-feedback"))
                .stream().anyMatch(e -> {
                    try {
                        return e.isDisplayed() && e.getText() != null && !e.getText().trim().isEmpty();
                    } catch (Exception ex) {
                        return false;
                    }
                });

        // Don’t hard-fail if their UI doesn’t show a message, but keep it for better reporting:
        System.out.println("Has visible error/alert message? " + hasAnyError);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {

        // ====== IF CONDITIONS IN AFTER METHOD ======
        if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✅ PASSED: " + result.getName());

        } else if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("❌ FAILED: " + result.getName());
            System.out.println("Reason: " + result.getThrowable());

            // Screenshot on failure
            try {
                if (driver != null) {
                    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                    String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    Path outDir = Path.of("screenshots");
                    Files.createDirectories(outDir);

                    Path outFile = outDir.resolve(result.getName() + "_" + ts + ".png");
                    Files.copy(src.toPath(), outFile, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("📸 Screenshot saved to: " + outFile.toAbsolutePath());
                }
            } catch (Exception e) {
                System.out.println("⚠ Could not capture screenshot: " + e.getMessage());
            }

        } else if (result.getStatus() == ITestResult.SKIP) {
            System.out.println("⏭ SKIPPED: " + result.getName());

        } else {
            System.out.println("ℹ️ FINISHED WITH STATUS " + result.getStatus() + ": " + result.getName());
        }

        // Always quit
        try {
            if (driver != null) driver.quit();
        } catch (Exception ignored) {}
    }
}
