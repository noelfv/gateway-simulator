package com.bbva.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class InputTextPane extends VBox {

    private static final String NAVY       = "#004481";
    private static final String BLUE       = "#1464A0";
    private static final String LIGHT_BLUE = "#E8F4FD";
    private static final String WHITE      = "#FFFFFF";
    private static final String BORDER     = "#D0E4F7";

    private final TextArea textArea;
    private final Button btnPrimary;
    private final Button btnSecondary;
    private ComboBox<ComboItem> comboBox;

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText) {
        this(title, primaryBtnText, secondaryBtnText, null);
    }

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText,
                         ComboBox<ComboItem> comboBox) {
        setStyle(
            "-fx-border-color: " + NAVY + "; " +
            "-fx-border-width: 1.5; " +
            "-fx-background-color: " + WHITE + ";"
        );
        setSpacing(0);

        // ── Title bar ──────────────────────────────────────────────
        HBox titleBar = new HBox(8);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setStyle("-fx-background-color: " + NAVY + ";");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 11px; " +
            "-fx-font-family: 'Segoe UI';"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleBar.getChildren().addAll(titleLabel, spacer);

        if (comboBox != null) {
            this.comboBox = comboBox;
            Label comboLabel = new Label("Marca:");
            comboLabel.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-size: 11px; " +
                "-fx-font-family: 'Segoe UI';"
            );
            comboBox.setStyle(
                "-fx-font-size: 11px; " +
                "-fx-background-color: white; " +
                "-fx-border-color: " + BORDER + "; " +
                "-fx-border-radius: 3; " +
                "-fx-background-radius: 3;"
            );
            titleBar.getChildren().addAll(comboLabel, comboBox);
        }

        // ── Text area ──────────────────────────────────────────────
        textArea = new TextArea();
        textArea.setStyle(
            "-fx-control-inner-background: " + WHITE + "; " +
            "-fx-font-family: Consolas; " +
            "-fx-font-size: 11px; " +
            "-fx-border-color: transparent;"
        );
        textArea.setWrapText(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        VBox.setMargin(textArea, new Insets(6, 8, 0, 8));

        // ── Button bar ─────────────────────────────────────────────
        HBox buttonBar = new HBox(8);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(8, 8, 8, 8));

        btnSecondary = createSecondaryButton(secondaryBtnText);
        btnPrimary   = createPrimaryButton(primaryBtnText);
        buttonBar.getChildren().addAll(btnSecondary, btnPrimary);

        getChildren().addAll(titleBar, textArea, buttonBar);
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        String normal = styleBtn(NAVY, "white");
        String hover  = styleBtn(BLUE, "white");
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(normal));
        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        String normal = styleBtnOutline(NAVY, NAVY, "transparent");
        String hover  = styleBtnOutline(NAVY, NAVY, LIGHT_BLUE);
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(normal));
        return btn;
    }

    private static String styleBtn(String bg, String fg) {
        return "-fx-background-color: " + bg + "; " +
               "-fx-text-fill: " + fg + "; " +
               "-fx-font-weight: bold; " +
               "-fx-font-size: 11px; " +
               "-fx-font-family: 'Segoe UI'; " +
               "-fx-padding: 7 20 7 20; " +
               "-fx-cursor: hand; " +
               "-fx-background-radius: 4;";
    }

    private static String styleBtnOutline(String border, String fg, String bg) {
        return "-fx-background-color: " + bg + "; " +
               "-fx-text-fill: " + fg + "; " +
               "-fx-font-weight: bold; " +
               "-fx-font-size: 11px; " +
               "-fx-font-family: 'Segoe UI'; " +
               "-fx-padding: 6 18 6 18; " +
               "-fx-cursor: hand; " +
               "-fx-border-color: " + border + "; " +
               "-fx-border-radius: 4; " +
               "-fx-border-width: 1.5; " +
               "-fx-background-radius: 4;";
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
