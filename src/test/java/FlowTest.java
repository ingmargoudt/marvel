import io.github.ingmargoudt.marvel.Flow;
import io.github.ingmargoudt.marvel.steps.BaseSteps;
import io.github.ingmargoudt.marvel.steps.Steps;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class FlowTest {


    @Mock
    WebDriver webDriver;

    @Test
    public void testFlow(){
        webDriver = mock(WebDriver.class);
        SampleFlow s = new SampleFlow(webDriver);
        assertThat(s.sampleSteps).isNotNull();
    }
}
class SampleFlow extends Flow {

    @Steps
    SampleSteps sampleSteps;

    public SampleFlow(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void run() {

    }
}

class SampleSteps extends BaseSteps {
    /**
     * @param webDriver
     */
    protected SampleSteps(WebDriver webDriver) {
        super(webDriver);
    }
}