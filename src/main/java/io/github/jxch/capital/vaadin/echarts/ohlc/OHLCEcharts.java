package io.github.jxch.capital.vaadin.echarts.ohlc;

import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import io.github.jxch.capital.vaadin.echarts.VaadinEcharts;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class OHLCEcharts extends Div {
    private final VaadinEcharts vaadinEcharts;

    public <T> void setDataset(List<T> data, Function<T, Date> date, Function<T, Number> open, Function<T, Number> close, Function<T, Number> high, Function<T, Number> low, Function<T, Number> volume) {
        JsonObject source = Json.createObject();
        List<?> datasetK =  data.stream().map(item -> List.of(date.apply(item).getTime(), open.apply(item), close.apply(item), low.apply(item), high.apply(item), volume.apply(item))).toList();
        JsonValue dataset = Json.instance().parse(new Gson().toJson(datasetK));
        source.put("source", dataset);
        JsonObject option = vaadinEcharts.getOption();
        option.put("dataset", source);
        vaadinEcharts.setOption(option);
    }

    public OHLCEcharts() {
        this(800, 600);
    }

    public OHLCEcharts(int width, int height) {
        getElement().executeJs("""
                if (!window.echarts_ohlc_tooltip) {
                    window.echarts_ohlc_tooltip = function (params) {
                                     const param = params[0];
                                     const data = param.data;
                                     const date = new Date(Number(data[0])).toLocaleDateString();
                                     const open = data[1].toFixed(2);
                                     const close = data[2].toFixed(2);
                                     const low = data[3].toFixed(2);
                                     const high = data[4].toFixed(2);
                                     const vol = (data[5] / 1000000).toFixed(2);
                                     return `${param.marker} ${date}<br/>
                                         开盘: ${open}<br/>
                                         收盘: ${close}<br/>
                                         最高: ${high}<br/>
                                         最低: ${low}<br/>
                                         成交: ${vol}kk`;
                    }
                }
                """);

        vaadinEcharts = new VaadinEcharts(width, height);
        vaadinEcharts.setOptionNeedConvert("""
                {
                  title: {
                    text: 'QQQ'
                  },
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                      type: 'line'
                    },
                    formatter: '$$function (params) {return window.echarts_ohlc_tooltip(params);}$$'
                  },
                  toolbox: {
                    feature: {
                      dataZoom: {
                        yAxisIndex: false
                      }
                    }
                  },
                  grid: [
                    {
                      left: '10%%',
                      right: '10%%',
                      bottom: 200
                    },
                    {
                      left: '10%%',
                      right: '10%%',
                      height: 80,
                      bottom: 80
                    }
                  ],
                  xAxis: [
                    {
                      type: 'category',
                      boundaryGap: false,
                      axisLine: { onZero: false },
                      splitLine: { show: false },
                      min: 'dataMin',
                      max: 'dataMax',
                      axisLabel: {
                         formatter: '$$function (value) {return new Date(Number(value)).toLocaleDateString();}$$'
                     }
                    },
                    {
                      type: 'category',
                      gridIndex: 1,
                      boundaryGap: false,
                      axisLine: { onZero: false },
                      axisTick: { show: false },
                      splitLine: { show: false },
                      axisLabel: { show: false },
                      min: 'dataMin',
                      max: 'dataMax'
                    }
                  ],
                  yAxis: [
                    {
                      scale: true,
                      splitArea: {
                        show: true
                      }
                    },
                    {
                      scale: true,
                      gridIndex: 1,
                      splitNumber: 2,
                      axisLabel: { show: false },
                      axisLine: { show: false },
                      axisTick: { show: false },
                      splitLine: { show: false }
                    }
                  ],
                  dataZoom: [
                    {
                      type: 'inside',
                      xAxisIndex: [0, 1],
                      start: 10,
                      end: 100
                    },
                    {
                      show: true,
                      xAxisIndex: [0, 1],
                      type: 'slider',
                      bottom: 10,
                      start: 10,
                      end: 100
                    }
                  ],
                  visualMap: {
                    show: false,
                    seriesIndex: 1,
                    dimension: 6,
                    pieces: [
                      {
                        value: 1,
                        color: '#00da3c'
                      },
                      {
                        value: -1,
                        color: '#ec0000'
                      }
                    ]
                  },
                  series: [
                    {
                      type: 'candlestick',
                      itemStyle: {
                        color: '#00da3c',
                        color0: '#ec0000',
                        borderColor: '#008F28',
                        borderColor0: '#8A0000'
                      },
                      encode: {
                        x: 0,
                        y: [1, 2, 3, 4]
                      }
                    },
                    {
                      name: 'Volume',
                      type: 'line',
                      xAxisIndex: 1,
                      yAxisIndex: 1,
                      itemStyle: {
                        color: '#575958'
                      },
                      large: true,
                      encode: {
                        x: 0,
                        y: 5
                      }
                    }
                  ]
                }""");

        add(vaadinEcharts);
    }
}
