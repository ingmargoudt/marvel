package io.github.ingmargoudt.programs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ProgramFactory {


    /**
     * @param clzz      The Program class to instantiate
     * @param webDriver A reference to the WebDriver
     * @param <T>
     * @return an instance of the Program
     */
    public static <T> T create(Class<T> clzz, WebDriver webDriver) {
        if (BaseProgram.class.isAssignableFrom(clzz)) {
            return PageFactory.initElements(webDriver, clzz);
        }

        throw new IllegalArgumentException("Program " + clzz.getSimpleName() + " could not be instantiated as it is does not inherits from BaseProgram");
    }


}
