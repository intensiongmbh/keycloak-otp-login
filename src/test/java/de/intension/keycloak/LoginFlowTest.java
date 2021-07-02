package de.intension.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
@TestMethodOrder(OrderAnnotation.class)
class LoginFlowTest extends KeycloakExtensionTestBase
{

    /**
     * GIVEN: user with email
     * WHEN: code is requested (on enter-email.ftl)
     * THEN: email containing code is sent
     * WHEN: code is entered (on enter-code.ftl)
     * THEN: user gets logged in
     * WHEN: sign out button is clicked
     * THEN: user gets signed out
     */
    @Test
    @Order(1)
    void login_logout_flow_test(FirefoxDriver driver)
        throws Exception
    {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        getAccountUrl(driver);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("landingSignInButton")));
        driver.findElementById("landingSignInButton").click();
        String email = "test@test.com";
        wait.until(ExpectedConditions.elementToBeClickable(By.id("sendCode")));
        driver.findElementById("email").sendKeys(email);
        driver.findElementById("sendCode").click();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:1080/email"))
            .header("Content-Type", "application/json").GET().build();
        String response = HttpClient.newBuilder().version(Version.HTTP_1_1).build()
            .send(request, HttpResponse.BodyHandlers.ofString()).body();
        String code = response.substring(response.lastIndexOf("<h3>") + 4, response.lastIndexOf("</h3>"));
        driver.findElementById("codeInput").sendKeys(code);
        TimeUnit.SECONDS.sleep(2); // necessary due to codeActivationDelay
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        driver.findElementById("login").click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("landingSignOutButton")));
        assertEquals("http://localhost:" + keycloak.getHttpPort() + "/auth/realms/" + REALM + "/account/#/",
                     driver.getCurrentUrl());
        driver.findElementById("landingSignOutButton").click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("landingSignInButton")));
        assertEquals(
                     "http://localhost:" + keycloak.getHttpPort() + "/auth/realms/" + REALM + "/account/#/",
                     driver.getCurrentUrl());

    }

    private void getAccountUrl(FirefoxDriver driver)
    {
        driver.get("http://localhost:" + keycloak.getHttpPort() + "/auth/realms/" + REALM + "/account/#/");
    }
}