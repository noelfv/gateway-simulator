package org.example.gui.panels;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.example.gui.componentes.*;
import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.iso8583.ISO8583;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GeneratePanelGpt extends JPanel {

    private JTextArea outputTextArea;
    private JTree resultTree;
    private JButton processarButton;
    private DefaultTreeModel treeModel;
    private MyDoggyToolWindowManager toolWindowManager;
    private ISO8583 iso8583;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFieldsByDescription;
    private boolean editorListenerAdded = false;

    public GeneratePanelGpt() {
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
            for (int i = 1; i <= 10; i++) {
                root.add(new DefaultMutableTreeNode(
                        new TreeNodeData("P" + ISOUtil.padLeft("" + i, 3, '0'), "")));
            }
            treeModel.setRoot(root);
        }

        // Configurar renderer y editor
        resultTree.setCellRenderer(new CellRendererGpt());
        resultTree.setCellEditor(new CellEditorGpt());
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
        if (userObject instanceof TreeNodeData) {
            TreeNodeData data = (TreeNodeData) userObject;
            output.append(indent)
                    .append(data.getLabel())
                    .append(" = ")
                    .append(data.getValue())
                    .append("\n");
        }

        // Recorremos hijos recursivamente
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collectTreeData(child, output, indent + "  "); // indentación visual
        }
    }

    private void updateOutputTextArea() {
        StringBuilder output = new StringBuilder();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) resultTree.getModel().getRoot();
        collectTreeData(root, output, "");
        outputTextArea.setText(output.toString());
    }
}