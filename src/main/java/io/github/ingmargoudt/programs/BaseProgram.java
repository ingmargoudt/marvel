package io.github.ingmargoudt.programs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;

public abstract class BaseProgram {


    public static int explicit_timeout = 30;
    protected final WebDriver webDriver;


    /**
     *
     * @param webDriver
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
            WebDriverWait wait = new WebDriverWait(webDriver, 1);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<WebElement> get(By by) {
        if (isDisplayed(by)) {
            return Optional.of(webDriver.findElement(by));
        }
        return Optional.empty();
    }
}
