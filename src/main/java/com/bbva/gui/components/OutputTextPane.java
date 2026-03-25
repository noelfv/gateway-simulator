package com.bbva.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class OutputTextPane extends VBox {

    private static final String DARK_BG     = "#1E1E2E";
    private static final String DARK_HEADER = "#12122A";
    private static final String DARK_BORDER = "#2D2D4E";
    private static final String DARK_FOOT   = "#16162A";
    private static final String TEXT_OUT    = "#CDD6F4";
    private static final String ACCENT      = "#89B4FA";
    private static final String ACCENT_DIM  = "#5A7FCC";

    private final TextArea textArea;
    private final Button btnPrimary;

    public OutputTextPane(String title, String primaryBtnText) {
        setStyle(
            "-fx-border-color: " + DARK_BORDER + "; " +
            "-fx-border-width: 1.5; " +
            "-fx-background-color: " + DARK_BG + ";"
        );
        setSpacing(0);

        // ── Title bar ──────────────────────────────────────────────
        Label titleLabel = new Label(title);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(5, 10, 5, 10));
        titleLabel.setStyle(
            "-fx-background-color: " + DARK_HEADER + "; " +
            "-fx-text-fill: " + ACCENT + "; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 11px; " +
            "-fx-font-family: 'Segoe UI'; " +
            "-fx-border-color: transparent transparent " + DARK_BORDER + " transparent; " +
            "-fx-border-width: 0 0 1 0;"
        );

        // ── Text area ──────────────────────────────────────────────
        textArea = new TextArea();
        textArea.setStyle(
            "-fx-control-inner-background: " + DARK_BG + "; " +
            "-fx-font-family: Consolas; " +
            "-fx-font-size: 11px; " +
            "-fx-text-fill: " + TEXT_OUT + "; " +
            "-fx-border-color: transparent;"
        );
        textArea.setWrapText(true);
        textArea.setEditable(false);
        VBox.setVgrow(textArea, Priority.ALWAYS);

        // ── Button bar ─────────────────────────────────────────────
        HBox buttonBar = new HBox();
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(6, 8, 8, 8));
        buttonBar.setStyle("-fx-background-color: " + DARK_FOOT + ";");

        btnPrimary = createButton(primaryBtnText);
        buttonBar.getChildren().add(btnPrimary);

        getChildren().addAll(titleLabel, textArea, buttonBar);
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        String normal = "-fx-background-color: transparent; " +
                        "-fx-text-fill: " + ACCENT + "; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-family: 'Segoe UI'; " +
                        "-fx-padding: 6 18 6 18; " +
                        "-fx-cursor: hand; " +
                        "-fx-border-color: " + ACCENT + "; " +
                        "-fx-border-radius: 4; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-radius: 4;";
        String hover  = "-fx-background-color: " + ACCENT_DIM + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-family: 'Segoe UI'; " +
                        "-fx-padding: 6 18 6 18; " +
                        "-fx-cursor: hand; " +
                        "-fx-border-color: " + ACCENT + "; " +
                        "-fx-border-radius: 4; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-radius: 4;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(normal));
        return btn;
    }
}
