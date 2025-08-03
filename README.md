# PlayerController API Test Suite 🧪

Automated API tests for the `/player` service using **TestNG**, **Rest-Assured**, **Allure**, and **Maven**.

---

## ✅ Features

- Test coverage for:
  - Create Player
  - Update Player
  - Delete Player
  - Get Player
  - Role-based Access Control
- Logging (SLF4J)
- Allure Reporting
- Parallel execution support (3 threads)
- Thread-safe ID cleanup via `ThreadLocal`

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/dastanshokimov/playercontroller
cd api-tests
```
2. Run tests
Make sure you have Java and Maven installed.

```bash
mvn clean test
```
⚠️ If you're behind a corporate proxy or restricted network, make sure Maven can access public repositories.

📦 Tech Stack 
* Java 17+ 
* TestNG 
* Rest-Assured 
* Maven Surefire Plugin 
* Allure 
* SLF4J

🧪 Parallel Execution
Parallel test execution is enabled in testng.xml:

```xml
<suite name="TestSuite" parallel="classes" thread-count="3">
```
📊 Allure Report
To generate the Allure report:

```bash
allure serve target/allure-results