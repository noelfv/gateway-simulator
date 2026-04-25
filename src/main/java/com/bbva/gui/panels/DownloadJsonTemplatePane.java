package com.bbva.gui.panels;

import com.bbva.gui.theme.UITheme;
import com.bbva.gui.utils.FXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DownloadJsonTemplatePane extends BorderPane {

    private static final String RESOURCE_BASE = "/trama/";

    private ComboBox<String> comboRed;
    private ComboBox<String> comboOperacion;
    private ComboBox<String> comboMensaje;
    private Label lblArchivoActual;
    private Label lblDisponibilidad;
    private TextArea previewArea;
    private Button btnDescargar;
    private Button btnCopiar;

    public DownloadJsonTemplatePane() {
        initComponents();
        buildLayout();
        actualizarPreview();
    }

    private void initComponents() {
        comboRed = new ComboBox<>();
        comboRed.getItems().addAll("Mastercard", "Visa");
        comboRed.setValue("Mastercard");
        styleCombo(comboRed);

        comboOperacion = new ComboBox<>();
        comboOperacion.getItems().addAll("COMPRA", "RETIRO", "P2P");
        comboOperacion.setValue("COMPRA");
        styleCombo(comboOperacion);

        comboMensaje = new ComboBox<>();
        comboMensaje.getItems().addAll("0100", "0200", "0120");
        comboMensaje.setValue("0100");
        styleCombo(comboMensaje);

        lblArchivoActual = new Label();
        lblArchivoActual.setStyle(
                "-fx-font-family: Consolas; -fx-font-size: 11px; " +
                        "-fx-text-fill: " + UITheme.NAVY + "; -fx-font-weight: bold;");

        lblDisponibilidad = new Label();
        lblDisponibilidad.setStyle("-fx-font-size: 10px; -fx-font-family: 'Segoe UI';");

        previewArea = new TextArea();
        previewArea.setStyle(
                "-fx-control-inner-background: " + UITheme.DARK_BG + "; " +
                        "-fx-font-family: Consolas; -fx-font-size: 11px; " +
                        "-fx-text-fill: " + UITheme.TEXT_OUT + "; " +
                        "-fx-border-color: transparent;");
        previewArea.setEditable(false);
        previewArea.setWrapText(false);

        btnDescargar = createPrimaryBtn("Descargar");
        btnDescargar.setMaxWidth(Double.MAX_VALUE);
        btnDescargar.setOnAction(e -> descargarPlantilla());

        btnCopiar = createOutlineBtn("Copiar JSON", UITheme.ACCENT, "transparent", UITheme.ACCENT_DIM);
        btnCopiar.setOnAction(e -> {
            String texto = previewArea.getText();
            if (!texto.isEmpty() && !texto.startsWith("Plantilla no disponible")) {
                ClipboardContent content = new ClipboardContent();
                content.putString(texto);
                Clipboard.getSystemClipboard().setContent(content);
                FXUtils.mostrarTooltipTemporal(btnCopiar, "¡JSON copiado!", 1500);
            }
        });

        comboRed.setOnAction(e -> actualizarPreview());
        comboOperacion.setOnAction(e -> actualizarPreview());
        comboMensaje.setOnAction(e -> actualizarPreview());
    }

    private void buildLayout() {
        setTop(createHeaderPanel());
        setCenter(createMainPanel());
    }

    private HBox createHeaderPanel() {
        Label title = new Label("Descargar Plantilla JSON ISO8583");
        title.setStyle(
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 12px; -fx-font-family: 'Segoe UI';");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(7, 12, 7, 12));
        header.setStyle("-fx-background-color: " + UITheme.NAVY + ";");
        header.getChildren().add(title);
        return header;
    }

    private SplitPane createMainPanel() {
        SplitPane split = new SplitPane(createFormPanel(), createPreviewPanel());
        split.setOrientation(Orientation.HORIZONTAL);
        split.setDividerPositions(0.28);
        return split;
    }

    private VBox createFormPanel() {
        Label titleBar = new Label("Configuración");
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        VBox form = new VBox(14);
        form.setPadding(new Insets(14, 12, 14, 12));
        form.setStyle("-fx-background-color: " + UITheme.WHITE + ";");

        form.getChildren().addAll(
                labeledControl("Red de pago:", comboRed),
                labeledControl("Tipo de operación:", comboOperacion),
                labeledControl("Tipo de mensaje:", comboMensaje),
                createFileInfoBox(),
                btnDescargar);

        VBox.setVgrow(form, Priority.ALWAYS);

        VBox panel = new VBox(0, titleBar, form);
        panel.setStyle(
                "-fx-border-color: " + UITheme.NAVY + "; " +
                        "-fx-border-width: 1.5;");
        return panel;
    }

    private VBox createPreviewPanel() {
        Label titleBar = new Label("Previsualización");
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + UITheme.DARK_HEADER + "; " +
                        "-fx-text-fill: " + UITheme.ACCENT + "; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                        "-fx-border-color: transparent transparent " + UITheme.DARK_BORDER + " transparent; " +
                        "-fx-border-width: 0 0 1 0;");

        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(6, 8, 8, 8));
        footer.setStyle("-fx-background-color: " + UITheme.DARK_FOOT + ";");
        footer.getChildren().add(btnCopiar);

        VBox panel = new VBox(0, titleBar, previewArea, footer);
        panel.setStyle(
                "-fx-border-color: " + UITheme.DARK_BORDER + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + UITheme.DARK_BG + ";");
        VBox.setVgrow(previewArea, Priority.ALWAYS);
        return panel;
    }

    private VBox createFileInfoBox() {
        Label lbl = new Label("Archivo resuelto:");
        lbl.setStyle(
                "-fx-text-fill: " + UITheme.TEXT_MUTED + "; " +
                        "-fx-font-size: 10px; -fx-font-family: 'Segoe UI';");

        VBox box = new VBox(4, lbl, lblArchivoActual, lblDisponibilidad);
        box.setPadding(new Insets(8, 10, 8, 10));
        box.setStyle(
                "-fx-background-color: " + UITheme.STRIPE + "; " +
                        "-fx-border-color: " + UITheme.BORDER + "; " +
                        "-fx-border-radius: 4; -fx-background-radius: 4;");
        return box;
    }

    private void actualizarPreview() {
        String filename = buildFilename();
        lblArchivoActual.setText(filename);

        InputStream is = getClass().getResourceAsStream(RESOURCE_BASE + filename);
        if (is != null) {
            try {
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                previewArea.setText(content);
                previewArea.positionCaret(0);
                setDisponible(true);
                btnDescargar.setDisable(false);
                btnCopiar.setDisable(false);
            } catch (Exception ex) {
                previewArea.setText("Error al leer la plantilla: " + ex.getMessage());
                setDisponible(false);
            }
        } else {
            previewArea.setText("Plantilla no disponible: " + filename);
            setDisponible(false);
            btnDescargar.setDisable(true);
            btnCopiar.setDisable(true);
        }
    }

    private void descargarPlantilla() {
        String filename = buildFilename();
        InputStream is = getClass().getResourceAsStream(RESOURCE_BASE + filename);
        if (is == null) {
            FXUtils.showErrorAlert("No existe la plantilla: " + filename);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Plantilla JSON");
        fileChooser.setInitialFileName(filename);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File destino = fileChooser.showSaveDialog(
                getScene() != null ? getScene().getWindow() : null);

        if (destino != null) {
            try (InputStream src = getClass().getResourceAsStream(RESOURCE_BASE + filename)) {
                Files.copy(src, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                FXUtils.showInfoAlert("Descarga exitosa",
                        "Plantilla guardada en:\n" + destino.getAbsolutePath());
            } catch (Exception ex) {
                FXUtils.showErrorAlert("Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }

    private String buildFilename() {
        String red = comboRed.getValue().toUpperCase();
        String operacion = comboOperacion.getValue();
        String mensaje = comboMensaje.getValue();
        return mensaje + "-" + operacion + "-" + red + ".json";
    }

    private void setDisponible(boolean disponible) {
        if (disponible) {
            lblDisponibilidad.setText("✔ Disponible");
            lblDisponibilidad.setStyle(
                    "-fx-text-fill: #22A050; -fx-font-size: 10px; " +
                            "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        } else {
            lblDisponibilidad.setText("✘ No disponible");
            lblDisponibilidad.setStyle(
                    "-fx-text-fill: #CC4444; -fx-font-size: 10px; " +
                            "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        }
    }

    private VBox labeledControl(String labelText, Control control) {
        Label lbl = new Label(labelText);
        lbl.setStyle(
                "-fx-text-fill: " + UITheme.NAVY + "; " +
                        "-fx-font-size: 11px; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        VBox box = new VBox(4, lbl, control);
        return box;
    }

    private void styleCombo(ComboBox<String> combo) {
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle(
                "-fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                        "-fx-background-color: " + UITheme.WHITE + "; " +
                        "-fx-border-color: " + UITheme.BORDER + "; " +
                        "-fx-border-radius: 3; -fx-background-radius: 3;");
    }

    private Button createPrimaryBtn(String text) {
        Button btn = new Button(text);
        String normal = "-fx-background-color: " + UITheme.NAVY + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-background-radius: 4;";
        String hover = "-fx-background-color: " + UITheme.BLUE + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-background-radius: 4;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    private Button createOutlineBtn(String text, String borderColor, String bgNormal, String bgHover) {
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
