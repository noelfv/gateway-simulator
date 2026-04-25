package com.bbva.gui.commons;

import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.utils.ISOUtil;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public final class FXParseGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FXParseGUI.class);

    private FXParseGUI() {
    }

    public static void updateTreeView(TreeView<String> treeView, ParseResult result) {
        TreeItem<String> root = new TreeItem<>("Mensaje Parseado");
        TreeItem<String> header = new TreeItem<>("TypeMessage");
        TreeItem<String> bitmap1 = new TreeItem<>("Bitmap1");
        TreeItem<String> bitmap2 = new TreeItem<>("Bitmap2");

        Map<Integer, TreeItem<String>> sorted1 = new TreeMap<>();
        Map<Integer, TreeItem<String>> sorted2 = new TreeMap<>();

        String typeMessage = result.fieldsByDescription().get("messageType");
        if (typeMessage != null)
            header.getChildren().add(new TreeItem<>(typeMessage));

        for (Map.Entry<String, String> entry : result.fieldsById().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            String nodeText = "P" + UtilGUI.padLeft(field, 3, '0') + ": [" + value + "]";
            TreeItem<String> fieldNode = new TreeItem<>(nodeText);

            if (field.equals("0")) {
                bitmap1.getChildren().add(new TreeItem<>("P000: [" + ISOUtil.convertBITMAPtoHEX(value) + "]"));
            } else if (field.equals("1")) {
                bitmap1.getChildren().add(new TreeItem<>("P001: [" + ISOUtil.convertBITMAPtoHEX(value) + "]"));
            } else {
                try {
                    int num = Integer.parseInt(field);
                    (num < 65 ? sorted1 : sorted2).put(num, fieldNode);
                } catch (NumberFormatException e) {
                    bitmap1.getChildren().add(fieldNode);
                }
            }
        }

        sorted1.values().forEach(n -> bitmap1.getChildren().add(n));
        sorted2.values().forEach(n -> bitmap2.getChildren().add(n));

        if (!header.getChildren().isEmpty())
            root.getChildren().add(header);
        if (!bitmap1.getChildren().isEmpty())
            root.getChildren().add(bitmap1);
        if (!bitmap2.getChildren().isEmpty())
            root.getChildren().add(bitmap2);

        root.setExpanded(true);
        header.setExpanded(true);
        bitmap1.setExpanded(true);
        bitmap2.setExpanded(true);
        treeView.setRoot(root);
    }

    public static void updateTreeViewTLV(TreeView<String> treeView, ParseResult result) {
        TreeItem<String> root = new TreeItem<>("DATO TLV");
        TreeItem<String> header = new TreeItem<>("Campo 48");

        result.fieldsById().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> header.getChildren().add(
                        new TreeItem<>(e.getKey() + ": [" + e.getValue() + "]")));

        if (!header.getChildren().isEmpty())
            root.getChildren().add(header);
        root.setExpanded(true);
        header.setExpanded(true);
        treeView.setRoot(root);
    }
}
