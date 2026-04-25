package com.bbva.gui.components;

import com.bbva.gui.theme.UITheme;
import javafx.scene.control.Button;

public final class UIButtonFactory {

    private UIButtonFactory() {
    }

    public static Button createPrimaryBtn(String text) {
        Button btn = new Button(text);
        String normal = "-fx-background-color: " + UITheme.NAVY + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 7 20 7 20; -fx-cursor: hand; -fx-background-radius: 4;";
        String hover = "-fx-background-color: " + UITheme.BLUE + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 7 20 7 20; -fx-cursor: hand; -fx-background-radius: 4;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    public static Button createOutlineBtn(String text, String borderColor, String bgNormal, String bgHover) {
        Button btn = new Button(text);
        String base = "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 6 18 6 18; -fx-cursor: hand; " +
                "-fx-border-color: " + borderColor + "; -fx-border-radius: 4; " +
                "-fx-border-width: 1.5; -fx-background-radius: 4;";
        String normal = "-fx-background-color: " + bgNormal + "; -fx-text-fill: " + borderColor + "; " + base;
        String hover = "-fx-background-color: " + bgHover + "; -fx-text-fill: " + borderColor + "; " + base;
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }
}
