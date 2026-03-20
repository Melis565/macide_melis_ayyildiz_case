package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class QAPositionsPage extends BasePage {

    @FindBy(css = ".content-wrapper.list-page")
    private WebElement qaPositionsPage;

    @FindBy(css = ".postings-wrapper .posting")
    protected List<WebElement> jobsList;

    @FindBy(css = "[aria-label='Filter by Team: Quality Assurance']")
    private WebElement qualityAssuranceDepartmentFilter;

    @FindBy(css = "[aria-label='Filter by Location: Istanbul, Turkiye']")
    private WebElement istanbulLocationFilter;

    @FindBy(css = "[aria-label = 'Filter by Location: All']")
    private WebElement jobLocationFilter;

    @FindBy(css = ".postings-wrapper .posting .posting-apply")
    protected List<WebElement> applyButtons;

    @FindBy(css = "[data-qa = 'posting-name']")
    protected List<WebElement> positionTitles;

    @FindBy(css = ".postings-btn")
    protected List<WebElement> applyForThisJobButtons;

    @FindBy(css = "#btn-submit")
    private WebElement submitApplicationButton;

    public int getJobCount() {
        return jobsList.size();
    }

    public boolean isDepartmentQualityAssuranceSelected() {
        return isDisplayed(qualityAssuranceDepartmentFilter);
    }

    public boolean isQAPositionsPageIsVisible() {
        return isDisplayed(qaPositionsPage);
    }

    public boolean isIstanbulLocationSelected() {
        return isDisplayed(istanbulLocationFilter);
    }

    public void clickProvidedLocation(String locationName) {
        waitForVisibility(jobLocationFilter);
        jobLocationFilter.click();

        WebElement filterPopup = jobLocationFilter.findElement(By.cssSelector(".filter-popup ul"));

        waitForVisibility(filterPopup);

        List<WebElement> links = filterPopup.findElements(By.cssSelector("a.category-link"));

        for (WebElement link : links) {
            if (link.getText().contains(locationName)) {
                ((JavascriptExecutor) driver).executeScript(
                        "var popup = arguments[0].closest('.filter-popup');" +
                                "popup.scrollTop = arguments[0].offsetTop - popup.offsetTop;",
                        link);

                // wait for scroll to locationName dropdown list
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
                jsClick(link);
                return;
            }
        }

        throw new RuntimeException("Location not found: " + locationName);
    }

    public void clickApplyByPositionTitleContaining(String positionFilter) {
        int i = 0;
        for (WebElement _ : jobsList) {
            WebElement currentPositionTitle = positionTitles.get(i);

            if (currentPositionTitle.getText().contains(positionFilter)) {

                WebElement currentApplyButton = applyButtons.get(i);
                currentApplyButton.click();
                break;
            }

            i++;
        }
    }

    public void clickFirstApplyForThisJobButton() {
        waitForAllElementsVisibility(applyForThisJobButtons);
        WebElement firstApplyForThisJobButton = applyForThisJobButtons.getFirst();
        waitForClickable(firstApplyForThisJobButton);
        firstApplyForThisJobButton.click();
    }

    public boolean checkLeverApplicationFormPageIsVisible() {
        waitForVisibility(submitApplicationButton);
        waitForClickable(submitApplicationButton);
        return isDisplayed(submitApplicationButton);
    }

}
