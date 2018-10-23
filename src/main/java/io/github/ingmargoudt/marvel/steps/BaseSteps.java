package io.github.ingmargoudt.marvel.steps;

import io.github.ingmargoudt.marvel.programs.Program;
import io.github.ingmargoudt.marvel.programs.ProgramFactory;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public abstract class BaseSteps {

    private final static String READ_COLOR = "yellow";
    private final static String CLICK_COLOR = "red";
    public static int explicit_timeout = 30;
    private WebElement previousHighlightedElement = null;
    private String previousBackground = "";
    protected final WebDriver driver;


    /**
     * @param webDriver
     */
    protected BaseSteps(WebDriver webDriver) {
        this.driver = webDriver;
        for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Program.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(this, ProgramFactory.create(field.getType(), webDriver));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (field.isAnnotationPresent(Steps.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(this, StepFactory.create(field.getType(), webDriver));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param webElement
     * @return
     */
    public boolean isDisplayed(WebElement webElement) {
        if (webElement == null) {
            return false;
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param webElement
     * @return
     */
    protected boolean isNotDisplayed(WebElement webElement) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.invisibilityOf(webElement));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param webElement
     * @return if the element is not visible, return empty string. If there is a value of the attribute 'value',
     * return it. If there is a value for webElement.getText, return it. Else, return empty string.
     */
    protected String read(WebElement webElement) {
        Objects.requireNonNull(webElement, "parameter of type WebElement can not be null");
        String text;

        try {
            new WebDriverWait(driver, 1).until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception e) {
            return "";
        }
        highlightElement(webElement, READ_COLOR);
        try {
            text = webElement.getAttribute("value");
        } catch (Exception e) {
            text = "";
        }
        if (text != null && !text.isEmpty()) {
            return text;
        }
        try {
            text = webElement.getText();
        } catch (Exception s) {
            text = "";
        }
        if (text != null && !text.isEmpty()) {
            return text;
        }
        return "";
    }

    /**
     * @param webElement
     * @param value
     */
    protected void write(WebElement webElement, int value) {
        write(webElement, String.valueOf(value));
    }


    /**
     * @param webElement
     * @param message
     */
    protected void write(WebElement webElement, String message) {

        sendKeys(webElement, message);

    }

    /**
     * @param webElement A reference to the WebElement
     * @param message    the message or keystrokes
     */
    protected void sendKeys(WebElement webElement, CharSequence message) {
        waitForWebElementToBeVisible(webElement);
        webElement.sendKeys(message);
    }

    /**
     * @param webElement
     */
    protected void submit(WebElement webElement) {
        waitForWebElementToBeVisible(webElement);
        webElement.submit();
    }

    /**
     * @param webElement
     */
    protected void clear(WebElement webElement) {
        new WebDriverWait(driver, explicit_timeout).until(ExpectedConditions.visibilityOf(webElement));
        new WebDriverWait(driver, explicit_timeout).until(ExpectedConditions.elementToBeClickable(webElement));
        webElement.clear();
    }


    /**
     * @param webElement
     */
    protected void clickOn(WebElement webElement) {
        waitForWebElementToBeVisible(webElement);
        highlightElement(webElement, CLICK_COLOR);
        webElement.click();

    }

    /**
     * @param webElement
     */
    protected void clickAndClose(WebElement webElement) {
        clickOn(webElement);

        new WebDriverWait(driver, explicit_timeout).until(ExpectedConditions.invisibilityOf(webElement));

    }


    /**
     * @param element
     * @param color
     */
    private void highlightElement(WebElement element, String color) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        if (previousHighlightedElement != null) {
            try {
                javascriptExecutor.executeScript("arguments[0].style.backgroundColor = '" + previousBackground + "'", previousHighlightedElement);
            } catch (Exception e) {
            }
            previousHighlightedElement = null;
        }

        try {
            previousBackground = element.getCssValue("backgroundColor");
            javascriptExecutor.executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
            previousHighlightedElement = element;
        } catch (Exception e) {
        }

    }


    /**
     * @param webElement a reference to the WebElement to be right clicked on
     */
    protected void rightClickOn(WebElement webElement) {
        new Actions(driver).contextClick(webElement).perform();
    }


    /**
     * @param webElement a reference to the WebElement to be double clicked on
     */
    protected void doubleClick(WebElement webElement) {
        new Actions(driver).doubleClick(webElement).perform();
    }

    /**
     * @param webElement a reference to the WebElement that requires a click with JavaScript
     */
    protected void executeJavascriptClick(WebElement webElement) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", webElement);

    }

    private void waitForWebElementToBeVisible(WebElement webElement) {
        new WebDriverWait(driver, explicit_timeout).until(ExpectedConditions.visibilityOf(webElement));
        new WebDriverWait(driver, explicit_timeout).until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public void dragAndDropJQuery(final By from, final By to) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String script = getDragAndDropScript();
        System.out.println(script);
        String selectorFrom = "#" + from.toString().split(":")[1].trim();
        String selectorTo = "#" + to.toString().split(":")[1].trim();
        executor.executeScript(script + "$('" + selectorFrom + "').simulateDragDrop({ dropTarget: '" + selectorTo + "'});");
    }

    private String getDragAndDropScript() {
        try (InputStream inputStream = BaseSteps.class.getClassLoader().getResourceAsStream("dnd.js")) {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}