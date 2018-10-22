package io.github.ingmargoudt.marvel.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarvelReporter {

    private static Map<String, List<StepResult>> results = new HashMap<>();

    public static void logStep(String testName, StepResult stepResult) {
        List<StepResult> steps = results.getOrDefault(testName, new ArrayList<>());
        steps.add(stepResult);
        results.put(testName, steps);


    }

    public static void generateReport() {

    }
}
