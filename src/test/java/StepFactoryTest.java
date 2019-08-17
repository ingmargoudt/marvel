import io.github.ingmargoudt.marvel.steps.BaseSteps;
import io.github.ingmargoudt.marvel.steps.Step;
import io.github.ingmargoudt.marvel.steps.StepFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class StepFactoryTest {

    @Mock
    public WebDriver webDriver;


    @Test
    public void createSteps() {
        webDriver = Mockito.mock(WebDriver.class);
        FakeSteps steps = StepFactory.create(FakeSteps.class, webDriver);
        assertThat(steps).isNotNull();
    }

}

class FakeSteps extends BaseSteps {

    /**
     * @param webDriver
     */
    protected FakeSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Step
    public void fakeStep(){

    }


}