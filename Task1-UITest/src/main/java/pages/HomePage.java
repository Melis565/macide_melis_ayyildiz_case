package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    private static final String HOME_URL = "https://insiderone.com/";

    @FindBy(css = ".header-top-menu")
    private WebElement navbar;

    @FindBy(css = ".header-wrapper")
    private WebElement headerMenu;

    @FindBy(css = ".homepage-hero")
    private WebElement heroSection;

    @FindBy(tagName = "footer")
    private WebElement footer;

    public HomePage open() {
        driver.get(HOME_URL);
        return this;
    }

    public boolean isNavbarDisplayed() {
        return isDisplayed(navbar);
    }

    public boolean isHeaderMenuDisplayed() {
        return isDisplayed(headerMenu);
    }

    public boolean isHeroSectionDisplayed() {
        return isDisplayed(heroSection);
    }

    public boolean isFooterDisplayed() {
        return isDisplayed(footer);
    }
}
