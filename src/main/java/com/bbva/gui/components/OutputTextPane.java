package com.bbva.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class OutputTextPane extends VBox {

    private static final String BBVA_NAVY = "#004481";
    private static final String BBVA_BLACK = "#282832";
    private static final String BBVA_ACCENT_BLUE = "#12BEFF";

    private final TextArea textArea;
    private final Button btnPrimary;

    public OutputTextPane(String title, String primaryBtnText) {
        setStyle("-fx-border-color: " + BBVA_NAVY + "; -fx-border-width: 1; -fx-padding: 4;");
        setPadding(new Insets(4));
        setSpacing(4);

        textArea = new TextArea();
        textArea.setStyle("-fx-control-inner-background: " + BBVA_BLACK + "; " +
                "-fx-font-family: Tahoma; -fx-font-size: 10px; -fx-text-fill: " + BBVA_ACCENT_BLUE + ";");
        textArea.setWrapText(true);
        textArea.setEditable(false);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        getChildren().add(textArea);

        HBox buttonPanel = new HBox(8);
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setPadding(new Insets(4, 0, 0, 0));
        btnPrimary = createButton(primaryBtnText);
        buttonPanel.getChildren().add(btnPrimary);
        getChildren().add(buttonPanel);
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + BBVA_NAVY + "; -fx-text-fill: white; " +
                "-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-padding: 8 25 8 25; -fx-cursor: hand;");
        return btn;
    }
}
