package com.bbva.gui.components;

import com.bbva.gui.utils.ComponentsUtil;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static com.bbva.gui.utils.ComponentsUtil.*;

@Getter
public class InputTextPanel extends JPanel {

    // Getters para configurar acciones desde fuera
    private final JTextArea textArea;
    private final JButton btnPrimary;
    private final JButton btnSecondary;
    private JComboBox<ComboItem> comboBox;

    public InputTextPanel(String title, String primaryBtnText, String secondaryBtnText) {
        this(title, primaryBtnText, secondaryBtnText, null);
    }

    public InputTextPanel(String title, String primaryBtnText, String secondaryBtnText, JComboBox<ComboItem> comboBox) {
        setLayout(new BorderLayout());

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), title);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        setBorder(border);
        
        if (comboBox != null) {
            this.comboBox = comboBox;
            this.comboBox.setPreferredSize(new Dimension(180, 24));
            this.comboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
            this.comboBox.setForeground(BBVA_NAVY);
            this.comboBox.setBackground(BBVA_WHITE);
            this.comboBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BBVA_ACCENT_BLUE),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)));

            JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            comboWrapper.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));

            JLabel label = new JLabel("Marca : ");
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(BBVA_NAVY);

            comboWrapper.add(label);
            comboWrapper.add(Box.createHorizontalStrut(6));
            comboWrapper.add(this.comboBox);
            add(comboWrapper, BorderLayout.NORTH);
        }

        // Área de Texto
        textArea = ComponentsUtil.createInputTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPrimary = ComponentsUtil.createButton(primaryBtnText);
        btnSecondary = ComponentsUtil.createButton(secondaryBtnText);

        buttonPanel.add(btnPrimary);
        buttonPanel.add(btnSecondary);
        add(buttonPanel, BorderLayout.SOUTH);
    }



    @Getter
    public static class ComboItem {
        private final String id;
        private final String label;

        public ComboItem(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

}
