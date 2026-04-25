package com.bbva.gui.panels;

import com.bbva.gui.theme.UITheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class FieldSelectionPanel extends HBox {

    private static final String STYLE_TF_RO = "-fx-control-inner-background: " + UITheme.TF_RO_BG + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + UITheme.TF_BORDER + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_TF_EDIT = "-fx-control-inner-background: " + UITheme.WHITE + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + UITheme.NAVY + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_OFF = "-fx-background-color: transparent; -fx-text-fill: #8899AA; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_ON = "-fx-background-color: " + UITheme.NAVY + "; -fx-text-fill: white; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_DEL_OFF = "-fx-background-color: transparent; -fx-text-fill: #CC4444; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_DEL_ON = "-fx-background-color: #FFEEEE; -fx-text-fill: #CC0000; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";

    private final Map<Integer, CheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, TextField> textFields = new TreeMap<>();

    /**
     * @param campos               ordered list of ISO field IDs to display
     * @param defaultValueProvider returns the default text value for a given field ID
     * @param mandatoryFields      field IDs whose checkboxes start selected
     * @param onDelete             called with the field ID when the ✕ button is clicked
     */
    public FieldSelectionPanel(List<Integer> campos,
                               Function<Integer, String> defaultValueProvider,
                               Set<Integer> mandatoryFields,
                               Consumer<Integer> onDelete) {
        super(8);
        setPadding(new Insets(8));

        int total = campos.size();
        int perCol = (int) Math.ceil(total / 3.0);

        for (int col = 0; col < 3; col++) {
            int from = col * perCol;
            int to = Math.min(from + perCol, total);
            if (from < total) {
                getChildren().add(buildColumn(
                        "Bloque " + (col + 1),
                        campos.subList(from, to),
                        defaultValueProvider,
                        mandatoryFields,
                        onDelete));
            }
        }
    }

    public Map<Integer, CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public Map<Integer, TextField> getTextFields() {
        return textFields;
    }

    private VBox buildColumn(String title,
                             List<Integer> campos,
                             Function<Integer, String> defaultValueProvider,
                             Set<Integer> mandatoryFields,
                             Consumer<Integer> onDelete) {
        Label titleBar = new Label(title);
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        VBox fieldsBox = new VBox(0);
        fieldsBox.setStyle("-fx-background-color: " + UITheme.WHITE + ";");

        int rowIdx = 0;
        for (int id : campos) {
            String bg = (rowIdx % 2 == 0) ? UITheme.WHITE : UITheme.STRIPE;
            fieldsBox.getChildren().add(buildFieldRow(id, bg, defaultValueProvider, mandatoryFields, onDelete));
            rowIdx++;
        }

        VBox column = new VBox(0, titleBar, fieldsBox);
        column.setStyle(
                "-fx-border-color: " + UITheme.NAVY + "; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-color: " + UITheme.WHITE + ";");
        VBox.setVgrow(fieldsBox, Priority.ALWAYS);
        HBox.setHgrow(column, Priority.ALWAYS);
        return column;
    }

    private HBox buildFieldRow(int id,
                               String bg,
                               Function<Integer, String> defaultValueProvider,
                               Set<Integer> mandatoryFields,
                               Consumer<Integer> onDelete) {
        CheckBox chk = new CheckBox();
        chk.setSelected(mandatoryFields.contains(id));
        checkBoxes.put(id, chk);

        Label badge = new Label(String.format("P%03d", id));
        badge.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 9px; -fx-font-weight: bold; " +
                "-fx-padding: 1 5 1 5; -fx-background-radius: 3;");

        TextField txt = new TextField(defaultValueProvider.apply(id));
        txt.setStyle(STYLE_TF_RO);
        txt.setDisable(true);
        txt.setEditable(false);
        HBox.setHgrow(txt, Priority.ALWAYS);
        textFields.put(id, txt);

        Button editBtn = new Button("✎");
        editBtn.setStyle(STYLE_EDIT_OFF);
        editBtn.setOnAction(e -> {
            boolean editing = !txt.isEditable();
            txt.setDisable(!editing);
            txt.setEditable(editing);
            txt.setStyle(editing ? STYLE_TF_EDIT : STYLE_TF_RO);
            editBtn.setStyle(editing ? STYLE_EDIT_ON : STYLE_EDIT_OFF);
        });

        Button delBtn = new Button("✕");
        delBtn.setStyle(STYLE_DEL_OFF);
        delBtn.setOnMouseEntered(ev -> delBtn.setStyle(STYLE_DEL_ON));
        delBtn.setOnMouseExited(ev -> delBtn.setStyle(STYLE_DEL_OFF));
        delBtn.setOnAction(e -> onDelete.accept(id));

        HBox row = new HBox(6, chk, badge, txt, editBtn, delBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 8, 4, 8));
        row.setStyle(
                "-fx-background-color: " + bg + "; " +
                "-fx-border-color: transparent transparent " + UITheme.BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;");
        return row;
    }
}
