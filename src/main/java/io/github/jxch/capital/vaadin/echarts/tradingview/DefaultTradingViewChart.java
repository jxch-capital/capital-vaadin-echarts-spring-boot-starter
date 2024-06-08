package io.github.jxch.capital.vaadin.echarts.tradingview;

import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.Getter;

public class DefaultTradingViewChart extends Div {
    private final TradingViewChart tradingViewChart;
    @Getter
    private JsonObject option = Json.parse("""
                    {
                        "autosize": true,
                        "symbol": "NASDAQ:AAPL",
                        "interval": "D",
                        "timezone": "exchange",
                        "theme": "dark",
                        "style": "1",
                        "locale": "zh_CN",
                        "toolbar_bg": "#f1f3f6",
                        "enable_publishing": false,
                        "allow_symbol_change": true,
                        "details": false,
                        "hide_top_toolbar": false,
                        "save_image": true,
                        "hotlist": false,
                        "calendar": true,
                        "news": [
                            "headlines"
                        ],
                        "show_popup_button": false,
                        "popup_width": "1000",
                        "popup_height": "650",
                        "load_last_chart": true,
                        "hide_side_toolbar": false
                    }
            """);

    public DefaultTradingViewChart() {
        tradingViewChart = new TradingViewChart();
        tradingViewChart.setOption(option);
        add(tradingViewChart);
    }

    public void setOption(JsonObject option) {
        this.option = option;
        tradingViewChart.setOption(option);
    }

}
