import io.github.ingmargoudt.marvel.programs.ProgramFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProgramFactoryTest {
    @Mock
    public WebDriver webDriver;


    @Test
    public void createProgram() {
        webDriver = Mockito.mock(WebDriver.class);
        EmptyProgram program = ProgramFactory.create(EmptyProgram.class, webDriver);
        assertThat(program).isNotNull();
    }

    @Test()
    public void createUnsupportedProgram() {
        webDriver = Mockito.mock(WebDriver.class);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> ProgramFactory.create(Integer.class, webDriver));

    }

    @Test
    public void testNullDriver(){
    assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> ProgramFactory.create(EmptyProgram.class, null));
    }

    @Test
    public void testNullClzz(){
        webDriver = Mockito.mock(WebDriver.class);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProgramFactory.create(null, webDriver));
    }
}
