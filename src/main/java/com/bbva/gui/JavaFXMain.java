package com.bbva.gui;

import com.bbva.gui.panels.v2.*;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class JavaFXMain {

    private static final String BBVA_NAVY = "#004481";
    private static final String BBVA_WHITE = "#FFFFFF";

    private final BeanProviderInstance beans;
    private TabPane tabPane;

    public JavaFXMain(BeanProviderInstance beans) {
        this.beans = beans;
    }

    public void start(Stage stage) {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Gateway Message Parser - BBVA");
        stage.setMaximized(true);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setPadding(new Insets(0));

        // Menú Parsear
        Menu parseMenu = createMenu("Parsear");
        MenuItem parseItem = new MenuItem("Parsear mensaje");
        MenuItem parseClearItem = new MenuItem("Parsear mensaje claro");
        parseItem.setOnAction(e -> abrirTab("Parsear mensaje", new ParseViewerPane(beans)));
        parseClearItem.setOnAction(e -> abrirTab("Parsear mensaje claro", new ParseClearViewerPane(beans)));
        parseMenu.getItems().addAll(parseItem, parseClearItem);

        // Menú Conversion
        Menu conversionMenu = createMenu("Conversion");
        MenuItem convertirTramaItem = new MenuItem("Convertir trama");
        MenuItem convertirOriginalItem = new MenuItem("Convertir trama original");
        MenuItem convertirOriginalVisaItem = new MenuItem("Convertir trama original visa");
        MenuItem convertirIso20022Item = new MenuItem("Convertir Objeto ISO20022");
        MenuItem generarTramaItem = new MenuItem("Generar trama específica");
        MenuItem campo48Item = new MenuItem("Campo 48 (TLV)");

        convertirTramaItem.setOnAction(e -> abrirTab("Convertir trama", new ConverterTramaTextPlainPane(beans)));
        convertirOriginalItem.setOnAction(e -> abrirTab("Convertir trama original", new ConvertTramaOriginalPane(beans)));
        convertirOriginalVisaItem.setOnAction(e -> abrirTab("Convertir trama original visa", new ConvertTramaOriginalVisaPane(beans)));
        convertirIso20022Item.setOnAction(e -> abrirTab("Convertir Objeto ISO20022", new Transformer20022Pane(beans)));
        generarTramaItem.setOnAction(e -> abrirTab("Generar Trama Específica", new GenerateTramaISO8583Pane(beans)));
        campo48Item.setOnAction(e -> abrirTab("Parsear TLV", new TLVParseViewerPane(beans)));

        conversionMenu.getItems().addAll(
                convertirTramaItem, convertirOriginalItem, convertirOriginalVisaItem,
                convertirIso20022Item, generarTramaItem, campo48Item);

        // Menú Configuración
        Menu configMenu = createMenu("Configuración");
        MenuItem importarItem = new MenuItem("Importar Campos (JSON)");
        importarItem.setOnAction(e -> abrirTab("Configuration", new ConfigurationPane(beans)));
        configMenu.getItems().add(importarItem);

        menuBar.getMenus().addAll(parseMenu, conversionMenu, configMenu);
        menuBar.setStyle("-fx-background-color: " + BBVA_NAVY + "; -fx-padding: 5 10 5 10;");
        return menuBar;
    }

    private Menu createMenu(String text) {
        Menu menu = new Menu(text);
        menu.setStyle("-fx-text-fill: white; -fx-font-family: SansSerif; -fx-font-weight: bold; -fx-font-size: 12px;");
        return menu;
    }

    private void abrirTab(String title, javafx.scene.Node content) {
        FXUtils.abrirEnTab(tabPane, title, content);
    }
}
