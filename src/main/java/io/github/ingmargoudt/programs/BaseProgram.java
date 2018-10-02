package io.github.ingmargoudt.programs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

public abstract class BaseProgram {


    public static int explicit_timeout = 30;
    protected final WebDriver webDriver;


    /**
     *
     * @param webDriver an instance of the WebDriver
     */
    public BaseProgram(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     *
     * @param by
     * @return
     */
    public boolean isDisplayed(By by) {
        if (by == null) {
            return false;
        }
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, explicit_timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement get(By by) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, explicit_timeout);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return webDriver.findElement(by);
    }

    public List<WebElement> getList(By by){

        WebDriverWait webDriverWait = new WebDriverWait(webDriver, explicit_timeout);
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        return webDriver.findElements(by);
    }
}
