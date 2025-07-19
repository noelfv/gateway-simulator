package com.bbva.gui.temp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadConfigDialog extends JDialog {

    private JTextField filePathField;
    private List<String> loadedOptions; // Aquí se almacenarán las opciones cargadas

    public LoadConfigDialog(JFrame parentFrame) {
        super(parentFrame, "Cargar Configuración de Proceso", true); // Modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10)); // Espaciado

        loadedOptions = new ArrayList<>();

        // Panel superior para la ruta del archivo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Archivo de Configuración:"));
        filePathField = new JTextField(25);
        filePathField.setEditable(false);
        topPanel.add(filePathField);

        JButton browseButton = new JButton("Examinar...");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // Intenta usar el directorio de inicio del usuario como punto de partida
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setDialogTitle("Seleccionar archivo configuration.txt");
                int userSelection = fileChooser.showOpenDialog(LoadConfigDialog.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                    loadAndParseFile(selectedFile);
                }
            }
        });
        topPanel.add(browseButton);

        add(topPanel, BorderLayout.CENTER);

        // Panel inferior con botón Aceptar (para cerrar el diálogo)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si el archivo se cargó, las opciones ya están en loadedOptions
                dispose(); // Cierra el diálogo
            }
        });
        bottomPanel.add(acceptButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadAndParseFile(File file) {
        loadedOptions.clear(); // Limpiar opciones anteriores
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                // Expresión regular para extraer los elementos dentro de los corchetes
                Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String content = matcher.group(1); // Contenido: "compras,billeteras,cajeros"
                    String[] parts = content.split(",");
                    for (String part : parts) {
                        loadedOptions.add(part.trim()); // Añadir y limpiar espacios
                    }
                    JOptionPane.showMessageDialog(this, "Archivo cargado y procesado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Formato de archivo incorrecto. Esperado: [item1,item2,...]", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo está vacío.", "Archivo Vacío", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error de Lectura", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Método para obtener las opciones cargadas después de que el diálogo se cierra
    public List<String> getLoadedOptions() {
        return loadedOptions;
    }
}