package org.example.gui.temp;

import org.example.gui.ia.OllamaClient;
//import org.example.gui.utils.PDFProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManualAssistantGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton loadPdfButton;
    private JLabel statusLabel;

   // private PDFProcessor pdfProcessor;
    private OllamaClient ollamaClient;

    public ManualAssistantGUI() {
        initializeComponents();
        setupLayout();
        setupEventListeners();

        ollamaClient = new OllamaClient();
        checkOllamaStatus();
    }

    private void initializeComponents() {
        setTitle("Asistente de Manual - Ollama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 249, 250));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Enviar");
        sendButton.setEnabled(false);

        loadPdfButton = new JButton("Cargar PDF Manual");

        statusLabel = new JLabel("Estado: Verificando Ollama...");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel superior con botón de cargar PDF
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadPdfButton);
        add(topPanel, BorderLayout.NORTH);

        // Área de chat central
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con input y botón
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        loadPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPDF();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }


    private void loadPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String pdfPath = fileChooser.getSelectedFile().getAbsolutePath();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Estado: Procesando PDF...");
                        loadPdfButton.setEnabled(false);
                    });

                   // pdfProcessor = new PDFProcessor(pdfPath);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        statusLabel.setText("Estado: PDF cargado - Listo para consultas");
                        sendButton.setEnabled(true);
                        appendToChat("Sistema: PDF cargado exitosamente. Puedes hacer preguntas sobre el manual.\n\n");
                    } catch (Exception e) {
                        statusLabel.setText("Estado: Error al cargar PDF");
                        JOptionPane.showMessageDialog(ManualAssistantGUI.this,
                                "Error al procesar PDF: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        loadPdfButton.setEnabled(true);
                    }
                }
            };

            worker.execute();
        }
    }

    private void sendMessage() {
        String question = inputField.getText().trim();
      //  if (question.isEmpty() || pdfProcessor == null) return;

        inputField.setText("");
        inputField.setEnabled(false);
        sendButton.setEnabled(false);

        appendToChat("Tú: " + question + "\n\n");

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Estado: Consultando con Ollama...");
                });

               // List<String> relevantChunks = pdfProcessor.findRelevantChunks(question, 3);
                //return ollamaClient.queryDocument(question, relevantChunks);
                return null;
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    appendToChat("Asistente: " + response + "\n\n");
                    statusLabel.setText("Estado: Listo para consultas");
                } catch (Exception e) {
                    appendToChat("Error: No se pudo obtener respuesta de Ollama.\n\n");
                    statusLabel.setText("Estado: Error en consulta");
                } finally {
                    inputField.setEnabled(true);
                    sendButton.setEnabled(true);
                    inputField.requestFocus();
                }
            }
        };

        worker.execute();
    }

    private void appendToChat(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void checkOllamaStatus() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return ollamaClient.isOllamaAvailable();
            }

            @Override
            protected void done() {
                try {
                    boolean available = get();
                    if (available) {
                        statusLabel.setText("Estado: Ollama disponible - Carga un PDF para comenzar");
                        loadPdfButton.setEnabled(true);
                    } else {
                        statusLabel.setText("Estado: Ollama no disponible - Inicia Ollama primero");
                        JOptionPane.showMessageDialog(ManualAssistantGUI.this,
                                "Ollama no está disponible. Asegúrate de que esté instalado y ejecutándose.",
                                "Advertencia",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    statusLabel.setText("Estado: Error al verificar Ollama");
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {

        System.setProperty("user.home", "C:\\Users\\Public");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                // Usar look and feel por defecto
            }

            new ManualAssistantGUI().setVisible(true);
        });
    }
}