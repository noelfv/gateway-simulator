package org.example.gui.aaa;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Y extends JDialog {

    // Colores y Fuentes recomendados para una apariencia elegante y amigable
    private static final Color PRIMARY_ACCENT_COLOR = new Color(0, 102, 204); // Azul vibrante
    private static final Color SECONDARY_BG_COLOR = new Color(245, 245, 245); // Gris muy claro para fondos de panel
    private static final Color BORDER_COLOR = new Color(180, 180, 180); // Gris claro para bordes
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Gris oscuro para texto

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font COMPONENT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public Y(JFrame parentFrame) {
        super(parentFrame, "Generador de Trama", true); // Título y modal (true)
        initializeComponents(parentFrame);
        // --- Generar 5 Filas de Componentes ---

    }


    void initializeComponents(JFrame parentFrame){

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Cierra solo este diálogo al presionar X
        setSize(800, 600); // Tamaño adecuado para el diálogo
        setLocationRelativeTo(parentFrame); // Centrar respecto al frame padre

        // Contenedor principal del diálogo
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE); // Fondo blanco para el panel principal
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margen alrededor de todo el panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Espaciado entre componentes (top, left, bottom, right)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes se expanden horizontalmente

        // --- Encabezado del Panel ---
        JLabel headerLabel = new JLabel("Configuración de Generación de Trama");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_ACCENT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4; // Ocupa las 4 columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centrar el encabezado
        mainPanel.add(headerLabel, gbc);

        // Reset gridwidth y anchor para las filas de datos
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0; // Etiquetas no se expanden horizontalmente
        gbc.weighty = 0.0; // No se expanden verticalmente

        // Añadir una pequeña separación después del encabezado
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 15, 0); // Más espacio después del encabezado
        mainPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(8, 5, 8, 5); // Volver al espaciado normal para los componentes de fila

        String[] comboOptions = {"Opción A", "Opción B", "Opción C"};

        for (int i = 0; i < 5; i++) {
            gbc.gridy = i + 2; // +2 para empezar después del encabezado y el separador

            // JLabel
            JLabel label = new JLabel("Parámetro " + (i + 1) + ":");
            label.setFont(LABEL_FONT);
            label.setForeground(TEXT_COLOR);
            gbc.gridx = 0;
            gbc.weightx = 0.1; // Pequeño peso para que la columna de la etiqueta no sea demasiado ancha
            mainPanel.add(label, gbc);

            // JTextField
            JTextField textField = new JTextField(15); // Tamaño preferido inicial
            textField.setFont(COMPONENT_FONT);
            textField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1), // Borde elegante
                    new EmptyBorder(5, 5, 5, 5)     // Padding interno
            ));
            textField.setForeground(TEXT_COLOR);
            gbc.gridx = 1;
            gbc.weightx = 0.4; // Mayor peso para el campo de texto
            mainPanel.add(textField, gbc);

            // JComboBox
            JComboBox<String> comboBox = new JComboBox<>(comboOptions);
            comboBox.setFont(COMPONENT_FONT);
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(new LineBorder(BORDER_COLOR, 1)); // Borde elegante
            comboBox.setForeground(TEXT_COLOR);
            gbc.gridx = 2;
            gbc.weightx = 0.3; // Peso para el combo box
            mainPanel.add(comboBox, gbc);

            // JCheckBox
            JCheckBox checkBox = new JCheckBox("Activar");
            checkBox.setFont(COMPONENT_FONT);
            checkBox.setForeground(TEXT_COLOR);
            checkBox.setBackground(mainPanel.getBackground()); // Para que el fondo sea el mismo que el del panel
            gbc.gridx = 3;
            gbc.weightx = 0.2; // Peso para el checkbox
            mainPanel.add(checkBox, gbc);
        }

        // Un panel de relleno al final para empujar todo hacia arriba si hay espacio extra
        gbc.gridy = 7; // Después de las 5 filas + encabezado + separador
        gbc.weighty = 1.0; // Permite que esta fila ocupe todo el espacio vertical restante
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc); // Panel vacío y transparente


        // Añadir el mainPanel al content pane del JDialog
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    void filaPan(){

    }

    void panelBoton(){
        // Aquí puedes inicializar otros componentes o añadir botones de acción
        JButton okButton = new JButton("Aceptar");
        okButton.setFont(COMPONENT_FONT);
        okButton.setBackground(PRIMARY_ACCENT_COLOR);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes manejar la acción del botón Aceptar
                dispose(); // Cierra el diálogo
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(COMPONENT_FONT);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra el diálogo sin hacer nada
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Alinear a la derecha
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH); // Añadir el panel de botones al sur del diálogo

    }
    // Método main de ejemplo para probar solo este diálogo
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Un JFrame dummy para actuar como padre (puedes usar tu clase principal real)
            JFrame dummyParentFrame = new JFrame();
            dummyParentFrame.setSize(400, 300);
            dummyParentFrame.setLocationRelativeTo(null);
            dummyParentFrame.setVisible(false); // No es necesario que sea visible

            Y dialog = new Y(dummyParentFrame);
            dialog.setVisible(true); // Muestra el diálogo
            dummyParentFrame.dispose(); // Cierra el frame dummy cuando el diálogo se cierra
        });
    }
}