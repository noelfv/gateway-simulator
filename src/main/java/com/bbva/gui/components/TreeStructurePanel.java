package com.bbva.gui.components;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TreeStructurePanel extends JPanel {
    public TreeStructurePanel(String title, JTree externalTree) {
        setLayout(new BorderLayout());
        // Definimos un ancho de 300px. El alto será el 100% de la ventana gracias a MyDoggy
        setPreferredSize(new Dimension(300, 0));

        // Estilo Corporativo BBVA
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 68, 129), 1), title);
        border.setTitleColor(new Color(0, 68, 129));
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        setBorder(border);

        add(new JScrollPane(externalTree), BorderLayout.CENTER);
    }
}