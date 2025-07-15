package org.example.gui.aaa;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List; // Importar List
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratorDialog extends JDialog {

    // ... (Mantén los mismos colores y fuentes estáticos definidos antes) ...
    private static final Color PRIMARY_ACCENT_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_BG_COLOR = new Color(245, 245, 245);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font COMPONENT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);


    // Nuevo constructor que acepta una lista de opciones para el JComboBox de código de proceso
    public GeneratorDialog(JFrame parentFrame, List<String> processCodeOptions) {
        super(parentFrame, "Generador de Trama Personalizado", true); // Título y modal (true)
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Encabezado del Panel ---
        JLabel headerLabel = new JLabel("Configuración Detallada de Trama");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_ACCENT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
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
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        // --- Fila 1: Número de Tarjeta ---
        int currentRow = 2;

        JLabel label1 = new JLabel("Número de Tarjeta:");
        label1.setFont(LABEL_FONT);
        label1.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.weightx = 0.15;
        mainPanel.add(label1, gbc);

        JTextField cardNumberField = createStyledTextField(20);
        cardNumberField.setToolTipText("Ingrese el número de tarjeta (solo números)");
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(cardNumberField, gbc);

        String[] comboCardOptions = {"VISA", "MasterCard"}; // Estas se mantienen estáticas o se cargan de otro archivo si es necesario
        JComboBox<String> cardTypeCombo = createStyledComboBox(comboCardOptions);
        cardTypeCombo.setToolTipText("Seleccione el tipo de tarjeta");
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(cardTypeCombo, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        currentRow++;

        // --- Fila 2: Código de Proceso (AHORA DINÁMICO) ---
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

        // AQUÍ ES DONDE EL JCOMBOBOX RECIBE LA DATA DINÁMICAMENTE
        JComboBox<String> processTypeCombo = createStyledComboBox(processCodeOptions.toArray(new String[0]));
        processTypeCombo.setToolTipText("Seleccione el tipo de proceso");
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(processTypeCombo, gbc);

        JCheckBox processActiveCheckBox = createStyledCheckBox("Activo");
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

        JTextField merchantNameField = createStyledTextField(25);
        merchantNameField.setToolTipText("Ingrese el nombre del comercio");
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(merchantNameField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        JCheckBox merchantActiveCheckBox = createStyledCheckBox("Validar");
        merchantActiveCheckBox.setToolTipText("Validar comercio");
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        mainPanel.add(merchantActiveCheckBox, gbc);

        currentRow++;

        // Un panel de relleno al final para empujar todo hacia arriba
        gbc.gridy = currentRow;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);


        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // --- Métodos de Ayuda para Crear Componentes Estilizados (sin cambios) ---
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
        checkBox.setBackground(getBackground());
        return checkBox;
    }

    // --- DocumentFilters (sin cambios) ---
    private static class NumericDocumentFilter extends DocumentFilter { /* ... */
        private final Pattern pattern = Pattern.compile("\\d*");

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            Matcher matcher = pattern.matcher(string);
            if (matcher.matches()) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
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

    private static class DecimalDocumentFilter extends DocumentFilter { /* ... */
        private final Pattern pattern = Pattern.compile("\\d*\\.?\\d*");

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
            Matcher matcher = pattern.matcher(newText);

            if (matcher.matches()) {
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
                if (text.contains(".") && currentText.contains(".") && length == 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    super.replace(fb, offset, length, text, attrs);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    // El método main es solo para prueba, la clase principal lo llamará
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame dummyParentFrame = new JFrame();
            dummyParentFrame.setSize(400, 300);
            dummyParentFrame.setLocationRelativeTo(null);
            dummyParentFrame.setVisible(false);

            // Ejemplo de opciones por defecto si no se carga un archivo
            List<String> defaultOptions = new ArrayList<>();
            defaultOptions.add("Opción por Defecto 1");
            defaultOptions.add("Opción por Defecto 2");

            // Crear y mostrar el diálogo generador con opciones por defecto
            GeneratorDialog dialog = new GeneratorDialog(dummyParentFrame, defaultOptions);
            dialog.setVisible(true);

            dummyParentFrame.dispose();
        });
    }
}