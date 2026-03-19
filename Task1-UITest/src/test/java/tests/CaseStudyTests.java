package tests;

import org.junit.jupiter.api.*;
import pages.CareersPage;
import pages.HomePage;
import pages.QAPositionsPage;
import utils.ScreenshotHelper;
import org.junit.jupiter.api.extension.RegisterExtension;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CaseStudyTests extends BaseTest{

    @RegisterExtension
    ScreenshotHelper screenshotHelper = new ScreenshotHelper();

    @Test
    @Order(1)
    void testHomePageLoadsWithMainBlocks() {
        HomePage homePage = new HomePage().open();

        Assertions.assertTrue(homePage.getCurrentUrl().contains("insiderone.com"),
                "Home page URL should contain insiderone.com");

        Assertions.assertTrue(homePage.isNavbarDisplayed(),
                "Navigation bar should be visible");

        Assertions.assertTrue(homePage.isHeaderMenuDisplayed(),
                "Header menu should be visible");

        Assertions.assertTrue(homePage.isHeroSectionDisplayed(),
                "Hero section should be visible");

        Assertions.assertTrue(homePage.isFooterDisplayed(),
                "Footer should be visible");
    }

    @Test
    @Order(2)
    void testCareersPageQAJobsListIsPresent() {
        CareersPage careersPage = new CareersPage().open();
        careersPage.clickSeeAllTeams();
        QAPositionsPage qaPositionsPage = careersPage.clickQualityAssurance();
        qaPositionsPage.isQAPositionsPageIsVisible();

        Assertions.assertTrue(qaPositionsPage.getJobCount() > 0,
                "Quality Assurance jobs list should not be empty");
    }

    @Test
    @Order(3)
    void testQAJobsContainCorrectDetails() {
        CareersPage careersPage = new CareersPage().open();
        careersPage.clickSeeAllTeams();
        QAPositionsPage qaPositionsPage = careersPage.clickQualityAssurance();
        qaPositionsPage.isQAPositionsPageIsVisible();

        qaPositionsPage.isDepartmentQualityAssuranceSelected();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}

        qaPositionsPage.clickProvidedLocation("Istanbul, Turkiye");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}
        qaPositionsPage.clickApplyByPositionTitleContaining("Quality Assurance");
        qaPositionsPage.clickFirstApplyForThisJobButton();
        Assertions.assertTrue(qaPositionsPage.checkLeverApplicationFormPage(),
                "Lever application submit form is visible");
    }


}
