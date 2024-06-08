# capital-vaadin-echarts-spring-boot-starter
vaadin 集成 echarts, TradingView

---

## 示例

```xml
        <dependency>
            <groupId>io.github.jxch</groupId>
            <artifactId>capital-vaadin-echarts-spring-boot-starter</artifactId>
            <version>3.2.5-alpha.1.5.2</version>
        </dependency>
```

```java
@Route("test")
public class TestView extends Div {
    public TestView() {
        VaadinEcharts vaadinEcharts = new VaadinEcharts();
        // 非标准json格式（比如单引号、key没用双引号包裹）需要用setOptionNeedConvert先转化为标准json格式
        // 标准json格式直接用setOption即可
        vaadinEcharts.setOptionNeedConvert("""
                {
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                      type: 'shadow'
                    }
                  },
                  grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                  },
                  xAxis: [
                    {
                      type: 'category',
                      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                      axisTick: {
                        alignWithLabel: true
                      }
                    }
                  ],
                  yAxis: [
                    {
                      type: 'value'
                    }
                  ],
                  series: [
                    {
                      name: 'Direct',
                      type: 'bar',
                      barWidth: '60%',
                      data: [10, 52, 200, 334, 390, 330, 220]
                    }
                  ]
                 }""");
        add(vaadinEcharts);
    }
}
```

## ECharts版本依赖

```yaml
vaadin:
  echarts:
    version: 5.5.0
```
