import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseProgramTest {

    @Mock
    public WebDriver webDriver;


    @Test
    public void isDisplayedWithANullBy() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        assertThat(p.isDisplayed(null)).isFalse();
    }

    @Test
    public void cantFindWebElementWithNullBy() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> p.get(null));
    }


    //these test need some rework
    @Test
    public void cantFindElementWithNotexistingBy() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        p.setWait(mock(WebDriverWait.class));
        final ExpectedCondition<WebElement> condition = mock(ExpectedCondition.class);
        when(condition.apply(webDriver))
                .thenThrow(new NoSuchElementException("foo"));

        when(p.getWait().until(condition)).thenThrow(new NoSuchElementException("bar"));
        assertThat(p.get(By.id("the_id"))).isNull();
    }

    @Test
    public void canFindElementWithExistingBy() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        p.setWait(mock(WebDriverWait.class));

        WebElement mockElement = mock(WebElement.class);
        when(p.getWait().until(null)).thenReturn(true);
        when(webDriver.findElement(By.id("the_id"))).thenReturn(mockElement);
        assertThat(p.get(By.id("the_id"))).isEqualTo(mockElement);
    }

    @Test
    public void canFindElements() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        p.setWait(mock(WebDriverWait.class));

        WebElement mockElement = mock(WebElement.class);
        List<WebElement> returns = new ArrayList<>();
        returns.add(mockElement);
        when(p.getWait().until(null)).thenReturn(true);
        when(webDriver.findElements(By.className("the_class"))).thenReturn(returns);
        assertThat(p.getList(By.className("the_class"))).isEqualTo(returns);
    }
}
