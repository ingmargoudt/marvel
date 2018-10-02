import io.github.ingmargoudt.programs.BaseProgram;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseProgramTest {

    @Mock
    public WebDriver webDriver;



    @Test
    public void TestNullBy() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram p = new EmptyProgram(webDriver);
        assertThat(p.isDisplayed(null)).isFalse();
    }

//    @Test
//    public void cantFindWebElementWithNullBy(){
//        webDriver = Mockito.mock(WebDriver.class);
//        EmptyProgram p = new EmptyProgram(webDriver);
//        assertThat(p.get(null)).isNotPresent();
//    }
//
//
//    @Test
//    public void cantFindElementWithNotexistingBy(){
//        webDriver = Mockito.mock(WebDriver.class);
//        EmptyProgram p = new EmptyProgram(webDriver);
//        assertThat(p.get(By.id("the_id"))).isNotPresent();
//    }
}
