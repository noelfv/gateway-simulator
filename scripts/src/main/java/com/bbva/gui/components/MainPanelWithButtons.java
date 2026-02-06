package com.bbva.gui.components;

import lombok.Getter;
import lombok.Setter;
import javax.swing.*;

@Getter
@Setter
public class MainPanelWithButtons {
    private JPanel panel;
    private JButton parseButton;
    private JButton limpiarButton;

    public MainPanelWithButtons(JPanel panel, JButton parseButton, JButton limpiarButton) {
        this.panel = panel;
        this.parseButton = parseButton;
        this.limpiarButton = limpiarButton;
    }
}