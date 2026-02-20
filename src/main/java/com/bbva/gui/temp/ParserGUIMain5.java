package com.bbva.gui.temp;

import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.components.TreeStructurePanel;
import com.bbva.gui.panels.ConverterTramaTextPlainViewerPanel;
import com.bbva.gui.spring.BeanProviderInstance;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ParserGUIMain5 extends JFrame {
    private MyDoggyToolWindowManager windowManager;
    private JPanel centralCardPanel; // El contenedor con CardLayout
    private CardLayout cardLayout;
    private final BeanProviderInstance beanProviderInstance;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaEspecificaMenuItem;
    private JMenuItem convertirTramaMenuItem;
    private TreeStructurePanel sideTreePanel; // Panel modular para el JTree
    private OutputTextPanel bottomOutputPanel; // Panel modular para la Consola


    public ParserGUIMain5(BeanProviderInstance beanProviderInstance) {
        this.beanProviderInstance = beanProviderInstance;
        initializeComponents();
        setupEvents();
    }

    private void initializeComponents() {
        setTitle("BBVA Gateway Message Parser");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. Crear el CardLayout y el panel central
        cardLayout = new CardLayout();
        centralCardPanel = new JPanel(cardLayout);

        // 2. Crear el Panel Vacío Azul BBVA
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(0, 68, 129)); // Azul BBVA
        centralCardPanel.add(emptyPanel, "EMPTY");

        // 3. Inicializar Paneles laterales
       /* sideTreePanel = new TreeStructurePanel("Estructura ISO", new JTree());
        bottomOutputPanel = new OutputTextPanel("Resultado", "Copiar","limpiar");

        // 4. Configurar MyDoggy
        windowManager = PanelDoggy.setupStructureMyDoggy(sideTreePanel, centralCardPanel, bottomOutputPanel);
        getContentPane().add(windowManager, BorderLayout.CENTER);*/

        // Mostrar el panel azul por defecto
        cardLayout.show(centralCardPanel, "EMPTY");
        setJMenuBar(crearMenuBar());
    }


    private JMenuBar crearMenuBar(){
        //Crear barra de menú
        Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);
        EmptyBorder itemPadding = new EmptyBorder(5, 10, 5, 30);
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
        return menuBar;
    }


    private void setupEvents() {
        parseMenuItem.addActionListener(e -> {
            // 1. Crear el panel de conversión
           ConverterTramaTextPlainViewerPanel converterPanel =
                    new ConverterTramaTextPlainViewerPanel(beanProviderInstance);
           add(converterPanel);
/*
            // 2. Agregarlo al CardLayout y mostrarlo
            centralCardPanel.add(converterPanel, "CONVERTER");
            cardLayout.show(centralCardPanel, "CONVERTER");

            // Revalidar para asegurar que el foco y el layout se refresquen
            centralCardPanel.revalidate();
            centralCardPanel.repaint();*/
            //iniciar();
        });
    }

    public void iniciar() {
        // Instancias de tus módulos
        TreeStructurePanel panelArbol = new TreeStructurePanel("Estructura del Mensaje", new JTree());
        InputTextPanel panelEntrada = new InputTextPanel("Entrada", "Parsear", "Limpiar");
        OutputTextPanel panelSalida = new OutputTextPanel("Salida", "Copiar","limpiar");

        // Mantienes tus listeners para la funcionalidad
        panelEntrada.getBtnPrimary().addActionListener(e -> {
            // Tu lógica de negocio
        });

        // Orquestación
        MyDoggyToolWindowManager manager = PanelDoggy.setupStructureMyDoggy(panelArbol, panelEntrada, panelSalida);

        this.add(manager);
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