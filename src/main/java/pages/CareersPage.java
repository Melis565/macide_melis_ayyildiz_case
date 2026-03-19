package pages;

import driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CareersPage extends BasePage{

    private static final String CAREERSPAGE_URL = "https://insiderone.com/careers/#open-roles";

    @FindBy(css = ".inso-btn.see-more")
    private WebElement seeAllTeamsButton;

    @FindBy(css = "[data-department = 'Quality Assurance'] a")
    private WebElement qualityAssuranceLink;

    WebDriverWait shortWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));

    public CareersPage open() {
        driver.get(CAREERSPAGE_URL);
        waitForScrollToFinish();
        return this;
    }

    public CareersPage clickSeeAllTeams(){
        scrollToElement(seeAllTeamsButton);
        jsClick(seeAllTeamsButton);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}

        scrollToElement(qualityAssuranceLink);

        return this;
    }

    public QAPositionsPage clickQualityAssurance(){
        scrollToElement(qualityAssuranceLink);
        waitForVisibility(qualityAssuranceLink);
        jsClick(qualityAssuranceLink);
        return new QAPositionsPage();
    }

}
