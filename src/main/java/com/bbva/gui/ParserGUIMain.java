package com.bbva.gui;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.panels.*;
import com.bbva.gui.panels.v2.ParseViewerPanel;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.SwingUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ParserGUIMain extends JFrame {

    private JMenuItem parseMenuItem;
    private JMenuItem importarConfiguracionMenuItem;
    private JMenuItem generarTramaEspecificaMenuItem;
    private JMenuItem convertirTramaMenuItem;
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
        //desktopPane.setBackground(Color.BLACK);
        setContentPane(desktopPane);
        setJMenuBar(createMenuBar());
    }


    private JMenuBar createMenuBar() {
        //Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font menuFont =new Font("SansSerif", Font.BOLD, 12);
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
        importarConfiguracionMenuItem = new JMenuItem("Importar Campos (JSON)");
        generarTramaEspecificaMenuItem = new JMenuItem("Generar trama específica");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        convertirIso20022MenuItem = new JMenuItem("Convertir Objeto ISO20022");
        campo48MenuItem = new JMenuItem("Campo 48 (TLV)");
        parseMenu.add(parseMenuItem);
        conversionMenu.add(convertirTramaMenuItem);
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
        addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaTextPlainViewerPanel(beanProviderInstance), "Convertir mensaje");
        addInternalFrameMenuAction(convertirIso20022MenuItem, new Transformer20022Panel(beanProviderInstance), "Convertir Objeto ISO20022");
        addInternalFrameMenuAction(generarTramaEspecificaMenuItem, new GenerateTramaISO8583Panel(beanProviderInstance), "Generar Trama Específica");
        addInternalFrameMenuAction(campo48MenuItem, new TLVParseViewerPanel(beanProviderInstance), "Parsear TLV");
        addInternalFrameMenuAction(importarConfiguracionMenuItem, new ConfigurationViewerPanel(beanProviderInstance), "Configuration"); // Solo abre diálogo
    }

    private void addInternalFrameMenuAction(JMenuItem menuItem, JPanel panel, String title) {
        menuItem.addActionListener(new ActionListener() {
            private JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                JDesktopPane desktopPane = (JDesktopPane) getContentPane();
                if (internalFrame == null || internalFrame.isClosed()) {
                    internalFrame = SwingUtils.mostrarEnInternalFrame2(desktopPane, panel, title);
                    try {
                        internalFrame.setSelected(true);
                        internalFrame.toFront();
                        desktopPane.revalidate();
                        desktopPane.repaint();
                    } catch (java.beans.PropertyVetoException ex) {
                        LogsTraces.writeWarning(ex.getMessage());
                    }
                } else {
                    try {
                        internalFrame.setIcon(false);
                        internalFrame.setSelected(true);
                        internalFrame.toFront();
                        desktopPane.revalidate();
                        desktopPane.repaint();
                    } catch (Exception ex) {
                        LogsTraces.writeWarning(ex.getMessage());
                    }
                }
            }
        });
    }
}
