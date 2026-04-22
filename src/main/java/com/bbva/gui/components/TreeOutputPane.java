package com.bbva.gui.components;

import com.bbva.gui.theme.UITheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class TreeOutputPane extends VBox {

    private final TreeView<String> treeView;

    public TreeOutputPane(String title) {
        setStyle(
            "-fx-border-color: " + UITheme.NAVY + "; " +
            "-fx-border-width: 1.5; " +
            "-fx-border-radius: 4; " +
            "-fx-background-radius: 4; " +
            "-fx-background-color: " + UITheme.WHITE + ";"
        );

        Label titleLabel = new Label(title);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(5, 10, 5, 10));
        titleLabel.setStyle(
            "-fx-background-color: " + UITheme.NAVY + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 11px; " +
            "-fx-font-family: 'Segoe UI';"
        );

        TreeItem<String> root = new TreeItem<>("");
        treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setStyle(
            "-fx-background-color: " + UITheme.WHITE + "; " +
            "-fx-font-family: Consolas; " +
            "-fx-font-size: 11px;"
        );
        treeView.setCellFactory(tv -> new ISOTreeCell());
        VBox.setVgrow(treeView, Priority.ALWAYS);

        getChildren().addAll(titleLabel, treeView);
    }

    private static class ISOTreeCell extends TreeCell<String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(null);
            setText(null);

            if (empty || item == null) {
                setStyle("-fx-background-color: transparent;");
                return;
            }

            if (isSectionHeader()) {
                renderSection(item);
            } else if (item.matches("P\\d{3}:.*")) {
                renderField(item);
            } else {
                renderValue(item);
            }
        }

        private boolean isSectionHeader() {
            TreeItem<String> node = getTreeItem();
            if (node == null || getTreeView() == null) return false;
            return node.getParent() == getTreeView().getRoot();
        }

        private void renderSection(String item) {
            setText(item);
            setStyle(
                "-fx-background-color: " + UITheme.LIGHT_BLUE + "; " +
                "-fx-text-fill: " + UITheme.NAVY + "; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 11px; " +
                "-fx-padding: 4 8 4 6; " +
                "-fx-border-color: transparent transparent " + UITheme.BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;"
            );
        }

        private void renderField(String item) {
            String fieldNum = item.substring(0, 4);
            String rest     = item.substring(4);

            Label badge = new Label(fieldNum);
            badge.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 9px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 1 5 1 5; " +
                "-fx-background-radius: 3;"
            );

            Label value = new Label(rest);
            value.setStyle(
                "-fx-text-fill: " + UITheme.TEXT_DARK + "; " +
                "-fx-font-family: Consolas; " +
                "-fx-font-size: 11px;"
            );

            HBox row = new HBox(6, badge, value);
            row.setAlignment(Pos.CENTER_LEFT);
            setGraphic(row);

            String bg = (getIndex() % 2 == 0) ? UITheme.WHITE : UITheme.STRIPE;
            setStyle("-fx-background-color: " + bg + "; -fx-padding: 2 6;");
        }

        private void renderValue(String item) {
            setText(item);
            setStyle(
                "-fx-text-fill: " + UITheme.BLUE + "; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 3 10;"
            );
        }
    }
}
