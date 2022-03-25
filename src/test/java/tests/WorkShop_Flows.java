package tests;

import dataproviders.DataProviderDefault;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.NewsletterPage;
import utils.WebDriverUtils;

import java.util.Collections;
import java.util.Map;

public class WorkShop_Flows extends BaseTest{

    @Test(dataProvider = "Users",dataProviderClass = DataProviderDefault.class)
    public void TryingCommonFlows1(String user,String pass,String var_not_needed) throws InterruptedException {

        NewsletterPage news_page =CommonFlows.FlowGoToNewsLetter(topbarPage,
                Map.of("user", user,"pass", pass)
        );
        WebDriverUtils.highlight(driver.findElement(news_page.getPageLoadedLocator()), driver);

        topbarPage.goToHomePage();
        WebDriverUtils.highlight(driver.findElement(topbarPage.getPageLoadedLocator()), driver);

        CommonFlows.FlowGoToNewsLetter(topbarPage, Collections.emptyMap());
        WebDriverUtils.highlight(driver.findElement(news_page.getPageLoadedLocator()), driver);
    }
}
