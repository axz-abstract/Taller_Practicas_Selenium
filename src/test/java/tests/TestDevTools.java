package tests;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v95.emulation.Emulation;
import org.openqa.selenium.devtools.v95.log.Log;
import org.openqa.selenium.devtools.v95.network.Network;
import org.openqa.selenium.devtools.v95.network.model.BlockedReason;
import org.openqa.selenium.devtools.v95.network.model.ConnectionType;
import org.openqa.selenium.devtools.v95.network.model.ResourceType;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public class TestDevTools extends BaseTest {

    private DevTools devTools = null;

    @BeforeMethod
    public void setDevTools(){
        devTools = ((ChromeDriver)driver).getDevTools();
        devTools.createSession();
    }

    @Test
    public void BlockResourcesRequested(){
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty()));
        List<String> blockedExtensions = ImmutableList.of("*.jpg","*.png","*.css");
        // Block requested resources with a certain extension, see blockedExtensions list
        devTools.send(Network.setBlockedURLs(blockedExtensions));
        devTools.addListener(Network.loadingFailed(), loadingFailed -> {
            if (loadingFailed.getType().equals(ResourceType.STYLESHEET) || loadingFailed.getType().equals(ResourceType.IMAGE)) {
                Assert.assertEquals(loadingFailed.getBlockedReason().get(), BlockedReason.INSPECTOR);
            }
        });
        // Refresh and wait
        driver.navigate().refresh();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SimulateNetworkOffline(){
        // Max buffer set at 1M
        devTools.send(Network.enable(Optional.of(100000000),Optional.empty(),Optional.empty()));
        // Set Network as offline
        devTools.send(Network.emulateNetworkConditions(true,
                100,
                1000,
                2000,
                Optional.of(ConnectionType.CELLULAR3G)));
        devTools.addListener(Network.loadingFailed(), loadingFailed -> Assert.assertEquals(
                loadingFailed.getErrorText(), "net::ERR_INTERNET_DISCONNECTED"));
        // Refresh and wait
        driver.navigate().refresh();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SimulateSlowNetwork(){
        // Enable Slow Network
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty()));
        devTools.send(Network.emulateNetworkConditions(
           false,
           150,
           2500,
           2000,
            Optional.of(ConnectionType.CELLULAR3G)
        ));
        // Timing for slow network
        long startTimeSlow = System.currentTimeMillis();
        driver.get("https://duckduckgo.com/");
        System.out.println(driver.getTitle());
        long endTimeSlow = System.currentTimeMillis();
        System.out.println("Slow page loading under " + (endTimeSlow - startTimeSlow) + " milliseconds");
        try { Thread.sleep(2000);}
        catch (InterruptedException e) {e.printStackTrace();}
    }

    @Test
    public void PrintConsoleLogs(){
        // Enable logs
        devTools.send(Log.enable());
        // Listener for logs
        devTools.addListener(Log.entryAdded(), logEntry -> {
            System.out.println("================");
            System.out.println("Log: " + logEntry.toString());
            System.out.println("Source: " + logEntry.getSource());
            System.out.println("Text: " + logEntry.getText());
            System.out.println("Timestamp: " + logEntry.getTimestamp());
            System.out.println("Level: " + logEntry.getLevel());
        });
        // Load page with console logs and wait
        driver.get("http://the-internet.herokuapp.com/portfolio/");
        try { Thread.sleep(2000);}
        catch (InterruptedException e) {e.printStackTrace();}
    }

    @Test
    public void SimulateChangeLocation(){
        driver.get("https://where-am-i.org/");
        double latitude = 69.6354163, longitude = -42.1736914;
        // Send in new location coordinates
        devTools.send(Emulation.setGeolocationOverride(
                Optional.of(latitude),
                Optional.of(longitude),
                Optional.of(1)));
        // Reload page
        driver.navigate().refresh();
        Assert.assertEquals(driver.findElement(By.id("longitude")).getText(),String.valueOf(longitude));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
