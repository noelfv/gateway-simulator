package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.theme.UITheme;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.FieldUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;

public class GenerateTramaIFromJsonExportPane extends BorderPane {

    private static final String STYLE_TF_RO = "-fx-control-inner-background: " + UITheme.TF_RO_BG + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + UITheme.TF_BORDER + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_TF_EDIT = "-fx-control-inner-background: " + UITheme.WHITE + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + UITheme.NAVY + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_OFF = "-fx-background-color: transparent; -fx-text-fill: #8899AA; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_ON = "-fx-background-color: " + UITheme.NAVY + "; -fx-text-fill: white; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_DEL_OFF = "-fx-background-color: transparent; -fx-text-fill: #CC4444; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_DEL_ON = "-fx-background-color: #FFEEEE; -fx-text-fill: #CC0000; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";

    private final Map<Integer, CheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, TextField> textFields = new TreeMap<>();

    private final List<Integer> camposFromJson = new ArrayList<>();
    private final Map<Integer, String> valoresFromJson = new HashMap<>();
    private String networkFromJson = "peer02";
    private String messageTypeFromJson = "0100";
    private String headerDefaultFromJson = "";

    private ComboBox<String> comboRed;
    private TextArea outputTextArea;
    private VBox camposContainer;
    private final ParserFactory parserFactory;

    public GenerateTramaIFromJsonExportPane(BeanProviderInstance beans) {
        this.parserFactory = beans.parserFactory();
        initComponents();
        buildLayout();
    }

    private void initComponents() {
        comboRed = new ComboBox<>();
        comboRed.getItems().addAll("Mastercard", "Visa");
        comboRed.setValue("Mastercard");
        comboRed.setMaxWidth(Double.MAX_VALUE);
        comboRed.setStyle(
                "-fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                        "-fx-background-color: " + UITheme.WHITE + "; " +
                        "-fx-border-color: " + UITheme.BORDER + "; " +
                        "-fx-border-radius: 3; -fx-background-radius: 3;");
        comboRed.setOnAction(e -> networkFromJson = "Visa".equals(comboRed.getValue()) ? "peer01" : "peer02");

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
        Label title = new Label("Generar Trama ISO8583 desde JSON Exportado");
        title.setStyle(
                "-fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 12px; -fx-font-family: 'Segoe UI';");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(7, 12, 7, 12));
        header.setStyle("-fx-background-color: " + UITheme.NAVY + ";");
        header.getChildren().add(title);
        return header;
    }

    private SplitPane createCenterPanel() {
        camposContainer = new VBox();
        refreshCampos();
        ScrollPane scrollPane = new ScrollPane(camposContainer);
        scrollPane.setFitToWidth(true);

        SplitPane horizontalSplit = new SplitPane(createNetworkPanel(), scrollPane);
        horizontalSplit.setOrientation(Orientation.HORIZONTAL);
        horizontalSplit.setDividerPositions(0.18);

        VBox outputPanel = createOutputPanel();

        SplitPane verticalSplit = new SplitPane(horizontalSplit, outputPanel);
        verticalSplit.setOrientation(Orientation.VERTICAL);
        verticalSplit.setDividerPositions(0.65);
        return verticalSplit;
    }

    private VBox createNetworkPanel() {
        Label titleBar = new Label("Red de Pago");
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        Label lblRed = new Label("Seleccionar red:");
        lblRed.setStyle(
                "-fx-text-fill: " + UITheme.TEXT_DARK + "; " +
                        "-fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        VBox content = new VBox(8, lblRed, comboRed);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: " + UITheme.WHITE + ";");
        VBox.setVgrow(content, Priority.ALWAYS);

        VBox panel = new VBox(0, titleBar, content);
        panel.setStyle(
                "-fx-border-color: " + UITheme.NAVY + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + UITheme.WHITE + ";");
        return panel;
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

        Button btnLimpiar = createOutlineBtn("Limpiar", UITheme.ACCENT, "transparent", UITheme.ACCENT_DIM);
        btnLimpiar.setOnAction(e -> outputTextArea.setText(""));

        Button btnCopiar = createOutlineBtn("Copiar Trama", UITheme.ACCENT, "transparent", UITheme.ACCENT_DIM);
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

        VBox outputPanel = new VBox(0, titleLabel, outputTextArea, footer);
        outputPanel.setStyle(
                "-fx-border-color: " + UITheme.DARK_BORDER + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + UITheme.DARK_BG + ";");
        VBox.setVgrow(outputTextArea, Priority.ALWAYS);
        return outputPanel;
    }

    private void refreshCampos() {
        checkBoxes.clear();
        textFields.clear();
        camposContainer.getChildren().clear();

        Button procesarButton = createPrimaryBtn("Procesar");
        procesarButton.setOnAction(e -> procesarTrama());

        Button btnAgregarCampo = createOutlineBtn("+ Agregar campo", "#228B22", "transparent", "#E8F5E9");
        btnAgregarCampo.setOnAction(e -> mostrarDialogoAgregarCampo());

        Button btnImportar = createOutlineBtn("Importar JSON", UITheme.NAVY, UITheme.LIGHT_BLUE, UITheme.BORDER);
        btnImportar.setOnAction(e -> importarJson());

        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(6, 10, 6, 10));
        topBar.setStyle("-fx-background-color: " + UITheme.LIGHT_BLUE + "; " +
                "-fx-border-color: transparent transparent " + UITheme.BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;");
        topBar.getChildren().addAll(btnAgregarCampo, btnImportar, procesarButton);

        camposContainer.getChildren().add(topBar);

        if (camposFromJson.isEmpty()) {
            Label placeholder = new Label("Importe un archivo JSON exportado para visualizar y editar los campos.");
            placeholder.setStyle(
                    "-fx-text-fill: #667788; -fx-font-size: 12px; " +
                            "-fx-font-family: 'Segoe UI'; -fx-padding: 20;");
            camposContainer.getChildren().add(placeholder);
            return;
        }

        int totalCampos = camposFromJson.size();
        int camposPorColumna = (int) Math.ceil(totalCampos / 3.0);

        HBox columnsBox = new HBox(8);
        columnsBox.setPadding(new Insets(8));

        for (int col = 0; col < 3; col++) {
            int inicio = col * camposPorColumna;
            int fin = Math.min(inicio + camposPorColumna, totalCampos);
            if (inicio < totalCampos) {
                columnsBox.getChildren().add(
                        createColumnPanel("", camposFromJson.subList(inicio, fin)));
            }
        }

        camposContainer.getChildren().add(columnsBox);
    }

    private VBox createColumnPanel(String titulo, List<Integer> campos) {
        Label titleBar = new Label(titulo);
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + UITheme.NAVY + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        VBox fieldsBox = new VBox(0);
        fieldsBox.setStyle("-fx-background-color: " + UITheme.WHITE + ";");

        int rowIdx = 0;
        for (int i : campos) {
            String bg = (rowIdx % 2 == 0) ? UITheme.WHITE : UITheme.STRIPE;

            CheckBox chk = new CheckBox();
            chk.setSelected(true);
            checkBoxes.put(i, chk);

            Label badge = new Label(String.format("P%03d", i));
            badge.setStyle(
                    "-fx-background-color: " + UITheme.NAVY + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 9px; -fx-font-weight: bold; " +
                            "-fx-padding: 1 5 1 5; -fx-background-radius: 3;");

            String valor = valoresFromJson.getOrDefault(i, "");
            TextField txt = new TextField(valor);
            txt.setStyle(STYLE_TF_RO);
            txt.setDisable(true);
            txt.setEditable(false);
            HBox.setHgrow(txt, Priority.ALWAYS);
            textFields.put(i, txt);

            Button editBtn = new Button("✎");
            editBtn.setStyle(STYLE_EDIT_OFF);
            editBtn.setOnAction(e -> {
                boolean editing = !txt.isEditable();
                txt.setDisable(!editing);
                txt.setEditable(editing);
                txt.setStyle(editing ? STYLE_TF_EDIT : STYLE_TF_RO);
                editBtn.setStyle(editing ? STYLE_EDIT_ON : STYLE_EDIT_OFF);
            });

            Button delBtn = new Button("✕");
            delBtn.setStyle(STYLE_DEL_OFF);
            delBtn.setOnMouseEntered(ev -> delBtn.setStyle(STYLE_DEL_ON));
            delBtn.setOnMouseExited(ev -> delBtn.setStyle(STYLE_DEL_OFF));
            delBtn.setOnAction(e -> {
                camposFromJson.remove(Integer.valueOf(i));
                refreshCampos();
            });

            HBox fieldRow = new HBox(6, chk, badge, txt, editBtn, delBtn);
            fieldRow.setAlignment(Pos.CENTER_LEFT);
            fieldRow.setPadding(new Insets(4, 8, 4, 8));
            fieldRow.setStyle(
                    "-fx-background-color: " + bg + "; " +
                            "-fx-border-color: transparent transparent " + UITheme.BORDER + " transparent; " +
                            "-fx-border-width: 0 0 1 0;");
            fieldsBox.getChildren().add(fieldRow);
            rowIdx++;
        }

        VBox column = new VBox(0, titleBar, fieldsBox);
        column.setStyle(
                "-fx-border-color: " + UITheme.NAVY + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + UITheme.WHITE + ";");
        VBox.setVgrow(fieldsBox, Priority.ALWAYS);
        HBox.setHgrow(column, Priority.ALWAYS);
        return column;
    }

    private void importarJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar JSON Exportado");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File archivo = fileChooser.showOpenDialog(getScene() != null ? getScene().getWindow() : null);
        if (archivo == null)
            return;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(archivo);

            camposFromJson.clear();
            valoresFromJson.clear();

            if (root.has("NetworkName")) {
                String net = root.get("NetworkName").asText("");
                comboRed.setValue("Visa".equalsIgnoreCase(net) ? "Visa" : "Mastercard");
            } else {
                comboRed.setValue("Mastercard");
            }

            messageTypeFromJson = root.has("TypeMessage")
                    ? root.get("TypeMessage").asText("0100")
                    : "0100";

            headerDefaultFromJson = root.has("HeaderDefault")
                    ? root.get("HeaderDefault").asText("")
                    : "";

            Iterator<Map.Entry<String, JsonNode>> sections = root.fields();
            while (sections.hasNext()) {
                Map.Entry<String, JsonNode> section = sections.next();
                if ("NetworkName".equals(section.getKey()))
                    continue;

                JsonNode sectionValue = section.getValue();
                if (sectionValue.isObject()) {
                    Iterator<Map.Entry<String, JsonNode>> fields = sectionValue.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> field = fields.next();
                        String key = field.getKey();
                        if (key.matches("P\\d{3}")) {
                            int num = Integer.parseInt(key.substring(1));
                            if (!camposFromJson.contains(num)) {
                                camposFromJson.add(num);
                            }
                            valoresFromJson.put(num, field.getValue().asText(""));
                        }
                    }
                }
            }

            Collections.sort(camposFromJson);
            refreshCampos();

            FXUtils.showInfoAlert("Importar JSON",
                    "Se cargaron " + camposFromJson.size() + " campos correctamente.");
        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al importar el JSON: " + ex.getMessage());
        }
    }

    private void procesarTrama() {
        long seleccionados = checkBoxes.values().stream().filter(CheckBox::isSelected).count();
        if (seleccionados < 5) {
            FXUtils.showInfoAlert("Selección Insuficiente",
                    "Para generar una trama válida, debe seleccionar al menos 5 campos.");
            return;
        }

        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(networkFromJson);
        Map<String, String> isoDataMap = new HashMap<>();

        checkBoxes.forEach((id, chk) -> {
            if (chk.isSelected()) {
                ISOField fieldDef = getFieldDef(id);
                String value = textFields.get(id).getText();

                if (isValorVacio(value)) {
                    value = valorPorDefectoParaCampoVacio(fieldDef);
                }

                if (fieldDef != null) {
                    if (fieldDef.getTypeData().equals(ISODataType.NUMERIC_DECIMAL)) {
                        Double amount = FieldUtil.convertAmountDouble(value);
                        if (amount != null) {
                            value = FieldUtil.convertAmountString(amount);
                        }
                    }
                    isoDataMap.put(fieldDef.getName(), value);
                } else {
                    isoDataMap.put(String.format("P%02d", id), value);
                }
            }
        });

        isoDataMap.put("messageType", messageTypeFromJson);

        try {
            String trama = delegateParser.unParser(isoDataMap);
            outputTextArea.setText(headerDefaultFromJson + trama);
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
                    if (camposFromJson.contains(numero)) {
                        FXUtils.showInfoAlert("Agregar campo", "El campo " + numero + " ya está en la lista.");
                    } else {
                        camposFromJson.add(numero);
                        Collections.sort(camposFromJson);
                        refreshCampos();
                        FXUtils.showInfoAlert("Agregar campo", "Campo " + numero + " agregado.");
                    }
                } catch (NumberFormatException ex) {
                    FXUtils.showInfoAlert("Error", "Debe ingresar un número válido.");
                }
            }
        });
    }

    private ISOField getFieldDef(int id) {
        if ("peer01".equalsIgnoreCase(networkFromJson)) {
            return VisaISOField.getById(id);
        }
        return MastercardISOField.getById(id);
    }

    private boolean isValorVacio(String value) {
        return value == null || value.isBlank();
    }

    private String valorPorDefectoParaCampoVacio(IFieldDefinition fieldDef) {
        if (fieldDef == null)
            return "";
        if (fieldDef.isVariable()) {
            return fieldDef.getLength() == 2 ? "00" : "000";
        }
        if (fieldDef.getTypeData() == ISODataType.ALPHA_NUMERIC) {
            return " ".repeat(fieldDef.getLength());
        }
        if (fieldDef.getTypeData() == ISODataType.NUMERIC) {
            return "0".repeat(fieldDef.getLength());
        }
        return " ".repeat(fieldDef.getLength());
    }

    private Button createPrimaryBtn(String text) {
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
