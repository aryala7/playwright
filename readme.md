# Playwright Registration Tests

This project is a Java-based application that uses Spring Boot and Playwright to perform automated tests on the registration functionality of an e-commerce website.

## Project Structure

- `src/main/java/com/example/playwright/PlaywrightApplication.java`: The main entry point for the Spring Boot application.
- `src/main/java/com/example/playwright/RegistrationTests.java`: Contains the Playwright tests for the registration functionality.
- `.gitignore`: Specifies files and directories to be ignored by Git.

## Prerequisites

- Java 11 or higher
- Maven
- Node.js and npm (for Playwright)

## Setup

1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. Install Playwright:
    ```sh
    npm install -D @playwright/test
    ```

3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Running the Tests

To run the Playwright tests, execute the `RegistrationTests` class:

```sh
mvn exec:java -Dexec.mainClass="com.example.playwright.RegistrationTests"