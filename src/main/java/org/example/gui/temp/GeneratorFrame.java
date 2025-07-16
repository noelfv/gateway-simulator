package org.example.gui.temp;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneratorFrame extends JDialog implements ActionListener {
    private JLabel[] labels;
    private JTextField[] textFields;
    private JComboBox<String>[] comboBoxes;
    private JCheckBox[] checkBoxes;
    private JButton generateButton;
    private JButton cancelButton;

    public GeneratorFrame(Frame parent) {
        super(parent, "Generador de Campos", true);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        // Inicializar arrays para 5 filas
        labels = new JLabel[5];
        textFields = new JTextField[5];
        comboBoxes = new JComboBox[5];
        checkBoxes = new JCheckBox[5];

        // Configurar componentes para cada fila
        String[] labelTexts = {"Campo 1:", "Campo 2:", "Campo 3:", "Campo 4:", "Campo 5:"};
        String[] comboOptions = {"Opción A", "Opción B", "Opción C"};
        String[] checkTexts = {"Activar", "Habilitar", "Incluir", "Procesar", "Validar"};

        for (int i = 0; i < 5; i++) {
            labels[i] = new JLabel(labelTexts[i]);
            labels[i].setHorizontalAlignment(SwingConstants.RIGHT);
            labels[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            textFields[i] = new JTextField(15);
            textFields[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

            comboBoxes[i] = new JComboBox<>(comboOptions);
            comboBoxes[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            checkBoxes[i] = new JCheckBox(checkTexts[i]);
            checkBoxes[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        }

        // Botones
        generateButton = new JButton("Generar");
        cancelButton = new JButton("Cancelar");
        generateButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        cancelButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel principal con GridLayout para las filas
        JPanel mainPanel = new JPanel(new GridLayout(5, 4, 10, 8));
        mainPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Agregar componentes al grid (5 filas x 4 columnas)
        for (int i = 0; i < 5; i++) {
            mainPanel.add(labels[i]);
            mainPanel.add(textFields[i]);
            mainPanel.add(comboBoxes[i]);
            mainPanel.add(checkBoxes[i]);
        }

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);

        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        JLabel titleLabel = new JLabel("Configuración del Generador");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Agregar paneles al frame
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        generateButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            // Lógica para generar
            handleGenerate();
        } else if (e.getSource() == cancelButton) {
            // Cerrar diálogo
            dispose();
        }
    }

    private void handleGenerate() {
        // Procesar los valores de los componentes
        StringBuilder result = new StringBuilder("Configuración:\n");

        for (int i = 0; i < 5; i++) {
            result.append("Fila ").append(i + 1).append(": ");
            result.append("Texto=").append(textFields[i].getText()).append(", ");
            result.append("Combo=").append(comboBoxes[i].getSelectedItem()).append(", ");
            result.append("Check=").append(checkBoxes[i].isSelected()).append("\n");
        }

        JOptionPane.showMessageDialog(this, result.toString(), "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}