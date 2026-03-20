package utils;

import driver.DriverManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotHelper implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        takeFailedScreenshot(context);
        DriverManager.quitDriver();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        DriverManager.quitDriver();
    }

    private void takeFailedScreenshot(ExtensionContext context) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            System.err.println("Screenshot skipped: driver is already null");
            return;
        }

        try {
            File srcFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new java.util.Date());
            Path destPath = Paths.get("failures_screenshots",
                    context.getDisplayName() + "_FAILED_" + timestamp + ".png");
            Files.createDirectories(destPath.getParent());
            Files.copy(srcFile.toPath(), destPath);
            System.out.println("Failure screenshot: " + destPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }
}