package org.example.gui.panels;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.example.gui.componentes.NoIconTreeCellRenderer;
import org.example.gui.componentes.TreeNodeData;
import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.iso8583.ISO8583;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import org.example.gui.componentes.TextFieldTreeCellRenderer;
import org.example.gui.componentes.TextFieldTreeCellEditor;

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

    // Mapa para almacenar los valores de cada nodo
    private Map<String, String> nodeValues = new HashMap<>();

    public GenerarTramaViewerPanel() {
        initializeComponents();
        setupMyDoggy();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

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

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);

        // Inicializar el árbol
        initializeTree();

        add(toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        inputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }

    private void initializeTree() {
        // Crear raíz
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campos ISO8583");

        // Crear nodos hijos con TreeNodeData
        for (int i = 1; i <= 10; i++) {
            String nodeKey = "P" + ISOUtil.padLeft("" + i, 3, '0');
            TreeNodeData nodeData = new TreeNodeData(nodeKey, "");
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeData);
            root.add(childNode);
        }

        treeModel.setRoot(root);

        // Configurar renderer y editor
        resultTree.setCellRenderer(new TextFieldTreeCellRenderer());
        resultTree.setCellEditor(new TextFieldTreeCellEditor());
        resultTree.setEditable(true);

        // Agregar listener para manejar cambios
        setupEditorListener();

        // Expandir todos los nodos
        expandAllNodes();
    }

    private void setupEditorListener() {
        if (!editorListenerAdded) {
            resultTree.getCellEditor().addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    saveCurrentNodeValue();
                }

                @Override
                public void editingCanceled(ChangeEvent e) {
                    System.out.println("Edición cancelada");
                }
            });
            editorListenerAdded = true;
        }
    }

    private void saveCurrentNodeValue() {
        TreePath path = resultTree.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object userObject = node.getUserObject();

            if (userObject instanceof TreeNodeData) {
                TreeNodeData nodeData = (TreeNodeData) userObject;
                String value = (String) resultTree.getCellEditor().getCellEditorValue();

                // Guardar en el objeto TreeNodeData
                nodeData.setValue(value);

                // También guardar en el mapa para respaldo
                nodeValues.put(nodeData.getLabel(), value);

                System.out.println("Valor guardado: '" + value + "' para nodo: " + nodeData.getLabel());

                // Notificar cambio al modelo
                treeModel.nodeChanged(node);
            }
        }
    }

    private void expandAllNodes() {
        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
        }
    }

    // Método para obtener todos los valores ingresados
    public Map<String, String> getAllNodeValues() {
        Map<String, String> allValues = new HashMap<>();

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            Object userObject = child.getUserObject();

            if (userObject instanceof TreeNodeData) {
                TreeNodeData nodeData = (TreeNodeData) userObject;
                allValues.put(nodeData.getLabel(), nodeData.getValue() != null ? nodeData.getValue() : "");
            }
        }

        return allValues;
    }

    // Método para establecer valores programáticamente
    public void setNodeValue(String nodeLabel, String value) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            Object userObject = child.getUserObject();

            if (userObject instanceof TreeNodeData) {
                TreeNodeData nodeData = (TreeNodeData) userObject;
                if (nodeData.getLabel().equals(nodeLabel)) {
                    nodeData.setValue(value);
                    nodeValues.put(nodeLabel, value);
                    treeModel.nodeChanged(child);
                    break;
                }
            }
        }
    }

    // Método para imprimir todos los valores (para debugging)
    public void printAllValues() {
        System.out.println("=== Valores actuales ===");
        Map<String, String> values = getAllNodeValues();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            System.out.println(entry.getKey() + ": '" + entry.getValue() + "'");
        }
    }
}