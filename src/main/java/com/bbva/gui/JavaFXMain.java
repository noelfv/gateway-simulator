package com.bbva.gui;

import com.bbva.gui.panels.*;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class JavaFXMain {

    // ── BBVA Brand Colors ──────────────────────────────────────────
    private static final String NAVY = "#004481";
    private static final String NAVY_DARK = "#003366";
    private static final String NAVY_LIGHT = "#005A9E";
    private static final String AQUA = "#2DCCCD";
    private static final String LIGHT_GRAY = "#F4F6F9";
    private static final String MID_GRAY = "#DDE3ED";
    private static final String TEXT_DARK = "#1A2332";
    private static final String TEXT_MUTED = "#64748B";
    private static final String GREEN_DOT = "#22C55E";
    private static final String WHITE = "#FFFFFF";

    private static final String VERSION = "v2.0.0-NFV";

    private final BeanProviderInstance beans;
    private TabPane tabPane;

    public JavaFXMain(BeanProviderInstance beans) {
        this.beans = beans;
    }

    public void start(Stage stage) {
        tabPane = createTabPane();

        VBox topSection = new VBox(0, createHeader(), createMenuBar());

        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setCenter(tabPane);
        root.setBottom(createStatusBar());
        root.setStyle("-fx-background-color: " + LIGHT_GRAY + ";");

        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.setTitle("BBVA · Gateway Message Simulator");
        stage.setMaximized(true);
    }

    // ── Header ─────────────────────────────────────────────────────
    private HBox createHeader() {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle(
                "-fx-background-color: linear-gradient(to right, " + NAVY_DARK + " 0%, " + NAVY_LIGHT + " 100%);");

        // Marca visual (cuadrado BBVA)
        Region brand = new Region();
        brand.setPrefSize(26, 26);
        brand.setStyle(
                "-fx-background-color: " + AQUA + "; " +
                        "-fx-background-radius: 4;");

        Label appName = new Label("Gateway Message Simulator");
        appName.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: 'Segoe UI';");

        Label dot = new Label("·");
        dot.setStyle(
                "-fx-text-fill: " + AQUA + "; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-family: 'Segoe UI';");

        Label subtitle = new Label("ISO 8583  →  ISO 20022");
        subtitle.setStyle(
                "-fx-text-fill: rgba(255,255,255,0.60); " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-family: 'Consolas';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label version = new Label(VERSION);
        version.setStyle(
                "-fx-text-fill: rgba(255,255,255,0.38); " +
                        "-fx-font-size: 10px; " +
                        "-fx-font-family: 'Segoe UI';");

        header.getChildren().addAll(brand, appName, dot, subtitle, spacer, version);
        return header;
    }

    // ── Menu bar ───────────────────────────────────────────────────
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle(
                "-fx-background-color: #F0F2F5; " +
                        "-fx-padding: 3 12 3 12; " +
                        "-fx-border-color: transparent transparent #C8D0DC transparent; " +
                        "-fx-border-width: 0 0 1 0;");

        menuBar.getMenus().addAll(buildParseMenu(), buildConversionMenu(), buildConfigMenu(),buildIAMenu());
        return menuBar;
    }

    private Menu buildParseMenu() {
        Menu menu = styledMenu("Parsear");
        MenuItem parseItem = styledItem("Parsear mensaje");
        // MenuItem parseClearItem = styledItem("Parsear mensaje claro");
        parseItem.setOnAction(e -> abrirTab("Parsear mensaje", new ParseViewerPane(beans)));
        // parseClearItem.setOnAction(e -> abrirTab("Parsear mensaje claro", new
        // ParseClearViewerPane(beans)));
        menu.getItems().addAll(parseItem/* , new SeparatorMenuItem(), parseClearItem */);
        return menu;
    }

    private Menu buildConversionMenu() {
        Menu menu = styledMenu("Conversión");
        // MenuItem convertirTrama = styledItem("Convertir trama");
        MenuItem convertirOrig = styledItem("Convertir trama original");
        // MenuItem convertirVisa = styledItem("Convertir trama original Visa");
        MenuItem convertirIso = styledItem("Convertir ISO20022");
        MenuItem generarTrama = styledItem("Generar trama específica");
        MenuItem campo48 = styledItem("Campo 48 (TLV)");

        // convertirTrama.setOnAction(e -> abrirTab("Convertir trama", new
        // ConverterTramaTextPlainPane(beans)));
        convertirOrig.setOnAction(e -> abrirTab("Convertir trama original", new ConvertTramaOriginalPane(beans)));
        // convertirVisa.setOnAction(e -> abrirTab("Convertir trama original Visa", new
        // ConvertTramaOriginalVisaPane(beans)));
        convertirIso.setOnAction(e -> abrirTab("Convertir Objeto ISO20022", new Transformer20022Pane(beans)));
        generarTrama.setOnAction(e -> abrirTab("Generar Trama Específica", new GenerateTramaISO8583Pane(beans)));
        campo48.setOnAction(e -> abrirTab("Parsear TLV", new TLVParseViewerPane(beans)));

        menu.getItems().addAll(/* convertirTrama, */ convertirOrig, /* convertirVisa, */
                 convertirIso, new SeparatorMenuItem(),generarTrama,
                new SeparatorMenuItem(), campo48);
        return menu;
    }

    private Menu buildConfigMenu() {
        Menu menu = styledMenu("Configuración");
        MenuItem importarItem = styledItem("Importar Configuracion");
        MenuItem cargarLLM = styledItem("Cargar especificacion");
        //importarItem.setOnAction(e -> abrirTab("Configuración", new ConfigurationPane(beans)));
        menu.getItems().addAll(importarItem, new SeparatorMenuItem(),cargarLLM);
        return menu;
    }

    private Menu buildIAMenu() {
        Menu menu = styledMenu("IA");
        MenuItem importarItem = styledItem("Cargar modelo");
        MenuItem cargarLLM = styledItem("Cargar skill");
        menu.getItems().addAll(importarItem, new SeparatorMenuItem(),cargarLLM);
        return menu;
    }

    // ── Tab pane ───────────────────────────────────────────────────
    private TabPane createTabPane() {
        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tp.setStyle(
                "-fx-tab-min-height: 30px; " +
                        "-fx-tab-max-height: 30px; " +
                        "-fx-background-color: " + LIGHT_GRAY + ";");
        return tp;
    }

    // ── Status bar ─────────────────────────────────────────────────
    private HBox createStatusBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(4, 16, 4, 16));
        bar.setStyle(
                "-fx-background-color: " + MID_GRAY + "; " +
                        "-fx-border-color: " + MID_GRAY + " transparent transparent transparent; " +
                        "-fx-border-width: 1 0 0 0;");

        Region dot = new Region();
        dot.setPrefSize(8, 8);
        dot.setStyle("-fx-background-color: " + GREEN_DOT + "; -fx-background-radius: 4;");

        Label statusLabel = new Label("Listo");
        statusLabel.setStyle(mutedStyle());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label networks = new Label("PEER01 · Visa    |    PEER02 · Mastercard");
        networks.setStyle(mutedStyle());

        Separator sep = new Separator(Orientation.VERTICAL);
        sep.setPadding(new Insets(0, 4, 0, 4));

        Label port = new Label("Puerto 8083");
        port.setStyle(mutedStyle());

        bar.getChildren().addAll(dot, statusLabel, spacer, networks, sep, port);
        return bar;
    }

    // ── Factory helpers ────────────────────────────────────────────
    private Menu styledMenu(String text) {
        Menu menu = new Menu(text);
        menu.setStyle(
                "-fx-text-fill: " + NAVY_DARK + "; " +
                        "-fx-font-family: 'Segoe UI Semibold'; " +
                        "-fx-font-size: 13px;");
        return menu;
    }

    private MenuItem styledItem(String text) {
        MenuItem item = new MenuItem(text);
        item.setStyle(
                "-fx-font-family: 'Segoe UI'; " +
                        "-fx-font-size: 12px; " +
                        "-fx-text-fill: " + TEXT_DARK + ";");
        return item;
    }

    private String mutedStyle() {
        return "-fx-text-fill: " + TEXT_MUTED + "; -fx-font-size: 10px; -fx-font-family: 'Segoe UI';";
    }

    private void abrirTab(String title, javafx.scene.Node content) {
        FXUtils.abrirEnTab(tabPane, title, content);
    }
}
