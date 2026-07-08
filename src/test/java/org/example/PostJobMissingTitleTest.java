package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * TC4: Post Job Missing Title Test Case
 * This negative test validates that the system correctly rejects
 * a job posting without a job title (required field).
 *
 * Test Steps:
 * 1. Login to the application
 * 2. Navigate to job posting page
 * 3. Fill job details WITHOUT entering a job title
 * 4. Submit the job
 * 5. Verify that job posting is rejected
 *
 * Expected Result: Job should NOT be posted and user stays on posting page
 *
 * @author Test Automation Team
 */
public class PostJobMissingTitleTest {

    // WebDriver instance to be used across all test methods
    private WebDriver driver;

    // Test result flag to track test status
    private boolean testPassed = false;

    /**
     * Setup method that runs before each test method
     * Initializes WebDriver, maximizes browser window
     */
    @BeforeMethod
    public void setUp() {
        System.out.println("==========================================");
        System.out.println("Starting TC4: Post Job Missing Title Test");
        System.out.println("==========================================");

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Maximize browser window for better element visibility
        driver.manage().window().maximize();

        // Set implicit wait for element loading
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println("✓ Browser initialized successfully");
    }

    /**
     * Negative test method for posting a job without job title
     * This method attempts to post a job with empty title field
     *
     * @throws InterruptedException if thread sleep is interrupted
     */
    @Test(priority = 1, description = "Verify that system rejects job posting with missing job title")
    public void testPostJobWithMissingTitle() throws InterruptedException {

        try {
            // ==================== LOGIN SECTION ====================
            System.out.println("\n--- Login Phase ---");
            driver.get("https://forasna.com/");
            Thread.sleep(2000);

            // Click on login button
            WebElement loginButton = driver.findElement(By.xpath("//a[@onclick=\"gtagFN('Top Nav', 'Login','Homepage - Desktop');\"]"));
            loginButton.click();
            Thread.sleep(2000);
            System.out.println("✓ Login page opened");

            // Enter email credentials
            WebElement emailField = driver.findElement(By.id("email"));
            emailField.click();
            emailField.sendKeys("kamoh22341@mucate.com");
            Thread.sleep(1000);
            System.out.println("✓ Email entered");

            // Enter password
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("Youseif200");
            Thread.sleep(1000);
            System.out.println("✓ Password entered");

            // Click submit button to login
            WebElement submitButton = driver.findElement(By.id("btn-submit"));
            submitButton.click();
            Thread.sleep(4000);
            System.out.println("✓ Login successful");

            // ==================== NAVIGATE TO POST JOB PAGE ====================
            System.out.println("\n--- Navigating to Post Job Page ---");
            driver.get("https://forasna.com/employer/job/post");
            Thread.sleep(3000);
            System.out.println("✓ Post job page opened");

            // ==================== FILL JOB DETAILS WITHOUT TITLE ====================
            System.out.println("\n--- Filling Job Details (WITHOUT Job Title) ---");

            // ⚠ INTENTIONALLY NOT filling job title - this is the negative test
            System.out.println("⚠ Skipping Job Title (testing missing required field)");

            // Select Main Work Field
            WebElement mainWorkEl = driver.findElement(By.id("main_work_field_id"));
            Select mainWork = new Select(mainWorkEl);
            mainWork.selectByVisibleText("ادارة واعمال");
            Thread.sleep(1000);
            System.out.println("✓ Main work field selected: Management");

            // Wait for sub work field to be available and select it
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_sub_work_field_id")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_sub_work_field_id"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'select2-drop-active')]//div[contains(text(),'ادارة')]")
            )).click();
            System.out.println("✓ Sub work field selected");

            // Enter Number of Vacancies
            WebElement vacancies = driver.findElement(By.id("num_of_vacancies"));
            vacancies.clear();
            vacancies.sendKeys("2");
            Thread.sleep(1000);
            System.out.println("✓ Number of vacancies entered: 2");

            // Select Country
            Select countrySel = new Select(driver.findElement(By.id("country")));
            countrySel.selectByVisibleText("مصر");
            Thread.sleep(1000);
            System.out.println("✓ Country selected: Egypt");

            // Wait and Select Governorate
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_governorate")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_governorate"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='القاهرة']")
            )).click();
            Thread.sleep(1000);
            System.out.println("✓ Governorate selected: Cairo");

            // Wait and Select Area
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("row_area")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("s2id_area"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'select2-drop-active')]//div[normalize-space()='المعادي']")
            )).click();
            Thread.sleep(500);
            System.out.println("✓ Area selected: Maadi");

            // Select Education Level
            WebElement education = driver.findElement(By.id("education_level"));
            education.click();
            education.sendKeys("دبلوم");
            Thread.sleep(1000);
            System.out.println("✓ Education level selected: Diploma");

            // Select Career Level
            driver.findElement(By.id("career_level_3_ico")).click();
            Thread.sleep(1000);
            System.out.println("✓ Career level selected");

            // Enter Salary Range
            WebElement minSalary = driver.findElement(By.id("min_salary"));
            minSalary.clear();
            minSalary.sendKeys("1500");
            Thread.sleep(500);

            WebElement maxSalary = driver.findElement(By.id("max_salary"));
            maxSalary.clear();
            maxSalary.sendKeys("3500");
            Thread.sleep(500);
            System.out.println("✓ Salary range entered: 1500-3500");

            // Select Job Type
            driver.findElement(By.xpath("//i[@id='job_type_1_ico']/span")).click();
            Thread.sleep(500);
            System.out.println("✓ Job type selected");

            // Select Shift Type
            driver.findElement(By.id("shift_type_0_ico")).click();
            Thread.sleep(500);
            System.out.println("✓ Shift type selected");

            // Toggle Special Needs
            driver.findElement(By.xpath("//i[@id='for_special_needs_0_ico']/span")).click();
            Thread.sleep(500);
            System.out.println("✓ Special needs option selected");

            // Submit the job posting
            System.out.println("\n--- Attempting to Submit Job (should be rejected due to missing title) ---");
            driver.findElement(By.id("job-post-submit")).click();
            Thread.sleep(3000);

            // ==================== RESULT VERIFICATION (NEGATIVE TEST) ====================
            System.out.println("\n--- Verifying Result (Negative Test) ---");
            String currentUrl = driver.getCurrentUrl();

            // For negative test: we EXPECT the job to stay on the same page
            // because job title is a required field
            if (currentUrl.contains("/employer/job/post")) {
                testPassed = true;
                System.out.println("✅ TEST PASSED: Job posting was correctly REJECTED!");
                System.out.println("User remained on post job page (missing required field: job title)");
                System.out.println("Current URL: " + currentUrl);
                Assert.assertTrue(true, "Job correctly rejected due to missing title");
            } else {
                testPassed = false;
                System.out.println("❌ TEST FAILED: System accepted job posting without title!");
                System.out.println("Form was submitted despite missing required field");
                System.out.println("Current URL: " + currentUrl);
                Assert.fail("System incorrectly accepted job posting without title");
            }

        } catch (Exception e) {
            testPassed = false;
            System.out.println("❌ TEST FAILED with exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    /**
     * Teardown method that runs after each test method
     * Closes browser and prints test summary
     * Uses if conditions to check test status before cleanup
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("\n==========================================");
        System.out.println("TC4: Post Job Missing Title Test - Cleanup Phase");
        System.out.println("==========================================");

        // Check if driver is initialized before cleanup
        if (driver != null) {
            // Take screenshot if test failed
            if (!testPassed) {
                System.out.println("⚠ Test did not pass. Cleaning up...");
                // Here you could add screenshot capture logic
            } else {
                System.out.println("✓ Test passed successfully. Cleaning up...");
            }

            // Close the browser
            driver.quit();
            System.out.println("✓ Browser closed");
        } else {
            System.out.println("⚠ Driver was null, no cleanup needed");
        }

        // Print final test status
        if (testPassed) {
            System.out.println("\n✅ FINAL RESULT: TC4 - PASSED");
        } else {
            System.out.println("\n❌ FINAL RESULT: TC4 - FAILED");
        }

        System.out.println("==========================================\n");
    }
}
