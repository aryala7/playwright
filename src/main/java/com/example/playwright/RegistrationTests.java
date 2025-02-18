package com.example.playwright;

import com.microsoft.playwright.*;

import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrationTests {

    private static final String BASE_URL = "https://ecommerce-playground.lambdatest.io/index.php?route=account/register";

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            testSuccessfulRegistration(page);
            testInvalidMailAddress(page);
            testPasswordSecurityRequirements(page);
        }
    }

    private static void testSuccessfulRegistration(Page page) {
        page.navigate(BASE_URL);
        assertThat(page).hasTitle("Register Account");

        fillRegistrationForm(page, getSaltString() + "@gmail.com");

        page.locator("input#input-password").fill("Password123!");
        page.locator("input#input-confirm").fill("Password123!");

        submitForm(page);

        Locator responseHeader = page.locator("//h1[contains(@class,'page-title')]");
        assertThat(responseHeader).isVisible();
        assertThat(responseHeader).hasText("Your Account Has Been Created!");

        logout(page);
    }

    private static void testInvalidMailAddress(Page page) {
        page.navigate(BASE_URL);
        assertThat(page).hasTitle("Register Account");

        fillRegistrationForm(page, "invalid@com");

        submitForm(page);

        Locator emailError = page.locator("//div[contains(@class,'text-danger') and contains(text(),'E-Mail Address does not appear to be valid!')]");
        assertThat(emailError).isVisible();
    }
    private static void testPasswordSecurityRequirements(Page page) {
        page.navigate(BASE_URL);
        fillRegistrationForm(page, getSaltString() + "@gmail.com");

        testInvalidPassword(page, "123", "123", "Password must be between 4 and 20 characters!");
        testInvalidPassword(page, "Password123", "wordPass123", "Password confirmation does not match password!");
    }

    private static void fillRegistrationForm(Page page, String email) {
        validateInput(page, "First Name", "John", null);
        validateInput(page, "Last Name", "Doe", null);
        validateInput(page, "E-Mail", email, "email");
        validateInput(page, "Telephone", "+4915751820807", "tel");

        page.locator("label[for='input-agree']").click();
    }
    private static void testInvalidPassword(Page page, String password, String confirmPassword, String expectedErrorMessage) {
        page.locator("input#input-password").fill(password);
        page.locator("input#input-confirm").fill(confirmPassword);

        submitForm(page);

        assertThat(page).hasURL(BASE_URL);
        assertThat(page.locator("//div[@class='text-danger']")).isVisible();
        assertThat(page.locator("//div[@class='text-danger']")).hasText(expectedErrorMessage);
    }
    private static void validateInput(Page page, String placeholder, String value, String expectedType) {
        Locator input = page.getByPlaceholder(placeholder);
        assertThat(input).isVisible();
        if (expectedType != null) {
            assertThat(input).hasAttribute("type", expectedType);
        }
        input.fill(value);
    }
    protected static String getSaltString() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        return salt.toString();

    }
    private static void submitForm(Page page) {
        page.locator("//input[@type='submit' and @value='Continue']").click();
    }
    private static void logout(Page page) {
        page.locator("//a[contains(.,'Logout') and @class='list-group-item']").click();
    }
}
