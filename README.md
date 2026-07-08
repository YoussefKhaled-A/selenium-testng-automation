# 🧪 PostJob — Selenium Test Automation (TestNG + Cucumber BDD)

An automated **UI test suite** that drives a real browser to test the "Post a Job" flow of a web
application — built with **Selenium WebDriver**, **TestNG**, and **Cucumber (BDD)**.

> 🎓 Academic project (Software Testing, British University in Egypt). Demonstrates browser
> automation, structured test design, and behaviour-driven development.

## What it covers
End-to-end tests of a job-posting web form, including happy paths and validation/edge cases:
- ✅ Post a job with valid data
- ✅ Edit an existing job
- 🚫 Invalid **wage** / invalid **number of employees**
- 🚫 **Missing title**
- 🚫 **Posting limit reached**

Each scenario has a page-driving test class plus a matching TestNG test, with a **Cucumber**
feature/step-definition layer (`StepDefinitions/`, `runner/TestRunner`) for readable BDD specs.

## Tech
`Java` · `Selenium WebDriver` · `TestNG` · `Cucumber (cucumber-java / cucumber-junit)` · `WebDriverManager` · `Maven`

## Run
```bash
mvn test
```
(WebDriverManager auto-provisions the browser driver.)

## Layout
```
src/main/java/org/example/   → page interactions & test-case logic (PostJob*, EditJob*)
src/test/java/org/example/   → TestNG tests
src/test/java/StepDefinitions → Cucumber step definitions
src/test/java/runner         → Cucumber test runner
```

---
### About me
I'm **Youssef Khaled**, a web-scraping & data engineer. The **Selenium browser-automation** skills
here are the same ones I use to scrape JavaScript-rendered sites in my production scrapers — driving a
real browser, waiting on elements, and handling dynamic pages. 📧 youseifkhaled200@gmail.com
