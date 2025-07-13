package org.example.gui.componentes;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;

public class TextFieldTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private TreeNodeData currentNodeData;

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

        SwingUtilities.invokeLater(() -> textField.requestFocusInWindow());

        if (node.isRoot()) {
            label.setText(value.toString());
            panel.add(label, BorderLayout.CENTER);
        } else {
            currentNodeData = (TreeNodeData) node.getUserObject();
            if (currentNodeData instanceof TreeNodeData) {
                label.setText(currentNodeData.getLabel());
                // AQUÍ está la clave - recuperar el valor guardado
                String savedValue = currentNodeData.getValue();
                textField.setText(currentNodeData.getValue());
                System.out.println("EDITOR - Cargando nodo: " + currentNodeData.getLabel() +
                        " con valor: '" + savedValue + "' - ID objeto: " + currentNodeData.hashCode());
            } else {
                label.setText(value.toString());
                textField.setText("");
            }

            // Elimina listeners previos
            for (ActionListener al : textField.getActionListeners()) {
                textField.removeActionListener(al);
            }

            textField.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    moveToNextEditableNode(tree);
                });
            });

            for (java.awt.event.KeyListener kl : textField.getKeyListeners()) {
                textField.removeKeyListener(kl);
            }

            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {

                        // GUARDAR el valor ANTES de cambiar de nodo
                        if (currentNodeData instanceof TreeNodeData) {
                            String currentValue = textField.getText();
                            currentNodeData.setValue(currentValue);
                            System.out.println("EDITOR - Valor guardado: '" + currentValue +
                                    "' en nodo: " + currentNodeData.getLabel() +
                                    " - ID objeto: " + currentNodeData.hashCode());
                        }

                        //stopCellEditing();

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
        //return textField;
    }

    private void moveToNextEditableNode(JTree tree) {
        TreePath currentPath = tree.getSelectionPath();
        if (currentPath == null) return;

        int currentRow = tree.getRowForPath(currentPath);
        int nextRow = currentRow + 1;

        if (nextRow < tree.getRowCount()) {
            TreePath nextPath = tree.getPathForRow(nextRow);
            tree.setSelectionPath(nextPath);
            tree.scrollPathToVisible(nextPath);              // Opcional: hacer scroll
            tree.startEditingAtPath(nextPath);
            // Garantizar que el JTextField del siguiente nodo reciba el foco
            /*SwingUtilities.invokeLater(() -> {
                Component editorComponent = tree.getEditorComponent();
                if (editorComponent instanceof JTextField) {
                    editorComponent.requestFocusInWindow();
                }
            });*/
        }
    }


    @Override
    public Object getCellEditorValue() {
        //return textField.getText();
        return  currentNodeData;
    }

    @Override
    public boolean stopCellEditing() {
        if (currentNodeData != null) {
            currentNodeData.setValue(textField.getText());   // guarda lo escrito
        }
        return super.stopCellEditing();
    }
}