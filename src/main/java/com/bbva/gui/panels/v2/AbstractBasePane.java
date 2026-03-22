package com.bbva.gui.panels.v2;

import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
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
        treePane.getTreeView().setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 1) {
                TreeItem<String> selected = treePane.getTreeView().getSelectionModel().getSelectedItem();
                if (selected != null && selected.getChildren().isEmpty()) {
                    FXParseGUI.showNodeDetails(selected.getValue());
                }
            }
        });
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
