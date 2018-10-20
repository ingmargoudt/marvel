package io.github.ingmargoudt.marvel.programs;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class BaseProgram {


    public static int explicit_timeout = 30;
    protected final WebDriver webDriver;
    @Setter
    @Getter
    WebDriverWait wait;


    /**
     * @param webDriver an instance of the WebDriver
     */
    public BaseProgram(WebDriver webDriver) {
        this.webDriver = webDriver;
        wait = new WebDriverWait(webDriver, explicit_timeout);
    }

    /**
     * @param locator
     * @return boolean
     */
    public boolean isDisplayed(By locator) {
        if (locator == null) {
            return false;
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement get(By locator) {
        if (locator == null) {
            throw new NullPointerException("locator can not be null");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return webDriver.findElement(locator);
    }

    public List<WebElement> getList(By locator) {

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        return webDriver.findElements(locator);
    }
}
