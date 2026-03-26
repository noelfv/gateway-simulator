package com.bbva.gui.components;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class InputTextPane extends VBox {

    private static final String NAVY = "#004481";
    private static final String BLUE = "#1464A0";
    private static final String LIGHT_BLUE = "#E8F4FD";
    private static final String WHITE = "#FFFFFF";
    private static final String BORDER = "#D0E4F7";

    public enum ComboDirection {
        LEFT, RIGHT
    }

    private final TextArea textArea;
    private final Button btnPrimary;
    private final Button btnSecondary;
    private ComboBox<ComboItem> comboBox;
    private ComboBox<ComboItem> comboBox2;

    // ── Constructores ──────────────────────────────────────────────

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText) {
        this(title, primaryBtnText, secondaryBtnText, null, null, null, null);
    }

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText,
            ComboBox<ComboItem> comboBox) {
        this(title, primaryBtnText, secondaryBtnText, comboBox, null, null, null);
    }

    public InputTextPane(String title, String primaryBtnText, String secondaryBtnText,
            ComboBox<ComboItem> comboBox,
            ComboBox<ComboItem> comboBox2, String comboBox2LabelText,
            ComboDirection direction) {
        setStyle(
                "-fx-border-color: " + NAVY + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + WHITE + ";");
        setSpacing(0);

        // ── Title bar (solo título, sin combos) ────────────────────
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(6, 10, 6, 10));
        titleBar.setStyle("-fx-background-color: " + NAVY + ";");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-family: 'Segoe UI';");
        titleBar.getChildren().add(titleLabel);

        // ── Barra de combos ────────────────────────────────────────
        // Si solo hay un combo: va en el titleBar (comportamiento original)
        // Si hay dos combos: ambos van en el secondaryBar
        if (comboBox != null && comboBox2 == null) {
            this.comboBox = comboBox;
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label comboLabel = new Label("Marca:");
            comboLabel.setStyle(
                    "-fx-text-fill: white; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-family: 'Segoe UI';");
            styleCombo(comboBox);
            titleBar.getChildren().addAll(spacer, comboLabel, comboBox);
        }

        // ── Text area ──────────────────────────────────────────────
        textArea = new TextArea();
        textArea.setStyle(
                "-fx-control-inner-background: " + WHITE + "; " +
                        "-fx-font-family: Consolas; " +
                        "-fx-font-size: 11px; " +
                        "-fx-border-color: transparent;");
        textArea.setWrapText(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        VBox.setMargin(textArea, new Insets(6, 8, 0, 8));

        // ── Button bar ─────────────────────────────────────────────
        HBox buttonBar = new HBox(8);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(8, 8, 8, 8));
        btnSecondary = createSecondaryButton(secondaryBtnText);
        btnPrimary = createPrimaryButton(primaryBtnText);
        buttonBar.getChildren().addAll(btnSecondary, btnPrimary);

        getChildren().add(titleBar);

        // ── Secondary bar: ambos combos apilados con GridPane ────────
        if (comboBox != null && comboBox2 != null) {
            this.comboBox  = comboBox;
            this.comboBox2 = comboBox2;

            Label lblRedes = new Label("Marca:");
            lblRedes.setStyle(styleComboLabel());

            Label lblOpciones = new Label(
                    (comboBox2LabelText != null && !comboBox2LabelText.isBlank())
                            ? comboBox2LabelText : "Opción:");
            lblOpciones.setStyle(styleComboLabel());

            styleCombo(comboBox);
            styleCombo(comboBox2);

            // GridPane: col-0 = labels alineados a la derecha,
            //           col-1 = combos alineados a la izquierda.
            // Sin importar la longitud del label, los combos siempre quedan en la misma columna.
            ColumnConstraints colLabel = new ColumnConstraints();
            colLabel.setHalignment(HPos.RIGHT);
            colLabel.setMinWidth(Region.USE_PREF_SIZE);

            ColumnConstraints colCombo = new ColumnConstraints();
            colCombo.setHalignment(HPos.LEFT);

            GridPane grid = new GridPane();
            grid.setHgap(8);
            grid.setVgap(6);
            grid.getColumnConstraints().addAll(colLabel, colCombo);
            grid.add(lblRedes,    0, 0);
            grid.add(comboBox,    1, 0);
            grid.add(lblOpciones, 0, 1);
            grid.add(comboBox2,   1, 1);

            // Contenedor exterior: direction mueve el bloque a izquierda o derecha
            HBox secondaryBar = new HBox();
            secondaryBar.setPadding(new Insets(8, 10, 8, 10));
            secondaryBar.setStyle(
                    "-fx-background-color: " + LIGHT_BLUE + "; " +
                    "-fx-border-color: transparent transparent " + BORDER + " transparent; " +
                    "-fx-border-width: 0 0 1 0;");

            if (direction == ComboDirection.RIGHT) {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                secondaryBar.getChildren().addAll(spacer, grid);
            } else {
                secondaryBar.getChildren().add(grid);
            }

            getChildren().add(secondaryBar);
        }

        getChildren().addAll(textArea, buttonBar);
    }

    // ── Helpers de estilo ──────────────────────────────────────────

    private void styleCombo(ComboBox<ComboItem> combo) {
        combo.setPrefWidth(130);
        combo.setMaxWidth(130);
        combo.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-background-color: white; " +
                        "-fx-border-color: " + BORDER + "; " +
                        "-fx-border-radius: 3; " +
                        "-fx-background-radius: 3;");
    }

    private String styleComboLabel() {
        return "-fx-text-fill: " + NAVY + "; " +
                "-fx-font-size: 11px; " +
                "-fx-font-family: 'Segoe UI'; " +
                "-fx-font-weight: bold;";
    }

    // ── Botones ────────────────────────────────────────────────────

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        String normal = styleBtn(NAVY, "white");
        String hover = styleBtn(BLUE, "white");
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        String normal = styleBtnOutline(NAVY, NAVY, "transparent");
        String hover = styleBtnOutline(NAVY, NAVY, LIGHT_BLUE);
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
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

    // ── ComboItem ──────────────────────────────────────────────────

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
