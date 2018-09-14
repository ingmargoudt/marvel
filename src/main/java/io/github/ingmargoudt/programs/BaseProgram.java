package io.github.ingmargoudt.programs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseProgram {


    public static int explicit_timeout = 30;
    protected final WebDriver webDriver;

    public BaseProgram(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public boolean isDisplayed(By by) {
        if (by == null) {
            return false;
        }
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 1);
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
}
