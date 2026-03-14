package com.bbva.gui.panels;

import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.JtreeOutputPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.SwingUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractBasePanel extends JPanel {

    protected void setupCopyToClipboard(OutputTextPanel outputTextPanel) {
        outputTextPanel.getBtnPrimary().addActionListener(e -> {
            StringSelection selection = new StringSelection(outputTextPanel.getTextArea().getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            outputTextPanel.getTextArea().requestFocusInWindow();
            outputTextPanel.getTextArea().selectAll();
            SwingUtils.mostrarTooltipTemporal(outputTextPanel.getBtnPrimary(), "¡Texto copiado!", 1000);
        });
    }

    protected void setupTreeClickHandler(JtreeOutputPanel treePanel) {
        treePanel.getResultTree().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int selRow = treePanel.getResultTree().getRowForLocation(evt.getX(), evt.getY());
                    if (selRow != -1) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                treePanel.getResultTree().getLastSelectedPathComponent();
                        if (node != null && node.isLeaf()) {
                            ParseGUI.showNodeDetails(node, evt.getX(), evt.getY());
                        }
                    }
                }
            }
        });
    }

    protected void clearFields(InputTextPanel inputPanel, OutputTextPanel outputPanel,
                               JtreeOutputPanel treePanel, String rootLabel) {
        inputPanel.getTextArea().setText("");
        outputPanel.getTextArea().setText("");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootLabel);
        treePanel.getTreeModel().setRoot(root);
        treePanel.getTreeModel().reload();
    }

    protected void registerPrimaryButton(InputTextPanel inputPanel, Runnable action) {
        inputPanel.getBtnPrimary().addActionListener(e -> action.run());
    }

    protected void registerSecondaryButton(InputTextPanel inputPanel, Runnable action) {
        inputPanel.getBtnSecondary().addActionListener(e -> action.run());
    }
}
