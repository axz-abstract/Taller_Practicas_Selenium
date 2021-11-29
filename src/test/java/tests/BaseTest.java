package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LaptopsPage;
import pages.TopBar;
import utils.WebDriverUtils;

import java.util.List;

public class BaseTest {

    protected WebDriver driver = null;

    @BeforeSuite
    public void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        System.out.println("Downloading required driver...");
    }

    @BeforeMethod
    public void beforeTest() {
        System.out.println("Creating instance of WebDriver");
        driver = RemoteWebDriver.builder().oneOf(new ChromeOptions()).build();
        driver.manage().window().maximize();
        driver.get(getMainUrl());
    }

    protected String getMainUrl() {
        return "http://opencart.abstracta.us";
    }

    @AfterMethod
    public void afterTest()  {
        this.driver.quit();
    }


}

