package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.FieldUtil;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GenerateTramaISO8583Pane extends BorderPane {

    // Light palette
    private static final String NAVY = "#004481";
    private static final String BLUE = "#1464A0";
    private static final String LIGHT_BLUE = "#E8F4FD";
    private static final String WHITE = "#FFFFFF";
    private static final String STRIPE = "#F7F9FC";
    private static final String BORDER = "#D0E4F7";
    // Dark (output terminal)
    private static final String DARK_BG = "#1E1E2E";
    private static final String DARK_HEADER = "#12122A";
    private static final String DARK_BORDER = "#2D2D4E";
    private static final String DARK_FOOT = "#16162A";
    private static final String TEXT_OUT = "#CDD6F4";
    private static final String ACCENT = "#89B4FA";
    private static final String ACCENT_DIM = "#5A7FCC";
    // Field row
    private static final String TF_RO_BG = "#F0F4F8";
    private static final String TF_BORDER = "#B8CCE0";

    private static final String STYLE_TF_RO = "-fx-control-inner-background: " + TF_RO_BG + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + TF_BORDER + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_TF_EDIT = "-fx-control-inner-background: " + WHITE + "; " +
            "-fx-font-family: Consolas; -fx-font-size: 10px; " +
            "-fx-border-color: " + NAVY + "; " +
            "-fx-border-radius: 3; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_OFF = "-fx-background-color: transparent; -fx-text-fill: #8899AA; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";
    private static final String STYLE_EDIT_ON = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; " +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 1 5; -fx-background-radius: 3;";

    private final Map<Integer, CheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, TextField> textFields = new TreeMap<>();

    private final List<Integer> listCompras = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 11, 12, 13, 14, 19, 20, 25));
    private final List<Integer> listBilletera = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 25, 32));
    private final List<Integer> listRetiros = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 11, 41, 42));
    private final Set<Integer> camposMandatoriosCompras = new HashSet<>(Arrays.asList(2, 3, 4, 7, 11, 12, 13));
    private final Set<Integer> camposMandatoriosBilletera = new HashSet<>(Arrays.asList(2, 3, 4, 7, 25, 32));
    private final Set<Integer> camposMandatoriosRetiros = new HashSet<>(Arrays.asList(2, 3, 4, 7, 11, 41, 42));

    private ComboBox<String> tipoOperacionComboBox;
    private ComboBox<String> procesarComboBox;
    private TextArea outputTextArea;
    private VBox camposContainer;
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
                "-fx-control-inner-background: " + DARK_BG + "; " +
                        "-fx-font-family: Consolas; -fx-font-size: 11px; " +
                        "-fx-text-fill: " + TEXT_OUT + "; " +
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
        for (ComboBox<?> cb : new ComboBox[] { procesarComboBox, tipoOperacionComboBox }) {
            cb.setStyle(
                    "-fx-font-size: 11px; -fx-background-color: white; " +
                            "-fx-border-color: " + BORDER + "; -fx-border-radius: 3; -fx-background-radius: 3;");
        }

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(7, 12, 7, 12));
        header.setStyle("-fx-background-color: " + NAVY + ";");
        header.getChildren().addAll(title, spacer, lblMarca, procesarComboBox, lblTipo, tipoOperacionComboBox);
        return header;
    }

    private SplitPane createCenterPanel() {
        // Top: scrollable fields area
        camposContainer = new VBox();
        refreshCampos();
        ScrollPane scrollPane = new ScrollPane(camposContainer);
        scrollPane.setFitToWidth(true);

        // Bottom: output area
        VBox outputPanel = createOutputPanel();

        SplitPane split = new SplitPane(scrollPane, outputPanel);
        split.setOrientation(Orientation.VERTICAL);
        split.setDividerPositions(0.65);
        return split;
    }

    private VBox createOutputPanel() {
        Label titleLabel = new Label("Trama Generada");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(5, 10, 5, 10));
        titleLabel.setStyle(
                "-fx-background-color: " + DARK_HEADER + "; " +
                        "-fx-text-fill: " + ACCENT + "; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                        "-fx-border-color: transparent transparent " + DARK_BORDER + " transparent; " +
                        "-fx-border-width: 0 0 1 0;");

        Button btnLimpiar = createOutlineBtn("Limpiar", ACCENT, "transparent", ACCENT_DIM);
        btnLimpiar.setOnAction(e -> outputTextArea.setText(""));

        Button btnCopiar = createOutlineBtn("Copiar Trama", ACCENT, "transparent", ACCENT_DIM);
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
        footer.setStyle("-fx-background-color: " + DARK_FOOT + ";");
        footer.getChildren().addAll(btnLimpiar, btnCopiar);

        VBox outputPanel = new VBox(0, titleLabel, outputTextArea, footer);
        outputPanel.setStyle(
                "-fx-border-color: " + DARK_BORDER + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + DARK_BG + ";");
        VBox.setVgrow(outputTextArea, Priority.ALWAYS);
        return outputPanel;
    }

    private void refreshCampos() {
        checkBoxes.clear();
        textFields.clear();
        camposContainer.getChildren().clear();

        List<Integer> camposActuales = getCamposActuales();

        // Action buttons
        Button procesarButton = createPrimaryBtn("Procesar");
        procesarButton.setOnAction(e -> procesarTrama());
        Button btnAgregarCampo = createOutlineBtn("+ Agregar campo", "#228B22", "transparent", "#E8F5E9");
        btnAgregarCampo.setOnAction(e -> mostrarDialogoAgregarCampo());

        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(6, 10, 6, 10));
        topBar.setStyle("-fx-background-color: " + LIGHT_BLUE + "; " +
                "-fx-border-color: transparent transparent " + BORDER + " transparent; " +
                "-fx-border-width: 0 0 1 0;");
        topBar.getChildren().addAll(btnAgregarCampo, procesarButton);

        // Fields grid distributed in 3 columns
        int totalCampos = camposActuales.size();
        int camposPorColumna = (int) Math.ceil(totalCampos / 3.0);

        HBox columnsBox = new HBox(8);
        columnsBox.setPadding(new Insets(8));

        for (int col = 0; col < 3; col++) {
            int inicio = col * camposPorColumna;
            int fin = Math.min(inicio + camposPorColumna, totalCampos);
            if (inicio < totalCampos) {
                columnsBox.getChildren().add(
                        createColumnPanel("Bloque " + (col + 1), camposActuales.subList(inicio, fin)));
            }
        }

        camposContainer.getChildren().addAll(topBar, columnsBox);
    }

    private VBox createColumnPanel(String titulo, List<Integer> campos) {
        // ── Title bar ──────────────────────────────────────────────
        Label titleBar = new Label(titulo);
        titleBar.setMaxWidth(Double.MAX_VALUE);
        titleBar.setPadding(new Insets(4, 8, 4, 8));
        titleBar.setStyle(
                "-fx-background-color: " + NAVY + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI';");

        // ── Field rows ─────────────────────────────────────────────
        VBox fieldsBox = new VBox(0);
        fieldsBox.setStyle("-fx-background-color: " + WHITE + ";");

        Set<Integer> mandatorios = getMandatoriosActuales();
        int rowIdx = 0;
        for (int i : campos) {
            String bg = (rowIdx % 2 == 0) ? WHITE : STRIPE;

            CheckBox chk = new CheckBox();
            chk.setSelected(mandatorios.contains(i));
            checkBoxes.put(i, chk);

            Label badge = new Label(String.format("P%03d", i));
            badge.setStyle(
                    "-fx-background-color: " + NAVY + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 9px; -fx-font-weight: bold; " +
                            "-fx-padding: 1 5 1 5; -fx-background-radius: 3;");

            TextField txt = new TextField(getDefaultValue(i));
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

            HBox fieldRow = new HBox(6, chk, badge, txt, editBtn);
            fieldRow.setAlignment(Pos.CENTER_LEFT);
            fieldRow.setPadding(new Insets(4, 8, 4, 8));
            fieldRow.setStyle(
                    "-fx-background-color: " + bg + "; " +
                            "-fx-border-color: transparent transparent " + BORDER + " transparent; " +
                            "-fx-border-width: 0 0 1 0;");
            fieldsBox.getChildren().add(fieldRow);
            rowIdx++;
        }

        VBox column = new VBox(0, titleBar, fieldsBox);
        column.setStyle(
                "-fx-border-color: " + NAVY + "; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-color: " + WHITE + ";");
        VBox.setVgrow(fieldsBox, Priority.ALWAYS);
        HBox.setHgrow(column, Priority.ALWAYS);
        return column;
    }

    private void setupHandlers() {
        tipoOperacionComboBox.setOnAction(e -> {
            refreshCampos();
            actualizarValoresPorDefecto();
        });
    }

    private void procesarTrama() {
        long seleccionados = checkBoxes.values().stream().filter(CheckBox::isSelected).count();
        if (seleccionados < 5) {
            FXUtils.showInfoAlert("Selección Insuficiente",
                    "Para generar una trama válida, debe seleccionar al menos 5 campos.");
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

        isoDataMap.put("messageType", "0100");

        try {
            String trama = delegateParser.unParser(isoDataMap);
            outputTextArea.setText(trama);
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
        return switch (tipoOperacionComboBox.getValue()) {
            case "Billetera" -> camposMandatoriosBilletera;
            case "Retiros" -> camposMandatoriosRetiros;
            default -> camposMandatoriosCompras;
        };
    }

    private void actualizarValoresPorDefecto() {
        Set<Integer> mandatorios = getMandatoriosActuales();
        for (Integer campo : getCamposActuales()) {
            TextField txt = textFields.get(campo);
            if (txt != null && !txt.isEditable()) {
                txt.setText(getDefaultValue(campo));
            }
            CheckBox chk = checkBoxes.get(campo);
            if (chk != null) {
                chk.setSelected(mandatorios.contains(campo));
            }
        }
    }

    private String getDefaultValue(int i) {
        String tipo = tipoOperacionComboBox != null ? tipoOperacionComboBox.getValue() : "Compras";
        return switch (tipo) {
            case "Billetera" -> switch (i) {
                case 2 -> "5536508888888888";
                case 3 -> "311000";
                case 4 -> "50.00";
                case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
                case 11 -> String.format("%06d", new Random().nextInt(1000000));
                case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
                case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                case 14 -> "2701";
                default -> "";
            };
            case "Retiros" -> switch (i) {
                case 2 -> "5536507777777777";
                case 3 -> "010000";
                case 4 -> "200.00";
                case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
                case 11 -> String.format("%06d", new Random().nextInt(1000000));
                case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
                case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                case 14 -> "2701";
                default -> "";
            };
            default -> switch (i) {
                case 2 -> "5536509999999999";
                case 3 -> "000000";
                case 4 -> "100.00";
                case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
                case 11 -> String.format("%06d", new Random().nextInt(1000000));
                case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
                case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                case 14 -> "2701";
                default -> "";
            };
        };
    }

    private boolean isValorVacio(String value) {
        return value == null || value.isBlank();
    }

    private String valorPorDefectoParaCampoVacio(MastercardISOField fieldDef) {
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
        String normal = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; " +
                "-fx-padding: 7 20 7 20; -fx-cursor: hand; -fx-background-radius: 4;";
        String hover = "-fx-background-color: " + BLUE + "; -fx-text-fill: white; " +
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
