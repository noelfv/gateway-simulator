package com.bbva.gui;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.commons.InternalFrameActionListener;
import com.bbva.gui.components.SwingUtils;
import com.bbva.gui.components.viewers.*;
import com.bbva.gui.temp.LoadConfigDialog;
import com.bbva.gui.temp.Y;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class MastercardParserGUI6 extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MastercardParserGUI6.class);
    private JMenuBar menuBar;
    private JMenu parseMenu;
    private JMenu conversionMenu;
    private JMenu especificacionMenu;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private JMenuItem convertirIso20022MenuItem;
    private JMenuItem campo48MenuItem;
    private JMenuItem campo54MenuItem;
    private JMenu subcamposMenu;
    private JMenuItem tramasExampleMenuItem;
    private JMenuItem especificacionMastercadMenuItem;
    private JMenuItem especificacionVisaMenuItem;
    private JMenuItem mostrarCalculadoraMenuItem;
    private JMenu configuracionesMenu;
    private JMenuItem cargarConfiguracionMenuItem;
    private JMenuItem cargarLlmMenuItem;

    private List<String> processCodeOptions = new ArrayList<>(); // Lista para almacenar las opciones cargadas


    public MastercardParserGUI6() {
        logger.info("Iniciando construcción de la ventana");
        processCodeOptions.add("Default Option 1");
        processCodeOptions.add("Default Option 2");
        processCodeOptions.add("Default Option 3");
        initializeComponents();
        actionsMenu();
        customMenu();
        borderMenu();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //establecer tamaño de la pantalla
        //setSize(1200, 800); //Tamaño fijo
        // Configurar para pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Mantener decoraciones de ventana (barra de título, botones)
        // Si quieres pantalla completa sin decoraciones, usa:
        //setUndecorated(true);
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);


        setLocationRelativeTo(null);
        // Cambiar el contenedor principal a JDesktopPane
        JDesktopPane desktopPane = new JDesktopPane();
        setContentPane(desktopPane);


        menuBar = new JMenuBar();

        // Crear menú "Parse"
        parseMenu = new JMenu("Menu");
        parseMenuItem = new JMenuItem("Parsear mensaje");
        parseMenu.add(parseMenuItem);

        // Crear menú "Conversión" con submenús
        conversionMenu = new JMenu("Conversion");
        generarTramaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        convertirIso20022MenuItem = new JMenuItem("Convertir Objeto");
        subcamposMenu = new JMenu("Campos variables");
        campo48MenuItem=new JMenuItem("Campo 48");
        campo54MenuItem=new JMenuItem("Campo 54");
        subcamposMenu.add(campo48MenuItem);
        subcamposMenu.add(campo54MenuItem);
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(convertirIso20022MenuItem);
        conversionMenu.add(generarTramaMenuItem);
        conversionMenu.add(subcamposMenu);

        especificacionMenu = new JMenu("Especificacion");
        especificacionMastercadMenuItem=new JMenuItem("Mastercard IA");
        especificacionVisaMenuItem=new JMenuItem("Visa IA");
        tramasExampleMenuItem=new JMenuItem("Tramas de ejemplo");
        cargarLlmMenuItem=new JMenuItem("Cargar LLM");

        especificacionMenu.add(especificacionMastercadMenuItem);
        especificacionMenu.add(especificacionVisaMenuItem);
        especificacionMenu.addSeparator();
        especificacionMenu.add(cargarLlmMenuItem);

        // Nuevo JMenuItem para la calculadora

        conversionMenu.addSeparator();
        mostrarCalculadoraMenuItem = new JMenuItem("Mostrar Calculadora");
        conversionMenu.add(tramasExampleMenuItem); // Añadir el nuevo item

        configuracionesMenu= new JMenu("Configuraciones");
        cargarConfiguracionMenuItem=new JMenuItem("Cargar Configuración de Proceso");
        configuracionesMenu.add(cargarConfiguracionMenuItem);

        // Agregar menús a la barra
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);
        //menuBar.add(especificacionMenu);
        menuBar.add(configuracionesMenu);

        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);
    }

    //muy pronto el logo
    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // Asegúrate de que la ruta sea correcta y la imagen esté en resources
        ImageIcon icon = new ImageIcon(getClass().getResource("/leonhard-unsplash.jpg"));
        JLabel label = new JLabel(icon, JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void actionsMenu() {

        // Configurar los listeners para los menús
        parseMenuItem.addActionListener(new ActionListener() {
            JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                JDesktopPane desktopPane = (JDesktopPane) getContentPane();
               if (internalFrame == null || internalFrame.isClosed()) {
                    internalFrame = SwingUtils.mostrarEnInternalFrame2(desktopPane, new ParseViewerPanel(), "Parsear mensaje");
                    desktopPane.revalidate();
                    desktopPane.repaint();
                } else {
                    try {
                        internalFrame.setIcon(false);
                        internalFrame.setSelected(true);
                        internalFrame.toFront();
                        desktopPane.revalidate();
                        desktopPane.repaint();
                    } catch (Exception ex) {
                        LogsTraces.writeWarning(ex.getMessage());
                        // Manejo de excepción
                    }
                }
            }
        });


/*
        parseMenuItem.addActionListener(
                new InternalFrameActionListener(
                        (JDesktopPane) getContentPane(),
                        new ParseViewerPanel(),
                        "Parser Principal"
                )
        );*/


        convertirTramaMenuItem.addActionListener(new ActionListener() {
            JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                JDesktopPane desktopPane = (JDesktopPane) getContentPane();
                if (internalFrame == null || internalFrame.isClosed()) {
                    internalFrame = SwingUtils.mostrarEnInternalFrame2(desktopPane, new ConverterTramaViewerPanel(), "Convertir trama");
                    desktopPane.revalidate();
                    desktopPane.repaint();
                } else {
                    try {
                        internalFrame.setIcon(false);
                        internalFrame.setSelected(true);
                        internalFrame.toFront();
                        desktopPane.revalidate();
                        desktopPane.repaint();
                    } catch (Exception ex) {
                        LogsTraces.writeWarning(ex.getMessage());
                        // Manejo de excepción
                    }
                }
            }
        });

        /*
        convertirTramaMenuItem.addActionListener(
                new InternalFrameActionListener(
                        (JDesktopPane) getContentPane(),
                        new ConverterTramaViewerPanel(),
                        "Convertir trama"
                )
        );

         */

        convertirIso20022MenuItem.addActionListener(new ActionListener() {
            JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                JDesktopPane desktopPane = (JDesktopPane) getContentPane();
                if (internalFrame == null || internalFrame.isClosed()) {
                    internalFrame = SwingUtils.mostrarEnInternalFrame2(desktopPane, new ConverterIso20022ViewerPanel(), "Convertir Objeto");
                    desktopPane.revalidate();
                    desktopPane.repaint();
                } else {
                    try {
                        internalFrame.setIcon(false);
                        internalFrame.setSelected(true);
                        internalFrame.toFront();
                        desktopPane.revalidate();
                        desktopPane.repaint();
                    } catch (Exception ex) {
                        LogsTraces.writeWarning(ex.getMessage());
                        // Manejo de excepción
                    }
                }
            }
        });


/*
        convertirIso20022MenuItem.addActionListener(
                new InternalFrameActionListener(
                        (JDesktopPane) getContentPane(),
                        new ConverterTramaViewerPanel(),
                        "Convertir Objeto"
                )
        );
  */

        generarTramaMenuItem.addActionListener(new ActionListener() {
            JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new GenerarTramaViewerPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        tramasExampleMenuItem.addActionListener(new ActionListener() {
            JInternalFrame internalFrame;
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                JPanel parserPanel = new TramaExamplesViewerPanel();
                getContentPane().add(parserPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        // Listener para el nuevo JMenuItem de la calculadora
        mostrarCalculadoraMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog calculator = new Y(MastercardParserGUI6.this); // Pasa 'this' como padre
                calculator.setVisible(true);
            }
        });


        cargarConfiguracionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadConfigDialog loadDialog = new LoadConfigDialog(MastercardParserGUI6.this);
                loadDialog.setVisible(true); // Se muestra el diálogo de carga
                // Cuando loadDialog se cierra (después de presionar "Aceptar"), obtenemos las opciones
                List<String> loaded = loadDialog.getLoadedOptions();
                if (loaded != null && !loaded.isEmpty()) {
                    processCodeOptions = loaded; // Actualizar la lista de opciones
                    JOptionPane.showMessageDialog(MastercardParserGUI6.this,
                            "Opciones de código de proceso actualizadas: " + processCodeOptions,
                            "Configuración Cargada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Si no se cargó nada o el archivo estaba vacío, se mantiene la lista anterior o la por defecto
                    JOptionPane.showMessageDialog(MastercardParserGUI6.this,
                            "No se cargaron nuevas opciones de código de proceso. Se mantienen las actuales.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Agregar listener para campo48MenuItem
        campo48MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDesktopPane desktopPane = (JDesktopPane) getContentPane();
                JPanel parserPanel = new TramaExamplesViewerPanel();
                SwingUtils.mostrarEnInternalFrame(desktopPane, parserPanel, "Formato del Campo 48");
            }
        });

    }


    private void customMenu(){

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16);
        menuBar.setFont(menuFont);
        parseMenu.setFont(menuFont);
        conversionMenu.setFont(menuFont);
        especificacionMenu.setFont(menuFont);
        configuracionesMenu.setFont(menuFont);

        Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 14);
        parseMenuItem.setFont(menuItemFont);
        generarTramaMenuItem.setFont(menuItemFont);
        convertirTramaMenuItem.setFont(menuItemFont);
        convertirIso20022MenuItem.setFont(menuItemFont);
        campo48MenuItem.setFont(menuItemFont);
        campo54MenuItem.setFont(menuItemFont);

        // En initializeComponents() después de crear los componentes
        Color menuBgColor = new Color(50, 50, 50); // Un gris oscuro para el fondo del menú
        Color menuFgColor = Color.BLACK; // Texto blanco para el menú
        Color menuItemBgColor = new Color(70, 70, 70); // Un gris ligeramente más claro para elementos
        Color menuItemFgColor = Color.BLACK; // Texto blanco para elementos
        Color selectedItemBgColor = new Color(0, 120, 215); // Un azul para el elemento seleccionado

        // Colores para JMenuBar
        menuBar.setBackground(menuBgColor);
        menuBar.setForeground(menuFgColor);

        // Colores para JMenu
        parseMenu.setBackground(menuBgColor);
        parseMenu.setForeground(menuFgColor);
        conversionMenu.setBackground(menuBgColor);
        conversionMenu.setForeground(menuFgColor);
        especificacionMenu.setBackground(menuBgColor);
        especificacionMenu.setForeground(menuFgColor);
        subcamposMenu.setBackground(menuBgColor);
        subcamposMenu.setForeground(menuFgColor);

        // Colores para JMenuItem
        parseMenuItem.setBackground(menuItemBgColor);
        parseMenuItem.setForeground(menuItemFgColor);
        generarTramaMenuItem.setBackground(menuItemBgColor);
        generarTramaMenuItem.setForeground(menuItemFgColor);
        convertirTramaMenuItem.setBackground(menuItemBgColor);
        convertirTramaMenuItem.setForeground(menuItemFgColor);
        convertirIso20022MenuItem.setBackground(menuItemBgColor);
        convertirIso20022MenuItem.setForeground(menuItemFgColor);
        campo48MenuItem.setBackground(menuItemBgColor);
        campo48MenuItem.setForeground(menuItemFgColor);
        campo54MenuItem.setBackground(menuItemBgColor);
        campo54MenuItem.setForeground(menuItemFgColor);

        // Para cambiar el color de selección (hover), es más complejo y generalmente se maneja
        // a través del UIManager o un Look and Feel personalizado.
        // Sin embargo, para un cambio rápido, podrías intentar:
        UIManager.put("MenuItem.selectionBackground", selectedItemBgColor);
        UIManager.put("MenuItem.selectionForeground", menuItemFgColor);
        UIManager.put("Menu.selectionBackground", selectedItemBgColor);
        UIManager.put("Menu.selectionForeground", menuItemFgColor);
    }

    private void borderMenu(){
        EmptyBorder itemPadding = new EmptyBorder(5, 10, 5, 30);
        conversionMenu.setBorder(itemPadding);
        parseMenu.setBorder(itemPadding);
        especificacionMenu.setBorder(itemPadding);

        /*
        parseMenuItem.setBorder(itemPadding);
        generarTramaMenuItem.setBorder(itemPadding);
        convertirTramaMenuItem.setBorder(itemPadding);
        campo48MenuItem.setBorder(itemPadding);
        campo54MenuItem.setBorder(itemPadding);

         */
    }


    public static void main(String[] args) {
        // Establece el Look and Feel ANTES de crear cualquier componente Swing
        //FlatLightLaf.setup(); // O el tema que prefieras
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                    }

                } catch (Exception e) {
                    // Usar el look and feel por defecto
                }

                MastercardParserGUI6 gui = new MastercardParserGUI6();
                gui.setVisible(true);
            }
        });
    }
}