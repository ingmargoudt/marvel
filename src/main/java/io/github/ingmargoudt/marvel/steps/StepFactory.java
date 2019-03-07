package io.github.ingmargoudt.marvel.steps;

import io.github.ingmargoudt.marvel.reporting.MarvelReporter;
import io.github.ingmargoudt.marvel.reporting.StepResult;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
@Log4j2
public class StepFactory {


    /**
     * @param clzz
     * @param driver
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clzz, WebDriver driver) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clzz);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.isAnnotationPresent(Step.class)) {
                if (method.getDeclaringClass() != Object.class) {
                    logger.info(method.getDeclaringClass().getSimpleName() + " - " + method.getName() + " " + Arrays.stream(args).filter(p -> p.getClass() != Class.class).map(Object::toString).collect(Collectors.joining(", ")));

                }

            }
            return proxy.invokeSuper(obj, args);
        });
        return clzz.cast(enhancer.create(new Class[]{WebDriver.class}, new Object[]{driver}));
    }

}
