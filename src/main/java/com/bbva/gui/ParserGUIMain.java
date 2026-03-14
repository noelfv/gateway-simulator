package com.bbva.gui;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.panels.*;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.SwingUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class ParserGUIMain extends JFrame {

    private JMenuItem parseMenuItem;
    private JMenuItem parseClearMenuItem;
    private JMenuItem importarConfiguracionMenuItem;
    private JMenuItem generarTramaEspecificaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private JMenuItem convertirTramaOriginalMenuItem;
    private JMenuItem convertirTramaOriginalVisaMenuItem;
    private JMenuItem convertirIso20022MenuItem;
    private JMenuItem campo48MenuItem;
    private final BeanProviderInstance beanProviderInstance;

    public ParserGUIMain(BeanProviderInstance beanProviderInstance) {
        this.beanProviderInstance = beanProviderInstance;
        initializeComponents();
        actionsMenu();
    }

    private void initializeComponents() {
        setFont(new Font("SansSerif", Font.BOLD, 16));
        setTitle("Gateway Message Parser - BBVA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLocationRelativeTo(null);
        JDesktopPane desktopPane = new JDesktopPane();
        setContentPane(desktopPane);
        setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        Font menuFont = new Font("SansSerif", Font.BOLD, 12);
        EmptyBorder itemPadding = new EmptyBorder(5, 10, 5, 30);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JMenu parseMenu = new JMenu("Parsear");
        parseMenu.setFont(menuFont);
        parseMenu.setBorder(itemPadding);

        JMenu conversionMenu = new JMenu("Conversion");
        conversionMenu.setFont(menuFont);
        conversionMenu.setBorder(itemPadding);

        JMenu configurationMenu = new JMenu("Configuración");
        configurationMenu.setFont(menuFont);
        configurationMenu.setBorder(itemPadding);

        parseMenuItem = new JMenuItem("Parsear mensaje");
        parseClearMenuItem = new JMenuItem("Parsear mensaje claro");
        importarConfiguracionMenuItem = new JMenuItem("Importar Campos (JSON)");
        generarTramaEspecificaMenuItem = new JMenuItem("Generar trama específica");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        convertirTramaOriginalMenuItem = new JMenuItem("Convertir trama original");
        convertirTramaOriginalVisaMenuItem = new JMenuItem("Convertir trama original visa");
        convertirIso20022MenuItem = new JMenuItem("Convertir Objeto ISO20022");
        campo48MenuItem = new JMenuItem("Campo 48 (TLV)");

        parseMenu.add(parseMenuItem);
        parseMenu.add(parseClearMenuItem);
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(convertirTramaOriginalMenuItem);
        conversionMenu.add(convertirTramaOriginalVisaMenuItem);
        conversionMenu.add(convertirIso20022MenuItem);
        conversionMenu.add(generarTramaEspecificaMenuItem);
        conversionMenu.add(campo48MenuItem);
        configurationMenu.add(importarConfiguracionMenuItem);

        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);
        menuBar.add(configurationMenu);

        return menuBar;
    }

    private void actionsMenu() {
        addInternalFrameMenuAction(parseMenuItem, new ParseViewerPanel(beanProviderInstance), "Parsear mensaje");
        addInternalFrameMenuAction(parseClearMenuItem, new ParseClearViewerPanel(beanProviderInstance), "Parsear mensaje clear");
        addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaTextPlainViewerPanel2(beanProviderInstance), "Convertir mensaje");
        addInternalFrameMenuAction(convertirTramaOriginalMenuItem, new ConvertTramaOriginalViewerPanel(beanProviderInstance), "Convertir mensaje original");
        addInternalFrameMenuAction(convertirTramaOriginalVisaMenuItem, new ConvertTramaOriginalVisaViewerPanel(beanProviderInstance), "Convertir mensaje original visa");
        addInternalFrameMenuAction(convertirIso20022MenuItem, new Transformer20022Panel(beanProviderInstance), "Convertir Objeto ISO20022");
        addInternalFrameMenuAction(generarTramaEspecificaMenuItem, new GenerateTramaISO8583Panel2(beanProviderInstance), "Generar Trama Específica");
        addInternalFrameMenuAction(campo48MenuItem, new TLVParseViewerPanel(beanProviderInstance), "Parsear TLV");
        addInternalFrameMenuAction(importarConfiguracionMenuItem, new ConfigurationViewerPanel(beanProviderInstance), "Configuration");
    }

    private void addInternalFrameMenuAction(JMenuItem menuItem, JPanel panel, String title) {
        JInternalFrame[] frameHolder = {null};
        menuItem.addActionListener(e -> {
            JDesktopPane desktopPane = (JDesktopPane) getContentPane();
            if (frameHolder[0] == null || frameHolder[0].isClosed()) {
                frameHolder[0] = SwingUtils.mostrarEnInternalFrame2(desktopPane, panel, title);
                try {
                    frameHolder[0].setSelected(true);
                    frameHolder[0].toFront();
                    desktopPane.revalidate();
                    desktopPane.repaint();
                } catch (java.beans.PropertyVetoException ex) {
                    LogsTraces.writeWarning(ex.getMessage());
                }
            } else {
                try {
                    frameHolder[0].setIcon(false);
                    frameHolder[0].setSelected(true);
                    frameHolder[0].toFront();
                    desktopPane.revalidate();
                    desktopPane.repaint();
                } catch (Exception ex) {
                    LogsTraces.writeWarning(ex.getMessage());
                }
            }
        });
    }
}
