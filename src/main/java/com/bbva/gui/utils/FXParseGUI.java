package com.bbva.gui.utils;

import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.gui.dto.ParseResult;
import com.bbva.orchestrator.core.utils.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FXParseGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FXParseGUI.class);

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

    public static void showNodeDetails(String nodeText) {
        if (nodeText == null || !nodeText.startsWith("P")) {
            showSimpleInfo(nodeText);
            return;
        }
        try {
            String raw = nodeText.substring(1, 4).trim();
            String fieldId = raw.replaceFirst("^0+", "");
            if (fieldId.isEmpty()) fieldId = "0";

            String data = "";
            int bracketStart = nodeText.indexOf('[');
            int bracketEnd   = nodeText.lastIndexOf(']');
            if (bracketStart >= 0 && bracketEnd > bracketStart) {
                data = nodeText.substring(bracketStart + 1, bracketEnd);
            } else if (nodeText.length() > 6) {
                data = nodeText.substring(6).trim();
            }

            ISOFieldInfo info = ISOFieldFinder.getDataTypeISO8583(fieldId);

            // ── Header navy ──────────────────────────────────────────────────
            Label lblCampo = new Label("Campo  " + fieldId);
            lblCampo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            lblCampo.setStyle("-fx-text-fill: white;");

            Label lblNombre = new Label(nvl(info.getName()));
            lblNombre.setFont(Font.font("Segoe UI", 11));
            lblNombre.setStyle("-fx-text-fill: #A8CFED;");

            VBox header = new VBox(3, lblCampo, lblNombre);
            header.setPadding(new Insets(14, 20, 14, 20));
            header.setStyle("-fx-background-color: #004481;");

            // ── Filas de metadatos ───────────────────────────────────────────
            String[][] meta = {
                { "ID",              String.valueOf(info.getId()) },
                { "Tipo dato",       nvl(info.getTypeData()) },
                { "Caracteristicas", nvl(info.getCaracteristicaDato()) },
                { "Longitud",        String.valueOf(info.getLength()) }
            };

            VBox metaBox = new VBox(0);
            metaBox.setPadding(new Insets(6, 0, 6, 0));
            for (int i = 0; i < meta.length; i++) {
                HBox row = new HBox();
                row.setPadding(new Insets(7, 20, 7, 20));
                row.setStyle("-fx-background-color: " + (i % 2 == 0 ? "#FFFFFF" : "#F7F9FC") + ";");

                Label key = new Label(meta[i][0]);
                key.setMinWidth(130);
                key.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                key.setStyle("-fx-text-fill: #004481;");

                Label val = new Label(meta[i][1]);
                val.setFont(Font.font("Segoe UI", 12));
                val.setStyle("-fx-text-fill: #1A1A2E;");
                HBox.setHgrow(val, Priority.ALWAYS);

                row.getChildren().addAll(key, val);
                metaBox.getChildren().add(row);
            }

            // ── Sección Data ─────────────────────────────────────────────────
            Label lblDataTitle = new Label("DATA");
            lblDataTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
            lblDataTitle.setStyle("-fx-text-fill: #004481;");

            TextField tfData = new TextField(data);
            tfData.setEditable(false);
            tfData.setFont(Font.font("Consolas", 12));
            tfData.setMaxWidth(Double.MAX_VALUE);
            tfData.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #B0C8E8; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-padding: 6 8;");

            VBox dataSection = new VBox(5, lblDataTitle, tfData);
            dataSection.setPadding(new Insets(12, 20, 14, 20));
            dataSection.setStyle(
                "-fx-background-color: #EBF4FF; " +
                "-fx-border-color: #D0E4F7; " +
                "-fx-border-width: 1 0 0 0;");

            // ── Dialog ───────────────────────────────────────────────────────
            VBox content = new VBox(header, metaBox, dataSection);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Detalle del campo");
            DialogPane pane = dialog.getDialogPane();
            pane.setHeader(null);
            pane.setGraphic(null);
            pane.setContent(content);
            pane.getButtonTypes().add(ButtonType.CLOSE);
            pane.setPrefWidth(480);
            pane.setStyle("-fx-padding: 0; -fx-background-color: white;");
            dialog.showAndWait();
        } catch (Exception e) {
            LOGGER.error("Error showing field details for node '{}': {}", nodeText, e.getMessage(), e);
            showSimpleInfo(nodeText);
        }
    }

    private static String nvl(String s) {
        return s != null ? s : "-";
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

}
