package io.github.ingmargoudt.marvel;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.ingmargoudt.marvel.reporting.MarvelReporter;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

@Getter
public abstract class TestExecution {
    protected static final Logger logger = LogManager.getLogger();
    protected WebDriver webDriver;
    @Setter
    private String baseURL;
    private Properties properties;

    protected TestExecution() {

        String env = System.getProperty("environment", "test");
        URL resource = this.getClass().getClassLoader().getResource(env + "/config.properties");
        if (resource == null) {
            logger.warn("There is no config.properties found for " + env);
        }
        else{
            try(InputStream is = new FileInputStream(resource.getPath())) {
                properties = new Properties();
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @BeforeEach
    private void setup(TestInfo testInfo) {
        logger.info(testInfo.getDisplayName());
        if(!testInfo.getTags().isEmpty()) {
            logger.info("tags : " + testInfo.getTags());
        }
        if (testInfo.getTestMethod().isPresent()) {
            defineBrowser(testInfo.getTestMethod().get());
        }
        prepareTestData();
    }

    private void defineBrowser(Method method) {
        if(method.isAnnotationPresent(Browser.class)) {
            switch (method.getAnnotation(Browser.class).value()) {
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
                    logger.info("Unknown browser");

            }
        }

    }


    @AfterEach
    private void cooldown(TestInfo testInfo) {
        if (webDriver != null) {
            logger.info("Closing down webdriver connection ");
            webDriver.quit();
        }
        logger.info("Finishing test " + testInfo.getDisplayName());
        logger.info("-------------------------------------------------------");

    }

    @AfterAll
    private static void report(){
        MarvelReporter.generateReport();
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
            BufferedWriter bw = new BufferedWriter(Files.newBufferedWriter(temp, Charset.defaultCharset()));
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
