package io.github.ingmargoudt.marvel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrokenLinksChecker {

    public static List<String> findBrokenLinks(WebDriver webDriver) {
        return findThem(webDriver, "a");
    }

    public static List<String> findBrokenImages(WebDriver webDriver) {
        return findThem(webDriver, "img");
    }

    private static List<String> findThem(WebDriver webDriver, String tag) {
        List<String> links = new ArrayList<>();
        for (WebElement webElement : webDriver.findElements(By.tagName(tag))) {
            String link = "";
            if(tag.equals("img")){
                link = webElement.getAttribute("src");
            }
            else {
                link = webElement.getAttribute("href");
            }
            if (link == null || link.isEmpty()) {
                continue;
            }
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(link).openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() > 400) {
                    links.add(link);
                }
                httpURLConnection.getResponseCode();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return links;
    }
}
