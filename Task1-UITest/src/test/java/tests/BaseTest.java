package tests;

import driver.DriverManager;
import org.junit.jupiter.api.BeforeEach;


public class BaseTest {
    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");
        DriverManager.setupDriver(browser);
    }
}
