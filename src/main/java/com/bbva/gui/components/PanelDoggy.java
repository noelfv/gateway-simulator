package com.bbva.gui.components;

import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowType;
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

    public static MyDoggyToolWindowManager setupStructureMyDoggy(
            TreeStructurePanel treePanel,
            InputTextPanel inputPanel,
            OutputTextPanel outputPanel) {

        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();

        // 1. EL CENTRO: Ahora solo contiene el Panel de Entrada
        // Ocupará todo el espacio que dejen las ToolWindows laterales
        toolWindowManager.getContentManager().addContent(
                "main_editor",
                "Editor de Tramas ISO",
                null,
                inputPanel);

        // 2. EL ÁRBOL: Registrado como ToolWindow a la IZQUIERDA (LEFT)
        ToolWindow treeTW = toolWindowManager.registerToolWindow(
                "tree_view",
                "Estructura ISO",
                null,
                treePanel,
                ToolWindowAnchor.LEFT); // Se ancla a la izquierda
        // Definimos el ancho específico (ej. 300 píxeles)
        //treeTW.getTypeDescriptor(DockedTypeDescriptor.class).setDockedVisible(true);
        DockedTypeDescriptor descriptorTree = (DockedTypeDescriptor) treeTW.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptorTree != null) {
            descriptorTree.setDockLength(150); // Altura del panel
            descriptorTree.setPopupMenuEnabled(true);
        }
        treeTW.setAvailable(true);
        treeTW.setVisible(true);
        treeTW.aggregate(); // Esto ayuda a que ocupe el alto total si es necesario

        // 3. LA SALIDA: Registrada como ToolWindow ABAJO (BOTTOM)
        ToolWindow outputTW = toolWindowManager.registerToolWindow(
                "out_console",
                "Consola de Resultados",
                null,
                outputPanel,
                ToolWindowAnchor.BOTTOM);

        DockedTypeDescriptor descriptorOut = (DockedTypeDescriptor) outputTW.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptorOut != null) {
            descriptorOut.setDockLength(450); // Altura del panel
            descriptorOut.setPopupMenuEnabled(true);
        }
        outputTW.setAvailable(true);
        outputTW.setVisible(true);

        return toolWindowManager;
    }

    public static MyDoggyToolWindowManager setupStructureMyDoggy(
            InputTextPanel inputPanel,
            OutputTextPanel outputPanel) {

        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();

        // 1. EL CENTRO: Ahora solo contiene el Panel de Entrada
        // Ocupará todo el espacio que dejen las ToolWindows laterales
        toolWindowManager.getContentManager().addContent(
                "main_editor",
                "Editor de Tramas ISO",
                null,
                inputPanel);

        // 3. LA SALIDA: Registrada como ToolWindow ABAJO (BOTTOM)
        ToolWindow outputTW = toolWindowManager.registerToolWindow(
                "out_console",
                "Consola de Resultados",
                null,
                outputPanel,
                ToolWindowAnchor.BOTTOM);

        DockedTypeDescriptor descriptorOut = (DockedTypeDescriptor) outputTW.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptorOut != null) {
            descriptorOut.setDockLength(450); // Altura del panel
            descriptorOut.setPopupMenuEnabled(true);
        }
        outputTW.setAvailable(true);
        outputTW.setVisible(true);

        aplicarEstiloVisual(toolWindowManager);

        return toolWindowManager;
    }


    /**
     * Configura la estructura tipo IDE (IntelliJ) para la aplicación.
     * * @param treePanel    Panel que contiene el JTree (Estructura)
     * @param centralPanel Panel o JTabbedPane central (Editor/Conversor)
     * @param outputPanel  Panel de salida (Consola)
     * @return El manager configurado para agregarlo al JFrame
     */
    public static MyDoggyToolWindowManager setupStructureMyDoggy(
            JPanel treePanel,
            JComponent centralPanel,
            JPanel outputPanel) {

        // 1. Instanciar el gestor de ventanas
        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();

        // 2. CONFIGURAR EL ÁREA CENTRAL (Editor/Tabs)
        // En MyDoggy, el ContentManager maneja lo que "siempre debe estar visible"
        toolWindowManager.getContentManager().addContent(
                "main_content",
                "Editor Principal",
                null,
                centralPanel);

        // 3. CONFIGURAR EL ÁRBOL (ToolWindow Izquierda)
        ToolWindow treeTW = toolWindowManager.registerToolWindow(
                "tree_view",          // ID único
                "Estructura",         // Título visible
                null,                 // Icono (opcional)
                treePanel,            // El componente JTree
                ToolWindowAnchor.LEFT // Posición estilo IntelliJ
        );

        // Propiedades para que se vea y anime como un IDE
        treeTW.setAvailable(true);
        treeTW.setVisible(true);
        treeTW.setType(ToolWindowType.DOCKED); // Anclado por defecto

        // 4. CONFIGURAR LA CONSOLA (ToolWindow Inferior)
        ToolWindow outputTW = toolWindowManager.registerToolWindow(
                "output_view",
                "Consola de Resultados",
                null,
                outputPanel,
                ToolWindowAnchor.BOTTOM // Posición inferior
        );

        outputTW.setAvailable(true);
        outputTW.setVisible(true);
        outputTW.setType(ToolWindowType.DOCKED);

        // 5. PERSONALIZACIÓN VISUAL (Look & Feel)
        aplicarEstiloVisual(toolWindowManager);

        return toolWindowManager;
    }

    private static void aplicarEstiloVisual(MyDoggyToolWindowManager manager) {
        // Color Azul BBVA para resaltar la ventana activa
        Color bbvaNavy = new Color(0, 68, 129);

        // Ajustamos los descriptores para que las pestañas laterales sean más limpias
        for (ToolWindow tw : manager.getToolWindows()) {
            DockedTypeDescriptor descriptor = tw.getTypeDescriptor(DockedTypeDescriptor.class);
            descriptor.setAnimating(true); // Animación suave al abrir/cerrar
            //descriptor.setDockedVisible(true);
        }

        // Configuramos el UIManager para los colores de MyDoggy
        UIManager.put("MyDoggyToolWindowTitleBar.activeBackground", bbvaNavy);
        UIManager.put("MyDoggyToolWindowTitleBar.activeForeground", Color.WHITE);
    }

}
