package io.github.jxch.capital.vaadin.echarts.util;

public class JsonUtil {

    public static String standard(String json) {
        String intermediateJson = json.replaceAll("'", "\"");
        return intermediateJson.replaceAll("(\\w+)(\\s*:\\s*)", "\"$1\"$2");
    }

}
