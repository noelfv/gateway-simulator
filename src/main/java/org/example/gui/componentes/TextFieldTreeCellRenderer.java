package org.example.gui.componentes;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class TextFieldTreeCellRenderer extends JPanel implements TreeCellRenderer {
    private JLabel label;
    private JTextField textField;

    public TextFieldTreeCellRenderer() {
        setLayout(new BorderLayout());
        label = new JLabel();
        textField = new JTextField(10);
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
            Object userObject = node.getUserObject();
            if (userObject instanceof TreeNodeData) {
                TreeNodeData nodeData = (TreeNodeData) userObject;

                label.setText(nodeData.getLabel());

                // AQUÍ está la clave - recuperar el valor guardado
                String savedValue = nodeData.getValue();
                textField.setText(savedValue != null ? savedValue : "");

                // LOG ADICIONAL para verificar si el renderer recupera el valor
                System.out.println("RENDERER - Nodo: " + nodeData.getLabel() +
                        " muestra valor: '" + textField.getText() + "' - ID: " + nodeData.hashCode());
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

/*

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        removeAll();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.isRoot()) {
            label.setText(value.toString());
            add(label, BorderLayout.CENTER);
        } else {
            label.setText(value.toString());
            textField.setText(""); // o el valor correspondiente
            add(label, BorderLayout.WEST);
            add(textField, BorderLayout.CENTER);
        }
        return this;
    }
*/
}