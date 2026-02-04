package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class frmGenerator2 extends JPanel {

    // Declaración manual de controles para cada campo
    private JCheckBox chkP02, chkP03, chkP04, chkP05, chkP06, chkP07, chkP08, chkP09, chkP10, chkP11;
    private JCheckBox chkP12, chkP13, chkP14, chkP15, chkP16, chkP17, chkP18, chkP19, chkP20, chkP21, chkP22;
    private JCheckBox chkP23, chkP24, chkP25, chkP26, chkP27, chkP28, chkP29, chkP30, chkP31, chkP32, chkP33;
    private JCheckBox chkP34, chkP35, chkP36, chkP37, chkP38, chkP39, chkP40, chkP41, chkP42, chkP43, chkP44;
    private JCheckBox chkP45, chkP46, chkP47, chkP48, chkP49, chkP50, chkP51, chkP52, chkP53, chkP54, chkP55;
    private JCheckBox chkP56, chkP57, chkP58, chkP59, chkP60, chkP61, chkP62, chkP63, chkP64, chkP65;

    private JTextField txtP02, txtP03, txtP04, txtP05, txtP06, txtP07, txtP08, txtP09, txtP10, txtP11;
    private JTextField txtP12, txtP13, txtP14, txtP15, txtP16, txtP17, txtP18, txtP19, txtP20, txtP21, txtP22;
    private JTextField txtP23, txtP24, txtP25, txtP26, txtP27, txtP28, txtP29, txtP30, txtP31, txtP32, txtP33;
    private JTextField txtP34, txtP35, txtP36, txtP37, txtP38, txtP39, txtP40, txtP41, txtP42, txtP43, txtP44;
    private JTextField txtP45, txtP46, txtP47, txtP48, txtP49, txtP50, txtP51, txtP52, txtP53, txtP54, txtP55;
    private JTextField txtP56, txtP57, txtP58, txtP59, txtP60, txtP61, txtP62, txtP63, txtP64, txtP65;

    private JRadioButton rbP02, rbP03, rbP04, rbP05, rbP06, rbP07, rbP08, rbP09, rbP10, rbP11;
    private JRadioButton rbP12, rbP13, rbP14, rbP15, rbP16, rbP17, rbP18, rbP19, rbP20, rbP21, rbP22;
    private JRadioButton rbP23, rbP24, rbP25, rbP26, rbP27, rbP28, rbP29, rbP30, rbP31, rbP32, rbP33;
    private JRadioButton rbP34, rbP35, rbP36, rbP37, rbP38, rbP39, rbP40, rbP41, rbP42, rbP43, rbP44;
    private JRadioButton rbP45, rbP46, rbP47, rbP48, rbP49, rbP50, rbP51, rbP52, rbP53, rbP54, rbP55;
    private JRadioButton rbP56, rbP57, rbP58, rbP59, rbP60, rbP61, rbP62, rbP63, rbP64, rbP65;

    private final JButton procesarButton = new JButton("Procesar");
    private final JTextArea inputTextArea = new JTextArea(4, 40);
    private final ParserFactory parserFactory;
    private ISO8583DelegateParser delegateParser;

    public frmGenerator2(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel principal con GridLayout de 1 fila y 3 columnas
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 3, 20, 10));

        // Columna 1 (P02 - P23)
        JPanel columna1 = new JPanel(new GridLayout(22, 3, 4, 4));
        columna1.setBorder(BorderFactory.createTitledBorder("Campos P02 - P23"));

        // Columna 2 (P24 - P45)
        JPanel columna2 = new JPanel(new GridLayout(22, 3, 4, 4));
        columna2.setBorder(BorderFactory.createTitledBorder("Campos P24 - P45"));

        // Columna 3 (P46 - P65)
        JPanel columna3 = new JPanel(new GridLayout(20, 3, 4, 4));
        columna3.setBorder(BorderFactory.createTitledBorder("Campos P46 - P65"));

        // ===== COLUMNA 1: P02 - P23 =====
        chkP02 = new JCheckBox("P02", true); txtP02 = new JTextField("5536509999999999", 12); rbP02 = new JRadioButton("Enabled", true);
        rbP02.addActionListener(e -> txtP02.setEditable(!rbP02.isSelected()));
        columna1.add(chkP02); columna1.add(txtP02); columna1.add(rbP02);

        chkP03 = new JCheckBox("P03", true); txtP03 = new JTextField("000000", 12); rbP03 = new JRadioButton("Enabled", true);
        rbP03.addActionListener(e -> txtP03.setEditable(!rbP03.isSelected()));
        columna1.add(chkP03); columna1.add(txtP03); columna1.add(rbP03);

        chkP04 = new JCheckBox("P04", true); txtP04 = new JTextField("1000", 12); rbP04 = new JRadioButton("Enabled", true);
        rbP04.addActionListener(e -> txtP04.setEditable(!rbP04.isSelected()));
        columna1.add(chkP04); columna1.add(txtP04); columna1.add(rbP04);

        chkP05 = new JCheckBox("P05", true); txtP05 = new JTextField("2000", 12); rbP05 = new JRadioButton("Enabled", true);
        rbP05.addActionListener(e -> txtP05.setEditable(!rbP05.isSelected()));
        columna1.add(chkP05); columna1.add(txtP05); columna1.add(rbP05);

        chkP06 = new JCheckBox("P06", true); txtP06 = new JTextField("3000", 12); rbP06 = new JRadioButton("Enabled", true);
        rbP06.addActionListener(e -> txtP06.setEditable(!rbP06.isSelected()));
        columna1.add(chkP06); columna1.add(txtP06); columna1.add(rbP06);

        chkP07 = new JCheckBox("P07", true); txtP07 = new JTextField("202406101200", 12); rbP07 = new JRadioButton("Enabled", true);
        rbP07.addActionListener(e -> txtP07.setEditable(!rbP07.isSelected()));
        columna1.add(chkP07); columna1.add(txtP07); columna1.add(rbP07);

        chkP08 = new JCheckBox("P08", true); txtP08 = new JTextField("123456", 12); rbP08 = new JRadioButton("Enabled", true);
        rbP08.addActionListener(e -> txtP08.setEditable(!rbP08.isSelected()));
        columna1.add(chkP08); columna1.add(txtP08); columna1.add(rbP08);

        chkP09 = new JCheckBox("P09", true); txtP09 = new JTextField("4000", 12); rbP09 = new JRadioButton("Enabled", true);
        rbP09.addActionListener(e -> txtP09.setEditable(!rbP09.isSelected()));
        columna1.add(chkP09); columna1.add(txtP09); columna1.add(rbP09);

        chkP10 = new JCheckBox("P10", true); txtP10 = new JTextField("5000", 12); rbP10 = new JRadioButton("Enabled", true);
        rbP10.addActionListener(e -> txtP10.setEditable(!rbP10.isSelected()));
        columna1.add(chkP10); columna1.add(txtP10); columna1.add(rbP10);

        chkP11 = new JCheckBox("P11", true); txtP11 = new JTextField("", 12); rbP11 = new JRadioButton("Enabled", true);
        rbP11.addActionListener(e -> txtP11.setEditable(!rbP11.isSelected()));
        columna1.add(chkP11); columna1.add(txtP11); columna1.add(rbP11);

        chkP12 = new JCheckBox("P12", true); txtP12 = new JTextField("", 12); rbP12 = new JRadioButton("Enabled", true);
        rbP12.addActionListener(e -> txtP12.setEditable(!rbP12.isSelected()));
        columna1.add(chkP12); columna1.add(txtP12); columna1.add(rbP12);

        chkP13 = new JCheckBox("P13", true); txtP13 = new JTextField("", 12); rbP13 = new JRadioButton("Enabled", true);
        rbP13.addActionListener(e -> txtP13.setEditable(!rbP13.isSelected()));
        columna1.add(chkP13); columna1.add(txtP13); columna1.add(rbP13);

        chkP14 = new JCheckBox("P14", true); txtP14 = new JTextField("", 12); rbP14 = new JRadioButton("Enabled", true);
        rbP14.addActionListener(e -> txtP14.setEditable(!rbP14.isSelected()));
        columna1.add(chkP14); columna1.add(txtP14); columna1.add(rbP14);

        chkP15 = new JCheckBox("P15", true); txtP15 = new JTextField("0610", 12); rbP15 = new JRadioButton("Enabled", true);
        rbP15.addActionListener(e -> txtP15.setEditable(!rbP15.isSelected()));
        columna1.add(chkP15); columna1.add(txtP15); columna1.add(rbP15);

        chkP16 = new JCheckBox("P16", true); txtP16 = new JTextField("", 12); rbP16 = new JRadioButton("Enabled", true);
        rbP16.addActionListener(e -> txtP16.setEditable(!rbP16.isSelected()));
        columna1.add(chkP16); columna1.add(txtP16); columna1.add(rbP16);

        chkP17 = new JCheckBox("P17", true); txtP17 = new JTextField("", 12); rbP17 = new JRadioButton("Enabled", true);
        rbP17.addActionListener(e -> txtP17.setEditable(!rbP17.isSelected()));
        columna1.add(chkP17); columna1.add(txtP17); columna1.add(rbP17);

        chkP18 = new JCheckBox("P18", true); txtP18 = new JTextField("", 12); rbP18 = new JRadioButton("Enabled", true);
        rbP18.addActionListener(e -> txtP18.setEditable(!rbP18.isSelected()));
        columna1.add(chkP18); columna1.add(txtP18); columna1.add(rbP18);

        chkP19 = new JCheckBox("P19", true); txtP19 = new JTextField("", 12); rbP19 = new JRadioButton("Enabled", true);
        rbP19.addActionListener(e -> txtP19.setEditable(!rbP19.isSelected()));
        columna1.add(chkP19); columna1.add(txtP19); columna1.add(rbP19);

        chkP20 = new JCheckBox("P20", true); txtP20 = new JTextField("", 12); rbP20 = new JRadioButton("Enabled", true);
        rbP20.addActionListener(e -> txtP20.setEditable(!rbP20.isSelected()));
        columna1.add(chkP20); columna1.add(txtP20); columna1.add(rbP20);

        chkP21 = new JCheckBox("P21", true); txtP21 = new JTextField("", 12); rbP21 = new JRadioButton("Enabled", true);
        rbP21.addActionListener(e -> txtP21.setEditable(!rbP21.isSelected()));
        columna1.add(chkP21); columna1.add(txtP21); columna1.add(rbP21);

        chkP22 = new JCheckBox("P22", true); txtP22 = new JTextField("", 12); rbP22 = new JRadioButton("Enabled", true);
        rbP22.addActionListener(e -> txtP22.setEditable(!rbP22.isSelected()));
        columna1.add(chkP22); columna1.add(txtP22); columna1.add(rbP22);

        chkP23 = new JCheckBox("P23", true); txtP23 = new JTextField("", 12); rbP23 = new JRadioButton("Enabled", true);
        rbP23.addActionListener(e -> txtP23.setEditable(!rbP23.isSelected()));
        columna1.add(chkP23); columna1.add(txtP23); columna1.add(rbP23);

        // ===== COLUMNA 2: P24 - P45 =====
        chkP24 = new JCheckBox("P24", true); txtP24 = new JTextField("", 12); rbP24 = new JRadioButton("Enabled", true);
        rbP24.addActionListener(e -> txtP24.setEditable(!rbP24.isSelected()));
        columna2.add(chkP24); columna2.add(txtP24); columna2.add(rbP24);

        chkP25 = new JCheckBox("P25", true); txtP25 = new JTextField("", 12); rbP25 = new JRadioButton("Enabled", true);
        rbP25.addActionListener(e -> txtP25.setEditable(!rbP25.isSelected()));
        columna2.add(chkP25); columna2.add(txtP25); columna2.add(rbP25);

        chkP26 = new JCheckBox("P26", true); txtP26 = new JTextField("", 12); rbP26 = new JRadioButton("Enabled", true);
        rbP26.addActionListener(e -> txtP26.setEditable(!rbP26.isSelected()));
        columna2.add(chkP26); columna2.add(txtP26); columna2.add(rbP26);

        chkP27 = new JCheckBox("P27", true); txtP27 = new JTextField("", 12); rbP27 = new JRadioButton("Enabled", true);
        rbP27.addActionListener(e -> txtP27.setEditable(!rbP27.isSelected()));
        columna2.add(chkP27); columna2.add(txtP27); columna2.add(rbP27);

        chkP28 = new JCheckBox("P28", true); txtP28 = new JTextField("", 12); rbP28 = new JRadioButton("Enabled", true);
        rbP28.addActionListener(e -> txtP28.setEditable(!rbP28.isSelected()));
        columna2.add(chkP28); columna2.add(txtP28); columna2.add(rbP28);

        chkP29 = new JCheckBox("P29", true); txtP29 = new JTextField("", 12); rbP29 = new JRadioButton("Enabled", true);
        rbP29.addActionListener(e -> txtP29.setEditable(!rbP29.isSelected()));
        columna2.add(chkP29); columna2.add(txtP29); columna2.add(rbP29);

        chkP30 = new JCheckBox("P30", true); txtP30 = new JTextField("", 12); rbP30 = new JRadioButton("Enabled", true);
        rbP30.addActionListener(e -> txtP30.setEditable(!rbP30.isSelected()));
        columna2.add(chkP30); columna2.add(txtP30); columna2.add(rbP30);

        chkP31 = new JCheckBox("P31", true); txtP31 = new JTextField("", 12); rbP31 = new JRadioButton("Enabled", true);
        rbP31.addActionListener(e -> txtP31.setEditable(!rbP31.isSelected()));
        columna2.add(chkP31); columna2.add(txtP31); columna2.add(rbP31);

        chkP32 = new JCheckBox("P32", true); txtP32 = new JTextField("", 12); rbP32 = new JRadioButton("Enabled", true);
        rbP32.addActionListener(e -> txtP32.setEditable(!rbP32.isSelected()));
        columna2.add(chkP32); columna2.add(txtP32); columna2.add(rbP32);

        chkP33 = new JCheckBox("P33", true); txtP33 = new JTextField("", 12); rbP33 = new JRadioButton("Enabled", true);
        rbP33.addActionListener(e -> txtP33.setEditable(!rbP33.isSelected()));
        columna2.add(chkP33); columna2.add(txtP33); columna2.add(rbP33);

        chkP34 = new JCheckBox("P34", true); txtP34 = new JTextField("", 12); rbP34 = new JRadioButton("Enabled", true);
        rbP34.addActionListener(e -> txtP34.setEditable(!rbP34.isSelected()));
        columna2.add(chkP34); columna2.add(txtP34); columna2.add(rbP34);

        chkP35 = new JCheckBox("P35", true); txtP35 = new JTextField("", 12); rbP35 = new JRadioButton("Enabled", true);
        rbP35.addActionListener(e -> txtP35.setEditable(!rbP35.isSelected()));
        columna2.add(chkP35); columna2.add(txtP35); columna2.add(rbP35);

        chkP36 = new JCheckBox("P36", true); txtP36 = new JTextField("", 12); rbP36 = new JRadioButton("Enabled", true);
        rbP36.addActionListener(e -> txtP36.setEditable(!rbP36.isSelected()));
        columna2.add(chkP36); columna2.add(txtP36); columna2.add(rbP36);

        chkP37 = new JCheckBox("P37", true); txtP37 = new JTextField("", 12); rbP37 = new JRadioButton("Enabled", true);
        rbP37.addActionListener(e -> txtP37.setEditable(!rbP37.isSelected()));
        columna2.add(chkP37); columna2.add(txtP37); columna2.add(rbP37);

        chkP38 = new JCheckBox("P38", true); txtP38 = new JTextField("", 12); rbP38 = new JRadioButton("Enabled", true);
        rbP38.addActionListener(e -> txtP38.setEditable(!rbP38.isSelected()));
        columna2.add(chkP38); columna2.add(txtP38); columna2.add(rbP38);

        chkP39 = new JCheckBox("P39", true); txtP39 = new JTextField("", 12); rbP39 = new JRadioButton("Enabled", true);
        rbP39.addActionListener(e -> txtP39.setEditable(!rbP39.isSelected()));
        columna2.add(chkP39); columna2.add(txtP39); columna2.add(rbP39);

        chkP40 = new JCheckBox("P40", true); txtP40 = new JTextField("", 12); rbP40 = new JRadioButton("Enabled", true);
        rbP40.addActionListener(e -> txtP40.setEditable(!rbP40.isSelected()));
        columna2.add(chkP40); columna2.add(txtP40); columna2.add(rbP40);

        chkP41 = new JCheckBox("P41", true); txtP41 = new JTextField("", 12); rbP41 = new JRadioButton("Enabled", true);
        rbP41.addActionListener(e -> txtP41.setEditable(!rbP41.isSelected()));
        columna2.add(chkP41); columna2.add(txtP41); columna2.add(rbP41);

        chkP42 = new JCheckBox("P42", true); txtP42 = new JTextField("", 12); rbP42 = new JRadioButton("Enabled", true);
        rbP42.addActionListener(e -> txtP42.setEditable(!rbP42.isSelected()));
        columna2.add(chkP42); columna2.add(txtP42); columna2.add(rbP42);

        chkP43 = new JCheckBox("P43", true); txtP43 = new JTextField("", 12); rbP43 = new JRadioButton("Enabled", true);
        rbP43.addActionListener(e -> txtP43.setEditable(!rbP43.isSelected()));
        columna2.add(chkP43); columna2.add(txtP43); columna2.add(rbP43);

        chkP44 = new JCheckBox("P44", true); txtP44 = new JTextField("", 12); rbP44 = new JRadioButton("Enabled", true);
        rbP44.addActionListener(e -> txtP44.setEditable(!rbP44.isSelected()));
        columna2.add(chkP44); columna2.add(txtP44); columna2.add(rbP44);

        chkP45 = new JCheckBox("P45", true); txtP45 = new JTextField("", 12); rbP45 = new JRadioButton("Enabled", true);
        rbP45.addActionListener(e -> txtP45.setEditable(!rbP45.isSelected()));
        columna2.add(chkP45); columna2.add(txtP45); columna2.add(rbP45);

        // ===== COLUMNA 3: P46 - P65 =====
        chkP46 = new JCheckBox("P46", true); txtP46 = new JTextField("", 12); rbP46 = new JRadioButton("Enabled", true);
        rbP46.addActionListener(e -> txtP46.setEditable(!rbP46.isSelected()));
        columna3.add(chkP46); columna3.add(txtP46); columna3.add(rbP46);

        chkP47 = new JCheckBox("P47", true); txtP47 = new JTextField("", 12); rbP47 = new JRadioButton("Enabled", true);
        rbP47.addActionListener(e -> txtP47.setEditable(!rbP47.isSelected()));
        columna3.add(chkP47); columna3.add(txtP47); columna3.add(rbP47);

        chkP48 = new JCheckBox("P48", true); txtP48 = new JTextField("", 12); rbP48 = new JRadioButton("Enabled", true);
        rbP48.addActionListener(e -> txtP48.setEditable(!rbP48.isSelected()));
        columna3.add(chkP48); columna3.add(txtP48); columna3.add(rbP48);

        chkP49 = new JCheckBox("P49", true); txtP49 = new JTextField("", 12); rbP49 = new JRadioButton("Enabled", true);
        rbP49.addActionListener(e -> txtP49.setEditable(!rbP49.isSelected()));
        columna3.add(chkP49); columna3.add(txtP49); columna3.add(rbP49);

        chkP50 = new JCheckBox("P50", true); txtP50 = new JTextField("", 12); rbP50 = new JRadioButton("Enabled", true);
        rbP50.addActionListener(e -> txtP50.setEditable(!rbP50.isSelected()));
        columna3.add(chkP50); columna3.add(txtP50); columna3.add(rbP50);

        chkP51 = new JCheckBox("P51", true); txtP51 = new JTextField("", 12); rbP51 = new JRadioButton("Enabled", true);
        rbP51.addActionListener(e -> txtP51.setEditable(!rbP51.isSelected()));
        columna3.add(chkP51); columna3.add(txtP51); columna3.add(rbP51);

        chkP52 = new JCheckBox("P52", true); txtP52 = new JTextField("", 12); rbP52 = new JRadioButton("Enabled", true);
        rbP52.addActionListener(e -> txtP52.setEditable(!rbP52.isSelected()));
        columna3.add(chkP52); columna3.add(txtP52); columna3.add(rbP52);

        chkP53 = new JCheckBox("P53", true); txtP53 = new JTextField("", 12); rbP53 = new JRadioButton("Enabled", true);
        rbP53.addActionListener(e -> txtP53.setEditable(!rbP53.isSelected()));
        columna3.add(chkP53); columna3.add(txtP53); columna3.add(rbP53);

        chkP54 = new JCheckBox("P54", true); txtP54 = new JTextField("", 12); rbP54 = new JRadioButton("Enabled", true);
        rbP54.addActionListener(e -> txtP54.setEditable(!rbP54.isSelected()));
        columna3.add(chkP54); columna3.add(txtP54); columna3.add(rbP54);

        chkP55 = new JCheckBox("P55", true); txtP55 = new JTextField("", 12); rbP55 = new JRadioButton("Enabled", true);
        rbP55.addActionListener(e -> txtP55.setEditable(!rbP55.isSelected()));
        columna3.add(chkP55); columna3.add(txtP55); columna3.add(rbP55);

        chkP56 = new JCheckBox("P56", true); txtP56 = new JTextField("", 12); rbP56 = new JRadioButton("Enabled", true);
        rbP56.addActionListener(e -> txtP56.setEditable(!rbP56.isSelected()));
        columna3.add(chkP56); columna3.add(txtP56); columna3.add(rbP56);

        chkP57 = new JCheckBox("P57", true); txtP57 = new JTextField("", 12); rbP57 = new JRadioButton("Enabled", true);
        rbP57.addActionListener(e -> txtP57.setEditable(!rbP57.isSelected()));
        columna3.add(chkP57); columna3.add(txtP57); columna3.add(rbP57);

        chkP58 = new JCheckBox("P58", true); txtP58 = new JTextField("", 12); rbP58 = new JRadioButton("Enabled", true);
        rbP58.addActionListener(e -> txtP58.setEditable(!rbP58.isSelected()));
        columna3.add(chkP58); columna3.add(txtP58); columna3.add(rbP58);

        chkP59 = new JCheckBox("P59", true); txtP59 = new JTextField("", 12); rbP59 = new JRadioButton("Enabled", true);
        rbP59.addActionListener(e -> txtP59.setEditable(!rbP59.isSelected()));
        columna3.add(chkP59); columna3.add(txtP59); columna3.add(rbP59);

        chkP60 = new JCheckBox("P60", true); txtP60 = new JTextField("", 12); rbP60 = new JRadioButton("Enabled", true);
        rbP60.addActionListener(e -> txtP60.setEditable(!rbP60.isSelected()));
        columna3.add(chkP60); columna3.add(txtP60); columna3.add(rbP60);

        chkP61 = new JCheckBox("P61", true); txtP61 = new JTextField("", 12); rbP61 = new JRadioButton("Enabled", true);
        rbP61.addActionListener(e -> txtP61.setEditable(!rbP61.isSelected()));
        columna3.add(chkP61); columna3.add(txtP61); columna3.add(rbP61);

        chkP62 = new JCheckBox("P62", true); txtP62 = new JTextField("", 12); rbP62 = new JRadioButton("Enabled", true);
        rbP62.addActionListener(e -> txtP62.setEditable(!rbP62.isSelected()));
        columna3.add(chkP62); columna3.add(txtP62); columna3.add(rbP62);

        chkP63 = new JCheckBox("P63", true); txtP63 = new JTextField("", 12); rbP63 = new JRadioButton("Enabled", true);
        rbP63.addActionListener(e -> txtP63.setEditable(!rbP63.isSelected()));
        columna3.add(chkP63); columna3.add(txtP63); columna3.add(rbP63);

        chkP64 = new JCheckBox("P64", true); txtP64 = new JTextField("", 12); rbP64 = new JRadioButton("Enabled", true);
        rbP64.addActionListener(e -> txtP64.setEditable(!rbP64.isSelected()));
        columna3.add(chkP64); columna3.add(txtP64); columna3.add(rbP64);

        chkP65 = new JCheckBox("P65", true); txtP65 = new JTextField("", 12); rbP65 = new JRadioButton("Enabled", true);
        rbP65.addActionListener(e -> txtP65.setEditable(!rbP65.isSelected()));
        columna3.add(chkP65); columna3.add(txtP65); columna3.add(rbP65);

        // Agregar columnas al panel principal
        panelPrincipal.add(columna1);
        panelPrincipal.add(columna2);
        panelPrincipal.add(columna3);

        // Panel de botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(procesarButton);

        // Panel inferior con área de texto
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Trama Generada"));
        panelInferior.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Agregar todo al panel principal
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(panelPrincipal), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        procesarButton.addActionListener(this::procesarTrama);
    }

    private void procesarTrama(ActionEvent e) {
        delegateParser = parserFactory.getDelegateParser("PEER02");
        Map<String, String> isoDataMap = new HashMap<>();

        if (chkP02.isSelected()) isoDataMap.put("P02", txtP02.getText());
        if (chkP03.isSelected()) isoDataMap.put("P03", txtP03.getText());
        if (chkP04.isSelected()) isoDataMap.put("P04", txtP04.getText());
        if (chkP05.isSelected()) isoDataMap.put("P05", txtP05.getText());
        if (chkP06.isSelected()) isoDataMap.put("P06", txtP06.getText());
        if (chkP07.isSelected()) isoDataMap.put("P07", txtP07.getText());
        if (chkP08.isSelected()) isoDataMap.put("P08", txtP08.getText());
        if (chkP09.isSelected()) isoDataMap.put("P09", txtP09.getText());
        if (chkP10.isSelected()) isoDataMap.put("P10", txtP10.getText());
        if (chkP11.isSelected()) isoDataMap.put("P11", txtP11.getText());
        if (chkP12.isSelected()) isoDataMap.put("P12", txtP12.getText());
        if (chkP13.isSelected()) isoDataMap.put("P13", txtP13.getText());
        if (chkP14.isSelected()) isoDataMap.put("P14", txtP14.getText());
        if (chkP15.isSelected()) isoDataMap.put("P15", txtP15.getText());
        if (chkP16.isSelected()) isoDataMap.put("P16", txtP16.getText());
        if (chkP17.isSelected()) isoDataMap.put("P17", txtP17.getText());
        if (chkP18.isSelected()) isoDataMap.put("P18", txtP18.getText());
        if (chkP19.isSelected()) isoDataMap.put("P19", txtP19.getText());
        if (chkP20.isSelected()) isoDataMap.put("P20", txtP20.getText());
        if (chkP21.isSelected()) isoDataMap.put("P21", txtP21.getText());
        if (chkP22.isSelected()) isoDataMap.put("P22", txtP22.getText());
        if (chkP23.isSelected()) isoDataMap.put("P23", txtP23.getText());
        if (chkP24.isSelected()) isoDataMap.put("P24", txtP24.getText());
        if (chkP25.isSelected()) isoDataMap.put("P25", txtP25.getText());
        if (chkP26.isSelected()) isoDataMap.put("P26", txtP26.getText());
        if (chkP27.isSelected()) isoDataMap.put("P27", txtP27.getText());
        if (chkP28.isSelected()) isoDataMap.put("P28", txtP28.getText());
        if (chkP29.isSelected()) isoDataMap.put("P29", txtP29.getText());
        if (chkP30.isSelected()) isoDataMap.put("P30", txtP30.getText());
        if (chkP31.isSelected()) isoDataMap.put("P31", txtP31.getText());
        if (chkP32.isSelected()) isoDataMap.put("P32", txtP32.getText());
        if (chkP33.isSelected()) isoDataMap.put("P33", txtP33.getText());
        if (chkP34.isSelected()) isoDataMap.put("P34", txtP34.getText());
        if (chkP35.isSelected()) isoDataMap.put("P35", txtP35.getText());
        if (chkP36.isSelected()) isoDataMap.put("P36", txtP36.getText());
        if (chkP37.isSelected()) isoDataMap.put("P37", txtP37.getText());
        if (chkP38.isSelected()) isoDataMap.put("P38", txtP38.getText());
        if (chkP39.isSelected()) isoDataMap.put("P39", txtP39.getText());
        if (chkP40.isSelected()) isoDataMap.put("P40", txtP40.getText());
        if (chkP41.isSelected()) isoDataMap.put("P41", txtP41.getText());
        if (chkP42.isSelected()) isoDataMap.put("P42", txtP42.getText());
        if (chkP43.isSelected()) isoDataMap.put("P43", txtP43.getText());
        if (chkP44.isSelected()) isoDataMap.put("P44", txtP44.getText());
        if (chkP45.isSelected()) isoDataMap.put("P45", txtP45.getText());
        if (chkP46.isSelected()) isoDataMap.put("P46", txtP46.getText());
        if (chkP47.isSelected()) isoDataMap.put("P47", txtP47.getText());
        if (chkP48.isSelected()) isoDataMap.put("P48", txtP48.getText());
        if (chkP49.isSelected()) isoDataMap.put("P49", txtP49.getText());
        if (chkP50.isSelected()) isoDataMap.put("P50", txtP50.getText());
        if (chkP51.isSelected()) isoDataMap.put("P51", txtP51.getText());
        if (chkP52.isSelected()) isoDataMap.put("P52", txtP52.getText());
        if (chkP53.isSelected()) isoDataMap.put("P53", txtP53.getText());
        if (chkP54.isSelected()) isoDataMap.put("P54", txtP54.getText());
        if (chkP55.isSelected()) isoDataMap.put("P55", txtP55.getText());
        if (chkP56.isSelected()) isoDataMap.put("P56", txtP56.getText());
        if (chkP57.isSelected()) isoDataMap.put("P57", txtP57.getText());
        if (chkP58.isSelected()) isoDataMap.put("P58", txtP58.getText());
        if (chkP59.isSelected()) isoDataMap.put("P59", txtP59.getText());
        if (chkP60.isSelected()) isoDataMap.put("P60", txtP60.getText());
        if (chkP61.isSelected()) isoDataMap.put("P61", txtP61.getText());
        if (chkP62.isSelected()) isoDataMap.put("P62", txtP62.getText());
        if (chkP63.isSelected()) isoDataMap.put("P63", txtP63.getText());
        if (chkP64.isSelected()) isoDataMap.put("P64", txtP64.getText());
        if (chkP65.isSelected()) isoDataMap.put("P65", txtP65.getText());

        isoDataMap.put("messageType", "0100");
        String trama = delegateParser.unParser(isoDataMap);
        inputTextArea.setText(trama);
    }
}
