# Task3-ApiTest - Petstore API Testing Suite

A comprehensive API testing framework using Java and JUnit 5 for testing the Swagger Petstore REST API. This project implements complete CRUD operation testing with detailed assertions and error handling scenarios.

## Project Overview

This project contains automated API tests for the Petstore API, specifically validating:
- Pet creation with valid and invalid data
- Retrieving pet information by ID and status
- Updating pet details and form data
- Deleting pets and verifying deletion
- Complete CRUD lifecycle workflows
- Error handling and edge cases
- API response validation

## Tech Stack

- **Language**: Java 25
- **Testing Framework**: JUnit 5 (Jupiter)
- **HTTP Client**: Java HttpClient (java.net.http)
- **JSON Processing**: Jackson (com.fasterxml.jackson)
- **Build Tool**: Maven
- **API Under Test**: Swagger Petstore API


## Setup Instructions

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- Internet connection to access Petstore API
- Git for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Task3-ApiTest
   ```

2. **Verify Java installation**
   ```bash
   java -version
   ```

3. **Verify Maven installation**
   ```bash
   mvn --version
   ```

4. **Install project dependencies**
   ```bash
   mvn clean install
   ```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Tests with Detailed Output
```bash
mvn test -e
```

### Run Specific Test Class
```bash
mvn test -Dtest=PetApiTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=PetApiTest#createPetWithValidDataShouldReturn200
```

### Run Tests with Maven Surefire Report
```bash
mvn clean test surefire-report:report
```

### Run Tests and Generate Test Report
```bash
mvn clean test
# Then view the report
open target/surefire-reports/com.petstore.tests.PetApiTest.txt
```

## Test Execution Order

Tests are ordered using `@Order` annotation via `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`:

1. **Order 1-9**: Sequential CRUD operations on main test pet
2. **Order 10**: Complete lifecycle test with separate pet
3. **Order 11-18**: Error and edge case testing

This ordering ensures:
- Pet creation before retrieval/update/delete
- Dependencies between tests are satisfied
- Error cases don't interfere with happy path tests

## Configuration

### API Endpoint Configuration

Modify in `ApiConfig.java` or `ApiClient.java`:

```java
private static final String BASE_URL = "https://petstore.swagger.io/v2";
private static final int TIMEOUT_SECONDS = 10;
```

## Test Reports

### JUnit Test Report

Located in: `target/surefire-reports/`

**Files**:
- `PetApiTest.txt` - Plain text summary
- `com.petstore.tests.PetApiTest.xml` - JUnit XML format
- `failsafe-summary.xml` - Overall test execution summary

**View report**:
```bash
mvn surefire-report:report
open target/site/surefire-report.html
```

## Dependencies
- JUnit Jupiter API, 5.11.4, Test framework
- Jackson Databind, 2.17.3, JSON processing
- Jackson Core, 2.17.3, JSON core

See `pom.xml` for complete dependency list.

## Useful Commands

```bash
# Clean and build
mvn clean install

# Run only tests
mvn test

# Skip tests during build
mvn clean install -DskipTests

# Run with debug output
mvn test -X

# Run with specific log level
mvn test -o
```

## File Structure Explanation

### Source Files

- **ApiClient.java** - HTTP client wrapper providing GET, POST, PUT, DELETE methods
- **Pet.java** - Model class representing a pet with properties and annotations
- **Category.java** - Model class for pet category
- **Tag.java** - Model class for pet tags
- **User.java** - Model class for user
- **Order.java** - Model class for order
- **ApiConfig.java** - Centralized API configuration and constants


### Test Files

- **PetApiTest.java** - Main test suite with 19 comprehensive test cases
- **StoreApiTest.java** - Main test suite with 11 comprehensive test cases
- **UserApiTest.java** - Main test suite with 18 comprehensive test cases


