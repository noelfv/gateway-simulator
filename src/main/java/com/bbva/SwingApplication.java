package com.bbva;

import com.bbva.gui.ParserGUIMain;
import com.bbva.gui.ParserGUIMain2;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import javax.swing.*;

@SpringBootApplication(scanBasePackages = "com.bbva")
public class SwingApplication implements ApplicationRunner {

    private final ConfigurableApplicationContext context;// ← Inyectado por Spring
    private BeanProviderInstance beanProviderInstance;

    public SwingApplication(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(SwingApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 🔥 Desactivar modo headless ANTES de cualquier operación gráfica
        System.setProperty("java.awt.headless", "false");
        // ✅ Guardar el contexto para uso futuro
        ApplicationContextProvider.setContext(context);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Ahora sí, inicia tu GUI original
            ParserFactory parserFactory = ApplicationContextProvider.getBean(ParserFactory.class);
            MapperFactory mapperFactory = ApplicationContextProvider.getBean(MapperFactory.class);
            FieldLogicFactory fieldLogicFactory = ApplicationContextProvider.getBean(FieldLogicFactory.class);
            beanProviderInstance=new BeanProviderInstance(parserFactory,mapperFactory, fieldLogicFactory);
            ParserGUIMain gui = new ParserGUIMain(beanProviderInstance);
            gui.setVisible(true);
        });
    }
}

