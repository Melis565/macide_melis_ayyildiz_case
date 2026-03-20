package pages;

import driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CareersPage extends BasePage {

    private static final String CAREERS_PAGE_URL = "https://insiderone.com/careers/#open-roles";
    private static final long ANIMATION_WAIT_DURATION = 30L * 1000L;
    private static final long QUALITY_ASSURANCE_POSITION_BUTTON_WAIT_DURATION = 120L * 1000L;

    @FindBy(css = ".inso-btn.see-more")
    private WebElement seeAllTeamsButton;

    @FindBy(css = "[data-department = 'Quality Assurance']")
    private WebElement qualityAssurancePositionCard;

    @FindBy(css = "[data-department = 'Quality Assurance'] a")
    private WebElement qualityAssuranceLink;

    public void waitForScrollAnimationToFinish(WebElement element) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ANIMATION_WAIT_DURATION));
        wait.until(_ -> {
            int initialY = element.getLocation().getY();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int finalY = element.getLocation().getY();
            return initialY == finalY;
        });
    }

    public CareersPage open() {
        driver.get(CAREERS_PAGE_URL);
        return this;
    }

    public void clickSeeAllTeams() {
        waitForScrollAnimationToFinish(seeAllTeamsButton);
        scrollToElement(seeAllTeamsButton);
        jsClick(seeAllTeamsButton);
    }

    public QAPositionsPage clickQualityAssurance() {
        waitForScrollAnimationToFinish(qualityAssuranceLink);
        scrollToElement(qualityAssuranceLink);
        waitForScrollAnimationToFinish(qualityAssuranceLink);
        waitForVisibility(qualityAssuranceLink);

        try {
            WebDriverWait elementWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(QUALITY_ASSURANCE_POSITION_BUTTON_WAIT_DURATION));
            elementWait.until(
                    ExpectedConditions.not(
                            ExpectedConditions.textToBePresentInElement(qualityAssurancePositionCard, "0 Open Positions")
                    )
            );

            jsClick(qualityAssuranceLink);
        } catch (Exception e) {
            throw new RuntimeException("No open position found! Tests will be ended...");
        }

        return new QAPositionsPage();
    }

}
