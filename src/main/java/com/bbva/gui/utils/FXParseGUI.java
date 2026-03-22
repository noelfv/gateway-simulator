package com.bbva.gui.utils;

import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.gui.dto.ParseResult;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field48;
import com.bbva.orchestrator.core.utils.ISOUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

        if (!headerNode.getChildren().isEmpty()) root.getChildren().add(headerNode);
        if (!bitmapNode1.getChildren().isEmpty()) root.getChildren().add(bitmapNode1);
        if (!bitmapNode2.getChildren().isEmpty()) root.getChildren().add(bitmapNode2);

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

        if (!headerNode.getChildren().isEmpty()) root.getChildren().add(headerNode);
        root.setExpanded(true);
        headerNode.setExpanded(true);
        treeView.setRoot(root);
    }

    public static ParseResult process(Map<String, String> mapValues) {
        Map<String, String> fieldsById = new HashMap<>();
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
            if (fieldId.isEmpty()) fieldId = "0";
            String data = nodeText.length() > 6 ? nodeText.substring(6).trim() : "";
            ISOFieldInfo dataType = getDataTypeISO8583(fieldId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalle del campo");
            alert.setHeaderText("Campo " + fieldId);
            String content = String.format("ID: %d%nNombre: %s%nTipo dato: %s%nCaracteristicas: %s%nLongitud: %d%nData: %s",
                    dataType.getId(), dataType.getTypeData(),
                    dataType.getTypeData(), dataType.getCaracteristicaDato(),
                    dataType.getLength(), data);
            alert.setContentText(content);
            alert.showAndWait();
        } catch (Exception e) {
            showSimpleInfo(nodeText);
        }
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
