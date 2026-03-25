package com.bbva.gui.components;

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

    private static final String NAVY       = "#004481";
    private static final String BLUE       = "#1464A0";
    private static final String LIGHT_BLUE = "#E8F4FD";
    private static final String STRIPE     = "#F7F9FC";
    private static final String WHITE      = "#FFFFFF";
    private static final String TEXT_DARK  = "#1A1A2E";
    private static final String BORDER     = "#D0E4F7";

    private final TreeView<String> treeView;

    public TreeOutputPane(String title) {
        setStyle(
            "-fx-border-color: " + NAVY + "; " +
            "-fx-border-width: 1.5; " +
            "-fx-border-radius: 4; " +
            "-fx-background-radius: 4; " +
            "-fx-background-color: " + WHITE + ";"
        );

        Label titleLabel = new Label(title);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(5, 10, 5, 10));
        titleLabel.setStyle(
            "-fx-background-color: " + NAVY + "; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 11px; " +
            "-fx-font-family: 'Segoe UI';"
        );

        TreeItem<String> root = new TreeItem<>("");
        treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setStyle(
            "-fx-background-color: " + WHITE + "; " +
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
                "-fx-background-color: " + LIGHT_BLUE + "; " +
                "-fx-text-fill: " + NAVY + "; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 11px; " +
                "-fx-padding: 4 8 4 6; " +
                "-fx-border-color: transparent transparent " + BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;"
            );
        }

        private void renderField(String item) {
            String fieldNum = item.substring(0, 4);   // "P001"
            String rest     = item.substring(4);      // ": [value]"

            Label badge = new Label(fieldNum);
            badge.setStyle(
                "-fx-background-color: " + NAVY + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 9px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 1 5 1 5; " +
                "-fx-background-radius: 3;"
            );

            Label value = new Label(rest);
            value.setStyle(
                "-fx-text-fill: " + TEXT_DARK + "; " +
                "-fx-font-family: Consolas; " +
                "-fx-font-size: 11px;"
            );

            HBox row = new HBox(6, badge, value);
            row.setAlignment(Pos.CENTER_LEFT);
            setGraphic(row);

            String bg = (getIndex() % 2 == 0) ? WHITE : STRIPE;
            setStyle("-fx-background-color: " + bg + "; -fx-padding: 2 6;");
        }

        private void renderValue(String item) {
            setText(item);
            setStyle(
                "-fx-text-fill: " + BLUE + "; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 3 10;"
            );
        }
    }
}
