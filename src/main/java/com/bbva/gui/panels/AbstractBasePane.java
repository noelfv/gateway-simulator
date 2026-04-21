package com.bbva.gui.panels;

import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

public abstract class AbstractBasePane extends BorderPane {

    protected SplitPane createLayoutWithTree(TreeOutputPane tree, InputTextPane input, OutputTextPane output) {
        SplitPane verticalSplit = new SplitPane(input, output);
        verticalSplit.setOrientation(Orientation.VERTICAL);
        verticalSplit.setDividerPositions(0.55);

        SplitPane horizontalSplit = new SplitPane(tree, verticalSplit);
        horizontalSplit.setDividerPositions(0.25);
        return horizontalSplit;
    }

    protected SplitPane createSimpleLayout(InputTextPane input, OutputTextPane output) {
        SplitPane split = new SplitPane(input, output);
        split.setOrientation(Orientation.VERTICAL);
        split.setDividerPositions(0.55);
        return split;
    }

    protected SplitPane createSimpleLayout(Node top, Node bottom) {
        SplitPane split = new SplitPane(top, bottom);
        split.setOrientation(Orientation.VERTICAL);
        split.setDividerPositions(0.65);
        return split;
    }

    protected void setupCopyToClipboard(OutputTextPane outputPane) {
        outputPane.getBtnPrimary().setOnAction(e -> {
            String text = outputPane.getTextArea().getText();
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            Clipboard.getSystemClipboard().setContent(content);
            FXUtils.mostrarTooltipTemporal(outputPane.getBtnPrimary(), "¡Texto copiado!", 1500);
        });
    }

    protected void setupTreeClickHandler(TreeOutputPane treePane) {
        MenuItem menuCopiar = new MenuItem("Copiar valor");
        MenuItem menuExportar = new MenuItem("Exportar como JSON");
        ContextMenu contextMenu = new ContextMenu(menuCopiar, new SeparatorMenuItem(), menuExportar);

        menuCopiar.setOnAction(e -> {
            TreeItem<String> selected = treePane.getTreeView().getSelectionModel().getSelectedItem();
            if (selected != null) {
                String texto = extractNodeValue(selected.getValue());
                ClipboardContent content = new ClipboardContent();
                content.putString(texto);
                Clipboard.getSystemClipboard().setContent(content);
                FXUtils.mostrarTooltipTemporal(treePane.getTreeView(), "¡Valor copiado!", 1500);
            }
        });

        menuExportar.setOnAction(e -> FXParseGUI.showExportJsonDialog(treePane.getTreeView()));

        treePane.getTreeView().setContextMenu(contextMenu);

        treePane.getTreeView().setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 1) {
                TreeItem<String> selected = treePane.getTreeView().getSelectionModel().getSelectedItem();
                if (selected != null && selected.getChildren().isEmpty()) {
                    FXParseGUI.showNodeDetails(selected.getValue());
                }
            }
        });
    }

    private String extractNodeValue(String nodeText) {
        if (nodeText == null) return "";
        // Para nodos tipo "P001: [valor]" extrae solo el valor entre corchetes
        int start = nodeText.indexOf('[');
        int end = nodeText.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return nodeText.substring(start + 1, end);
        }
        return nodeText;
    }

    protected void clearFields(InputTextPane inputPane, OutputTextPane outputPane, TreeOutputPane treePane) {
        inputPane.getTextArea().setText("");
        outputPane.getTextArea().setText("");
        treePane.getTreeView().setRoot(new TreeItem<>(""));
    }

    protected void registerPrimaryButton(InputTextPane inputPane, Runnable action) {
        inputPane.getBtnPrimary().setOnAction(e -> action.run());
    }

    protected void registerSecondaryButton(InputTextPane inputPane, Runnable action) {
        inputPane.getBtnSecondary().setOnAction(e -> action.run());
    }
}
