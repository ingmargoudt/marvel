package io.github.ingmargoudt.programs;

import org.assertj.core.api.Fail;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ProgramFactory {


    public static <T> T create(Class<T> clzz, WebDriver webDriver) {
        if (BaseProgram.class.isAssignableFrom(clzz)) {
            return PageFactory.initElements(webDriver, clzz);
        } else {
            Fail.fail("Program " + clzz.getSimpleName() + " could not be instantiated as it is does not inherits from BaseProgram");
        }

        throw new IllegalArgumentException();
    }


}
