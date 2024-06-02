package io.github.jxch.capital.vaadin.echarts.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class VaadinEchartsAutoConfig implements ApplicationContextAware {
    @Value("${vaadin.echarts.version:5.5.0}")
    private String echartsVersion;

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static String getEchartsVersion() {
        return context.getBean(VaadinEchartsAutoConfig.class).echartsVersion;
    }

}
