package org.example.gui.aaa;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.Objects;

public class CustomJTreeNodesExample extends JFrame {

    public CustomJTreeNodesExample() {
        setTitle("JTree con Nodos Personalizados (JTextField y JComboBox)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // 1. Crear el nodo raíz
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Configuración del Sistema");

        // 2. Crear nodos con diferentes tipos de datos (para que el renderer los interprete)
        DefaultMutableTreeNode userSettings = new DefaultMutableTreeNode("Configuración de Usuario");
        DefaultMutableTreeNode networkSettings = new DefaultMutableTreeNode("Configuración de Red");
        DefaultMutableTreeNode displaySettings = new DefaultMutableTreeNode("Configuración de Pantalla");

        root.add(userSettings);
        root.add(networkSettings);
        root.add(displaySettings);

        // Nodos para JText
        userSettings.add(new DefaultMutableTreeNode(new NodeTextData("Nombre de Usuario", "admin")));
        userSettings.add(new DefaultMutableTreeNode(new NodeTextData("Contraseña", "********")));

        // Nodos para JComboBox
        String[] protocols = {"HTTP", "HTTPS", "FTP", "SFTP"};
        networkSettings.add(new DefaultMutableTreeNode(new NodeComboData("Protocolo", protocols, "HTTPS")));
        String[] connectionTypes = {"Wi-Fi", "Ethernet", "Móvil"};
        networkSettings.add(new DefaultMutableTreeNode(new NodeComboData("Tipo de Conexión", connectionTypes, "Wi-Fi")));

        // Nodo mixto (texto normal + sub-nodos con componentes)
        displaySettings.add(new DefaultMutableTreeNode("Resolución"));
        DefaultMutableTreeNode resolutionNode = (DefaultMutableTreeNode) displaySettings.getLastChild();
        resolutionNode.add(new DefaultMutableTreeNode(new NodeTextData("Ancho", "1920")));
        resolutionNode.add(new DefaultMutableTreeNode(new NodeTextData("Alto", "1080")));

        String[] refreshRates = {"60Hz", "75Hz", "120Hz", "144Hz"};
        displaySettings.add(new DefaultMutableTreeNode(new NodeComboData("Frecuencia de Actualización", refreshRates, "60Hz")));


        // 3. Crear el JTree
        JTree tree = new JTree(root);

        // 4. Establecer el Cell Renderer personalizado
        tree.setCellRenderer(new CustomNodeRenderer());

        // 5. Expandir algunos nodos por defecto
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        // 6. Añadir el JTree a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tree);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    // --- Clases Auxiliares para los Datos del Nodo ---

    // Clase base para los datos de los nodos personalizados
    static class NodeData {
        private String label;

        public NodeData(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label; // Esto es lo que se mostrará si no se usa el renderer personalizado
        }
    }

    // Clase para nodos que contendrán un JTextField
    static class NodeTextData extends NodeData {
        private String value;

        public NodeTextData(String label, String value) {
            super(label);
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Clase para nodos que contendrán un JComboBox
    static class NodeComboData extends NodeData {
        private String[] options;
        private String selectedOption;

        public NodeComboData(String label, String[] options, String selectedOption) {
            super(label);
            this.options = options;
            this.selectedOption = selectedOption;
        }

        public String[] getOptions() {
            return options;
        }

        public String getSelectedOption() {
            return selectedOption;
        }
    }

    // --- Implementación del Cell Renderer Personalizado ---

    static class CustomNodeRenderer extends DefaultTreeCellRenderer {

        private JPanel panel;
        private JLabel labelComponent; // Para el texto del nodo
        private JTextField textField;
        private JComboBox<String> comboBox;

        public CustomNodeRenderer() {
            // Inicializar los componentes que se usarán para dibujar
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // FlowLayout para alinear horizontalmente
            panel.setOpaque(false); // Para que el fondo del panel sea transparente y se vea el fondo del árbol

            labelComponent = new JLabel();
            textField = new JTextField(10); // Tamaño inicial
            comboBox = new JComboBox<>();

            panel.add(labelComponent);
            // No añadir textField ni comboBox directamente al panel aquí,
            // se añadirán condicionalmente en getTreeCellRendererComponent
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            // Limpiar el panel de componentes anteriores
            panel.removeAll();

            // Llamar a la implementación por defecto para manejar el color de selección y el icono
            // Esto es crucial para que los iconos de expansión/colapso y el color de selección funcionen
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            // Obtener el nodo mutable del árbol
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject(); // Obtener el objeto real que el nodo representa

            // Configurar el color de fondo para la selección (importante hacerlo aquí)
            if (selected) {
                panel.setBackground(getBackgroundSelectionColor());
                labelComponent.setForeground(getTextSelectionColor());
                textField.setBackground(getBackgroundSelectionColor());
                textField.setForeground(getTextSelectionColor());
                comboBox.setBackground(getBackgroundSelectionColor());
                comboBox.setForeground(getTextSelectionColor());
            } else {
                panel.setBackground(getBackgroundNonSelectionColor());
                labelComponent.setForeground(getTextNonSelectionColor());
                textField.setBackground(getBackgroundNonSelectionColor());
                textField.setForeground(getTextNonSelectionColor());
                comboBox.setBackground(getBackgroundNonSelectionColor());
                comboBox.setForeground(getTextNonSelectionColor());
            }

            // Establecer el icono del nodo (si hay uno, o por defecto)
            // Esto es un poco delicado con un panel, ya que el super.getTreeCellRendererComponent()
            // dibuja el icono. Aquí estamos construyendo un nuevo componente.
            // Para mantener el icono del JTree, podrías añadir un JLabel para el icono.
            // Por simplicidad, este ejemplo se centra en los JTextField/JComboBox.
            // Si quieres los iconos, tendrías que obtenerlos de super.getIcon() y añadirlos.
            Icon icon = getIcon();
            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                panel.add(iconLabel); // Añadir el icono primero
            }


            // Lógica para dibujar el componente según el tipo de objeto de usuario
            if (userObject instanceof NodeTextData) {
                NodeTextData data = (NodeTextData) userObject;
                labelComponent.setText(data.getLabel() + ": "); // Añadir el label
                textField.setText(data.getValue());
                panel.add(labelComponent);
                panel.add(textField);
            } else if (userObject instanceof NodeComboData) {
                NodeComboData data = (NodeComboData) userObject;
                labelComponent.setText(data.getLabel() + ": ");
                comboBox.setModel(new DefaultComboBoxModel<>(data.getOptions()));
                comboBox.setSelectedItem(data.getSelectedOption());
                panel.add(labelComponent);
                panel.add(comboBox);
            } else {
                // Para nodos de texto normales
                labelComponent.setText(Objects.toString(userObject, ""));
                panel.add(labelComponent);
            }

            // Devolver el panel que contiene los componentes
            return panel;
        }
    }

    public static void main(String[] args) {
        // Establecer el Look and Feel (opcional, pero mejora la apariencia)
        try {
            // Usa el Look and Feel del sistema para una integración nativa
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new CustomJTreeNodesExample().setVisible(true);
        });
    }
}