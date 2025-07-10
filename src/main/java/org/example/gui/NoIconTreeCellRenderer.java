package org.example.gui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

class NoIconTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        // Llama al método de la superclase para manejar el renderizado básico (texto, selección)
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        // Establece el icono a null para que no se muestre ningún icono
        setIcon(null);

        // Opcional: Podrías cambiar el texto si lo necesitas, pero por defecto
        // ya usa el toString() del userObject del nodo.
        // setText("Texto Personalizado para: " + value.toString());

        return this;
    }
}