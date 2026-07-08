package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class EditJobValidTcs {

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

        // Open "إدارة الوظائف" (Manage Jobs)
        driver.findElement(By.linkText("إدارة الوظائف")).click();
        Thread.sleep(3000);

        // -------------------- JOBS LIST PAGE --------------------
        driver.get("https://forasna.com/employer/job/list-all");
        Thread.sleep(3000);

        // Assert that a job with title "امين مخزن" exists in the table
        boolean jobExists = driver.findElements(
                By.xpath("//table[@id='jobsList']//td[contains(.,'امين مخزن')]")
        ).size() > 0;

        if (jobExists) {
            System.out.println("✅ Job row found in jobs list.");
        } else {
            System.out.println("❌ Job row NOT found in jobs list.");
        }

        // Click the edit icon in the row (same as Katalon: //table[@id='jobsList']/tbody/tr/td[8]/a/i)
        driver.findElement(By.xpath("//table[@id='jobsList']/tbody/tr/td[8]/a/i")).click();
        Thread.sleep(3000);

        // -------------------- EDIT JOB PAGE --------------------
        // (If needed, force-open the edit URL as in Katalon)
        driver.get("https://forasna.com/employer/job/edit/405657");
        Thread.sleep(3000);

        // Change number of vacancies: from 3 → 1
        WebElement vacancies = driver.findElement(By.id("num_of_vacancies"));
        String oldVacancies = vacancies.getAttribute("value");
        System.out.println("Old vacancies value = " + oldVacancies);

        vacancies.clear();
        vacancies.sendKeys("1");
        Thread.sleep(1000);

        // Optional: click job_type_2 label (same as Katalon)
        try {
            driver.findElement(By.xpath("//label[@id='job_type_2_lbl']/span")).click();
        } catch (Exception e) {
            System.out.println("job_type_2_lbl not clickable, continuing...");
        }
        Thread.sleep(1000);

        // Assert submit button exists
        boolean submitPresent = driver.findElements(By.id("submit_edit_form")).size() > 0;
        if (submitPresent) {
            System.out.println("✅ Edit form submit button is present.");
        } else {
            System.out.println("❌ Edit form submit button is NOT present.");
        }

        // Click "Save" (submit_edit_form)
        driver.findElement(By.id("submit_edit_form")).click();
        Thread.sleep(4000);

        // -------------------- POST-CONDITION CHECK --------------------
        // Go back to jobs list and verify vacancies changed to 1
        driver.get("https://forasna.com/employer/job/list-all");
        Thread.sleep(3000);

        // Here we simply read the vacancies cell from the first row
        // (you can adjust the column index if needed)
        String vacanciesText = "";
        try {
            vacanciesText = driver.findElement(
                    By.xpath("//table[@id='jobsList']/tbody/tr[1]/td[4]")
            ).getText().trim();
        } catch (Exception e) {
            System.out.println("Could not read vacancies cell.");
        }

        if ("1".equals(vacanciesText)) {
            System.out.println("✅ TC6 Passed: Job edited successfully, vacancies = 1.");
        } else {
            System.out.println("❌ TC6 Failed: Vacancies not updated correctly. Found: " + vacanciesText);
        }

        // Close any pending-approval modal if it appears (like in Katalon)
        try {
            driver.get("https://forasna.com/job/p/%D8%A7%D9%85%D9%8A%D9%86-%D9%85%D8%AE%D8%B2%D9%86-405657");
            Thread.sleep(3000);
            driver.findElement(By.xpath("//div[@id='company-pending-approval-modal']/div/div/a/i")).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            // If page or modal doesn't exist, just continue.
        }

        driver.quit();
    }
}
