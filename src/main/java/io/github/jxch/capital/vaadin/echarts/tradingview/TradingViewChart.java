package io.github.jxch.capital.vaadin.echarts.tradingview;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.Getter;

import java.util.UUID;

@Tag("div")
public class TradingViewChart extends Div {
    private final static String TRADING_VIEW_SRC = "https://s3.tradingview.com/tv.js";
    private final String id = UUID.randomUUID().toString();
    @Getter
    private JsonObject option;

    public TradingViewChart() {
        this(800, 600);
    }

    public TradingViewChart(int width, int height) {
        super();
        setId(id);
        setWidth(width + "px");
        setHeight(height + "px");
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
        this.option.put("container_id", id);
        getElement().executeJs(String.format("""
                if (!window.tradingView) {
                    window.tradingView = {};
                }
                
                function initializeTradingView(chartId, option) {
                    const widget = new TradingView.widget(option);
                    window.tradingView[chartId] = widget;
                }
                
                if (window.TradingView) {
                    initializeTradingView('%1$s', %2$s);
                } else if (!document.querySelector('script[src="%3$s"]')) {
                    var script = document.createElement('script');
                    script.src = '%3$s';

                    script.onload = function() {
                        initializeTradingView('%1$s', %2$s);
                    };

                    script.onerror = function() {
                        console.error('ECharts script could not be loaded.');
                    };

                    document.body.appendChild(script);
                } else {
                    document.querySelector('script[src="%3$s"]').addEventListener('load', function() {
                        initializeTradingView('%1$s', %2$s);
                    });
                }
                """,id, option.toJson(), TRADING_VIEW_SRC, getHeight(), getWidth()));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        getElement().executeJs("""
                    var chart = window.tradingView['%1$s'];
                    if (chart) {
                        window.tradingView['%1$s'] = undefined;
                    }
                """, id);
    }

}
