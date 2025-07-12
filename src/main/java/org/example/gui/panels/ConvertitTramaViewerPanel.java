package org.example.gui.panels;

import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.mastercard.processor.ISOStringMapper;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ConvertitTramaViewerPanel extends JPanel {
    private JRadioButton ebcidicRadioButton;
    private JRadioButton asciiRadioButton;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton convertirTramaButton;
    private JButton limpiarButton;
    private MyDoggyToolWindowManager toolWindowManager;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFieldsByDescription;
    private Map<String, String> currentMappedFields;

    public ConvertitTramaViewerPanel() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        setupEventHandlers();
        setupMyDoggy();
    }

   public void initializeComponents() {

       setLayout(new BorderLayout());

       inputTextArea = new JTextArea(8, 60);
       inputTextArea.setLineWrap(true);
       inputTextArea.setWrapStyleWord(true);
       inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

       outputTextArea = new JTextArea(12, 60);
       outputTextArea.setLineWrap(true);
       outputTextArea.setWrapStyleWord(true);
       outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

       convertirTramaButton = new JButton("Convertir Trama");
       limpiarButton = new JButton("Limpiar");
    }

    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();
        // Panel principal con entrada y botones
        JPanel mainPanel = createMainPanel();
        // Crear panel para los radio buttons
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBorder(BorderFactory.createTitledBorder("Tipo de trama"));

        // Crear grupo de botones y radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();
        ebcidicRadioButton = new JRadioButton("EBCDIC");
        asciiRadioButton = new JRadioButton("ASCCII");
        // Seleccionar el primero por defecto
        ebcidicRadioButton.setSelected(true);
        // Agregar los radio buttons al grupo
        buttonGroup.add(ebcidicRadioButton);
        buttonGroup.add(asciiRadioButton);
        // Agregar los radio buttons al panel
        radioPanel.add(ebcidicRadioButton);
        radioPanel.add(asciiRadioButton);

        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
       // contentPanel.add(radioPanel, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, contentPanel);

        // Tool Window para resultados
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);
        // Cambiar esta línea para obtener el componente correcto

        add(toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(convertirTramaButton);
        buttonPanel.add(limpiarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        panel.add(compareButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void setupEventHandlers() {
        convertirTramaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertMessage(asciiRadioButton.isSelected());
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
                //parseMessage();
            }
        });
    }

    private void convertMessage(boolean clear) {
        try {
            String inputMessage = inputTextArea.getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(inputMessage.startsWith("F0")){
                System.out.println("Mensaje en formato EBCDIC");
                currentMappedFieldsByDescription = ISOStringMapper.mapFields(inputMessage);
                clear=true;
            }else {
                currentMappedFieldsByDescription = ISOStringMapper.mapFieldsTramaClaro(inputMessage);
                clear=false;
            }

            String trama=messageParser.unParser(currentMappedFieldsByDescription, clear);
            System.out.println("Trama generada: [" + trama+"]");


            // Actualizar el área de texto
            StringBuilder output = new StringBuilder();
            output.append("=== CAMPOS PARSEADOS ===\n\n");
            output.append(trama);

            outputTextArea.setText(output.toString() );

        } catch (Exception ex) {
            showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        // Limitar mensaje a 150 caracteres y usar HTML para wrap
        String displayMessage = message.length() > 200 ?
                message.substring(0, 200) + "..." : message;

        String htmlMessage = "<html><body style='width: 250px; padding: 10px;'>" +
                displayMessage.replace("\n", "<br>") + "</body></html>";

        JOptionPane.showMessageDialog(this, htmlMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }




}