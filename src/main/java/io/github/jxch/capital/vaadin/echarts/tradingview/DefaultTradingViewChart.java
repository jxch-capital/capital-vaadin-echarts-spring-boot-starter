package io.github.jxch.capital.vaadin.echarts.tradingview;

import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.Getter;

public class DefaultTradingViewChart extends Div {
    private TradingViewChart tradingViewChart;
    @Getter
    private JsonObject option = Json.parse("""
                    {
                        "autosize": true,
                        "width": "100%",
                        "height": "100%",
                        "popup_width": "1000",
                        "popup_height": "650",
                        "show_popup_button": true,
                        "enable_publishing": false,
                        "allow_symbol_change": true,
                        "hide_top_toolbar": false,
                        "hide_side_toolbar": false,
                        "details": false,
                        "hotlist": false,
                        "symbol": "NASDAQ:AAPL",
                        "interval": "D",
                        "timezone": "exchange",
                        "style": "1",
                        "theme": "dark",
                        "locale": "zh_CN",
                        "studies": [{
                             "id": "MASimple@tv-basicstudies",
                             "inputs": {"length": 20},
                        }],
                    }
            """);

    public DefaultTradingViewChart() {
        setHeight("100%");
        addTradingViewChart();
    }

    private void addTradingViewChart() {
        tradingViewChart = new TradingViewChart();
        tradingViewChart.setOption(option);
        add(tradingViewChart);
    }

    public void setOption(JsonObject option) {
        this.option = option;
        tradingViewChart.setOption(option);
    }

}
