package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import info.clearthought.layout.TableLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class GenerarTramaPersonalizadaViewerPanel extends JPanel {

    private final int TOTAL_CAMPOS = 64;
    private final String[] campos = new String[TOTAL_CAMPOS];
    private final String[] valoresPorDefecto = new String[TOTAL_CAMPOS];
    private final JCheckBox[] checkBoxes = new JCheckBox[TOTAL_CAMPOS];
    private final JTextField[] textFields = new JTextField[TOTAL_CAMPOS];
    private final JRadioButton[] radioButtons = new JRadioButton[TOTAL_CAMPOS];
    private final JButton procesarButton = new JButton("Procesar");
    private final JTextArea outputTextArea = new JTextArea(8, 40);
    private final JTextArea inputTextArea = new JTextArea(4, 40);
    private final ParserFactory parserFactory;
    private ISO8583DelegateParser delegateParser;

    public GenerarTramaPersonalizadaViewerPanel(BeanProviderInstance beanProviderInstance){
        inicializarCampos();
        initializeComponents();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
    }

    private void inicializarCampos() {
        // Inicializa los nombres y valores por defecto para los 64 campos
        for (int i = 0; i < TOTAL_CAMPOS; i++) {
            campos[i] = "P" + String.format("%02d", i + 2);
            valoresPorDefecto[i] = ""; // Puedes personalizar los valores por defecto aquí
        }
        // Ejemplo de valores por defecto para los primeros campos
        valoresPorDefecto[0] = "5536509999999999"; // P02 (PAN)
        valoresPorDefecto[1] = "000000"; // P03
        valoresPorDefecto[2] = "1000"; // P04
        valoresPorDefecto[3] = "2000"; // P05
        valoresPorDefecto[4] = "3000"; // P06
        valoresPorDefecto[5] = "202406101200"; // P07
        valoresPorDefecto[6] = "123456"; // P08
        valoresPorDefecto[7] = "4000"; // P09
        valoresPorDefecto[8] = "5000"; // P10
        valoresPorDefecto[13] = "0610"; // P15
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        // Definición de TableLayout para tres columnas principales
        int[] colSizes = {22, 21, 21};
        double[] cols = {TableLayout.FILL, TableLayout.FILL, TableLayout.FILL};
        double[] rows = {TableLayout.FILL};
        JPanel panelCampos = new JPanel(new TableLayout(cols, rows));

        // Paneles de columnas
        JPanel[] paneles = new JPanel[3];
        for (int c = 0, idx = 0; c < 3; c++) {
            double[] subRows = new double[colSizes[c]];
            for (int i = 0; i < colSizes[c]; i++) subRows[i] = TableLayout.PREFERRED;
            double[] subCols = {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED};
            paneles[c] = new JPanel(new TableLayout(subCols, subRows));
            for (int i = 0; i < colSizes[c]; i++, idx++) {
                checkBoxes[idx] = new JCheckBox(campos[idx], true);
                textFields[idx] = new JTextField(valoresPorDefecto[idx], 16);
                textFields[idx].setEditable(false);
                radioButtons[idx] = new JRadioButton("Enabled", true);
                int campoIdx = idx;
                radioButtons[idx].addActionListener(e -> {
                    textFields[campoIdx].setEditable(!radioButtons[campoIdx].isSelected());
                });
                paneles[c].add(checkBoxes[idx], "0," + i);
                paneles[c].add(textFields[idx], "1," + i);
                paneles[c].add(radioButtons[idx], "2," + i);
            }
        }
        panelCampos.add(paneles[0], "0,0");
        panelCampos.add(paneles[1], "1,0");
        panelCampos.add(paneles[2], "2,0");

        JScrollPane scrollPane = new JScrollPane(panelCampos);
        scrollPane.setPreferredSize(new Dimension(1200, 600));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(procesarButton);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(inputTextArea), BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        procesarButton.addActionListener(this::procesarTrama);
    }

    private void procesarTrama(ActionEvent e) {
        delegateParser = parserFactory.getDelegateParser("PEER02");
        Map<String, String> isoDataMap = new HashMap<>();
        for (int i = 0; i < TOTAL_CAMPOS; i++) {
            if (checkBoxes[i].isSelected()) {
                String valor = textFields[i].getText().isBlank() ? valoresPorDefecto[i] : textFields[i].getText();
                isoDataMap.put(campos[i], valor);
            }
        }
        isoDataMap.put("messageType", "0100");
        String trama = delegateParser.unParser(isoDataMap);
        inputTextArea.setText(trama);
    }


}