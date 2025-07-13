package org.example.gui.componentes;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class TextFieldTreeCellRenderer extends JPanel implements TreeCellRenderer {
    private JLabel label;
    private JTextField textField;
    private TreeNodeData currentNodeData;

    public TextFieldTreeCellRenderer() {
        setLayout(new BorderLayout());
        label = new JLabel();
        textField = new JTextField(10);
        textField.setEditable(false);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        removeAll();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.isRoot()) {
            label.setText(value.toString());
            add(label, BorderLayout.CENTER);
        } else {
            currentNodeData = (TreeNodeData) node.getUserObject();
            if (currentNodeData instanceof TreeNodeData) {
                label.setText(currentNodeData.getLabel());
                // AQUÍ está la clave - recuperar el valor guardado
                String savedValue = currentNodeData.getValue();
                textField.setText(currentNodeData.getValue());
                System.out.println("RENDERER - Cargando nodo: " + currentNodeData.getLabel() +
                        " con valor: '" + savedValue + "' - ID objeto: " + currentNodeData.hashCode());

            } else {
                label.setText(value.toString());
                textField.setText("");
            }

            add(label, BorderLayout.WEST);
            add(textField, BorderLayout.CENTER);
        }

        if (selected) {
            setBackground(UIManager.getColor("Tree.selectionBackground"));
            setForeground(UIManager.getColor("Tree.selectionForeground"));
            label.setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            setBackground(UIManager.getColor("Tree.background"));
            setForeground(UIManager.getColor("Tree.foreground"));
            label.setForeground(UIManager.getColor("Tree.foreground"));
        }

        setOpaque(true); // Importante para que se vea el color de fondo


        return this;
    }

}