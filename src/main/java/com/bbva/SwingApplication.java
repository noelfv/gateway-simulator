package com.bbva;

import com.bbva.gui.JavaFXMain;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.bbva")
public class SwingApplication implements ApplicationRunner {

    private final ConfigurableApplicationContext context;

    public SwingApplication(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(SwingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ApplicationContextProvider.setContext(context);

        ParserFactory parserFactory = ApplicationContextProvider.getBean(ParserFactory.class);
        MapperFactory mapperFactory = ApplicationContextProvider.getBean(MapperFactory.class);
        FieldLogicFactory fieldLogicFactory = ApplicationContextProvider.getBean(FieldLogicFactory.class);
        BeanProviderInstance beans = new BeanProviderInstance(parserFactory, mapperFactory, fieldLogicFactory);

        Platform.startup(() -> {
            Stage stage = new Stage();
            new JavaFXMain(beans).start(stage);
            stage.show();
        });
    }
}
