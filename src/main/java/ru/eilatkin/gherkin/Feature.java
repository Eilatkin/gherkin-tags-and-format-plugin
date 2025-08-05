package ru.eilatkin.gherkin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

final class Feature {

    public static final String[] FEATURE = {"Функция","Функциональность","Функционал","Свойство"};
    public static final String[] SCENARIO = {"Пример","Сценарий"};
    public static final String[] SCENARIO_OUTLINE = {"Структура сценария","Шаблон сценария"};
    private static final String COMMENT_MARK = "#";
    private static final String LINE_SEPARATOR_REGEX = "\n";
    private static final String LINE_SEPARATOR = "\n";
    private static final String SEPARATOR = ">";
    private static final String SPACE = " ";
    private static final String EXTERNAL_ID_TAG = "@ExternalId=";
    private static final String DISPALY_NAME_TAG = "@DisplayName=";
    private static final String SCREENSHOT_TAG = "@Screenshot";
    private static final String NESTED_TAG = "@NESTED";
    private static final String SCREENSHOT_STEP_REGEX = ".*эталон.*скриншот.*";


    private String _feature = null;
    private Map<Integer, String> _scenarios = null;
    private List<Integer> _screenshotStepsIndexes = new ArrayList<Integer>();
    private String[] _info = null;

    private Feature(String feature, Map<Integer, String> scenarios, List<Integer> screenshotStepsIndexes, String[] info) {
        _feature = feature;
        _scenarios = scenarios;
        _screenshotStepsIndexes = screenshotStepsIndexes;
        _info = info;
    }

    public String allTags(){
        String[] lines = StringUtils.splitPreserveAllTokens(_feature, LINE_SEPARATOR_REGEX, -1);
        StringBuilder sb = new StringBuilder();
        int i=0;
        for (String line : lines) {
            i++;
            if (_scenarios.containsKey(i)) {
                sb.append("  ").append(DISPALY_NAME_TAG).append(generateDisplayName(i)).append(LINE_SEPARATOR)
                  .append("  ").append(EXTERNAL_ID_TAG).append(generateExternalId(i)).append(LINE_SEPARATOR);
            }
            if (_screenshotStepsIndexes.contains(i)) {
                sb.append("  ").append(SCREENSHOT_TAG).append(LINE_SEPARATOR);
            }
            sb.append(line).append(LINE_SEPARATOR);
        }
        String output = sb.toString();
        return output.substring(0, output.length() - 1);
    }

    private String generateExternalId(int i) {
        String raw = _info[0] + SEPARATOR + generateDisplayName(i);
        return DigestUtils.md5Hex(raw).toUpperCase();
    }

    public String generateDescription() {
        // TODO
        return "@Description=из_комментариев";
    }

//    @DisplayName=Меню_избранное_по_всем_страницам•Добавление_всех_страниц_в_избранное
//    @ExternalId=IJ plugin template•Меню_избранное_по_всем_страницам•Добавление_всех_страниц_в_избранное

    private String generateDisplayName(int i) {
        String output = _info[1].replaceAll("/src/test/resources/","");
        output = output.replaceAll("features/","");
        output = output.replaceAll("\\.feature","");
        output = output.replaceAll("/",SEPARATOR)+SEPARATOR+getScenarioName(_scenarios.get(i));
        return output.replaceAll(" ",SPACE);
    }

    @Override
    public String toString() {
        return generateDescription();
    }

    private static String getScenarioName(String scenario) {
        AtomicReference<String> output = new AtomicReference<>(new String());
        output.set(scenario);
        Stream.concat(Arrays.stream(SCENARIO), Arrays.stream(SCENARIO_OUTLINE)).forEach(keyWord -> output.set(output.get().replaceFirst(keyWord+":", "")));
        return output.get().strip();
    }

    private static String getFeatureName(String feature) {
        AtomicReference<String> output = new AtomicReference<>(new String());
        output.set(feature);
        Arrays.stream(FEATURE).forEach(keyWord -> output.set(output.get().replaceFirst(keyWord+":", "")));
        return output.get().strip();
    }

    public static boolean isFeatureRow(String row) {
        return Arrays.stream(FEATURE).anyMatch(name -> StringUtils.stripToEmpty(row).startsWith(name));
    }
    public static boolean isScenarioRow(String row) {
        return Stream.concat(Arrays.stream(SCENARIO), Arrays.stream(SCENARIO_OUTLINE)).anyMatch(keyWord -> StringUtils.stripToEmpty(row).startsWith(keyWord+":"));
    }
    public static boolean isIgnoredTagRow(String row) {
        return StringUtils.stripToEmpty(row).startsWith(EXTERNAL_ID_TAG)
                || StringUtils.stripToEmpty(row).startsWith(NESTED_TAG);
    }
    public static boolean isScreenshotScenario(String row) {
        return StringUtils.stripToEmpty(row).matches(SCREENSHOT_STEP_REGEX);
    }

    public static Optional<Feature> tryParse(String text, String[] info) {
        String[] lines = StringUtils.splitPreserveAllTokens(text, LINE_SEPARATOR_REGEX, -1);

        String featureName = "";
        String[] parsedPath = info[1].split("/");
        String fileName = parsedPath[parsedPath.length-1].replaceAll("\\.feature","");
        Map<Integer, String> scenarios = new HashMap<>();
        List<Integer> screenshotStepsIndexes = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();
        boolean skipNextScenario = false;
        int skippedScenarioIndex = 0;
        int lastScenarioWithoutTagsIndex = 0;
        int i=0;
        for (String line : lines) {
            i++;
            if (isFeatureRow(line)) {
                featureName = getFeatureName(StringUtils.strip(line));
                if (!fileName.equals(featureName)) line = FEATURE[2]+": " + fileName;
            }
            else if (isIgnoredTagRow(line)) {
                skipNextScenario = true;
            }
            else if (isScenarioRow(line)) {
                if (!scenarios.containsKey(i) && !skipNextScenario) {
                    scenarios.put(i, StringUtils.strip(line));
                    lastScenarioWithoutTagsIndex = i;
                }
                else {
                    skippedScenarioIndex = i;
                    skipNextScenario = false;
                }
            }
            else if (isScreenshotScenario(line) && skippedScenarioIndex<lastScenarioWithoutTagsIndex) {
                screenshotStepsIndexes.add(lastScenarioWithoutTagsIndex);
            }
            sb.append(line).append(LINE_SEPARATOR);
        }

        if (featureName == null) {
            return Optional.empty();
        }

        String output = sb.toString();
        output = output.substring(0, output.length() - 1);

        return Optional.of(new Feature(output, scenarios, screenshotStepsIndexes, info));
    }
}
