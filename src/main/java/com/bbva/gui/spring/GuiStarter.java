package com.bbva.gui.spring;

import com.bbva.gui.components.views.MastercardParserGUI6;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import javax.swing.*;

@Component
public class GuiStarter implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context; // ← Inyectado por Spring

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
            MastercardParserGUI6 gui = new MastercardParserGUI6();
            gui.setVisible(true);
        });
    }
}