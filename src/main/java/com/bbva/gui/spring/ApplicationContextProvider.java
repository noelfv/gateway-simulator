package com.bbva.gui.spring;

import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider {

    @Setter
    private static ConfigurableApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException("El contexto de Spring no ha sido inicializado");
        }
        return context.getBean(beanClass);
    }
}