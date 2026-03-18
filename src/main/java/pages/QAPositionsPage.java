package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class QAPositionsPage extends BasePage {

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

    public int getJobCount() {
        return jobsList.size();
    }

    public List<String> getPositionTitlesTexts() {
        return positionTitles.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getLocationTexts() {
        return jobLocations.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    // TODO: To be implemented
    public void clickApplyByPositionTitleContaining(String positionFilter) {
        for(WebElement job : jobsList){

        }
    }
}
