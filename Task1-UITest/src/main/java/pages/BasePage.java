package pages;

import driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage(){
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void jsClick(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            waitForVisibility(element);
            return element.isDisplayed();
        } catch (Exception e){
            return false;
        }
    }

    protected void switchToNewWindow(String originalWindowHandle) {
        wait.until(driver -> {
            Set<String> handles = driver.getWindowHandles();
            return handles.size() > 1 ? handles : null;
        });

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindowHandle)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }

    public String getCurrentUrl() { return driver.getCurrentUrl(); }

    public String getWindowHandle() { return driver.getWindowHandle(); }

    protected void waitForScrollToFinish() {
        wait.until(driver -> {
            Number scrollBefore = (Number) ((JavascriptExecutor) driver)
                    .executeScript("return window.pageYOffset;");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
            Number scrollAfter = (Number) ((JavascriptExecutor) driver)
                    .executeScript("return window.pageYOffset;");
            return scrollBefore.equals(scrollAfter);
        });
    }
}
