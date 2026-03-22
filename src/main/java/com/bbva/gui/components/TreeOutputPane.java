package com.bbva.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class TreeOutputPane extends VBox {

    private static final String BBVA_NAVY = "#004481";
    private static final String BBVA_LIGHT_GRAY = "#F4F4F4";

    private final TreeView<String> treeView;

    public TreeOutputPane(String title) {
        setStyle("-fx-border-color: " + BBVA_NAVY + "; -fx-border-width: 1; -fx-padding: 4;");
        setPadding(new Insets(4));

        TreeItem<String> root = new TreeItem<>("");
        treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setStyle("-fx-background-color: " + BBVA_LIGHT_GRAY + "; -fx-font-family: Monospaced; -fx-font-size: 10px;");
        VBox.setVgrow(treeView, Priority.ALWAYS);
        getChildren().add(treeView);
    }
}
