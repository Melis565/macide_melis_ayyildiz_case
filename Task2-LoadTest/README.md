# Task2-LoadTest - Load Testing with JMeter

A load testing suite using Apache JMeter to simulate user traffic and measure performance metrics for the e-commerce platform.

## Project Overview

This project contains load testing scenarios for the e-commerce website, specifically validating:
- Search functionality performance under load
- Response times and throughput
- System behavior under concurrent user load
- Performance bottleneck identification

## Tech Stack

- **Tool**: Apache JMeter 5.6+
- **Protocol**: HTTP/HTTPS
- **Scripting**: JMeter Test Plan (.jmx files)
- **Reporting**: JMeter HTML Dashboard
- **Platform**: macOS/Linux

## Setup Instructions

### Prerequisites

- Java 8 or higher installed
- Apache JMeter 5.6+ installed
- 4GB+ RAM available (for load testing)
- Internet connection for accessing n11.com

### Installation

1. **Install Java** (if not already installed)
   ```bash
   java -version
   ```

2. **Download and Install JMeter**
   ```bash
   # Using Homebrew on macOS
   brew install jmeter
   
   # Or download from official source
   # https://jmeter.apache.org/download_jmeter.cgi
   ```

3. **Navigate to project directory**
   ```bash
   cd Task2-LoadTest
   ```

4. **Make JMeter script executable** (if needed)
   ```bash
   chmod +x ./jmeter.sh
   ```

## Running Tests

### Run the Main Load Test

```bash
./jmeter.sh -n -t ./plans/n11_Search_Load_Test.jmx -l ./logs/n11-load-test-logs.jtl -e -o ./n11-test-report
```

## Test Plan Details

### n11_Search_Load_Test.jmx

This test plan simulates load on the website search functionality:

**Test Configuration:**
- **Target URL**: https://www.n11.com/
- **Endpoints**: Search API/Pages
- **Concurrent Users (Threads)**: [Configured in plan]
- **Ramp-up Time**: [Configured in plan]
- **Duration**: [Configured in plan]
- **Request Frequency**: [Configured in plan]

**Key Scenarios:**
1. Home page load
2. Search query execution
3. Results page navigation
4. Product details page load

### Save Test Results in Different Formats

**XML Format:**
```bash
-l ./logs/n11-load-test-logs.xml
```

**JSON Format:**
```bash
-l ./logs/n11-load-test-logs.json
```

## Report Navigation

**Open the generated report:**
   ```bash
   open ./n11-test-report/index.html
   ```
