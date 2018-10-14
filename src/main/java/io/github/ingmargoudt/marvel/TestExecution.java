package io.github.ingmargoudt.marvel;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

@Getter
public abstract class TestExecution {
    protected static final Logger logger = LogManager.getLogger();
    protected WebDriver webDriver;
    private boolean noBrowser;
    @Setter
    private String baseURL;

    protected TestExecution() {

        String env = System.getProperty("environment", "test");
        URL resource = this.getClass().getClassLoader().getResource(env + "/config.json");
        if (resource == null) {
            logger.warn("There is no config.json found for " + env);
        }

        if (!noBrowser) {
            defineBrowser();
        }
        prepareTestData();
    }

    private void defineBrowser() {
        Optional<Method> method = Arrays.stream(getClass().getMethods())
                .filter(p -> p.isAnnotationPresent(Browser.class)).findFirst();
        if (method.isPresent()) {
            switch (method.get().getAnnotation(Browser.class).value()) {
                case CHROME:
                    WebDriverManager.chromedriver().setup();

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("start-maximized");
                    webDriver = new ChromeDriver(options);
                    break;
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    webDriver = new FirefoxDriver();
                    break;
                case INTERNET_EXPLORER:
                    WebDriverManager.iedriver().setup();
                    webDriver = new InternetExplorerDriver();
                    break;
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    webDriver = new EdgeDriver();
                    break;
                default:
                    logger.info("no annotation present");

            }

        }

    }

    protected void close() {
        if (webDriver != null) {
            logger.info("Closing down webdriver connection ");
            webDriver.quit();
        }
        logger.info("Finishing test " + getClass().getSimpleName());
        logger.info("-------------------------------------------------------");
    }

    protected void takeScreenshot() {
        if (webDriver == null) {
            logger.warn("Cant make a screenshot , webdriver == null");
            return;
        }
        File screenshotAs = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(screenshotAs.toPath(), Paths.get("target/" + getClass().getSimpleName() + ".png"), StandardCopyOption.REPLACE_EXISTING);
            logger.info("screenshot saved to target/" + getClass().getSimpleName() + ".png");
        } catch (IOException e) {
            logger.info("error during copying screenshot");
            e.printStackTrace();
        }
    }

    protected void dumpDOM() {
        if (webDriver == null) {
            logger.warn("DOM can not be saved, webDriver == null");
            return;
        }
        try {
            Path temp = Files.createFile(Paths.get("target/" + this.getClass().getSimpleName() + "_" + DateTime.now().getMillis() + "_DOM.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp.toFile()));
            bw.write(webDriver.getPageSource());
            logger.info("writing " + temp.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void openURL() {
        webDriver.get(baseURL);
    }

    protected void openURL(String url) {
        webDriver.get(url);
    }


    public abstract void prepareTestData();

}
