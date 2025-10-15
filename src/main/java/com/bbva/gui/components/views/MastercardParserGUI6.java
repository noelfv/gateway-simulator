package com.bbva.gui.components.views;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.SwingUtils;
import com.bbva.gui.components.panels.*;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MastercardParserGUI6 extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MastercardParserGUI6.class);
    private JMenuBar menuBar;
    private JMenu parseMenu;
    private JMenu conversionMenu;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private JMenuItem convertirIso20022MenuItem;
    private JMenuItem campo48MenuItem;
    private BeanProviderInstance beanProviderInstance;

    public MastercardParserGUI6(BeanProviderInstance beanProviderInstance) {
        this.beanProviderInstance = beanProviderInstance;
        initializeComponents();
        actionsMenu();
    }

    private void initializeComponents() {
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Mantener decoraciones de ventana (barra de título, botones)
        setLocationRelativeTo(null);
        // Cambiar el contenedor principal a JDesktopPane
        JDesktopPane desktopPane = new JDesktopPane();
        setContentPane(desktopPane);
        Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);
        EmptyBorder itemPadding = new EmptyBorder(5, 10, 5, 30);

        //Crear barra de menú
        menuBar = new JMenuBar();
        // Crear items de menú
        parseMenu = new JMenu("Menu");
        parseMenu.setFont(menuFont);
        parseMenu.setBorder(itemPadding);
        conversionMenu = new JMenu("Conversion");
        conversionMenu.setFont(menuFont);
        conversionMenu.setBorder(itemPadding);
        // Crear sub items
        parseMenuItem = new JMenuItem("Parsear mensaje");
        generarTramaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        convertirIso20022MenuItem = new JMenuItem("Convertir Objeto");
        campo48MenuItem = new JMenuItem("Campo 48");

        // Agregar sub items
        parseMenu.add(parseMenuItem);
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(convertirIso20022MenuItem);
        conversionMenu.add(generarTramaMenuItem);
        conversionMenu.add(campo48MenuItem);
        // Agregar a la barra de menú
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);
        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);
    }

    private void actionsMenu() {
        System.out.println("Configurando acciones del menú");
       // addInternalFrameMenuAction(parseMenuItem, new ParseViewerPanel(), "Parsear mensaje");
        addInternalFrameMenuAction(parseMenuItem, new ParseViewerPanel(beanProviderInstance), "Parsear mensaje");
        //addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaViewerPanel(), "Convertir mensaje");
        addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaViewerPanel(beanProviderInstance), "Convertir mensaje");
        addInternalFrameMenuAction(convertirIso20022MenuItem, new ConverterIso20022ViewerPanel(beanProviderInstance), "Convertir Objeto");
        addInternalFrameMenuAction(generarTramaMenuItem, new GenerarTramaViewerPanel(), "Generar Trama");
       // addInternalFrameMenuAction(campo48MenuItem, new TLVParseViewerPanel(), "Parsear TLV");
        addInternalFrameMenuAction(campo48MenuItem, new TLVParseViewerPanel(beanProviderInstance), "Parsear TLV");
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