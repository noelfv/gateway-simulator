package com.bbva.gui.components;

import com.bbva.gui.utils.ComponentsUtil;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static com.bbva.gui.utils.ComponentsUtil.BBVA_NAVY;

@Getter
public class OutputTextPanel extends JPanel {

    // Getters para configurar acciones desde fuera
    private final JTextArea textArea;
    private final JButton btnPrimary;
    private final JButton btnSecondary;

    public OutputTextPanel(String title, String primaryBtnText, String secondaryBtnText) {
        setLayout(new BorderLayout());

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), title);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        //setBorder(BorderFactory.createTitledBorder(title));
        setBorder(border);

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

}
