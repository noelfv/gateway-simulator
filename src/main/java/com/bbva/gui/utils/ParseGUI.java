package com.bbva.gui.utils;

import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.gui.dto.ParseResult;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ParseGUI {



    public static void updateTreeView(DefaultTreeModel treeModel, JTree resultTree, ParseResult result) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");

        // Crear nodos categorizados
        DefaultMutableTreeNode headerNode = new DefaultMutableTreeNode("TypeMessage");
        DefaultMutableTreeNode bitmapNode1 = new DefaultMutableTreeNode("Bitmap1");
        DefaultMutableTreeNode bitmapNode2 = new DefaultMutableTreeNode("Bitmap2");
        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode("Campos de Datos");

        // Usar TreeMap para ordenar automáticamente por clave numérica
        Map<Integer, DefaultMutableTreeNode> sortedFieldsBitmap1 = new TreeMap<>();
        Map<Integer, DefaultMutableTreeNode> sortedFieldsBitmap2 = new TreeMap<>();

        String typeMessage = result.fieldsByDescription().get("messageType");
        headerNode.add(new DefaultMutableTreeNode("typeMessage : "+ typeMessage));

        for (Map.Entry<String, String> entry : result.fieldsById().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            String nodeText = "P"+ UtilGUI.padLeft(field,3,'0') + ": " + "["+value+"]";
            DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(nodeText);

            // Categorizar campos
            if (field.equals("0")) {
                fieldNode.setUserObject("bitmap_primary : " + ISOUtil.convertBITMAPtoHEX(value));
                bitmapNode1.add(fieldNode);
         /*   }if (field.equals("0")) {
                fieldNode.setUserObject("bitmap_prymary : " + ISOUtil.convertBITMAPtoHEX(value));
                bitmapNode1.add(fieldNode);*/
            }else {
                // Para campos numéricos, intentar convertir a entero
                try {
                    int fieldNumber = Integer.parseInt(field);
                    if (fieldNumber < 65) {
                        // Campos del bitmap primario (1-64)
                        String bitmap1=(String)fieldNode.getUserObject();
                        sortedFieldsBitmap1.put(fieldNumber, fieldNode);
                    } else {
                        sortedFieldsBitmap2.put(fieldNumber, fieldNode);
                    }
                } catch (NumberFormatException e) {
                    // Si no es numérico, agregar directamente a campos de datos
                    fieldsNode.add(fieldNode);
                }
            }
        }

        // Agregar los campos ordenados numéricamente
        for (DefaultMutableTreeNode node : sortedFieldsBitmap1.values()) {
            bitmapNode1.add(node);
        }
        for (DefaultMutableTreeNode node : sortedFieldsBitmap2.values()) {
            bitmapNode2.add(node);
        }

        if (headerNode.getChildCount() > 0) root.add(headerNode);
        if (bitmapNode1.getChildCount() > 0) root.add(bitmapNode1);
        if (bitmapNode2.getChildCount() > 0) root.add(bitmapNode2);

        treeModel.setRoot(root);

        // Expandir todos los nodos
        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
        }
    }


    public static ParseResult process(Map<String,String> mapValues) throws Exception {

        Map<String, String> fieldsById = new HashMap<>();

        // Iterar sobre los campos ya mapeados
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Para el mapa por ID, necesitamos mapear las descripciones a IDs
            String fieldId = findFieldIdByName(key);
            if (fieldId != null) {
                fieldsById.put(fieldId, value);
            } else {
                // Si no encontramos el ID, usar la descripción como clave
                fieldsById.put(key, value);
            }
        }

        return new ParseResult(mapValues, fieldsById);
    }

    private static  String findFieldIdByName(String fieldName) {
        for (ISOField field : ISOFieldMastercard.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }



    public static void showNodeDetails(DefaultMutableTreeNode node, int x, int y) {
        String nodeText = node.getUserObject().toString();

        // Extraer información del nodo
        String title = "Detalle del campo";
        String message = nodeText;

        // Si es un nodo de campo de datos, intentar extraer más información
        if (nodeText.startsWith("P")) {
            try {
                // Extraer el ID del campo (P001, P002, etc.)
                String fieldId = nodeText.substring(1, 4).trim();
                while (fieldId.startsWith("0") && fieldId.length() > 1) {
                    fieldId = fieldId.substring(1);
                }

                // Buscar información adicional del campo
                ISOFieldInfo dataType = getDataTypeISO8583(fieldId);

                // Crear panel personalizado para el popup
                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Título
                JLabel titleLabel = new JLabel("Campo " + fieldId);
                titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
                panel.add(titleLabel, BorderLayout.NORTH);

                // Contenido
                JPanel contentPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                contentPanel.add(new JLabel("ID: " + dataType.getId()));
                contentPanel.add(new JLabel("Tipo dato: " + dataType.getTypeData()));
                contentPanel.add(new JLabel("Caracteristicas: " + dataType.getCaracteristicaDato()));
                contentPanel.add(new JLabel("Longitud: " + dataType.getLength()));

                panel.add(contentPanel, BorderLayout.CENTER);

                // Mostrar el panel en un JOptionPane
                JOptionPane.showMessageDialog(null, panel, title, JOptionPane.INFORMATION_MESSAGE);
                return;
            } catch (Exception e) {
                // Si hay algún error, mostrar el mensaje simple
            }
        }
        // Si no es un campo especial o hubo error, mostrar mensaje simple
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }


    private static ISOFieldInfo getDataTypeISO8583(String fieldId) {
        try {
            int id = Integer.parseInt(fieldId);
            // Buscar en el enum ISOFieldMastercard por el ID numérico
            for (ISOFieldMastercard field : ISOFieldMastercard.values()) {
                if (field.getId() == id) {
                    //return field.getDescription();
                    if(!field.isVariable()){
                        return new ISOFieldInfo(field.getId(),field.getName(),
                                field.getTypeData().name(), "FIXED", field.getLength());
                    }

                    return new ISOFieldInfo(field.getId(),field.getName(),
                            field.getTypeData().name(), "VARIABLE", field.getLength());
                }
            }
        } catch (NumberFormatException e) {
            // Si no es un número válido, no hacer nada
        }

        return new ISOFieldInfo();
    }


}
