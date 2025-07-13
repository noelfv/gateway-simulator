package org.example;

import org.example.gui.MastercardParserGUI5;
import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        // Establece el Look and Feel ANTES de crear cualquier componente Swing
        //FlatLightLaf.setup(); // O el tema que prefieras
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                    }

                } catch (Exception e) {
                    // Usar el look and feel por defecto
                }

                MastercardParserGUI5 gui = new MastercardParserGUI5();
                gui.setVisible(true);
            }
        });
    }
}
