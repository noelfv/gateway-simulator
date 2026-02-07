package com.bbva.gui;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.panels.*;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.SwingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class ParserGUIMain2 extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(ParserGUIMain2.class);
    private JMenuItem parseMenuItem;
    private JMenuItem importarConfiguracionMenuItem;
    private JMenuItem generarTramaEspecificaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private JMenuItem convertirIso20022MenuItem;
    private JMenuItem campo48MenuItem;
    private final BeanProviderInstance beanProviderInstance;

    public ParserGUIMain2(BeanProviderInstance beanProviderInstance) {
        this.beanProviderInstance = beanProviderInstance;
        initializeComponents();
        actionsMenu();
    }

    private void initializeComponents() {
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setTitle("Gateway Message Parser");
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
        JMenuBar menuBar = new JMenuBar();
        // Crear items de menú
        JMenu parseMenu = new JMenu("Menu");
        parseMenu.setFont(menuFont);
        parseMenu.setBorder(itemPadding);
        JMenu conversionMenu = new JMenu("Conversion");
        conversionMenu.setFont(menuFont);
        conversionMenu.setBorder(itemPadding);
        /*JMenu configuracionMenu = new JMenu("Configuración");
        configuracionMenu.setFont(menuFont);
        configuracionMenu.setBorder(itemPadding);*/

        // Crear sub items
        parseMenuItem = new JMenuItem("Parsear mensaje");
        //importarConfiguracionMenuItem = new JMenuItem("Importar Campos (JSON)");
        generarTramaEspecificaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        convertirIso20022MenuItem = new JMenuItem("Convertir Objeto");
        campo48MenuItem = new JMenuItem("Campo 48");

        // Agregar sub items
        parseMenu.add(parseMenuItem);
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(convertirIso20022MenuItem);
        conversionMenu.add(generarTramaEspecificaMenuItem);
        conversionMenu.add(campo48MenuItem);

       // configuracionMenu.add(importarConfiguracionMenuItem);

        // Agregar a la barra de menú
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);
        menuBar.add(crearMenuConfiguracion());
        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);
    }

    private void actionsMenu() {
       // addInternalFrameMenuAction(parseMenuItem, new ParseViewerPanel(), "Parsear mensaje");
        addInternalFrameMenuAction(parseMenuItem, new ParseViewerPanel(beanProviderInstance), "Parsear mensaje");
        //addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaViewerPanel(), "Convertir mensaje");
        addInternalFrameMenuAction(convertirTramaMenuItem, new ConverterTramaTextPlainViewerPanel(beanProviderInstance), "Convertir mensaje");
        addInternalFrameMenuAction(convertirIso20022MenuItem, new Transformer20022Panel(beanProviderInstance), "Convertir Objeto");
      //  addInternalFrameMenuAction(importarConfiguracionMenuItem, new GenerarTramaViewerPanel(), "Generar Trama");
        addInternalFrameMenuAction(generarTramaEspecificaMenuItem, new GenerateTramaISO8583Panel(beanProviderInstance), "Generar Trama Especifica");
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

    public JMenuBar crearMenuConfiguracion() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Configuración");
        JMenuItem itemCargar = new JMenuItem("Importar Campos (JSON)...");
        itemCargar.addActionListener(e -> importarConfiguracionCampos());
        menuArchivo.add(itemCargar);
        menuBar.add(menuArchivo);
        return menuBar;
    }


    private void importarConfiguracionCampos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Configuración de Campos (JSON)");

        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                // Leer el contenido del archivo
                String content = new String(java.nio.file.Files.readAllBytes(archivo.toPath()));

                // Parsear JSON manual (si no tienes GSON)
                // Espera: {"listCampos": "1,2,3,4,5,6,7,8,9"}
                String valores = content.split(":")[1].replace("\"", "").replace("}", "").trim();

                List<Integer> nuevaLista = Arrays.stream(valores.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(java.util.stream.Collectors.toList());

                // Actualizar lista y refrescar UI
                //this.camposPermitidos = nuevaLista;
                //refreshUI(); // Método para reconstruir los paneles

                JOptionPane.showMessageDialog(this, "Configuración cargada: " + nuevaLista.size() + " campos.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo JSON: " + e.getMessage(),
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}