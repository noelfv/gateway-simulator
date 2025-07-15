package org.example.gui.panels;


import org.example.gui.componentes.NoIconTreeCellRenderer;
import org.example.gui.componentes.TextFieldTreeCellEditor;
import org.example.gui.componentes.TextFieldTreeCellRenderer;
import org.example.gui.componentes.TreeNodeData;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

public class TramaExamplesViewerPanel extends JPanel {

    private JTextArea outputTextArea;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private MyDoggyToolWindowManager toolWindowManager;
    private DefaultMutableTreeNode top;
    private TreeNodeData currentEditingData;

    public TramaExamplesViewerPanel() {
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

        // 1. Crear el nodo raíz del árbol
        top = new DefaultMutableTreeNode("Tramas de ejemplo");

        // 2. Crear los nodos principales (primer nivel)
        DefaultMutableTreeNode m0100 = new DefaultMutableTreeNode("0100");
        DefaultMutableTreeNode m0120 = new DefaultMutableTreeNode("0120");
        DefaultMutableTreeNode m0400 = new DefaultMutableTreeNode("0400");

        // 3. Añadir los nodos principales al nodo raíz
        top.add(m0100);
        top.add(m0120);
        top.add(m0400);

        // 4. Crear subnodos para "Documentos"
        DefaultMutableTreeNode ecomerce = new DefaultMutableTreeNode("Ecomerce");
        DefaultMutableTreeNode pos = new DefaultMutableTreeNode("POS");
        DefaultMutableTreeNode atm = new DefaultMutableTreeNode("ATM");
        m0100.add(ecomerce);
        m0100.add(pos);
        m0100.add(atm);

        // 5. Crear subnodos para "Imágenes"
        DefaultMutableTreeNode fotosVacaciones = new DefaultMutableTreeNode("Data erronea");
        DefaultMutableTreeNode fotosFamilia = new DefaultMutableTreeNode("Reverso");
        DefaultMutableTreeNode capturasPantalla = new DefaultMutableTreeNode("Cancelacion");
        m0400.add(fotosVacaciones);
        m0400.add(fotosFamilia);
        m0400.add(capturasPantalla);

        // 6. Crear sub-subnodos para "Vacaciones 2024"
        fotosVacaciones.add(new DefaultMutableTreeNode("Reverso"));
        fotosVacaciones.add(new DefaultMutableTreeNode("Cancelacion"));

        // 7. Crear subnodos para "Música"
        m0120.add(new DefaultMutableTreeNode("Aviso"));

        // 8. Crear el JTree con el nodo raíz
        tree = new JTree(top);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                String nodeName = selectedNode.getUserObject().toString();

                // Verificar si es el nodo m0100
                if ("0100".equals(nodeName)) {
                    openTextAreaForNode("0100");
                } else if ("0120".equals(nodeName)) {
                    openTextAreaForNode("0120");
                } else if ("0400".equals(nodeName)) {
                    openTextAreaForNode("0400");
                }
            }
        });
       // tree.expandPath(new TreePath(documentos.getPath()));
        //tree.expandPath(new TreePath(fotosVacaciones.getPath()));
        //JScrollPane scrollPane = new JScrollPane(tree);

    }


    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();
        // Panel principal con entrada y botones
        //JPanel mainPanel = createMainPanel();
        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
       //contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Data", null, contentPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(tree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);
        treeToolWindow.setActive(true);

        add(toolWindowManager, BorderLayout.CENTER);

    }


    private void openTextAreaForNode(String nodeType) {
        // Crear un nuevo TextArea para el nodo específico
       /* JTextArea nodeTextArea = new JTextArea(15, 50);
        nodeTextArea.setLineWrap(true);
        nodeTextArea.setWrapStyleWord(true);
        nodeTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));*/

        // Establecer contenido específico según el tipo de nodo
        switch (nodeType) {
            case "0100":
                outputTextArea.setText("Contenido para trama 0100:\n\n" +
                        "Ejemplo de estructura de datos para mensaje 0100...");
                break;
            case "0120":
                outputTextArea.setText("Contenido para trama 0120:\n\n" +
                        "Ejemplo de estructura de datos para mensaje 0120...");
                break;
            case "0400":
                outputTextArea.setText("Contenido para trama 0400:\n\n" +
                        "Ejemplo de estructura de datos para mensaje 0400...");
                break;
        }

        // Crear o actualizar el ToolWindow con el TextArea
        ToolWindow existingWindow = toolWindowManager.getToolWindow("content_" + nodeType);
        if (existingWindow != null) {
            // Si ya existe, solo activarlo
            existingWindow.setActive(true);
        } else {
            // Crear nuevo ToolWindow
            ToolWindow contentWindow = toolWindowManager.registerToolWindow(
                    "content_" + nodeType,
                    "Trama " + nodeType,
                    null,
                    new JScrollPane(outputTextArea),
                    ToolWindowAnchor.RIGHT
            );
            contentWindow.setAvailable(true);
            contentWindow.setActive(true);
        }

        add(toolWindowManager, BorderLayout.CENTER);
    }


   /* private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        inputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }*/


}