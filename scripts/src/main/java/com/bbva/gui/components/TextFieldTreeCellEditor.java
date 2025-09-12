package com.bbva.gui.components;

import com.bbva.gui.utils.UtilGUI;

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
                /*System.out.println("EDITOR - Cargando nodo: " + currentNodeData.getLabel() +
                        " con valor: '" + savedValue + "' - ID objeto: " + currentNodeData.hashCode());*/
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

                        // VALIDAR el valor ANTES de cambiar de nodo
                        if (currentNodeData instanceof TreeNodeData) {
                            String currentValue = textField.getText();
                            String label = currentNodeData.getLabel();

                            // Validar específicamente el campo P002
                            if ("P002".equals(label)) {
                                if (!UtilGUI.validateP002Field(currentValue)) {
                                    // Mostrar popup de error
                                    showErrorDialog(tree,currentValue,currentValue.length());
                                    // Mantener el foco en el campo actual y no avanzar
                                    SwingUtilities.invokeLater(() -> {
                                        textField.requestFocusInWindow();
                                        textField.selectAll();
                                    });
                                    e.consume();
                                    return; // No continuar con el cambio de nodo
                                }
                            }

                            if ("P004".equals(label)) {
                                if (!UtilGUI.validateP002Field(currentValue)) {
                                    // Mostrar popup de error
                                    showErrorDialog(tree,currentValue,currentValue.length());
                                    // Mantener el foco en el campo actual y no avanzar
                                    SwingUtilities.invokeLater(() -> {
                                        textField.requestFocusInWindow();
                                        textField.selectAll();
                                    });
                                    e.consume();
                                    return; // No continuar con el cambio de nodo
                                }
                            }

                            // Si la validación es exitosa, guardar el valor
                            currentNodeData.setValue(currentValue);
                        }

                        // Solo si la validación pasó, cambiar al siguiente nodo
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


    private void showErrorDialog(JTree tree, String currentValue, int longitud) {

        JOptionPane.showMessageDialog(
                tree,
                "Error en campo P002 (PAN):\n" +
                        "- Debe tener entre 16 y 19 dígitos\n" +
                        "- Solo se permiten números\n" +
                        "Valor ingresado: '" + currentValue + "' (longitud: " + longitud+ ")",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
        );

/*
        String htmlMessage = "<html><body style='width: 250px; padding: 10px;'>" +
                currentValue.replace("\n", "<br>") + "</body></html>";

        JOptionPane.showMessageDialog(tree, htmlMessage, "Error", JOptionPane.ERROR_MESSAGE);*/
    }
}