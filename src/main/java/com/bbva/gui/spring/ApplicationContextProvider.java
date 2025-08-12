package com.bbva.gui.spring;

import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextProvider {

    private static ConfigurableApplicationContext context;

    public static void setContext(ConfigurableApplicationContext ctx) {
        context = ctx;
    }

    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException("El contexto de Spring no ha sido inicializado");
        }
        return context.getBean(beanClass);
    }
}