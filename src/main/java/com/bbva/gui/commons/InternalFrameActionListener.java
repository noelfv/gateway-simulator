package com.bbva.gui.commons;

import com.bbva.gui.components.SwingUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InternalFrameActionListener implements ActionListener {
    private final JDesktopPane desktopPane;
    private final JPanel panel;
    private final String titulo;
    private JInternalFrame internalFrame;

    /*static {
        UIManager.put("InternalFrame.activeTitleBackground", new Color(0, 120, 215));
        UIManager.put("InternalFrame.activeTitleForeground", Color.WHITE);
        UIManager.put("InternalFrame.inactiveTitleBackground", new Color(200, 200, 200));
    }*/
    public InternalFrameActionListener(JDesktopPane desktopPane, JPanel panel, String titulo) {
        this.desktopPane = desktopPane;
        this.panel = panel;
        this.titulo = titulo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (internalFrame == null || internalFrame.isClosed()) {
            System.out.println("Creando nuevo JInternalFrame");
            internalFrame=SwingUtils.mostrarEnInternalFrame2(desktopPane, panel, titulo);
            internalFrame.setBorder(BorderFactory.createLineBorder(new Color(0,120,215),3));
            //internalFrame.setBackground(Color.BLACK);
            desktopPane.revalidate();
            desktopPane.repaint();
        } else {
            try {
                System.out.println("Mostrando JInternalFrame existente");
                internalFrame.setIcon(false);
                internalFrame.setSelected(true);
                internalFrame.toFront();
                desktopPane.revalidate();
                desktopPane.repaint();
            } catch (Exception ex) {
                // Manejo de excepción
            }
        }
    }
}