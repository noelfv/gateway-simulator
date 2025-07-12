package org.example.gui.componentes;

// TextFieldTreeCellEditor.java
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;

public class TextFieldTreeCellEditor2 extends AbstractCellEditor implements TreeCellEditor {
    private JPanel panel;
    private JLabel label;
    private JTextField textField;

    public TextFieldTreeCellEditor2() {
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
                // LOG: Verificar valor recuperado
                System.out.println("Cargando nodo: " + nodeData.getLabel() +
                        " con valor: '" + nodeData.getValue() + "'");
                textField.setText(nodeData.getValue()); // Recupera el valor guardado
            } else {
                label.setText(value.toString());
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

                        // NO guardar manualmente aquí - deja que el CellEditorListener se encargue
                        // ((TreeNodeData) userObject).setValue(textField.getText());

                        stopCellEditing(); // Esto activa el CellEditorListener

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

            /*
            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {

                        String currentValue = textField.getText();
                        System.out.println("Guardando valor: '" + currentValue +
                                "' en nodo: " + ((TreeNodeData) userObject).getLabel());

                        // Guarda el valor antes de moverse
                        if (userObject instanceof TreeNodeData) {
                            ((TreeNodeData) userObject).setValue(textField.getText());
                            // LOG: Verificar valor después de guardar
                            System.out.println("Valor guardado confirmado: '" +
                                    ((TreeNodeData) userObject).getValue() + "'");
                        }

                        stopCellEditing();

                        // Mueve al siguiente nodo
                        SwingUtilities.invokeLater(() -> {
                            int nextRow = tree.getLeadSelectionRow() + 1;
                            if (nextRow < tree.getRowCount() && !tree.getPathForRow(nextRow).getLastPathComponent().equals(tree.getModel().getRoot())) {
                                tree.setSelectionRow(nextRow);
                                tree.startEditingAtPath(tree.getPathForRow(nextRow));
                            }
                        });

                        e.consume();
                    }
                }
            });
*/
            panel.add(label, BorderLayout.WEST);
            panel.add(textField, BorderLayout.CENTER);
        }
        return panel;
    }


    /*

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                boolean isSelected, boolean expanded, boolean leaf, int row) {
        panel.removeAll();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.isRoot()) {
            label.setText(value.toString());
            panel.add(label, BorderLayout.CENTER);
        } else {
            label.setText(value.toString());
            Object userObject = node.getUserObject();
            if (userObject instanceof TreeNodeData) {
                textField.setText(((TreeNodeData) userObject).getValue());
            } else {
                textField.setText("");
            }

            // Elimina listeners previos para evitar duplicados
            for (java.awt.event.KeyListener kl : textField.getKeyListeners()) {
                textField.removeKeyListener(kl);
            }

            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
                        stopCellEditing();
                        int nextRow = tree.getLeadSelectionRow() + 1;
                        if (nextRow < tree.getRowCount()) {
                            tree.setSelectionRow(nextRow);
                            tree.startEditingAtPath(tree.getPathForRow(nextRow));
                        }
                        e.consume(); // Evita que el foco se mueva fuera del árbol
                    }
                }
            });

            panel.add(label, BorderLayout.WEST);
            panel.add(textField, BorderLayout.CENTER);
        }
        return panel;
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
            label.setText(value.toString());
            textField.setText(value.toString()); // o el valor correspondiente
            // Recupera el valor guardado en el usuarioObject del nodo
            Object userObject = node.getUserObject();
            //if (userObject instanceof TreeNodeData) {
               // textField.setText(((TreeNodeData) userObject).getValue());
          //  }
            if (userObject instanceof TreeNodeData) {
                textField.setText(((TreeNodeData) userObject).getValue());
            } else {
                textField.setText("");
            }

            textField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER ||
                            e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
                        // Confirma la edición
                        stopCellEditing();
                        // Selecciona el siguiente nodo si existe
                        JTree tree = (JTree) SwingUtilities.getAncestorOfClass(JTree.class, textField);
                        if (tree != null) {
                            int row = tree.getLeadSelectionRow();
                            if (row < tree.getRowCount() - 1) {
                                tree.setSelectionRow(row + 1);
                                tree.startEditingAtPath(tree.getPathForRow(row + 1));
                            }
                        }
                        e.consume();
                    }
                }
            });

            panel.add(label, BorderLayout.WEST);
            panel.add(textField, BorderLayout.CENTER);
        }
        return panel;
    }

 */


    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }
}