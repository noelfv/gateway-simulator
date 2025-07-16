package org.example.gui.temp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class X extends JPanel {

    // Colores y Fuentes recomendados para una apariencia elegante y amigable
    private static final Color PRIMARY_ACCENT_COLOR = new Color(0, 102, 204); // Azul vibrante
    private static final Color SECONDARY_BG_COLOR = new Color(245, 245, 245); // Gris muy claro para fondos de panel
    private static final Color BORDER_COLOR = new Color(180, 180, 180); // Gris claro para bordes
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Gris oscuro para texto

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font COMPONENT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);


    public X() {
        // Usamos GridBagLayout para un control preciso sobre la disposición
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE); // Fondo blanco para el panel principal
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Margen alrededor de todo el panel

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
        add(headerLabel, gbc);

        // Reset gridwidth y anchor para las filas de datos
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0; // Etiquetas no se expanden horizontalmente
        gbc.weighty = 0.0; // No se expanden verticalmente

        // Añadir una pequeña separación después del encabezado
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 15, 0); // Más espacio después del encabezado
        add(new JSeparator(), gbc);
        gbc.insets = new Insets(8, 5, 8, 5); // Volver al espaciado normal para los componentes de fila

        // --- Generar 5 Filas de Componentes ---
        String[] comboOptions = {"Opción 1", "Opción 2", "Opción 3"};

        for (int i = 0; i < 5; i++) {
            gbc.gridy = i + 2; // +2 para empezar después del encabezado y el separador

            // JLabel
            JLabel label = new JLabel("Campo " + (i + 1) + ":");
            label.setFont(LABEL_FONT);
            label.setForeground(TEXT_COLOR);
            gbc.gridx = 0;
            gbc.weightx = 0.1; // Pequeño peso para que la columna de la etiqueta no sea demasiado ancha
            add(label, gbc);

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
            add(textField, gbc);

            // JComboBox
            JComboBox<String> comboBox = new JComboBox<>(comboOptions);
            comboBox.setFont(COMPONENT_FONT);
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(new LineBorder(BORDER_COLOR, 1)); // Borde elegante
            comboBox.setForeground(TEXT_COLOR);
            gbc.gridx = 2;
            gbc.weightx = 0.3; // Peso para el combo box
            add(comboBox, gbc);

            // JCheckBox
            JCheckBox checkBox = new JCheckBox("Habilitar");
            checkBox.setFont(COMPONENT_FONT);
            checkBox.setForeground(TEXT_COLOR);
            checkBox.setBackground(getBackground()); // Para que el fondo sea el mismo que el del panel
            gbc.gridx = 3;
            gbc.weightx = 0.2; // Peso para el checkbox
            add(checkBox, gbc);
        }

        // Un panel de relleno al final para empujar todo hacia arriba si hay espacio extra
        gbc.gridy = 7; // Después de las 5 filas + encabezado + separador
        gbc.weighty = 1.0; // Permite que esta fila ocupe todo el espacio vertical restante
        gbc.fill = GridBagConstraints.BOTH;
        add(new JPanel() {{ setOpaque(false); }}, gbc); // Panel vacío y transparente
    }

}