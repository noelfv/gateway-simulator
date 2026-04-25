package com.bbva.gui.panels;

import com.bbva.gui.commons.FieldConfiguration;
import com.bbva.gui.commons.ISO8583DefaultValues;
import com.bbva.gui.components.UIButtonFactory;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.theme.UITheme;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.FieldUtil;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;

import java.util.*;

public class GenerateTramaISO8583Pane extends BorderPane {

    private static final int MIN_REQUIRED_FIELDS = 5;

    private final List<Integer> listCompras = new ArrayList<>(FieldConfiguration.COMPRAS);
    private final List<Integer> listBilletera = new ArrayList<>(FieldConfiguration.BILLETERA);
    private final List<Integer> listRetiros = new ArrayList<>(FieldConfiguration.RETIROS);

    private ComboBox<String> tipoOperacionComboBox;
    private ComboBox<String> procesarComboBox;
    private TextArea outputTextArea;
    private VBox camposContainer;
    private FieldSelectionPanel fieldSelectionPanel;
    private final ParserFactory parserFactory;

    public GenerateTramaISO8583Pane(BeanProviderInstance beans) {
        this.parserFactory = beans.parserFactory();
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        procesarComboBox = new ComboBox<>();
        procesarComboBox.getItems().addAll("Mastercard", "Visa");
        procesarComboBox.setValue("Mastercard");

        tipoOperacionComboBox = new ComboBox<>();
        tipoOperacionComboBox.getItems().addAll("Compras", "Billetera", "Retiros");
        tipoOperacionComboBox.setValue("Compras");

        outputTextArea = new TextArea();
        outputTextArea.setStyle(
                "-fx-control-inner-background: " + UITheme.DARK_BG + "; " +
                        "-fx-font-family: Consolas; -fx-font-size: 11px; " +
                        "-fx-text-fill: " + UITheme.TEXT_OUT + "; " +
                        "-fx-border-color: transparent;");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);
    }

    private void buildLayout() {
        setTop(createHeaderPanel());
        setCenter(createCenterPanel());
    }

    private HBox createHeaderPanel() {
        Label title = new Label("Generar Trama ISO8583");
        title.setStyle(
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 12px; -fx-font-family: 'Segoe UI';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblMarca = new Label("Marca:");
        Label lblTipo = new Label("Tipo de operación:");
        for (Label lbl : new Label[] { lblMarca, lblTipo }) {
            lbl.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");
        }
        String comboStyle = "-fx-font-size: 11px; -fx-background-color: white; " +
                "-fx-border-color: " + UITheme.BORDER + "; -fx-border-radius: 3; -fx-background-radius: 3;";
        procesarComboBox.setStyle(comboStyle);
        tipoOperacionComboBox.setStyle(comboStyle);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(7, 12, 7, 12));
        header.setStyle("-fx-background-color: " + UITheme.NAVY + ";");
        header.getChildren().addAll(title, spacer, lblMarca, procesarComboBox, lblTipo, tipoOperacionComboBox);
        return header;
    }

    private SplitPane createCenterPanel() {
        camposContainer = new VBox();
        refreshCampos();
        ScrollPane scrollPane = new ScrollPane(camposContainer);
        scrollPane.setFitToWidth(true);

        SplitPane split = new SplitPane(scrollPane, createOutputPanel());
        split.setOrientation(Orientation.VERTICAL);
        split.setDividerPositions(0.65);
        return split;
    }

    private VBox createOutputPanel() {
        Label titleLabel = new Label("Trama Generada");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(5, 10, 5, 10));
        titleLabel.setStyle(
                "-fx-background-color: " + UITheme.DARK_HEADER + "; " +
                        "-fx-text-fill: " + UITheme.ACCENT + "; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                        "-fx-border-color: transparent transparent " + UITheme.DARK_BORDER + " transparent; " +
                        "-fx-border-width: 0 0 1 0;");

        Button btnLimpiar = UIButtonFactory.createOutlineBtn("Limpiar", UITheme.ACCENT, "transparent",
                UITheme.ACCENT_DIM);
        btnLimpiar.setOnAction(e -> outputTextArea.setText(""));

        Button btnCopiar = UIButtonFactory.createOutlineBtn("Copiar Trama", UITheme.ACCENT, "transparent",
                UITheme.ACCENT_DIM);
        btnCopiar.setOnAction(e -> {
            String trama = outputTextArea.getText();
            if (!trama.isEmpty()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(trama);
                Clipboard.getSystemClipboard().setContent(content);
                FXUtils.showInfoAlert("BBVA Generator", "Trama copiada al portapapeles");
            }
        });

        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(6, 8, 8, 8));
        footer.setStyle("-fx-background-color: " + UITheme.DARK_FOOT + ";");
        footer.getChildren().addAll(btnLimpiar, btnCopiar);

        VBox panel = new VBox(0, titleLabel, outputTextArea, footer);
        panel.setStyle(
                "-fx-border-color: " + UITheme.DARK_BORDER + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + UITheme.DARK_BG + ";");
        VBox.setVgrow(outputTextArea, Priority.ALWAYS);
        return panel;
    }

    private void refreshCampos() {
        camposContainer.getChildren().clear();

        Button procesarButton = UIButtonFactory.createPrimaryBtn("Procesar");
        procesarButton.setOnAction(e -> procesarTrama());
        Button btnAgregarCampo = UIButtonFactory.createOutlineBtn("+ Agregar campo", "#228B22", "transparent",
                "#E8F5E9");
        btnAgregarCampo.setOnAction(e -> mostrarDialogoAgregarCampo());

        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(6, 10, 6, 10));
        topBar.setStyle("-fx-background-color: " + UITheme.LIGHT_BLUE + "; " +
                "-fx-border-color: transparent transparent " + UITheme.BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;");
        topBar.getChildren().addAll(btnAgregarCampo, procesarButton);

        fieldSelectionPanel = new FieldSelectionPanel(
                getCamposActuales(),
                this::getDefaultValue,
                getMandatoriosActuales(),
                id -> {
                    getCamposActuales().remove(Integer.valueOf(id));
                    refreshCampos();
                    actualizarValoresPorDefecto();
                });

        camposContainer.getChildren().addAll(topBar, fieldSelectionPanel);
    }

    private void setupHandlers() {
        tipoOperacionComboBox.setOnAction(e -> {
            refreshCampos();
            actualizarValoresPorDefecto();
        });
    }

    private void procesarTrama() {
        Map<Integer, CheckBox> checkBoxes = fieldSelectionPanel.getCheckBoxes();
        Map<Integer, TextField> textFields = fieldSelectionPanel.getTextFields();

        long seleccionados = checkBoxes.values().stream().filter(CheckBox::isSelected).count();
        if (seleccionados < MIN_REQUIRED_FIELDS) {
            FXUtils.showInfoAlert("Selección Insuficiente",
                    "Para generar una trama válida, debe seleccionar al menos " + MIN_REQUIRED_FIELDS + " campos.");
            return;
        }

        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER02");
        Map<String, String> isoDataMap = new HashMap<>();

        checkBoxes.forEach((id, chk) -> {
            if (chk.isSelected()) {
                MastercardISOField fieldDef = MastercardISOField.getById(id);
                String value = textFields.get(id).getText();

                if (isValorVacio(value)) {
                    value = valorPorDefectoParaCampoVacio(fieldDef);
                }

                if (fieldDef != null) {
                    if (fieldDef.getTypeData().equals(ISODataType.NUMERIC_DECIMAL)) {
                        Double amount = FieldUtil.convertAmountDouble(value);
                        if (amount != null)
                            value = FieldUtil.convertAmountString(amount);
                    }
                    isoDataMap.put(fieldDef.getName(), value);
                } else {
                    isoDataMap.put(String.format("P%02d", id), value);
                }
            }
        });

        isoDataMap.put("messageType", "0100");

        try {
            outputTextArea.setText(delegateParser.unParser(isoDataMap));
        } catch (Exception ex) {
            outputTextArea.setText("ERROR EN LA GENERACIÓN: " + ex.getMessage());
        }
    }

    private void mostrarDialogoAgregarCampo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar campo");
        dialog.setHeaderText(null);
        dialog.setContentText("Número de campo (ej: 2, 3, 48):");
        dialog.showAndWait().ifPresent(valor -> {
            if (!valor.isBlank()) {
                try {
                    int numero = Integer.parseInt(valor.trim());
                    List<Integer> listaActual = getCamposActuales();
                    if (listaActual.contains(numero)) {
                        FXUtils.showInfoAlert("Agregar campo", "El campo " + numero + " ya está en la lista.");
                    } else {
                        listaActual.add(numero);
                        Collections.sort(listaActual);
                        refreshCampos();
                        actualizarValoresPorDefecto();
                        FXUtils.showInfoAlert("Agregar campo", "Campo " + numero + " agregado.");
                    }
                } catch (NumberFormatException ex) {
                    FXUtils.showInfoAlert("Error", "Debe ingresar un número válido.");
                }
            }
        });
    }

    private List<Integer> getCamposActuales() {
        return switch (tipoOperacionComboBox.getValue()) {
            case "Billetera" -> listBilletera;
            case "Retiros" -> listRetiros;
            default -> listCompras;
        };
    }

    private Set<Integer> getMandatoriosActuales() {
        return FieldConfiguration.getMandatorios(tipoOperacionComboBox.getValue());
    }

    private void actualizarValoresPorDefecto() {
        Map<Integer, TextField> textFields = fieldSelectionPanel.getTextFields();
        Map<Integer, CheckBox> checkBoxes = fieldSelectionPanel.getCheckBoxes();
        Set<Integer> mandatorios = getMandatoriosActuales();

        for (Integer campo : getCamposActuales()) {
            TextField txt = textFields.get(campo);
            if (txt != null && !txt.isEditable())
                txt.setText(getDefaultValue(campo));
            CheckBox chk = checkBoxes.get(campo);
            if (chk != null)
                chk.setSelected(mandatorios.contains(campo));
        }
    }

    private String getDefaultValue(int i) {
        String tipo = tipoOperacionComboBox != null ? tipoOperacionComboBox.getValue() : "Compras";
        return ISO8583DefaultValues.getFor(i, tipo);
    }

    private boolean isValorVacio(String value) {
        return value == null || value.isBlank();
    }

    private String valorPorDefectoParaCampoVacio(MastercardISOField fieldDef) {
        if (fieldDef == null)
            return "";
        if (fieldDef.isVariable())
            return fieldDef.getLength() == 2 ? "00" : "000";
        if (fieldDef.getTypeData() == ISODataType.ALPHA_NUMERIC)
            return " ".repeat(fieldDef.getLength());
        if (fieldDef.getTypeData() == ISODataType.NUMERIC)
            return "0".repeat(fieldDef.getLength());
        return " ".repeat(fieldDef.getLength());
    }
}
