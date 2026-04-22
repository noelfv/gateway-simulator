package com.bbva.gui.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider {

    private static volatile ConfigurableApplicationContext context;

    public static void setContext(ConfigurableApplicationContext ctx) {
        context = ctx;
    }

    public static <T> T getBean(Class<T> beanClass) {
        ConfigurableApplicationContext ctx = context;
        if (ctx == null) {
            throw new IllegalStateException("Spring context not initialized — ensure SwingApplication sets the context before UI starts");
        }
        return ctx.getBean(beanClass);
    }
}