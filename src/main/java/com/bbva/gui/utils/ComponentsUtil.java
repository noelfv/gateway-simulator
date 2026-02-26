package com.bbva.gui.utils;

import com.bbva.gui.components.InputTextPanel;

import javax.swing.*;
import java.awt.*;

public class ComponentsUtil {
    // Paleta de colores oficial
    public static final Color BBVA_NAVY = new Color(0, 68, 129);  // Azul principal
    public static final Color BBVA_WHITE = new Color(255, 255, 255);
    public static final Color BBVA_LIGHT_GRAY = new Color(230, 234, 255);
    public static final Color BBVA_ACCENT_BLUE = new Color(18, 190, 255); // Azul brillante para acentos
    public static final Color BBVA_BLACK= new Color(40, 40, 50); // Azul brillante para acentos

    public static JTextArea createInputTextArea() {
        JTextArea inputTextArea = new JTextArea(8, 120);
        inputTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BBVA_ACCENT_BLUE),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        inputTextArea.setBackground(BBVA_WHITE);
        return inputTextArea;
    }

    public static JTextArea createOutputTextArea() {
        JTextArea outputTextArea = new JTextArea(8, 120);
        outputTextArea.setBackground(BBVA_BLACK); // Fondo oscuro tipo terminal
        outputTextArea.setForeground(BBVA_ACCENT_BLUE);      // Letras azul neón
        outputTextArea.setCaretColor(Color.WHITE);
        outputTextArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        return outputTextArea;
    }


    public static JButton createButton(String text) {
        JButton jButton = new JButton(text);
        jButton.setBackground(BBVA_NAVY);
        jButton.setForeground(BBVA_WHITE);
        jButton.setFocusPainted(false);
        jButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        return jButton;
    }

    public static JButton createButton(String text, String tooltip) {
        JButton jButton = new JButton(text);
        //jButton.setIcon(new ImageIcon(getClass().getResource("/icons/copy.png")));
        jButton.setToolTipText(tooltip);
        jButton.setBackground(BBVA_NAVY);
        jButton.setForeground(BBVA_WHITE);
        jButton.setFocusPainted(false);
        jButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        return jButton;
    }


    public static JRadioButton createRadioButton(String text,Boolean selected) {
        // Colores oficiales BBVA
        JRadioButton rb = new JRadioButton(text,selected);
        rb.setBackground(BBVA_WHITE);
        rb.setForeground(BBVA_NAVY);
        rb.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Quitar el foco pintado (el cuadro punteado al hacer clic)
        rb.setFocusPainted(false);

        // Añadir un margen para que resalte
        rb.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        rb.setBorderPainted(true);

        // Cursor tipo mano para indicar interactividad
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tooltip para guiar al usuario
        rb.setToolTipText("Habilitar/Deshabilitar");
        return rb;
    }


    public static JComboBox<InputTextPanel.ComboItem> createDefaultCardComboBox() {
        DefaultComboBoxModel<InputTextPanel.ComboItem> model = new DefaultComboBoxModel<>();
        model.addElement(new InputTextPanel.ComboItem("peer02", "Mastercard"));
        model.addElement(new InputTextPanel.ComboItem("peer01", "Visa"));
        JComboBox<InputTextPanel.ComboItem> combo = new JComboBox<>(model);
        combo.setSelectedIndex(0); // Mastercard visible primero
        return combo;
    }
}
