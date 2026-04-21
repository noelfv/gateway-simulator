package com.bbva.gui.utils;

import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.gui.dto.ParseResult;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field48;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FXParseGUI {

    public static void updateTreeView(TreeView<String> treeView, ParseResult result) {
        TreeItem<String> root = new TreeItem<>("Mensaje Parseado");
        TreeItem<String> headerNode = new TreeItem<>("TypeMessage");
        TreeItem<String> bitmapNode1 = new TreeItem<>("Bitmap1");
        TreeItem<String> bitmapNode2 = new TreeItem<>("Bitmap2");

        Map<Integer, TreeItem<String>> sortedBitmap1 = new TreeMap<>();
        Map<Integer, TreeItem<String>> sortedBitmap2 = new TreeMap<>();

        String typeMessage = result.fieldsByDescription().get("messageType");
        if (typeMessage != null) {
            headerNode.getChildren().add(new TreeItem<>(typeMessage));
        }

        for (Map.Entry<String, String> entry : result.fieldsById().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            String nodeText = "P" + UtilGUI.padLeft(field, 3, '0') + ": [" + value + "]";
            TreeItem<String> fieldNode = new TreeItem<>(nodeText);

            if (field.equals("0")) {
                fieldNode = new TreeItem<>("P000: [" + ISOUtil.convertBITMAPtoHEX(value) + "]");
                bitmapNode1.getChildren().add(fieldNode);
            } else if (field.equals("1")) {
                fieldNode = new TreeItem<>("P001: [" + ISOUtil.convertBITMAPtoHEX(value) + "]");
                bitmapNode1.getChildren().add(fieldNode);
            } else {
                try {
                    int fieldNumber = Integer.parseInt(field);
                    if (fieldNumber < 65) {
                        sortedBitmap1.put(fieldNumber, fieldNode);
                    } else {
                        sortedBitmap2.put(fieldNumber, fieldNode);
                    }
                } catch (NumberFormatException e) {
                    bitmapNode1.getChildren().add(fieldNode);
                }
            }
        }

        sortedBitmap1.values().forEach(n -> bitmapNode1.getChildren().add(n));
        sortedBitmap2.values().forEach(n -> bitmapNode2.getChildren().add(n));

        if (!headerNode.getChildren().isEmpty())
            root.getChildren().add(headerNode);
        if (!bitmapNode1.getChildren().isEmpty())
            root.getChildren().add(bitmapNode1);
        if (!bitmapNode2.getChildren().isEmpty())
            root.getChildren().add(bitmapNode2);

        root.setExpanded(true);
        headerNode.setExpanded(true);
        bitmapNode1.setExpanded(true);
        bitmapNode2.setExpanded(true);

        treeView.setRoot(root);
    }

    public static void updateTreeViewTLV(TreeView<String> treeView, ParseResult result) {
        TreeItem<String> root = new TreeItem<>("DATO TLV");
        TreeItem<String> headerNode = new TreeItem<>("Campo 48");

        result.fieldsById().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> headerNode.getChildren().add(
                        new TreeItem<>(entry.getKey() + ": [" + entry.getValue() + "]")));

        if (!headerNode.getChildren().isEmpty())
            root.getChildren().add(headerNode);
        root.setExpanded(true);
        headerNode.setExpanded(true);
        treeView.setRoot(root);
    }

    public static ParseResult process(Map<String, String> mapValues) {
        Map<String, String> fieldsById = new HashMap<>();
        mapValues.remove("networkName");
        mapValues.remove("plainTextPCI");
        mapValues.remove("header");
        mapValues.remove("rejectFlag");
        mapValues.remove("transactionType");
        mapValues.remove("binCode");
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            String fieldId = findFieldIdByName(entry.getKey());
            fieldsById.put(fieldId != null ? fieldId : entry.getKey(), entry.getValue());
        }
        return new ParseResult(mapValues, fieldsById);
    }

    public static ParseResult processTLV(Map<String, String> mapValues) {
        Map<String, String> fieldsById = new HashMap<>();
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            String fieldId = findFieldTLVIdByName(entry.getKey());
            fieldsById.put(fieldId != null ? fieldId : entry.getKey(), entry.getValue());
        }
        return new ParseResult(mapValues, fieldsById);
    }

    public static void showNodeDetails(String nodeText) {
        if (nodeText == null || !nodeText.startsWith("P")) {
            showSimpleInfo(nodeText);
            return;
        }
        try {
            String raw = nodeText.substring(1, 4).trim();
            String fieldId = raw.replaceFirst("^0+", "");
            if (fieldId.isEmpty())
                fieldId = "0";
            // Extraer valor entre corchetes: "P001: [valor]" → "valor"
            String data = "";
            int bracketStart = nodeText.indexOf('[');
            int bracketEnd = nodeText.lastIndexOf(']');
            if (bracketStart >= 0 && bracketEnd > bracketStart) {
                data = nodeText.substring(bracketStart + 1, bracketEnd);
            } else if (nodeText.length() > 6) {
                data = nodeText.substring(6).trim();
            }

            ISOFieldInfo dataType = getDataTypeISO8583(fieldId);

            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(8);
            grid.setPadding(new Insets(12, 16, 4, 16));

            String[][] rows = {
                    { "ID", String.valueOf(dataType.getId()) },
                    { "Nombre", dataType.getName() },
                    { "Tipo dato", dataType.getTypeData() },
                    { "Caracteristicas", dataType.getCaracteristicaDato() },
                    { "Longitud", String.valueOf(dataType.getLength()) }
            };

            for (int i = 0; i < rows.length; i++) {
                Label lbl = new Label(rows[i][0] + ":");
                lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                Label val = new Label(rows[i][1]);
                val.setFont(Font.font("Segoe UI", 12));
                grid.add(lbl, 0, i);
                grid.add(val, 1, i);
            }

            Label lblData = new Label("Data:");
            lblData.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            TextField tfData = new TextField(data);
            tfData.setEditable(false);
            tfData.setFont(Font.font("Consolas", 12));
            tfData.setPrefWidth(420);
            tfData.setStyle("-fx-background-color: #F4F8FF; -fx-border-color: #B0C8E8; -fx-border-radius: 3;");
            grid.add(lblData, 0, rows.length);
            grid.add(tfData, 1, rows.length);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Detalle del campo");
            dialog.setHeaderText("Campo " + fieldId);
            DialogPane pane = dialog.getDialogPane();
            pane.setContent(grid);
            pane.getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (Exception e) {
            showSimpleInfo(nodeText);
        }
    }

    public static void showExportJsonDialog(TreeView<String> treeView) {
        try {
            String json = buildTreeJson(treeView);

            TextArea textArea = new TextArea(json);
            textArea.setEditable(false);
            textArea.setFont(Font.font("Consolas", 12));
            textArea.setPrefSize(540, 420);
            textArea.setWrapText(false);

            Button btnCopiar = new Button("Copiar al portapapeles");
            btnCopiar.setStyle(
                "-fx-background-color: #004481; -fx-text-fill: white; " +
                "-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; " +
                "-fx-padding: 5 14; -fx-cursor: hand;");
            btnCopiar.setOnAction(e -> {
                ClipboardContent content = new ClipboardContent();
                content.putString(json);
                Clipboard.getSystemClipboard().setContent(content);
                FXUtils.mostrarTooltipTemporal(btnCopiar, "¡JSON copiado!", 1500);
            });

            VBox vbox = new VBox(10, textArea, btnCopiar);
            vbox.setPadding(new Insets(12));
            vbox.setAlignment(Pos.CENTER_RIGHT);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Exportar como JSON");
            dialog.setHeaderText("Estructura ISO8583 del mensaje");
            DialogPane pane = dialog.getDialogPane();
            pane.setContent(vbox);
            pane.getButtonTypes().add(ButtonType.CLOSE);
            pane.setPrefWidth(580);
            dialog.showAndWait();
        } catch (Exception e) {
            FXUtils.showErrorAlert("Error al exportar el mensaje: " + e.getMessage());
        }
    }

    private static String buildTreeJson(TreeView<String> treeView) throws Exception {
        TreeItem<String> root = treeView.getRoot();
        if (root == null || root.getChildren().isEmpty()) return "{}";

        Map<String, Object> result = new LinkedHashMap<>();

        for (TreeItem<String> section : root.getChildren()) {
            String sectionName = section.getValue();
            List<TreeItem<String>> children = section.getChildren();
            if (children.isEmpty()) continue;

            boolean hasPFields = children.stream()
                    .anyMatch(c -> c.getValue() != null && c.getValue().matches("P\\d{3}:.*"));

            if (hasPFields) {
                Map<String, String> fields = new LinkedHashMap<>();
                for (TreeItem<String> child : children) {
                    String nodeText = child.getValue();
                    if (nodeText == null) continue;
                    if (nodeText.matches("P\\d{3}:.*")) {
                        String key = nodeText.substring(0, 4);
                        fields.put(key, extractBracketValue(nodeText));
                    }
                }
                result.put(sectionName, fields);
            } else if (children.size() == 1) {
                result.put(sectionName, children.get(0).getValue());
            } else {
                result.put(sectionName, children.stream()
                        .map(TreeItem::getValue)
                        .collect(Collectors.toList()));
            }
        }

        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

    private static String extractBracketValue(String nodeText) {
        int start = nodeText.indexOf('[');
        int end   = nodeText.lastIndexOf(']');
        if (start >= 0 && end > start) return nodeText.substring(start + 1, end);
        return nodeText.length() > 5 ? nodeText.substring(5).trim() : "";
    }

    private static void showSimpleInfo(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle del campo");
        alert.setHeaderText(null);
        alert.setContentText(text != null ? text : "");
        alert.showAndWait();
    }

    private static String findFieldIdByName(String fieldName) {
        for (ISOField field : MastercardISOField.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }

    private static String findFieldTLVIdByName(String fieldName) {
        for (ISOSubField field : Field48.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }

    private static ISOFieldInfo getDataTypeISO8583(String fieldId) {
        try {
            int id = Integer.parseInt(fieldId);
            for (MastercardISOField field : MastercardISOField.values()) {
                if (field.getId() == id) {
                    String caracteristica = field.isVariable() ? "VARIABLE" : "FIXED";
                    return new ISOFieldInfo(field.getId(), field.getName(),
                            field.getTypeData().name(), caracteristica, field.getLength());
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return new ISOFieldInfo();
    }
}
