package com.bbva.gui.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class FXUtils {

    public static void abrirEnTab(TabPane tabPane, String title, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(title)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }
        Tab tab = new Tab(title, content);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectLast();
    }

    public static void showErrorAlert(String message) {
        Runnable show = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            String displayMessage = message != null && message.length() > 300
                    ? message.substring(0, 300) + "..." : message;
            alert.setContentText(displayMessage);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }

    public static void showInfoAlert(String title, String message) {
        Runnable show = () -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }

    public static void mostrarTooltipTemporal(Node node, String text, int durationMs) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.ZERO);
        Tooltip.install(node, tooltip);
        PauseTransition pause = new PauseTransition(Duration.millis(durationMs));
        pause.setOnFinished(e -> Tooltip.uninstall(node, tooltip));
        pause.play();
    }
}
