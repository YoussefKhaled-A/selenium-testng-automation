package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import java.util.List;

public class EditJobValidTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // ====== Test Data (same as your script) ======
    private final String baseUrl = "https://forasna.com/";
    private final String email = "kamoh22341@mucate.com";
    private final String password = "Youseif200";

    private final String jobsListUrl = "https://forasna.com/employer/job/list-all";
    private final String editJobUrl = "https://forasna.com/employer/job/edit/405657";
    private final String jobPublicUrl = "https://forasna.com/job/p/%D8%A7%D9%85%D9%8A%D9%86-%D9%85%D8%AE%D8%B2%D9%86-405657";

    private final String jobTitleToCheck = "امين مخزن";
    private final String expectedVacancies = "1";

    @BeforeMethod
    public void beforeMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // -------- LOGIN --------
        driver.get(baseUrl);

        By loginBtn = By.xpath("//a[@onclick=\"gtagFN('Top Nav', 'Login','Homepage - Desktop');\"]");
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();

        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailInput.click();
        emailInput.sendKeys(email);

        WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passInput.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-submit"))).click();

        // wait until employer area loads
        wait.until(ExpectedConditions.urlContains("/employer/"));

        // -------- Go to Jobs List --------
        driver.get("https://forasna.com/employer/dashboard");
        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Open "إدارة الوظائف" (Manage Jobs)
        // (kept close to your script; sometimes partialLinkText is more stable than linkText)
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("إدارة الوظائف"))).click();

        driver.get(jobsListUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jobsList")));
    }

    @Test
    public void EditJobValidTest() {
        // -------- Assert job exists by title in table --------
        List<WebElement> matches = driver.findElements(
                By.xpath("//table[@id='jobsList']//td[contains(.,'" + jobTitleToCheck + "')]")
        );
        Assert.assertTrue(matches.size() > 0, "Job title not found in jobs list: " + jobTitleToCheck);

        // -------- Click Edit icon (first row) --------
        By editIcon = By.xpath("//table[@id='jobsList']/tbody/tr[1]/td[8]/a/i");
        wait.until(ExpectedConditions.elementToBeClickable(editIcon)).click();

        // Force open edit URL (same as your current script)
        driver.get(editJobUrl);
        wait.until(ExpectedConditions.urlContains("/employer/job/edit/"));

        // -------- Edit vacancies to 1 --------
        WebElement vacancies = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("num_of_vacancies")));
        String oldVacancies = vacancies.getAttribute("value");

        vacancies.click();
        vacancies.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        vacancies.sendKeys(Keys.BACK_SPACE);
        vacancies.sendKeys(expectedVacancies);

        // Optional: click job_type_2 label (same as your script)
        try {
            WebElement jobType2 = driver.findElement(By.xpath("//label[@id='job_type_2_lbl']/span"));
            if (jobType2.isDisplayed()) {
                jobType2.click();
            }
        } catch (Exception ignored) {
            // optional step, do nothing
        }

        // Assert submit button exists + click save
        WebElement submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit_edit_form")));
        Assert.assertTrue(submit.isEnabled(), "Submit button is not enabled on edit page.");
        submit.click();

        // -------- Post-condition: verify vacancies on list page --------
        driver.get(jobsListUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jobsList")));

        String vacanciesText = "";
        try {
            vacanciesText = driver.findElement(By.xpath("//table[@id='jobsList']/tbody/tr[1]/td[4]"))
                    .getText().trim();
        } catch (Exception ignored) {}

        // (Sometimes the cell might contain extra text; so we accept "contains 1")
        boolean updated = vacanciesText.equals(expectedVacancies) || vacanciesText.contains(expectedVacancies);

        Assert.assertTrue(
                updated,
                "Vacancies not updated correctly. Found: [" + vacanciesText + "] | Old value was: [" + oldVacancies + "]"
        );

        // Optional: close pending-approval modal if it appears
        try {
            driver.get(jobPublicUrl);
            Thread.sleep(1500);
            WebElement closeModal = driver.findElement(
                    By.xpath("//div[@id='company-pending-approval-modal']/div/div/a/i")
            );
            if (closeModal.isDisplayed()) closeModal.click();
        } catch (Exception ignored) {
            // modal may not appear
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        // ====== IF CONDITIONS (as you asked) ======
        if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✅ PASSED: " + result.getName());
        } else if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("❌ FAILED: " + result.getName());
            System.out.println("Reason: " + result.getThrowable());

            // Take screenshot on failure
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

        // Always quit driver
        try {
            if (driver != null) driver.quit();
        } catch (Exception ignored) {}
    }
}
