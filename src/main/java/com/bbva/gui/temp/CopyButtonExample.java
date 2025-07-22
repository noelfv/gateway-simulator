package com.bbva.gui.temp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection; // Para copiar al portapapeles
import java.awt.datatransfer.Clipboard; // Para copiar al portapapeles

public class CopyButtonExample extends JFrame {

    private JButton copyButton;
    private JLabel copiedLabel;
    private Timer hideTimer;

    public CopyButtonExample() {
        setTitle("Botón de Copiar con Confirmación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout()); // Usamos FlowLayout para simplicidad

        // Campo de texto de ejemplo para copiar su contenido
        JTextField textFieldToCopy = new JTextField("Texto de ejemplo para copiar", 25);
        add(textFieldToCopy);

        // 1. Crear el botón de copiar
        copyButton = new JButton("Copiar Texto");
        add(copyButton);

        // 2. Crear el JLabel para mostrar el mensaje "Copiado"
        copiedLabel = new JLabel("¡Copiado!");
        copiedLabel.setForeground(Color.BLUE); // Opcional: Dale un color distintivo
        copiedLabel.setVisible(false); // Por defecto, es invisible
        add(copiedLabel);

        // 3. Configurar el temporizador para ocultar el mensaje
        // El Timer se disparará después de 1000 milisegundos (1 segundo)
        hideTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiedLabel.setVisible(false); // Oculta el JLabel
                copyButton.setText("Copiar Texto");
                copyButton.setEnabled(true);// Detiene el temporizador para que no se repita
                textFieldToCopy.select(0, 0);
                hideTimer.stop();
            }
        });
        hideTimer.setRepeats(false); // Asegura que el temporizador solo se dispare una vez

        // 4. Añadir el ActionListener al botón
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para copiar el texto (ej. al portapapeles)
                String textToCopy = textFieldToCopy.getText();
                copyToClipboard(textToCopy);

                textFieldToCopy.requestFocusInWindow();
                textFieldToCopy.selectAll();

                copyButton.setText("copiado");
                copyButton.setEnabled(false);
                // Mostrar el mensaje "Copiado"
                copiedLabel.setVisible(true);
                // Reiniciar y arrancar el temporizador
                hideTimer.restart(); // Si ya estaba corriendo, lo resetea y vuelve a empezar
            }
        });

        pack(); // Ajusta el tamaño de la ventana a sus componentes
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    // Método auxiliar para copiar texto al portapapeles
    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.println("Texto copiado al portapapeles: " + text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CopyButtonExample().setVisible(true);
        });
    }
}