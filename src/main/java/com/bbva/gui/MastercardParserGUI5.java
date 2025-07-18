package com.bbva.gui;

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


public class MastercardParserGUI5 extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MastercardParserGUI5.class);
    private final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
    private JMenuBar menuBar;
    private JMenu parseMenu;
    private JMenu conversionMenu;
    private JMenu especificacionMenu;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaMenuItem;
    private JMenuItem convertirTramaMenuItem;
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


    public MastercardParserGUI5() {
        logger.info("Iniciando construcción de la ventana");
        processCodeOptions.add("Default Option 1");
        processCodeOptions.add("Default Option 2");
        processCodeOptions.add("Default Option 3");
        initializeComponents();
        actionsMenu();
        customMenu();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();

        // Crear menú "Parse"
        parseMenu = new JMenu("Menu");
        parseMenuItem = new JMenuItem("Parsear mensaje");
        parseMenu.add(parseMenuItem);

        // Crear menú "Conversión" con submenús
        conversionMenu = new JMenu("Conversion");
        generarTramaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        subcamposMenu = new JMenu("Campos variables");
        campo48MenuItem=new JMenuItem("Campo 48");
        campo54MenuItem=new JMenuItem("Campo 54");
        subcamposMenu.add(campo48MenuItem);
        subcamposMenu.add(campo54MenuItem);
        conversionMenu.add(convertirTramaMenuItem);
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
        menuBar.add(especificacionMenu);
        menuBar.add(configuracionesMenu);

        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);

        //ver si va
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JPanel(), BorderLayout.CENTER);
        //getContentPane().add(createImagePanel(), BorderLayout.CENTER);
    }

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
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                ParseViewerPanel parserPanel = new ParseViewerPanel();
                parserPanel.setInputText(SAMPLE_MESSAGE);
                getContentPane().add(parserPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        generarTramaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new GenerarTramaViewerPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        convertirTramaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new ConverterTramaViewerPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        campo54MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().add(new ConverterIso20022ViewerPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        tramasExampleMenuItem.addActionListener(new ActionListener() {
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
                // Crear e mostrar la nueva ventana de la calculadora
               // CalculatorDialog calculator = new CalculatorDialog(MastercardParserGUI5.this); // Pasa 'this' como padre
                JDialog calculator = new Y(MastercardParserGUI5.this); // Pasa 'this' como padre
                calculator.setVisible(true);
               /* JDesktopPane desktopPane = new JDesktopPane();
                setContentPane(desktopPane);
                JInternalFrame calculator = new Z();
                calculator.setVisible(true);
                desktopPane.add(calculator);*/
               /* getContentPane().removeAll();
                JPanel parserPanel = new X();
                getContentPane().add(parserPanel, BorderLayout.CENTER);
                revalidate();
                repaint();*/
            }
        });


        cargarConfiguracionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadConfigDialog loadDialog = new LoadConfigDialog(MastercardParserGUI5.this);
                loadDialog.setVisible(true); // Se muestra el diálogo de carga
                // Cuando loadDialog se cierra (después de presionar "Aceptar"), obtenemos las opciones
                List<String> loaded = loadDialog.getLoadedOptions();
                if (loaded != null && !loaded.isEmpty()) {
                    processCodeOptions = loaded; // Actualizar la lista de opciones
                    JOptionPane.showMessageDialog(MastercardParserGUI5.this,
                            "Opciones de código de proceso actualizadas: " + processCodeOptions,
                            "Configuración Cargada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Si no se cargó nada o el archivo estaba vacío, se mantiene la lista anterior o la por defecto
                    JOptionPane.showMessageDialog(MastercardParserGUI5.this,
                            "No se cargaron nuevas opciones de código de proceso. Se mantienen las actuales.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }


    private void customMenu(){

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);
        menuBar.setFont(menuFont);
        parseMenu.setFont(menuFont);
        conversionMenu.setFont(menuFont);
        especificacionMenu.setFont(menuFont);
        configuracionesMenu.setFont(menuFont);

        Font menuItemFont = new Font("Segoe UI", Font.PLAIN, 12);
        parseMenuItem.setFont(menuItemFont);
        generarTramaMenuItem.setFont(menuItemFont);
        convertirTramaMenuItem.setFont(menuItemFont);
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

                MastercardParserGUI5 gui = new MastercardParserGUI5();
                gui.setVisible(true);
            }
        });
    }
}