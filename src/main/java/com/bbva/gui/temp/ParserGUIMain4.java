package com.bbva.gui.temp;

import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.components.TreeStructurePanel;
import com.bbva.gui.panels.GenerateTramaISO8583Panel;
import com.bbva.gui.spring.BeanProviderInstance;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ParserGUIMain4 extends JFrame {
    private JTabbedPane centralTabbedPane;
    private MyDoggyToolWindowManager windowManager;
    private final BeanProviderInstance beanProviderInstance;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaEspecificaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private TreeStructurePanel sideTreePanel; // Panel modular para el JTree
    private OutputTextPanel bottomOutputPanel; // Panel modular para la Consola


    public ParserGUIMain4(BeanProviderInstance beanProviderInstance) {
        this.beanProviderInstance = beanProviderInstance;
        initializeComponents();
        handler();
    }

    private void initializeComponents() {
        // ... (Configuración de JFrame)
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setTitle("Gateway Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Mantener decoraciones de ventana (barra de título, botones)
        setLocationRelativeTo(null);
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
        JMenu configuracionMenu = new JMenu("Configuración");
        configuracionMenu.setFont(menuFont);
        configuracionMenu.setBorder(itemPadding);

        // Crear sub items
        parseMenuItem = new JMenuItem("Parsear mensaje");
        //importarConfiguracionMenuItem = new JMenuItem("Importar Campos (JSON)");
        generarTramaEspecificaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");

        // Agregar sub items
        parseMenu.add(parseMenuItem);
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(generarTramaEspecificaMenuItem);
        // configuracionMenu.add(importarConfiguracionMenuItem);
        // Agregar a la barra de menú
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);
        menuBar.add(configuracionMenu);
        // Establecer la barra de menú en el frame
        //m();
        setJMenuBar(menuBar);
    }


    private void m(){
        // 1. Paneles Globales (Estilo IntelliJ: Compartidos por todas las tabs)
        sideTreePanel = new TreeStructurePanel("Estructura del Mensaje", new JTree());
        bottomOutputPanel = new OutputTextPanel("Consola de Resultados", "Copiar","limpiar");

        // 2. Contenedor de Pestañas Central
        centralTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        centralTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        // Quitar bordes para que parezca integrado
        centralTabbedPane.setBorder(BorderFactory.createEmptyBorder());

        // 3. Orquestación MyDoggy (Look & Feel IntelliJ)
        windowManager = PanelDoggy.setupStructureMyDoggy(sideTreePanel, centralTabbedPane, bottomOutputPanel);
        getContentPane().add(windowManager, BorderLayout.CENTER);
    }


    private void abrirTabParseador() {
        InputTextPanel parseador = new InputTextPanel("Parseador", "Analizar", "Limpiar");
        // Lógica de botones aquí...
        centralTabbedPane.addTab("Parseador Mensaje", parseador);
        centralTabbedPane.setSelectedComponent(parseador);
    }

    private void abrirTabGenerador() {
        // Aquí usarías el frmGenerator que hicimos con el arreglo de campos
        JPanel generador = new GenerateTramaISO8583Panel(beanProviderInstance);
        centralTabbedPane.addTab("Generador ISO", generador);
        centralTabbedPane.setSelectedComponent(generador);
    }

    private void handler() {
        parseMenuItem.addActionListener(e -> {
            // Evitar duplicados si lo deseas, o abrir una nueva tab tipo IntelliJ
           /* m();
            ConverterTramaTextPlainViewerPanel panel = new ConverterTramaTextPlainViewerPanel(
                    beanProviderInstance, sideTreePanel, bottomOutputPanel);*/

           // centralTabbedPane.addTab("Parser: " + (centralTabbedPane.getTabCount() + 1), panel);
          //  centralTabbedPane.setSelectedComponent(panel);
        });
    }
    public static MyDoggyToolWindowManager setupStructureMyDoggy(
            TreeStructurePanel tree,
            JTabbedPane centralTabs, // Cambiamos JPanel por JTabbedPane
            OutputTextPanel output) {

        MyDoggyToolWindowManager manager = new MyDoggyToolWindowManager();

        // El contenedor de PESTAÑAS va al centro
        manager.getContentManager().addContent("main_tabs", "Editor Principal", null, centralTabs);

        // Árbol a la izquierda
        ToolWindow treeTW = manager.registerToolWindow("tree", "Estructura", null, tree, ToolWindowAnchor.LEFT);
        treeTW.setAvailable(true);
        treeTW.setVisible(true);

        // Consola abajo
        ToolWindow outTW = manager.registerToolWindow("out", "Resultado", null, output, ToolWindowAnchor.BOTTOM);
        outTW.setAvailable(true);
        outTW.setVisible(true);

        return manager;
    }
}