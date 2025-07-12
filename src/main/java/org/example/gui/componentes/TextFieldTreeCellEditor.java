package org.example.gui.componentes;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;

public class TextFieldTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {
    private JPanel panel;
    private JLabel label;
    private JTextField textField;

    public TextFieldTreeCellEditor() {
        panel = new JPanel(new BorderLayout());
        label = new JLabel();
        textField = new JTextField(10);
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                boolean isSelected, boolean expanded, boolean leaf, int row) {
        panel.removeAll();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.isRoot()) {
            label.setText(value.toString());
            panel.add(label, BorderLayout.CENTER);
        } else {
            Object userObject = node.getUserObject();
            if (userObject instanceof TreeNodeData) {
                TreeNodeData nodeData = (TreeNodeData) userObject;
                label.setText(nodeData.getLabel());


                // AQUÍ está la clave - recuperar el valor guardado
                String savedValue = nodeData.getValue();
                textField.setText(savedValue != null ? savedValue : "");

                System.out.println("EDITOR - Cargando nodo: " + nodeData.getLabel() +
                        " con valor: '" + savedValue + "' - ID objeto: " + nodeData.hashCode());

            } else {
                //label.setText(value.toString());
                textField.setText("");
            }

            // Elimina listeners previos
            for (java.awt.event.KeyListener kl : textField.getKeyListeners()) {
                textField.removeKeyListener(kl);
            }

            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {

                        // GUARDAR el valor ANTES de cambiar de nodo
                        if (userObject instanceof TreeNodeData) {
                            String currentValue = textField.getText();
                            ((TreeNodeData) userObject).setValue(currentValue);
                            System.out.println("EDITOR - Valor guardado: '" + currentValue +
                                    "' en nodo: " + ((TreeNodeData) userObject).getLabel() +
                                    " - ID objeto: " + userObject.hashCode());
                        }

                        stopCellEditing();

                        // Mueve al siguiente nodo
                        SwingUtilities.invokeLater(() -> {
                            int nextRow = tree.getLeadSelectionRow() + 1;
                            if (nextRow < tree.getRowCount() &&
                                    !tree.getPathForRow(nextRow).getLastPathComponent().equals(tree.getModel().getRoot())) {
                                tree.setSelectionRow(nextRow);
                                tree.startEditingAtPath(tree.getPathForRow(nextRow));
                            }
                        });

                        e.consume();
                    }
                }
            });

            panel.add(label, BorderLayout.WEST);
            panel.add(textField, BorderLayout.CENTER);
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }
}