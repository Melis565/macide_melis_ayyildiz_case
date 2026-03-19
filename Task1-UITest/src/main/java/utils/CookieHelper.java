package utils;

import driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

import java.time.Duration;

public class CookieHelper extends BasePage {
    @FindBy(css = "#wt-cli-accept-all-btn")
    private WebElement allowCookieButton;

    public void allowCookie() {
        try{
            WebDriverWait shortWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
            WebElement cookieButton = shortWait.until(
                    ExpectedConditions.elementToBeClickable(allowCookieButton)
            );
            cookieButton.click();
        } catch (Exception e) {
            System.out.println("No cookie modal found. Skipping..");
        }
    }
}
