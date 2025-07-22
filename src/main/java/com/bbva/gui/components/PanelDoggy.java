package com.bbva.gui.components;

import com.bbva.gui.components.viewers.ParseViewerPanel;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import java.awt.*;

public class PanelDoggy {


    public static MyDoggyToolWindowManager setupStructureMyDoggy(JPanel mainPanel,JTree resultTree,JPanel outputPanel) {


        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, outputPanel);
        splitPane.setResizeWeight(0.5); // 50% para cada panel
        splitPane.setDividerLocation(0.5); // Inicialmente mitad y mitad

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
        outputToolWindow.setAvailable(true);
        outputToolWindow.setVisible(true);

        return toolWindowManager;
    }


}
