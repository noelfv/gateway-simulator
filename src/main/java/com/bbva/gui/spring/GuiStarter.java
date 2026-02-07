package com.bbva.gui.spring;

import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

//@Component
public class GuiStarter implements ApplicationRunner {

    private final ConfigurableApplicationContext context;// ← Inyectado por Spring
    private BeanProviderInstance beanProviderInstance;

    public GuiStarter(ConfigurableApplicationContext context) {
        this.context = context;
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
            System.out.println("ParserFactory bean obtenido: " + parserFactory);
            //MastercardParserGUI6 gui = new MastercardParserGUI6();
            //ParserGUIMain gui = new ParserGUIMain(beanProviderInstance);
           // gui.setVisible(true);
        });
    }
}