package com.bbva.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class InputTextPane extends VBox {

    private static final String BBVA_NAVY = "#004481";
    private static final String BBVA_ACCENT_BLUE = "#12BEFF";

    private final TextArea textArea;
    private final Button btnPrimary;
    private final Button btnSecondary;
    private ComboBox<ComboItem> comboBox;

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText) {
        this(title, primaryBtnText, secondaryBtnText, null);
    }

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText,
                         ComboBox<ComboItem> comboBox) {
        setStyle("-fx-border-color: " + BBVA_NAVY + "; -fx-border-width: 1; -fx-padding: 4;");
        setPadding(new Insets(4));
        setSpacing(4);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + BBVA_NAVY + ";");
        getChildren().add(titleLabel);

        if (comboBox != null) {
            this.comboBox = comboBox;
            HBox comboWrapper = new HBox(6);
            comboWrapper.setAlignment(Pos.CENTER_RIGHT);
            comboWrapper.setPadding(new Insets(2, 4, 4, 4));
            Label label = new Label("Marca:");
            label.setStyle("-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + BBVA_NAVY + ";");
            comboWrapper.getChildren().addAll(label, comboBox);
            getChildren().add(comboWrapper);
        }

        textArea = new TextArea();
        textArea.setStyle("-fx-control-inner-background: white; -fx-font-family: Georgia; -fx-font-size: 10px;");
        textArea.setWrapText(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        getChildren().add(textArea);

        HBox buttonPanel = new HBox(8);
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setPadding(new Insets(4, 0, 0, 0));
        btnPrimary = createButton(primaryBtnText);
        btnSecondary = createButton(secondaryBtnText);
        buttonPanel.getChildren().addAll(btnPrimary, btnSecondary);
        getChildren().add(buttonPanel);
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + BBVA_NAVY + "; -fx-text-fill: white; " +
                "-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-padding: 8 25 8 25; -fx-cursor: hand;");
        return btn;
    }

    @Getter
    public static class ComboItem {
        private final String id;
        private final String label;

        public ComboItem(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
