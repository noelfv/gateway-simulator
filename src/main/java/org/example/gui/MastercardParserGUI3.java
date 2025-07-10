package org.example.gui;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import org.example.orchestrator.MCMessageParserImpl;
import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MastercardParserGUI3 extends JFrame {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextArea comparisonTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    private JButton unparseCleanButton;
    private JButton unparseEBCDICButton;
    private JButton compareButton;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFields;
    private ToolWindowManager toolWindowManager;

    public MastercardParserGUI3() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        setupMyDoggy();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser - TableLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        inputTextArea = new JTextArea(8, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

        comparisonTextArea = new JTextArea(5, 60);
        comparisonTextArea.setLineWrap(true);
        comparisonTextArea.setWrapStyleWord(true);
        comparisonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        comparisonTextArea.setEditable(false);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        resultTree.setRootVisible(true);

        parseButton = new JButton("Parser");
        unparseCleanButton = new JButton("Unparser (Clean)");
        unparseEBCDICButton = new JButton("Unparser (EBCDIC)");
        compareButton = new JButton("Compare");
        compareButton.setVisible(false);

        unparseCleanButton.setEnabled(false);
        unparseEBCDICButton.setEnabled(false);
    }

    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();

        JPanel mainPanel = createMainPanelWithTableLayout();

        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, mainPanel);

        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("tree",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);

        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanelWithTableLayout(), ToolWindowAnchor.RIGHT);
        outputToolWindow.setAvailable(true);

        ToolWindow comparisonToolWindow = toolWindowManager.registerToolWindow("comparison",
                "Comparación", null, createComparisonPanelWithTableLayout(), ToolWindowAnchor.BOTTOM);
        comparisonToolWindow.setAvailable(false);

        getContentPane().add((Component) toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanelWithTableLayout() {
        // Definir tamaños de columnas y filas
        double[][] tableSize = {
                // Columnas: [0] Label width, [1] Fill space
                {80, TableLayoutConstants.FILL},
                // Filas: [0] Label, [1] Input area, [2] Spacing, [3] Buttons
                {25, TableLayoutConstants.FILL, 10, 40}
        };

        JPanel panel = new JPanel(new TableLayout(tableSize));

        // Etiqueta
        JLabel inputLabel = new JLabel("Mensaje:");
        inputLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        panel.add(inputLabel, "0,0,0,0,l,c");

        // Área de texto con scroll
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        panel.add(inputScrollPane, "0,1,1,1");

        // Panel de botones con TableLayout
        JPanel buttonPanel = createButtonPanelWithTableLayout();
        panel.add(buttonPanel, "0,3,1,3");

        return panel;
    }

    private JPanel createButtonPanelWithTableLayout() {
        // Definir tamaños para 3 botones con espaciado
        double[][] buttonTableSize = {
                // Columnas: botón, espacio, botón, espacio, botón, relleno
                {120, 10, 140, 10, 150, TableLayoutConstants.FILL},
                // Una sola fila
                {TableLayoutConstants.PREFERRED}
        };

        JPanel buttonPanel = new JPanel(new TableLayout(buttonTableSize));

        buttonPanel.add(parseButton, "0,0");
        buttonPanel.add(unparseCleanButton, "2,0");
        buttonPanel.add(unparseEBCDICButton, "4,0");

        return buttonPanel;
    }

    private JPanel createOutputPanelWithTableLayout() {
        double[][] outputTableSize = {
                // Una columna que ocupe todo el espacio
                {TableLayoutConstants.FILL},
                // Filas: área de salida, espaciado, botón compare
                {TableLayoutConstants.FILL, 5, 30}
        };

        JPanel panel = new JPanel(new TableLayout(outputTableSize));

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Resultado"));
        panel.add(outputScrollPane, "0,0");

        // Panel para el botón compare centrado
        JPanel compareButtonPanel = new JPanel(new TableLayout(new double[][]{{TableLayoutConstants.FILL, 100, TableLayoutConstants.FILL}, {TableLayoutConstants.PREFERRED}}));
        compareButtonPanel.add(compareButton, "1,0");
        panel.add(compareButtonPanel, "0,2");

        return panel;
    }

    private JPanel createComparisonPanelWithTableLayout() {
        double[][] comparisonTableSize = {
                {TableLayoutConstants.FILL},
                {25, TableLayoutConstants.FILL}
        };

        JPanel panel = new JPanel(new TableLayout(comparisonTableSize));

        JLabel comparisonLabel = new JLabel("Comparación de Tramas:");
        comparisonLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        panel.add(comparisonLabel, "0,0,0,0,l,c");

        JScrollPane comparisonScrollPane = new JScrollPane(comparisonTextArea);
        comparisonScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.add(comparisonScrollPane, "0,1");

        return panel;
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
                ToolWindow comparisonToolWindow = toolWindowManager.getToolWindow("comparison");
                comparisonToolWindow.setAvailable(true);
                compareButton.setVisible(true);
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
            updateTreeView();

            StringBuilder output = new StringBuilder();
            output.append("=== CAMPOS PARSEADOS ===\n\n");

            for (Map.Entry<String, String> entry : currentMappedFields.entrySet()) {
                output.append("Campo ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
            }

            outputTextArea.setText(output.toString());
            unparseCleanButton.setEnabled(true);
            unparseEBCDICButton.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al parsear el mensaje: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void updateTreeView() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");

        DefaultMutableTreeNode headerNode = new DefaultMutableTreeNode("Header");
        DefaultMutableTreeNode bitmapNode = new DefaultMutableTreeNode("Bitmap");
        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode("Campos de Datos");

        for (Map.Entry<String, String> entry : currentMappedFields.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            String nodeText = "Campo " + field + ": " + (value.length() > 50 ? value.substring(0, 50) + "..." : value);
            DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(nodeText);

            if (field.equals("MTI") || field.equals("TPDU")) {
                headerNode.add(fieldNode);
            } else if (field.equals("Bitmap")) {
                bitmapNode.add(fieldNode);
            } else {
                fieldsNode.add(fieldNode);
            }
        }

        if (headerNode.getChildCount() > 0) root.add(headerNode);
        if (bitmapNode.getChildCount() > 0) root.add(bitmapNode);
        if (fieldsNode.getChildCount() > 0) root.add(fieldsNode);

        treeModel.setRoot(root);

        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
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
                if (i + 2 < lines.length) {
                    return lines[i + 2].trim();
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                // Usar el look and feel por defecto
            }

            MastercardParserGUI3 gui = new MastercardParserGUI3();
            gui.inputTextArea.setText(SAMPLE_MESSAGE);
            gui.setVisible(true);
        });
    }
}