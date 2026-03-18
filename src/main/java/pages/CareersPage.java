package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareersPage extends BasePage{

    private static final String CAREERSPAGE_URL = "https://insiderone.com/careers/#open-roles";

    //@FindBy(xpath = "//a[contains(text(),'See all teams')] | //button[contains(text(),'See all teams')]")
    @FindBy(css = ".inso-btn.see-more")
    private WebElement seeAllTeamsButton;

    //@FindBy(xpath = "//a[contains(text(),'Quality Assurance')] | //h3[contains(text(),'Quality Assurance')]")
    @FindBy(css = "[data-department = 'Quality Assurance'] a")
    private WebElement qualityAssuranceLink;

    public CareersPage open() {
        driver.get(CAREERSPAGE_URL);
        return this;
    }

    public CareersPage clickSeeAllTeams(){
        scrollToElement(seeAllTeamsButton);
        jsClick(seeAllTeamsButton);
        return this;
    }

    public QAPositionsPage clickQualityAssurance(){
        scrollToElement(qualityAssuranceLink);
        jsClick(qualityAssuranceLink);
        return new QAPositionsPage();
    }

}
