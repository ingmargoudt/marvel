package io.github.ingmargoudt;

import io.github.ingmargoudt.steps.StepFactory;
import io.github.ingmargoudt.steps.Steps;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;

public abstract class Flow {


    protected WebDriver webDriver = null;

    public Flow() {
        getSteps();
    }

    public Flow(WebDriver webDriver) {
        this();
        this.webDriver = webDriver;
    }

    protected void getSteps() {
        for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Steps.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(this, StepFactory.create(field.getType(), webDriver));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
