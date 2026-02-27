package com.bbva.gui.utils;

import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowManager;
import org.noos.xing.mydoggy.ToolWindowType;

public class ToolWindowUtil {

    public static void setupCommonToolWindow(
            ToolWindowManager toolWindowManager, String toolWindowId,
            int dockLength){

        ToolWindow testTool;
        DockedTypeDescriptor dockedTypeDescriptor;

        testTool = toolWindowManager.getToolWindow(toolWindowId);
        // DockedTypeDescriptor
        dockedTypeDescriptor = (DockedTypeDescriptor) testTool.getTypeDescriptor(ToolWindowType.DOCKED);
        dockedTypeDescriptor.setDockLength(dockLength);
        dockedTypeDescriptor.setIdVisibleOnTitleBar(false);
        //Config options
        testTool.getTypeDescriptor(ToolWindowType.SLIDING).setEnabled(false);
        testTool.getTypeDescriptor(ToolWindowType.FLOATING).setEnabled(false);
        testTool.setActive(true);
    }
}