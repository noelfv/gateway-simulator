package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.*;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Transformer20022Panel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transformer20022Panel.class);
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton convertirTramaButton;
    private JButton limpiarButton;
    private JButton copiarRespuestaButton;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    final String SAMPLE_ISO20022 = "{\"iccRelatedData\":\"\",\"networkName\":\"PEER02\",\"traceData\":[{\"value\":\"MDS6KC4PF\",\"key\":\"posAdditionalData\"},{\"value\":\"\",\"key\":\"header\"},{\"value\":\"a960cdbf-ecc6-4904-946a-6a5cac57f907\",\"key\":\"PAYMENT_ID\"}],\"messageFunction\":\"FAUQ\",\"monitoring\":{\"startDateMs\":\"1765560510774\",\"endDateMs\":\"1765560513369\",\"binCode\":\"519348\",\"binDescription\":\"MASTERCARD FPF\",\"merchantNameAceptor\":\"Plaza Vea\",\"merchantCategoryDescription\":\"TIENDAS DE COMESTIBLES Y SUPERMERCADOS\",\"transactionStatus\":\"Pending\",\"channelFilter\":\"ECOMMER\",\"operationFilter\":\"PURCHASE\",\"transactionTypeDescription\":\"COMPRAS\",\"countryDate\":\"2025-12-12T12:28:30.774-05:00\",\"isNextGen\":false},\"customDataLocal\":{\"additionalData\":[{\"request\":{\"value\":\"0120FEFF64010AE1E01A000000000000008016519348******970200000000000000536000000000159200000000536012121728187297014961000000061286122818121228121212121154116048100600796353462580612905171717174076797        Plaza Vea              Lima          PER400E3F6F3F1F5404040404040404040404040404040F6F6F4F5F0F1F0F1F2F0F2F3F6F782F3F88283F2F46083F784F260F4F9F5F3608285818460F5F5F3F083F9F282F1F7F486F4F2F0F7F0F1F0F3F2F1F2F4F3F2F892C1D486C2E4C9C1E3A882A88688E3A64E83619892C7C2C292C6E7E7F5F6F3F6C1D8E5F0F0F0C1D8E2F4F0F0C1D8C6F0F0F0C4C9C9F0D9C6C1C9D8F7D1C7C1D9C1C1C2C2F7F5F3F2F0F1F0F3F4F5F8F0F2F0F2F4F8F0F3F0F3F4F5F8F0F4F0F2F4F8F0F5F0F2F0F0F7F1F0F8F1F8C340F0F5E540604840604007102161102610251040060006040000000000009MDS6KC4PF006000001\",\"key\":\"ISO8583\"}},{\"request\":{\"value\":\"0120\",\"key\":\"MSGTYPE\"}},{\"request\":{\"value\":\"E3F6F3F1F5404040404040404040404040404040F6F6F4F5F0F1F0F1F2F0F2F3F6F782F3F88283F2F46083F784F260F4F9F5F3608285818460F5F5F3F083F9F282F1F7F486F4F2F0F7F0F1F0F3F2F1F2F4F3F2F892C1D486C2E4C9C1E3A882A88688E3A64E83619892C7C2C292C6E7E7F5F6F3F6C1D8E5F0F0F0C1D8E2F4F0F0C1D8C6F0F0F0C4C9C9F0D9C6C1C9D8F7D1C7C1D9C1C1C2C2F7F5F3F2F0F1F0F3F4F5F8F0F2F0F2F4F8F0F3F0F3F4F5F8F0F4F0F2F4F8F0F5F0F2F0F0F7F1F0F8F1F8C340F0F5E540\",\"key\":\"DE_48\"}}]},\"supplementaryData\":[{\"envelope\":\"1211\",\"placeAndName\":\"dateConversion\"},{\"envelope\":\"000001\",\"placeAndName\":\"authorizingAgentIdCode\"}],\"socketPort\":\"7003\",\"addendumData\":{\"additionalData\":[{\"value\":\"0120\",\"key\":\"UNSP\"}]},\"environment\":{\"card\":{\"pan\":\"519348******9702\",\"cardSequenceNumber\":\"\",\"serviceCode\":\"\"},\"terminal\":{\"capabilities\":{\"cardCaptureCapable\":false,\"cardReadingCapabilities\":[{\"capability\":\"UNKW\"}],\"cardholderVerificationCapabilities\":[{\"capability\":\"UNSP\"}]},\"terminalId\":{\"id\":\"17171717\",\"assigner\":\"1021611\",\"country\":\"604\"},\"key\":\"OTHN\",\"otherType\":\"Cardholder terminal\",\"offPremisesIndicator\":true},\"acquirer\":{\"id\":\"007963\",\"country\":\"604\",\"additionalId\":{\"key\":\"postalCode\",\"value\":\"\"}},\"sender\":{\"id\":\"\",\"additionalId\":{\"key\":\"additionalDataRetailer\",\"value\":\"E3F6F3F1F5404040404040404040404040404040F6F6F4F5F0F1F0F1F2F0F2F3F6F782F3F88283F2F46083F784F260F4F9F5F3608285818460F5F5F3F083F9F282F1F7F486F4F2F0F7F0F1F0F3F2F1F2F4F3F2F892C1D486C2E4C9C1E3A882A88688E3A64E83619892C7C2C292C6E7E7F5F6F3F6C1D8E5F0F0F0C1D8E2F4F0F0C1D8C6F0F0F0C4C9C9F0D9C6C1C9D8F7D1C7C1D9C1C1C2C2F7F5F3F2F0F1F0F3F4F5F8F0F2F0F2F4F8F0F3F0F3F4F5F8F0F4F0F2F4F8F0F5F0F2F0F0F7F1F0F8F1F8C340F0F5E540\"}},\"acceptor\":{\"id\":\"4076797        \",\"nameAndLocation\":\"Plaza Vea              Lima          PER\",\"localData\":{\"address\":{\"postalCode\":\"0000000000\"}}},\"issuer\":{\"assigner\":\"10251040060006040000000000\"}},\"context\":{\"transactionContext\":{\"settlementService\":{\"settlementServiceDates\":{\"settlementDate\":\"1212\"}},\"merchantCategoryCode\":\"5411\",\"merchantCategorySpecificData\":\"NATIONAL\",\"reconciliation\":{},\"additionalData\":[{\"value\":\"ALL\",\"key\":\"ENTRY_MODE\"},{\"value\":\"PURCHASE\",\"key\":\"OPERATION_TYPE\"},{\"value\":\"ECOMMER\",\"key\":\"CHANNEL\"},{\"value\":\"ONUS\",\"key\":\"OWNER\"}],\"transactionInitiator\":\"0000\"},\"verification\":[{\"verificationResult\":[{\"resultDetails\":[{\"value\":\"\",\"key\":\"PENDING\"}],\"key\":\"card_validation_code_result\"}]}],\"pointOfServiceContext\":{\"cardDataEntryMode\":\"OTHN\",\"cardPresent\":false,\"cardholderPresent\":false,\"unattendedLevelCategory\":\"CAT LEVEL 6\",\"ecommerceIndicator\":true,\"attendedIndicator\":false,\"additionalData\":[{\"value\":\"810\",\"key\":\"UNKNOWN\"}]},\"saleContext\":{\"additionalData\":[{\"value\":\"\",\"key\":\"campaignData\"}]}},\"isSimulation\":false,\"transaction\":{\"transactionId\":{\"transactionReference\":\"72f054d9-a465-395c-9650-771365cc528\",\"transmissionDateTime\":\"2025-12-12T17:28:18.000Z\",\"systemTraceAuditNumber\":\"061286\",\"localDate\":\"1212\",\"localTime\":\"122818\",\"localDateTime\":\"2025-12-12T12:28:18.000Z\",\"retrievalReferenceNumber\":\"534625806129\",\"originalDataElements\":{\"systemTraceAuditNumber\":\"061286\",\"transmissionDateTime\":\"1212172818\",\"senderIdentification\":\"00000000000\",\"messageFunction\":\"AUTQ\",\"acquirerId\":\"00000007963\"}},\"transactionType\":\"00\",\"accountFrom\":{\"accountId\":\"\",\"accountType\":\"00\"},\"accountTo\":{\"accountId\":\"\",\"accountType\":\"00\"},\"transactionAmounts\":{\"transactionAmount\":{\"amount\":53.6,\"currency\":\"PEN\"},\"reconciliationAmount\":{\"amount\":15.92,\"currency\":\"USD\",\"effectiveExchangeRate\":\".2970149\"},\"cardholderBillingAmount\":{\"amount\":53.6,\"currency\":\"PEN\",\"effectiveExchangeRate\":\"1.000000\"}},\"transactionAttribute\":0,\"messageReason\":\"\",\"additionalFee\":[{\"feeReconciliationAmount\":{},\"feeAmount\":{}}],\"additionalAmount\":[{\"amount\":{},\"key\":\"BLNCHECK\"}],\"additionalData\":[{\"value\":\"000000\",\"key\":\"opera\"},{\"value\":\"\",\"key\":\"redemptionPoints\"},{\"value\":\"212\",\"key\":\"electronic_commerce_indicators\"},{\"value\":\"05\",\"key\":\"ECI\"},{\"value\":\"\",\"key\":\"additionalResponseData\"},{\"value\":\"T\",\"key\":\"transaction_category_code\"},{\"value\":\"kAMfBUIATybyfhTw+c/qkGBBkFXX\",\"key\":\"universal_cardholder_authentication_field\"},{\"value\":\"18C 05V \",\"key\":\"on_behalf_services\"}],\"specialProgrammeQualification\":[{\"detail\":[{\"name\":\"mastercard_promotion_code\"}]}]},\"processingResult\":{\"additionalInformation\":[{\"value\":\"Denied\",\"key\":\"transaction\"}],\"approvalCode\":\"\",\"resultData\":{\"result\":\"PRCS\",\"otherResult\":\"legal_agreement_retain\",\"otherResultDetails\":\"05\"}}}";
    @Setter
    private JInternalFrame parentFrame;

    private  ParserFactory parserFactory;
    private  MapperFactory mapperFactory;
    private ISO8583DelegateParser delegateParser;
    private ISO20022DelegateMapper delegateMapper;

    public Transformer20022Panel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
        this.mapperFactory = beanProviderInstance.mapperFactory();

        //Poner el foco en el JTextArea de entrada
        SwingUtilities.invokeLater(() -> {
            inputTextArea.requestFocusInWindow();
        });
    }


    private String jsonPrettyPrint(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(SAMPLE_ISO20022, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            LOGGER.error("Error al formatear JSON: {}", e.getMessage());
            return json;
        }
    }

   public void initializeComponents() {

       setLayout(new BorderLayout());

       inputTextArea = new JTextArea(8, 60);
       inputTextArea.setLineWrap(true);
       inputTextArea.setWrapStyleWord(true);
       inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
       //inputTextArea.setBackground(Color.LIGHT_GRAY);
       inputTextArea.setText(jsonPrettyPrint(SAMPLE_ISO20022));

       outputTextArea = new JTextArea(12, 60);
       outputTextArea.setLineWrap(true);
       outputTextArea.setWrapStyleWord(true);
       outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

       convertirTramaButton = new JButton("Unparser");
       limpiarButton = new JButton("Limpiar");
       copiarRespuestaButton = new JButton("Copiar respuesta");

       DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
       treeModel = new DefaultTreeModel(root);
       resultTree = new JTree(treeModel);
       resultTree.setRootVisible(true);
    }

    private void  createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager= PanelDoggy.setupStructureMyDoggy(createMainPanel(), resultTree, createOutputPanel());
        add(toolWindowManager, BorderLayout.CENTER);
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("ISO20022 de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(convertirTramaButton);
        buttonPanel.add(limpiarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Establecer tamaño preferido para el panel
        panel.setPreferredSize(new Dimension(800, 200));

        outputTextArea.setPreferredSize(new Dimension(780, 180));
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        //JPanel compareButtonPanel = new JPanel(new FlowLayout());
        //panel.add(compareButtonPanel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(copiarRespuestaButton, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        return panel;
    }

    private void setupEventHandlers() {
        convertirTramaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Convertir Trama button clicked");
                convertMessage();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
                // Limpiar el JTree
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
                treeModel.setRoot(root);
                treeModel.reload();
            }
        });

        copiarRespuestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(outputTextArea.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                outputTextArea.requestFocusInWindow();
                outputTextArea.selectAll();


                // Mostrar tooltip temporal
                copiarRespuestaButton.setToolTipText("¡Texto copiado!");
                ToolTipManager.sharedInstance().mouseMoved(
                        new java.awt.event.MouseEvent(
                                copiarRespuestaButton, 0, 0, 0,
                                0, 0, // X-Y
                                0, false));
                // Quitar el tooltip después de 1 segundo
                Timer timer = new Timer(1000, evt -> copiarRespuestaButton.setToolTipText(null));
                timer.setRepeats(false);
                timer.start();

            }
        });
    }

    private void convertMessage() {

        ParseResult result;

        try {
            String jsonString = inputTextArea.getText().trim();
            if (!jsonString.startsWith("{")) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese una estructura json correcta en formato ISO20022",
                        "WARNING", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convertir de String JSON a objeto ISO20022
            ObjectMapper objectMapper = new ObjectMapper();
            // Configurar ObjectMapper de forma más permisiva
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            ISO20022 iso20022 = objectMapper.readValue(jsonString, ISO20022.class);

            delegateParser = parserFactory.getDelegateParser(iso20022.getNetworkName());
            delegateMapper = mapperFactory.getDelegateMapper(iso20022.getNetworkName());
            System.out.println("ConverterIso20022ViewerPanel DelegateParser bean: " + delegateParser);
            System.out.println("ConverterIso20022ViewerPanel DelegateMapper bean: " + delegateMapper);
            //delegateParser = ApplicationContextProvider.getBean(ParserFactory.class).getDelegateParser("PEER02");
           // delegateMapper = ApplicationContextProvider.getBean(MapperFactory.class).getDelegateMapper("PEER02");
            //System.out.println("Delegate Parser: " + delegateParser.getClass().getName());
           // System.out.println("DelegateMapper Mapper: " + delegateMapper.getClass().getName());
            Map<String, String> fieldsValues  = delegateMapper.unMapper(iso20022);
            String trama=delegateParser.unParserPlainText(fieldsValues);
            LOGGER.info("Trama generada: [{}]", trama);
            outputTextArea.setText(trama );
            Map<String,String> mapValues;
            if (iso20022.getNetworkName().equalsIgnoreCase("PEER02")) {
                mapValues=ISO8583Processor.createMapFieldsISO8583Mastercard(trama);
            }else{
                mapValues=ISO8583Processor.createMapFieldsISO8583Visa(trama);
            }

            result = ParseGUI.process(mapValues);
            //result = ParseGUI.process(fieldsValues);
            ParseGUI.updateTreeView(treeModel, resultTree, result);

        } catch (ParserFieldsException ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            /*result = ParseGUI.process(ex.getValuesMap());
            ParseGUI.updateTreeView(treeModel, resultTree, result);*/
            outputTextArea.setText("Error: " + ex.getMessage());
        }catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

}