package tests;

import dataproviders.DataProviderDefault;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.NewsletterPage;

import java.util.Collections;
import java.util.Map;

public class WorkShop_Flows extends BaseTest{

    @Test(dataProvider = "Users",dataProviderClass = DataProviderDefault.class)
    public void TryingCommonFlows1(String user,String pass,String var_not_needed) throws InterruptedException {

        CommonFlows.FlowGoToNewsLetter(topbarPage,
                Map.of("user", user,"pass", pass)
        );
        topbarPage.goToHomePage();
        CommonFlows.FlowGoToNewsLetter(topbarPage, Collections.emptyMap());

        Thread.sleep(2000);
    }
}
