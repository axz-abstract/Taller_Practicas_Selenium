package tests;
import pages.*;
import java.util.Map;

public class CommonFlows {

    public static NewsletterPage FlowGoToNewsLetter(TopBar topBar, Map<String, Object> parameters){
        if (parameters.containsKey("user")) {
            String user = (String) parameters.get("user"), pass = (String) parameters.get("pass");
            return topBar.goToLoginPage().login(user,pass).goToNewsletter();
        }
        else
            return topBar.goToMyAccount().goToNewsletter();
    }

}
