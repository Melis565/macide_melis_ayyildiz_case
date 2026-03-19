package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class QAPositionsPage extends BasePage {

    @FindBy(css = ".content-wrapper.list-page")
    private WebElement qaPositionsPage;

    @FindBy(css = ".postings-wrapper .posting")
    private List<WebElement> jobsList;

    @FindBy(css ="[aria-label='Filter by Team: Quality Assurance']")
    private WebElement qualityAssuranceDepartmentFilter;

    @FindBy(css = "[aria-label = 'Filter by Location: All']")
    private WebElement jobLocationFilter;

    @FindBy(css = "[aria-label = 'Filter by Location: All'] .filter-popup ul li")
    private List<WebElement> jobLocations;

    @FindBy(css = ".postings-wrapper .posting .posting-apply")
    private List<WebElement> applyButtons;

    @FindBy(css = "[data-qa = 'posting-name']")
    private List<WebElement> positionTitles;

    @FindBy(css = ".postings-btn")
    private List<WebElement> applyForThisJobButtons;

    @FindBy(css = "#btn-submit")
    private WebElement submitApplicationButton;

    public int getJobCount() {
        return jobsList.size();
    }

    public boolean isDepartmentQualityAssuranceSelected() { return isDisplayed(qualityAssuranceDepartmentFilter); }

    public boolean isQAPositionsPageIsVisible() { return isDisplayed(qaPositionsPage); }

    public void clickProvidedLocation(String locationName) {
        wait.until(ExpectedConditions.visibilityOf(jobLocationFilter));
        jobLocationFilter.click();

        WebElement filterPopup = jobLocationFilter.findElement(By.cssSelector(".filter-popup ul"));
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        List<WebElement> links = filterPopup.findElements(By.cssSelector("a.category-link"));

        for (WebElement link : links) {
            System.out.println("link text");
            System.out.println(link.getText());
            System.out.println("locationName");
            System.out.println(locationName);

            System.out.println("-------");
            if (link.getText().contains(locationName)) {
                System.out.println("Istanbul being found!!!");

                ((JavascriptExecutor) driver).executeScript(
                        "var popup = arguments[0].closest('.filter-popup');" +
                                "popup.scrollTop = arguments[0].offsetTop - popup.offsetTop;",
                        link);

                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                jsClick(link);
                return;
            }
        }

        throw new RuntimeException("Location not found: " + locationName);
    }

    public void clickApplyByPositionTitleContaining(String positionFilter) {
        int i = 0;
        for(WebElement job : jobsList){
            WebElement currentPositionTitle = positionTitles.get(i);

            if(currentPositionTitle.getText().contains(positionFilter)) {

                WebElement currentApplyButton = applyButtons.get(i);
                currentApplyButton.click();
                break;
            }

            i++;
        }
    }

    public void clickFirstApplyForThisJobButton() {
        WebElement firstApplyForThisJobButton = applyForThisJobButtons.getFirst();

        waitForVisibility(firstApplyForThisJobButton);
        waitForClickable(firstApplyForThisJobButton);
        firstApplyForThisJobButton.click();
    }

    public boolean checkLeverApplicationFormPage() {
        waitForVisibility(submitApplicationButton);
        waitForClickable(submitApplicationButton);
        return isDisplayed(submitApplicationButton);
    }

}
