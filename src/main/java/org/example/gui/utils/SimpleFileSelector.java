package org.example.gui.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SimpleFileSelector extends JDialog {
    private File selectedFile;
    private JTextField pathField;

    public SimpleFileSelector(JFrame parent) {
        super(parent, "Seleccionar archivo PDF", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(500, 150);
        setLocationRelativeTo(getParent());

        // Panel superior con instrucciones
        JLabel instructions = new JLabel("Selecciona un archivo PDF:");
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(instructions, BorderLayout.NORTH);

        // Panel central con campo de texto y botones
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        pathField = new JTextField();
        pathField.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton browseButton = new JButton("Examinar...");
        browseButton.addActionListener(e -> browseForFile());

        centerPanel.add(pathField, BorderLayout.CENTER);
        centerPanel.add(browseButton, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JButton okButton = new JButton("Aceptar");
        okButton.addActionListener(e -> confirmSelection());

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
            selectedFile = null;
            dispose();
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    private void browseForFile() {
        // Usar FileDialog nativo para evitar warnings
        try {
            FileDialog fileDialog = new FileDialog((Frame) getParent(),
                    "Seleccionar archivo PDF", FileDialog.LOAD);
            fileDialog.setFile("*.pdf");
            fileDialog.setVisible(true);

            String filename = fileDialog.getFile();
            String directory = fileDialog.getDirectory();

            if (filename != null && directory != null) {
                File file = new File(directory, filename);
                pathField.setText(file.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el selector de archivos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmSelection() {
        String path = pathField.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor selecciona un archivo.",
                    "Archivo requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "El archivo no existe.",
                    "Archivo no encontrado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            JOptionPane.showMessageDialog(this,
                    "El archivo debe ser un PDF.",
                    "Tipo de archivo inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedFile = file;
        dispose();
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public static File showFileSelector(JFrame parent) {
        SimpleFileSelector selector = new SimpleFileSelector(parent);
        selector.setVisible(true);
        return selector.getSelectedFile();
    }
}