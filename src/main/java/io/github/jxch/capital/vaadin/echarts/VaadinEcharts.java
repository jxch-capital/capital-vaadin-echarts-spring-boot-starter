package io.github.jxch.capital.vaadin.echarts;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;
import io.github.jxch.capital.vaadin.echarts.config.VaadinEchartsAutoConfig;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Tag("vaadin-echarts")
public class VaadinEcharts extends Div {
    private static String ECHARTS_CDN = null;
    private final String id = UUID.randomUUID().toString();
    @Getter
    private JsonObject option;

    public VaadinEcharts() {
        this(800, 600);
    }

    public VaadinEcharts(int width, int height) {
        super();
        setId(id);
        setWidth(width + "px");
        setHeight(height + "px");

        if (Objects.isNull(ECHARTS_CDN)) {
            ECHARTS_CDN = String.format("https://cdn.jsdelivr.net/npm/echarts@%s/dist/echarts.min.js", VaadinEchartsAutoConfig.getEchartsVersion());
        }
    }

    public void setOption(String standardJson) {
        setOption(Json.parse(standardJson));
    }

    public void setOptionNeedConvert(String jsonOption) {
        String intermediateJson = jsonOption.replaceAll("'", "\"");
        String standardJson = intermediateJson.replaceAll("(\\w+)(\\s*:\\s*)", "\"$1\"$2");
        setOption(Json.parse(standardJson));
    }

    public void setOption(JsonObject option) {
        this.option = option;
        getElement().executeJs(String.format("""
                if (!window.chartInstances) {
                    window.chartInstances = {};
                }
                
                function initializeChart(chartId, chartOption) {
                    var chart = window.chartInstances[chartId];
                    if (chart) {
                        chart.setOption(chartOption);
                    } else {
                        var chartElement = document.getElementById(chartId);
                        if (chartElement) {
                            chart = echarts.init(chartElement);
                            chart.setOption(chartOption);
                            window.chartInstances[chartId] = chart;
                        }
                    }
                }
                
                if (window.echarts) {
                    initializeChart('%1$s', %2$s);
                } else if (!document.querySelector('script[src="%3$s"]')) {
                    var script = document.createElement('script');
                    script.src = '%3$s';

                    script.onload = function() {
                        // 在加载ECharts后立即初始化当前图表
                        initializeChart('%1$s', %2$s);

                        // 初始化其他任何在脚本加载时尚未初始化的图表
                        for (var chartId in window.chartInstances) {
                            if (!window.chartInstances[chartId]) {
                                initializeChart(chartId, window.chartInstances[chartId]);
                            }
                        }
                    };

                    script.onerror = function() {
                        console.error('ECharts script could not be loaded.');
                    };

                    document.body.appendChild(script);
                } else {
                    // if the script is already there but not loaded yet,
                    // we can use event listeners to initialize the chart
                    document.querySelector('script[src="%3$s"]').addEventListener('load', function() {
                        initializeChart('%1$s', %2$s);
                    });
                }
                """, id, option.toJson().replaceAll("(?s)\"\\$\\$(.*?)\\$\\$\"", "$1").replaceAll("\\\"", "'"), ECHARTS_CDN));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        getElement().executeJs("""
                    var chart = window.chartInstances['%1$s'];
                    if (chart) {
                        chart.dispose();
                        window.chartInstances['%1$s'] = undefined;
                    }
                """, id);
    }

}
