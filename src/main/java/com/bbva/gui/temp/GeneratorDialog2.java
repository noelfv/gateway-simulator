package com.bbva.gui.temp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratorDialog2 extends JDialog {

    // Colores y Fuentes recomendados para una apariencia elegante y amigable
    private static final Color PRIMARY_ACCENT_COLOR = new Color(0, 102, 204); // Azul vibrante
    private static final Color SECONDARY_BG_COLOR = new Color(245, 245, 245); // Gris muy claro para fondos de panel
    private static final Color BORDER_COLOR = new Color(180, 180, 180); // Gris claro para bordes
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Gris oscuro para texto

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font COMPONENT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public GeneratorDialog2(JFrame parentFrame) {
        super(parentFrame, "Generador de Trama Personalizado", true); // Título y modal (true)
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(650, 550); // Tamaño ajustado para 4 filas y más espacio
        setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Espaciado entre componentes (top, left, bottom, right)
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Encabezado del Panel ---
        JLabel headerLabel = new JLabel("Configuración Detallada de Trama");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_ACCENT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4; // Ocupa las 4 columnas
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerLabel, gbc);

        // Separador
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 15, 0);
        mainPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(10, 5, 10, 5); // Volver al espaciado normal

        // Reset gridwidth y anchor para las filas de datos
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0; // Etiquetas no se expanden horizontalmente
        gbc.weighty = 0.0; // No se expanden verticalmente

        // --- Fila 1: Número de Tarjeta ---
        int currentRow = 2; // Inicia después del encabezado y separador

        // JLabel
        JLabel label1 = new JLabel("Número de Tarjeta:");
        label1.setFont(LABEL_FONT);
        label1.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.15; // Pequeño peso
        mainPanel.add(label1, gbc);

        // JTextField
        JTextField cardNumberField = createStyledTextField(20); // Más ancho para el número de tarjeta
        cardNumberField.setToolTipText("Ingrese el número de tarjeta (solo números)");
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new NumericDocumentFilter()); // Solo números
        gbc.gridx = 1;
        gbc.weightx = 0.45; // Mayor peso
        mainPanel.add(cardNumberField, gbc);

        // JComboBox (vacío o con opciones irrelevantes, o un JComponent vacío)
        String[] comboCardOptions = {"VISA", "MasterCard"};
        JComboBox<String> cardTypeCombo = createStyledComboBox(comboCardOptions);
        cardTypeCombo.setToolTipText("Seleccione el tipo de tarjeta");
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(cardTypeCombo, gbc);

        // JCheckBox (vacío, usar un JPanel)
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc); // Componente vacío para mantener la estética

        currentRow++;

        // --- Fila 2: Código de Proceso ---
        JLabel label2 = new JLabel("Código de Proceso:");
        label2.setFont(LABEL_FONT);
        label2.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.15;
        mainPanel.add(label2, gbc);

        JTextField processCodeField = createStyledTextField(10);
        processCodeField.setToolTipText("Ej. 000000 (solo números)");
        ((AbstractDocument) processCodeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(processCodeField, gbc);

        String[] processTypeOptions = {"Compra", "Consulta", "Reverso"};
        JComboBox<String> processTypeCombo = createStyledComboBox(processTypeOptions);
        processTypeCombo.setToolTipText("Seleccione el tipo de proceso");
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(processTypeCombo, gbc);

        JCheckBox processActiveCheckBox = createStyledCheckBox("Activo"); // Este sí tiene checkbox
        processActiveCheckBox.setToolTipText("Habilitar procesamiento");
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(processActiveCheckBox, gbc);

        currentRow++;

        // --- Fila 3: Monto ---
        JLabel label3 = new JLabel("Monto:");
        label3.setFont(LABEL_FONT);
        label3.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.15;
        mainPanel.add(label3, gbc);

        JTextField amountField = createStyledTextField(15);
        amountField.setToolTipText("Ingrese el monto (solo números y un punto decimal)");
        // DocumentFilter para permitir números y un solo punto decimal
        ((AbstractDocument) amountField.getDocument()).setDocumentFilter(new DecimalDocumentFilter());
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(amountField, gbc);

        String[] currencyOptions = {"PEN", "USD", "EUR"};
        JComboBox<String> currencyCombo = createStyledComboBox(currencyOptions);
        currencyCombo.setToolTipText("Seleccione la divisa");
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(currencyCombo, gbc);

        // JCheckBox (vacío, usar un JPanel)
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        currentRow++;

        // --- Fila 4: Nombre del Comercio ---
        JLabel label4 = new JLabel("Nombre Comercio:");
        label4.setFont(LABEL_FONT);
        label4.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.15;
        mainPanel.add(label4, gbc);

        JTextField merchantNameField = createStyledTextField(25); // Más ancho para el nombre
        merchantNameField.setToolTipText("Ingrese el nombre del comercio");
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(merchantNameField, gbc);

        // JComboBox (vacío, usar un JPanel)
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        JCheckBox merchantActiveCheckBox = createStyledCheckBox("Validar");
        merchantActiveCheckBox.setToolTipText("Validar comercio");
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(merchantActiveCheckBox, gbc);

        currentRow++;

        // Un panel de relleno al final para empujar todo hacia arriba si hay espacio extra
        gbc.gridy = currentRow;
        gbc.weighty = 1.0; // Permite que esta fila ocupe todo el espacio vertical restante
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc); // Panel vacío y transparente


        // Añadir el mainPanel al content pane del JDialog
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // --- Métodos de Ayuda para Crear Componentes Estilizados ---

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(COMPONENT_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        textField.setForeground(TEXT_COLOR);
        return textField;
    }

    private JComboBox<String> createStyledComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(COMPONENT_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(BORDER_COLOR, 1));
        comboBox.setForeground(TEXT_COLOR);
        return comboBox;
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(COMPONENT_FONT);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(getBackground()); // Fondo transparente respecto al panel
        return checkBox;
    }

    // --- DocumentFilter para JTextField de solo números ---
    private static class NumericDocumentFilter extends DocumentFilter {
        private final Pattern pattern = Pattern.compile("\\d*"); // Solo dígitos

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            Matcher matcher = pattern.matcher(string);
            if (matcher.matches()) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep(); // Sonido de error
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // --- DocumentFilter para JTextField de monto (números y un punto decimal) ---
    private static class DecimalDocumentFilter extends DocumentFilter {
        private final Pattern pattern = Pattern.compile("\\d*\\.?\\d*"); // Cero o más dígitos, opcionalmente un punto, cero o más dígitos

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
            Matcher matcher = pattern.matcher(newText);

            if (matcher.matches()) {
                // Asegurarse de que no haya más de un punto decimal
                if (string.contains(".") && currentText.contains(".")) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    super.insertString(fb, offset, string, attr);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String before = currentText.substring(0, offset);
            String after = currentText.substring(offset + length);
            String newText = before + text + after;
            Matcher matcher = pattern.matcher(newText);

            if (matcher.matches()) {
                // Asegurarse de que no haya más de un punto decimal
                if (text.contains(".") && currentText.contains(".") && length == 0) { // Si se está insertando un punto y ya existe
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    super.replace(fb, offset, length, text, attrs);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }


    // Método main de ejemplo para probar solo este diálogo
    public static void main(String[] args) {
        try {
            // FlatLightLaf.setup(); // Descomentar si tienes FlatLaf
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
            JFrame dummyParentFrame = new JFrame();
            dummyParentFrame.setSize(400, 300);
            dummyParentFrame.setLocationRelativeTo(null);
            dummyParentFrame.setVisible(false);

            GeneratorDialog2 dialog = new GeneratorDialog2(dummyParentFrame);
            dialog.setVisible(true);
            dummyParentFrame.dispose();
        });
    }
}