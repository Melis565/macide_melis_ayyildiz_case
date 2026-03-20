package utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class CookieHelper extends BasePage {
    @FindBy(css = "#wt-cli-accept-all-btn")
    private WebElement allowCookieButton;

    public void allowCookie() {
        try {
            waitForClickable(allowCookieButton);
            allowCookieButton.click();
        } catch (Exception e) {
            System.out.println("No cookie modal found. Skipping..");
        }
    }
}
