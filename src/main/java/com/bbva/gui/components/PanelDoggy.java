package com.bbva.gui.components;

import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import java.awt.*;

public class PanelDoggy {

    public static final Color BBVA_NAVY = new Color(0, 68, 129);  // Azul principal
    public static final Color BBVA_WHITE = new Color(255, 255, 255);
    public static final Color BBVA_LIGHT_GRAY = new Color(244, 244, 244);
    public static final Color BBVA_ACCENT_BLUE = new Color(18, 190, 255);


    public static MyDoggyToolWindowManager setupStructureMyDoggy(JPanel mainPanel,JTree resultTree,JPanel outputPanel) {

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, outputPanel);
        splitPane.setResizeWeight(0.8); // 50% para cada panel
        splitPane.setDividerLocation(0.8); // Inicialmente mitad y mitad

        //mainPanel.add(splitPane, BorderLayout.CENTER);

        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();
        // El main principal me lo pasan por parametro
        //JPanel mainPanel = createMainPanel();
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal",null , contentPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);
        treeToolWindow.setVisible(true);

        // Tool Window para la respuesta
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("OUT",
                "Resultado", null, outputPanel, ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(false);
        //outputToolWindow.setActive(false);
        outputToolWindow.setVisible(true);

        return toolWindowManager;
    }

    public static MyDoggyToolWindowManager setupStructureMyDoggy(JPanel mainPanel,JPanel outputPanel) {

        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(800, 600));
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal",null , contentPanel);

        // Tool Window para la respuesta
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("out",
                "Resultado", null, outputPanel, ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);
        outputToolWindow.setVisible(true);

        return toolWindowManager;
    }




    public static MyDoggyToolWindowManager setupStructureMyDoggy(JPanel mainPanel,JTextArea inputTextArea) {

/*
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, outputPanel);
        splitPane.setResizeWeight(0.8); // 50% para cada panel
        splitPane.setDividerLocation(0.8); // Inicialmente mitad y mitad*/

        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();


        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(800, 600));
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal",null , contentPanel);



        // Configuración del área de texto (Consola)
        inputTextArea.setBackground(new Color(30, 30, 30)); // Fondo oscuro tipo terminal
        // inputTextArea.setBackground(BBVA_WHITE); // Fondo oscuro tipo terminal
        inputTextArea.setForeground(BBVA_ACCENT_BLUE);      // Letras azul neón
        inputTextArea.setCaretColor(Color.WHITE);
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        // Tool Window para la respuesta
        ToolWindow toolWindow = toolWindowManager.registerToolWindow(
                "TRAMA_ISO", "Output Trama ISO-20022", null,
                new JScrollPane(inputTextArea), ToolWindowAnchor.BOTTOM
        );

        // toolWindow.getTypeDescriptor(DockedTypeDescriptor.class).setEnabled(true);
        DockedTypeDescriptor descriptor = (DockedTypeDescriptor) toolWindow.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptor != null) {
            descriptor.setDockLength(450); // Altura del panel
            descriptor.setPopupMenuEnabled(true);
        }
        toolWindow.setActive(true);

        return toolWindowManager;
    }



}
