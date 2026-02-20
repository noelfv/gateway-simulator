package com.bbva.gui.utils;


import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import static com.bbva.gui.utils.ComponentsUtil.*;

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
        aplicarEstiloBBVA(internalFrame);
        aplicarEstiloDesktop(desktopPane);

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

        aplicarEstiloBBVA(internalFrame);
        aplicarEstiloDesktop(desktopPane);
        internalFrame.setContentPane(panel);

        int desktopWidth = desktopPane.getWidth();
        int desktopHeight = desktopPane.getHeight();

        int frameWidth = (int) (desktopWidth * 0.49);
        int frameHeight = (int) (desktopHeight * 0.8);

        internalFrame.setSize(frameWidth, frameHeight);

        int x = (desktopWidth - frameWidth) / 2;
        int y = (desktopHeight - frameHeight) / 2;
        internalFrame.setLocation(x, y);

        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        return internalFrame;
    }

    private static void aplicarEstiloBBVA(JInternalFrame frame) {
        UIManager.put("InternalFrame.titleAlignment", "LEFT");
        frame.putClientProperty("JInternalFrame.titleAlignment", SwingConstants.LEFT);
        frame.setBackground(BBVA_WHITE);
        frame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        JComponent content = (JComponent) frame.getContentPane();
        content.setBackground(BBVA_WHITE);
        content.setOpaque(true);

        BasicInternalFrameUI ui = (BasicInternalFrameUI) frame.getUI();
        if (ui != null) {
            JComponent titlePane = ui.getNorthPane();
            if (titlePane != null) {
                titlePane.setBackground(BBVA_NAVY);
                titlePane.setForeground(BBVA_WHITE);
                titlePane.setFont(new Font("SansSerif", Font.BOLD, 12));
                titlePane.setOpaque(true);
                //alinearTituloIzquierda(titlePane);
            }
        }
    }

    private static void alinearTituloIzquierda(JComponent titlePane) {
        Component[] comps = titlePane.getComponents();
        JLabel titleLabel = null;
        java.util.List<Component> buttons = new java.util.ArrayList<>();

        for (Component c : comps) {
            if (c instanceof JLabel && titleLabel == null) {
                titleLabel = (JLabel) c;
            } else if (c instanceof AbstractButton) {
                buttons.add(c);
            }
        }

        if (titleLabel == null) {
            titlePane.revalidate();
            titlePane.repaint();
            return;
        }

        titlePane.removeAll();
        titlePane.setLayout(new BorderLayout(4, 0));

        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        titlePane.add(titleLabel, BorderLayout.WEST);

        if (!buttons.isEmpty()) {
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
            buttonsPanel.setOpaque(false);
            for (Component b : buttons) {
                buttonsPanel.add(b);
            }
            titlePane.add(buttonsPanel, BorderLayout.EAST);
        }

        titlePane.revalidate();
        titlePane.repaint();
    }

    private static void aplicarEstiloDesktop(JDesktopPane desktopPane) {
        desktopPane.setBackground(BBVA_WHITE);
        desktopPane.setOpaque(true);
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