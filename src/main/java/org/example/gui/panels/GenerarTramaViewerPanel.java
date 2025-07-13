package org.example.gui.panels;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.example.gui.componentes.*;
import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.common.ISOFieldInfo;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.iso8583.ISO8583;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.*;

public class GenerarTramaViewerPanel extends JPanel {

    private JTextArea outputTextArea;
    private JTree resultTree;
    private JButton processarButton;
    private DefaultTreeModel treeModel;
    private MyDoggyToolWindowManager toolWindowManager;
    private ISO8583 iso8583;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFieldsByDescription;
    private boolean editorListenerAdded = false;

    public GenerarTramaViewerPanel() {
        initializeComponents();
        setupEventHandlers();
        setupMyDoggy();
    }


    private void initializeComponents() {

        setLayout(new BorderLayout());
        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);
        //outputTextArea.setEnabled(false);
        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);
        processarButton = new JButton("Procesar");

    }


    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();
        // Panel principal con entrada y botones
        JPanel mainPanel = createMainPanel();
        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, contentPanel);
        // Agregar el panel principal como contenido central
        //toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, mainPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);

        // Tool Window para resultados
        /*ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.RIGHT);
        outputToolWindow.setAvailable(true);*/
        // Cambiar esta línea para obtener el componente correcto
        updateTreeView();
        add(toolWindowManager, BorderLayout.CENTER);

    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        inputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(processarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }


    private void setupEventHandlers() {
        processarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOutputTextArea();
            }
        });
    }


    private void updateTreeView() {
        // Solo crear el árbol una vez, no recrear si ya existe
        if (treeModel.getRoot() == null ||
                !treeModel.getRoot().toString().equals("Campos ISO8583")) {

            // Crear raíz con objeto personalizado
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campos ISO8583");
            for (int i = 2; i <= 10; i++) {
                root.add(new DefaultMutableTreeNode(
                        new TreeNodeData("P" + ISOUtil.padLeft("" + i, 3, '0'), "")));
            }
            treeModel.setRoot(root);
        }

        // Configurar renderer y editor
        resultTree.setCellRenderer(new TextFieldTreeCellRenderer());
        resultTree.setCellEditor(new TextFieldTreeCellEditor());
        resultTree.setEditable(true);
        resultTree.setInvokesStopCellEditing(true);

        // Agregar listener solo una vez
        if (!editorListenerAdded) {
            resultTree.getCellEditor().addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    /*TreePath path = resultTree.getSelectionPath();
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Object userObject = node.getUserObject();
                        if (userObject instanceof TreeNodeData) {
                            String value = (String) resultTree.getCellEditor().getCellEditorValue();
                            ((TreeNodeData) userObject).setValue(value);
                            treeModel.nodeChanged(node);
                        }
                    }*/
                    treeModel.nodeChanged((TreeNode) resultTree.getSelectionPath().getLastPathComponent());

                }

                @Override
                public void editingCanceled(ChangeEvent e) {
                    System.out.println("Edición cancelada");
                }
            });
            editorListenerAdded = true;
        }

        // Expandir todos los nodos
        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
        }
    }


    private void collectTreeData(DefaultMutableTreeNode node, StringBuilder output, String indent) {
        if (node == null) return;

        Object userObject = node.getUserObject();
        boolean nodoTieneValor = false;

        if (userObject instanceof TreeNodeData) {
            TreeNodeData data = (TreeNodeData) userObject;
            String label = data.getLabel();
            String value = data.getValue();
            int fieldId = extractFieldIdFromLabel(label);
            Map<String, String> mapData = new HashMap<>();
            if (value != null && !value.isBlank()) {

                ISOFieldMastercard.findById(fieldId).ifPresent(field -> {
                    ISOFieldInfo fieldInfo = new ISOFieldInfo(
                            field.getName(),
                            data.getValue()
                    );
                    mapData.put(field.getName(),data.getValue());
                    System.out.println("tamaño " + mapData.size());
                    System.out.println("data " + mapData);
                });

                output.append(indent)
                        .append(data.getLabel())
                        .append(" = ")
                        .append(value)
                        .append("\n");
                nodoTieneValor = true;
            }

        }

        // Recorremos hijos recursivamente
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collectTreeData(child, output, indent + "  "); // indentación visual
        }
    }


    private void collectTreeData(DefaultMutableTreeNode node, StringBuilder output, String indent, Map<String, String> mapData) {
        if (node == null) return;

        Object userObject = node.getUserObject();

        if (userObject instanceof TreeNodeData data) {
            String label = data.getLabel();
            String value = data.getValue();

            if (value != null && !value.isBlank()) {
                int fieldId = extractFieldIdFromLabel(label);

                ISOFieldMastercard.findById(fieldId).ifPresent(field -> {
                    mapData.put(field.getName(), value);
                    System.out.println("Map actualizado: " + mapData);
                });

                output.append(indent)
                        .append(label)
                        .append(" = ")
                        .append(value)
                        .append("\n");
            }
        }

        // Recorremos hijos recursivamente (pasando el mismo mapData)
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collectTreeData(child, output, indent + "  ", mapData);
        }
    }



    private void updateOutputTextArea() {
        StringBuilder output = new StringBuilder();
        Map<String, String> isoDataMap = new HashMap<>();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) resultTree.getModel().getRoot();
        collectTreeData(root, output, "", isoDataMap);

        outputTextArea.setText(output.toString());

        ISO8583 iso = mapToISO8583(isoDataMap);
        System.out.println(iso.getProcessingCode());
        System.out.println(iso.getPrimaryAccountNumber());
    }

    private int extractFieldIdFromLabel(String label) {
        try {
            // Elimina la "P" y convierte a entero
            return Integer.parseInt(label.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1; // o lanza una excepción si prefieres
        }
    }

    public ISO8583 mapToISO8583(Map<String, String> fieldMap) {
        ISO8583.ISO8583Builder builder = ISO8583.builder();   // usa tu builder real
        Class<?> bClass = builder.getClass();

        fieldMap.forEach((key, rawValue) -> {
            if (rawValue == null || rawValue.isBlank()) return;

            // Elimina espacios accidentales
            String methodName = key.trim();

            // Busca un método con ese nombre y un solo parámetro
            Method setter = Arrays.stream(bClass.getMethods())
                    .filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);

            if (setter == null) {
                System.out.println("Setter no encontrado en builder: " + methodName);
                return;
            }

            Class<?> paramType = setter.getParameterTypes()[0];
            Object converted;

            try {
                // Conversión básica si el campo NO es String
                if (paramType.equals(String.class)) {
                    converted = rawValue;
                } else if (paramType.equals(Integer.class) || paramType.equals(int.class)) {
                    converted = Integer.valueOf(rawValue);
                } else if (paramType.equals(Boolean.class) || paramType.equals(boolean.class)) {
                    converted = Boolean.valueOf(rawValue);
                } else {
                    System.out.println("Tipo no soportado para " + methodName + ": " + paramType.getSimpleName());
                    return;
                }

                setter.invoke(builder, converted);

            } catch (Exception ex) {
                throw new RuntimeException("Error asignando " + methodName + ": '" + rawValue + "'", ex);
            }
        });

        return builder.build();
    }


}