package com.bbva.gui.components;


import javax.swing.*;
import java.awt.*;

public class SwingUtils {
    /**
     * Muestra un JPanel dentro de un JInternalFrame centrado en un JDesktopPane.
     *
     * @param desktopPane el JDesktopPane donde se mostrará el JInternalFrame
     * @param panel       el JPanel que se mostrará dentro del JInternalFrame
     * @param titulo      el título del JInternalFrame
     */
    public static void mostrarEnInternalFrame(JDesktopPane desktopPane, JPanel panel, String titulo) {
       // desktopPane.removeAll();

        JInternalFrame internalFrame = new JInternalFrame(titulo, true, true, true, true);
        internalFrame.setContentPane(panel);

        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();

        int frameWidth = (int) (desktopWidth * 0.47);
        int frameHeight = (int) (desktopHeight * 0.8);

        internalFrame.setSize(frameWidth, frameHeight);

        int x = (desktopWidth - frameWidth) / 2;
        int y = (desktopHeight - frameHeight) / 2;
        internalFrame.setLocation(x, y);

        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    public static JInternalFrame mostrarEnInternalFrame2(JDesktopPane desktopPane, JPanel panel, String titulo) {
        JInternalFrame internalFrame = new JInternalFrame(titulo, true, true, true, true);
        internalFrame.setContentPane(panel);
       // internalFrame.setBackground(Color.BLACK);

        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();

        int frameWidth = (int) (desktopWidth * 0.47);
        int frameHeight = (int) (desktopHeight * 0.8);

        internalFrame.setSize(frameWidth, frameHeight);

        int x = (desktopWidth - frameWidth) / 2;
        int y = (desktopHeight - frameHeight) / 2;
        internalFrame.setLocation(x, y);

        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        return internalFrame;
    }



    public static void mostrarTooltipTemporal(JComponent componente, String texto, int duracionMs) {
        componente.setToolTipText(texto);
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().mouseMoved(
                new java.awt.event.MouseEvent(
                        componente, 0, 0, 0,
                        0, 0, // X-Y
                        0, true));
        Timer timer = new Timer(duracionMs, evt -> componente.setToolTipText(null));
        timer.setRepeats(false);
        timer.start();
    }

    public static void crearMenuContextual(JTextArea jTextArea) {
        // Crear el menú contextual
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copiarItem = new JMenuItem("Copiar");
        JMenuItem pegarItem = new JMenuItem("Pegar");

        // Acción de copiar
        copiarItem.addActionListener(e -> jTextArea.copy());
        // Acción de pegar
        pegarItem.addActionListener(e -> jTextArea.paste());

        popupMenu.add(copiarItem);
        popupMenu.add(pegarItem);

        // Mostrar el menú al hacer clic derecho
        jTextArea.setComponentPopupMenu(popupMenu);
    }
}