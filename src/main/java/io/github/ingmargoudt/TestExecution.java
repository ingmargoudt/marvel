package io.github.ingmargoudt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Fail;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.engine.TestExecutionResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class TestExecution {
    protected static final Logger logger = LogManager.getLogger();
    public WebDriver webDriver;
    public boolean noBrowser;
    protected String baseURL;
    private static final String chromeDriverSystemvariable = "webdriver.chrome.driver";

    protected TestExecution() {

        String env = System.getProperty("environment", "test");
        URL resource = this.getClass().getClassLoader().getResource(env + "/config.json");
        if (resource == null) {
            logger.warn("There is no config.json found for " + env);
        }

        if (!noBrowser) {
            String path = System.getProperty(chromeDriverSystemvariable);
            if (path == null) {
                Fail.fail(chromeDriverSystemvariable+" is not set, check your pom.xml or the config.json");
            }
            logger.info("starting " + System.getProperty(chromeDriverSystemvariable));

            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            webDriver = new ChromeDriver(options);

            prepareTestData();
        }
    }

    protected void initializeWebDriver(){}



    protected void close() {
        if (webDriver != null) {
            webDriver.quit();
        }
        logger.info("Finishing test " + getClass().getSimpleName());
        logger.info("-------------------------------------------------------");
    }

    protected void takeScreenshot(TestInfo testInfo) {
        File screenshotAs = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(screenshotAs.toPath(), Paths.get("target/" + testInfo.getDisplayName() + ".png"), StandardCopyOption.REPLACE_EXISTING);
            logger.info("screenshot saved to target/" + testInfo.getDisplayName() + ".png");
        } catch (IOException e) {
            logger.info("error during copying screenshot");
            e.printStackTrace();
        }
    }

    protected void dumpDOM() {
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


    public abstract void prepareTestData();

}
