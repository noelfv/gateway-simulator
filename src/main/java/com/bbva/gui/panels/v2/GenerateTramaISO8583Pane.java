package com.bbva.gui.panels.v2;

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

    private static final String BBVA_NAVY = "#004481";
    private static final String BBVA_WHITE = "#FFFFFF";
    private static final String BBVA_ACCENT_BLUE = "#12BEFF";
    private static final String BBVA_BLACK = "#282832";

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
        outputTextArea.setStyle("-fx-control-inner-background: " + BBVA_BLACK + "; " +
                "-fx-font-family: Tahoma; -fx-font-size: 10px; -fx-text-fill: " + BBVA_ACCENT_BLUE + ";");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);
    }

    private void buildLayout() {
        setTop(createHeaderPanel());
        setCenter(createCenterPanel());
    }

    private HBox createHeaderPanel() {
        Label lblProcesar = new Label("Marca:");
        lblProcesar.setStyle("-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + BBVA_NAVY + ";");
        Label lblTipo = new Label("Tipo de operación:");
        lblTipo.setStyle("-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + BBVA_NAVY + ";");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8, 8, 8, 8));
        header.setStyle("-fx-border-color: " + BBVA_NAVY + "; -fx-border-width: 0 0 1 0;");
        header.getChildren().addAll(lblProcesar, procesarComboBox, lblTipo, tipoOperacionComboBox);
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
        Button btnCopiar = createButton("Copiar Trama");
        btnCopiar.setOnAction(e -> {
            String trama = outputTextArea.getText();
            if (!trama.isEmpty()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(trama);
                Clipboard.getSystemClipboard().setContent(content);
                FXUtils.showInfoAlert("BBVA Generator", "Trama copiada al portapapeles");
            }
        });

        Button btnLimpiar = createButton("Limpiar");
        btnLimpiar.setOnAction(e -> outputTextArea.setText(""));

        HBox toolBar = new HBox(8);
        toolBar.setAlignment(Pos.CENTER_RIGHT);
        toolBar.setPadding(new Insets(4));
        toolBar.setStyle("-fx-background-color: #F4F4F4;");
        toolBar.getChildren().addAll(btnCopiar, btnLimpiar);

        VBox outputPanel = new VBox(toolBar, outputTextArea);
        VBox.setVgrow(outputTextArea, Priority.ALWAYS);
        return outputPanel;
    }

    private void refreshCampos() {
        checkBoxes.clear();
        textFields.clear();
        camposContainer.getChildren().clear();

        List<Integer> camposActuales = getCamposActuales();

        // Action buttons
        Button procesarButton = createButton("Procesar");
        procesarButton.setOnAction(e -> procesarTrama());
        Button btnAgregarCampo = createButton("Agregar campo");
        btnAgregarCampo.setStyle(btnAgregarCampo.getStyle() + " -fx-background-color: #228B22;");
        btnAgregarCampo.setOnAction(e -> mostrarDialogoAgregarCampo());

        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(4, 8, 4, 8));
        topBar.getChildren().addAll(btnAgregarCampo, procesarButton);

        // Fields grid distributed in 3 columns
        int totalCampos = camposActuales.size();
        int camposPorColumna = (int) Math.ceil(totalCampos / 3.0);

        HBox columnsBox = new HBox(8);
        columnsBox.setPadding(new Insets(4));

        for (int col = 0; col < 3; col++) {
            int inicio = col * camposPorColumna;
            int fin = Math.min(inicio + camposPorColumna, totalCampos);
            if (inicio < totalCampos) {
                List<Integer> camposColumna = camposActuales.subList(inicio, fin);
                columnsBox.getChildren().add(createColumnPanel("Bloque " + (col + 1), camposColumna));
            }
        }
        HBox.setHgrow(columnsBox.getChildren().isEmpty() ? new HBox() : columnsBox.getChildren().get(0), Priority.ALWAYS);

        camposContainer.getChildren().addAll(topBar, columnsBox);
    }

    private VBox createColumnPanel(String titulo, List<Integer> campos) {
        GridPane grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(2);
        grid.setPadding(new Insets(4));
        grid.setStyle("-fx-border-color: " + BBVA_NAVY + "; -fx-border-width: 1; -fx-background-color: white;");

        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle("-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + BBVA_NAVY + ";");

        int row = 0;
        for (int i : campos) {
            String fieldName = String.format("P%02d", i);

            CheckBox chk = new CheckBox(fieldName);
            chk.setStyle("-fx-text-fill: " + BBVA_NAVY + "; -fx-font-size: 11px;");
            chk.setSelected(getMandatoriosActuales().contains(i));
            checkBoxes.put(i, chk);

            TextField txt = new TextField(getDefaultValue(i));
            txt.setStyle("-fx-background-color: " + BBVA_ACCENT_BLUE + "; -fx-font-size: 10px;");
            txt.setDisable(true);
            txt.setEditable(false);
            txt.setPrefWidth(160);
            textFields.put(i, txt);

            RadioButton rb = new RadioButton("Edit");
            rb.setStyle("-fx-font-size: 10px;");
            rb.setOnAction(e -> {
                txt.setDisable(!rb.isSelected());
                txt.setEditable(rb.isSelected());
                if (rb.isSelected()) {
                    txt.setStyle("-fx-background-color: white; -fx-font-size: 10px;");
                } else {
                    txt.setStyle("-fx-background-color: " + BBVA_ACCENT_BLUE + "; -fx-font-size: 10px;");
                }
            });

            grid.add(chk, 0, row);
            grid.add(txt, 1, row);
            grid.add(rb, 2, row);
            row++;
        }

        VBox column = new VBox(4, tituloLabel, grid);
        column.setPadding(new Insets(4));
        VBox.setVgrow(grid, Priority.ALWAYS);
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
        if (fieldDef == null) return "";
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

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + BBVA_NAVY + "; -fx-text-fill: white; " +
                "-fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-padding: 8 25 8 25; -fx-cursor: hand;");
        return btn;
    }
}
