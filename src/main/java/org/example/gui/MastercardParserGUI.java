package org.example.gui;


import org.example.orchestrator.MCMessageParserImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MastercardParserGUI extends JFrame {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextArea comparisonTextArea;
    private JButton parseButton;
    private JButton unparseCleanButton;
    private JButton unparseEBCDICButton;
    private JButton compareButton;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFields;

    public MastercardParserGUI() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 710);
        setLocationRelativeTo(null);

        inputTextArea = new JTextArea(12, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        outputTextArea = new JTextArea(14, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

        // Nuevo JTextArea para comparación
        comparisonTextArea = new JTextArea(5, 60);
        comparisonTextArea.setLineWrap(true);
        comparisonTextArea.setWrapStyleWord(true);
        comparisonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        comparisonTextArea.setEditable(false);
        comparisonTextArea.setVisible(false); // Oculto inicialmente

        parseButton = new JButton("Parser");
        unparseCleanButton = new JButton("Unparser (Clean)");
        unparseEBCDICButton = new JButton("Unparser (EBCDIC)");
        compareButton = new JButton("Compare"); // Nuevo botón
        compareButton.setVisible(false); // Oculto inicialmente

        unparseCleanButton.setEnabled(false);
        unparseEBCDICButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel superior para entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones principales
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(parseButton);
        buttonPanel.add(unparseCleanButton);
        buttonPanel.add(unparseEBCDICButton);

        // Panel para salida con botón compare debajo
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        // Panel para el botón compare debajo del outputTextArea
        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        compareButtonPanel.add(compareButton);
        outputPanel.add(compareButtonPanel, BorderLayout.SOUTH);

        // Panel para comparación
        JPanel comparisonPanel = new JPanel(new BorderLayout());
        comparisonPanel.setBorder(BorderFactory.createTitledBorder("Comparación de Tramas"));
        comparisonPanel.add(new JScrollPane(comparisonTextArea), BorderLayout.CENTER);

        // Panel central que contiene output y comparison
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(outputPanel, BorderLayout.CENTER);
        centerPanel.add(comparisonPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
            }
        });

        unparseCleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(true);
            }
        });

        unparseEBCDICButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(false);
                // Mostrar el área de comparación y el botón
                comparisonTextArea.setVisible(true);
                compareButton.setVisible(true);
                revalidate();
                repaint();
            }
        });

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compareMessagesInNewArea();
            }
        });


    }

    private void parseMessage() {
        try {
            String inputMessage = inputTextArea.getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentMappedFields = messageParser.parser(inputMessage);

            StringBuilder output = new StringBuilder();
            output.append("=== CAMPOS PARSEADOS ===\n\n");

            for (Map.Entry<String, String> entry : currentMappedFields.entrySet()) {
                output.append("Campo ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
            }

            outputTextArea.setText(output.toString());

            // Habilitar botones de unparser
            unparseCleanButton.setEnabled(true);
            unparseEBCDICButton.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al parsear el mensaje: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void unparseMessage(boolean clean) {
        try {
            if (currentMappedFields == null || currentMappedFields.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero debe parsear un mensaje",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = messageParser.unParser(currentMappedFields, clean);
            String type = clean ? "CLEAN" : "EBCDIC";

            StringBuilder output = new StringBuilder();
            output.append("=== MENSAJE UNPARSED (").append(type).append(") ===\n\n");
            output.append(result);

            outputTextArea.setText(output.toString());
            outputTextArea.setEnabled(true);
            outputTextArea.setEditable(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al unparsear el mensaje: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void compareMessagesInNewArea() {
        String originalMessage = inputTextArea.getText().trim();
        String unparsedMessage = extractUnparsedMessage();

        if (originalMessage.isEmpty() || unparsedMessage.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mensajes para comparar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder comparison = new StringBuilder();
        comparison.append("=== COMPARACIÓN DE TRAMAS ===\n\n");
        /*comparison.append("ORIGINAL:  ").append(originalMessage).append("\n");
        comparison.append("UNPARSED:  ").append(unparsedMessage).append("\n\n");*/

        // Comparar carácter por carácter
        int minLength = Math.min(originalMessage.length(), unparsedMessage.length());
        comparison.append("RESULTADO:\n");

        boolean hasDifferences = false;
        for (int i = 0; i < minLength; i++) {
            if (originalMessage.charAt(i) != unparsedMessage.charAt(i)) {
                comparison.append("Posición ").append(i).append(": '")
                        .append(originalMessage.charAt(i)).append("' vs '")
                        .append(unparsedMessage.charAt(i)).append("'\n");
                hasDifferences = true;
            }
        }

        if (originalMessage.length() != unparsedMessage.length()) {
            comparison.append("Longitud diferente: ").append(originalMessage.length())
                    .append(" vs ").append(unparsedMessage.length()).append("\n");
            hasDifferences = true;
        }

        if (!hasDifferences) {
            comparison.append("¡Las tramas son idénticas!\n");
        }

        comparisonTextArea.setText(comparison.toString());
    }


    private String extractUnparsedMessage() {
        String output = outputTextArea.getText();
        String[] lines = output.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("=== MENSAJE UNPARSED")) {
                // Retorna la línea después del header
                if (i + 2 < lines.length) {
                    return lines[i + 2].trim();
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        // Mensaje de ejemplo para pruebas
        final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                } catch (Exception e) {
                    // Usar el look and feel por defecto
                }

                MastercardParserGUI gui = new MastercardParserGUI();
                gui.inputTextArea.setText(SAMPLE_MESSAGE);
                gui.setVisible(true);
            }
        });
    }
}