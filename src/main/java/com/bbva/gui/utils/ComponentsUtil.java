package com.bbva.gui.utils;

import javax.swing.*;
import java.awt.*;

public class ComponentsUtil {

    public static JTextArea createTextArea() {
        JTextArea inputTextArea = new JTextArea(8, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        //inputTextArea.setBackground(Color.LIGHT_GRAY);
        return inputTextArea;
    }
}
