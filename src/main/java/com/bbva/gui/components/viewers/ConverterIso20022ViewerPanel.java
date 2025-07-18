package com.bbva.gui.components.viewers;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.orchestrator.MCMessageParserImpl;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringConverterMastercard;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringMapper;
import com.bbva.orchestrator.parser.iso20022.ISO20022To8583Mapper;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.iso8583.ISO8583Builder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ConverterIso20022ViewerPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterIso20022ViewerPanel.class);
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton convertirTramaButton;
    private JButton limpiarButton;
    private MyDoggyToolWindowManager toolWindowManager;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFieldsByDescription;
    private Map<String, String> currentMappedFields;
    final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
    @Setter
    private JInternalFrame parentFrame;

    public ConverterIso20022ViewerPanel() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        setupEventHandlers();
        setupMyDoggy();
        //Poner el foco en el JTextArea de entrada
        SwingUtilities.invokeLater(() -> {
            inputTextArea.requestFocusInWindow();
        });
    }

   public void initializeComponents() {

       setLayout(new BorderLayout());

       inputTextArea = new JTextArea(8, 60);
       inputTextArea.setLineWrap(true);
       inputTextArea.setWrapStyleWord(true);
       inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
       //inputTextArea.setBackground(Color.LIGHT_GRAY);
       inputTextArea.setText(SAMPLE_MESSAGE);

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
        mainPanel.setPreferredSize(new Dimension(800, 600));

        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, contentPanel);

        // Tool Window para resultados
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);
        outputToolWindow.setVisible(true);

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

        // Establecer tamaño preferido para el panel
        panel.setPreferredSize(new Dimension(800, 200));

        outputTextArea.setPreferredSize(new Dimension(780, 180));
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        panel.add(compareButtonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createOutputPanel2() {
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
                convertMessage();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
            }
        });
    }

    private void convertMessage() {
        try {
            String jsonString = inputTextArea.getText().trim();
            if (jsonString.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convertir de String JSON a objeto ISO20022
            ObjectMapper objectMapper = new ObjectMapper();

            // Configurar ObjectMapper de forma más permisiva
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


            ISO20022 inputObject = objectMapper.readValue(jsonString, ISO20022.class);
            ISO8583 iso8583 = ISO20022To8583Mapper.translateToISO8583(inputObject);
            currentMappedFieldsByDescription = ISO8583Builder.buildMapISO8583(iso8583);
            LOGGER.info("Trama generada: [{}]", currentMappedFieldsByDescription);
            String trama=ISOStringConverterMastercard.getInstance().convertToISOString(currentMappedFieldsByDescription, true);
            LOGGER.info("Trama generada: [{}]", trama);
            outputTextArea.setText(trama );

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