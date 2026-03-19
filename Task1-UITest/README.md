# Task1-UITest - UI Test Automation

An automated UI testing framework built with Java and Selenium WebDriver for testing the website.

## Project Overview

This project contains automated test cases for the Insider careers page, specifically validating:
- Home page loading and main blocks visibility
- Careers page QA job listings
- QA job details and application form existance

## Tech Stack

- **Language**: Java 25
- **Testing Framework**: JUnit 5 (Jupiter)
- **Web Automation**: Selenium WebDriver 4.27.0
- **Build Tool**: Maven
- **Browser Support**: Chrome, Firefox

## Setup Instructions

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- Chrome or Firefox browser installed
- WebDriver binaries (automatically managed by Selenium)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Task1-UITest
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Tests with Specific Browser
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
```

### Run Specific Test Class
```bash
mvn test -Dtest=CaseStudyTests
```

### Run Specific Test Method
```bash
mvn test -Dtest=CaseStudyTests#testHomePageLoadsWithMainBlocks
```

## Test Cases

### 1. Home Page Validation (`testHomePageLoadsWithMainBlocks`)
- Verifies home page loads correctly
- Checks presence of navbar, header menu, hero section, and footer
- Validates URL contains "insiderone.com"

### 2. QA Job Listings (`testCareersPageQAJobsListIsPresent`)
- Navigates to careers page
- Selects Quality Assurance department
- Confirms job listings are available

### 3. QA Job Application (`testQAJobsContainCorrectDetails`)
- Filters jobs by Quality Assurance department
- Filters by location (Istanbul, Turkiye)
- Applies for a QA position
- Verifies Lever application form is displayed

## Key Components

### Page Object Model (POM)

All page interactions are handled through page classes:

- **[BasePage](src/main/java/pages/BasePage.java)** - Common functionality for all pages
  - Element waiting strategies
  - JavaScript execution
  - Window switching
  - Scroll management

- **[HomePage](src/main/java/pages/HomePage.java)** - Home page interactions

- **[CareersPage](src/main/java/pages/CareersPage.java)** - Careers page interactions

- **[QAPositionsPage](src/main/java/pages/QAPositionsPage.java)** - QA positions filtering and job application

### Driver Management

[DriverManager](src/main/java/driver/DriverManager.java) handles:
- ThreadLocal WebDriver instances for parallel execution
- Browser initialization (Chrome/Firefox)
- Driver cleanup after tests

### Utilities

- **[CookieHelper](src/main/java/utils/CookieHelper.java)** - Automatically accepts website cookies
- **[ScreenshotHelper](src/main/java/utils/ScreenshotHelper.java)** - Captures screenshots on test failures

## Configuration

### Browser Selection
Default browser is Chrome. Change via Maven property or System property:
```bash
mvn test -Dbrowser=firefox
```

### Timeouts
- Explicit wait timeout: 15 seconds
- Cookie modal wait: 3 seconds

Modify in respective page classes if needed.

## Test Reports

Test reports are generated in:
- `target/surefire-reports/` - JUnit reports
- `screenshots/` - Screenshots of passed tests
- `failures_screenshots/` - Screenshots of failed tests

## Dependencies

- Selenium WebDriver, 4.27.0, Browser automation
- JUnit Jupiter, 5.11.4, Test framework

See [pom.xml](pom.xml) for complete dependency list.