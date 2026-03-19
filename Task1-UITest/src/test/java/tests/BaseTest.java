package tests;

import driver.DriverManager;
import org.junit.jupiter.api.BeforeEach;
import utils.CookieHelper;


public class BaseTest {
    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser","chrome");
        DriverManager.setupDriver(browser);
        CookieHelper cookieHelper = new CookieHelper();
        cookieHelper.allowCookie();
    }
}
