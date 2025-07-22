package com.bbva.gui.temp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent; // Necesario para MouseEvent
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class CopyButtonWithTooltipFixed extends JFrame { // Cambié el nombre de la clase para evitar conflictos

    private JButton copyButton;
    private JTextField textFieldToCopy;
    private String originalTooltipText; // Para guardar el tooltip original del botón
    private Timer hideTooltipTimer;

    public CopyButtonWithTooltipFixed() {
        setTitle("Botón de Copiar con Tooltip Temporal (Corregido)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout()); // Usamos FlowLayout para simplicidad

        textFieldToCopy = new JTextField("Contenido para copiar al portapapeles", 30);
        add(textFieldToCopy);

        copyButton = new JButton("Copiar Texto");
        // Establece un tooltip inicial si lo deseas. Esto es lo que se mostrará normalmente.
        // **MUY IMPORTANTE**: Asegúrate de que este texto NO sea nulo si esperas que el tooltip aparezca.
        copyButton.setToolTipText("Haz clic para copiar");
        originalTooltipText = copyButton.getToolTipText(); // Guarda el texto original.

        // Si originalTooltipText es null, el ToolTipManager podría no hacer nada.
        // Podemos poner un valor por defecto si no se le asigna uno al principio.
        if (originalTooltipText == null) {
            originalTooltipText = ""; // O un mensaje como "Copiar texto"
        }

        add(copyButton);

        // --- Configuración del Timer para ocultar/resetear el tooltip ---
        hideTooltipTimer = new Timer(1500, new ActionListener() { // 1.5 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restablece el tooltip al texto original
                copyButton.setToolTipText(originalTooltipText);
                hideTooltipTimer.stop(); // Detiene el temporizador
            }
        });
        hideTooltipTimer.setRepeats(false); // Asegura que se ejecute solo una vez

        // --- ActionListener para el botón de copiar ---
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Copiar el texto al portapapeles
                String textToCopy = textFieldToCopy.getText();
                copyToClipboard(textToCopy);

                // 2. Cambiar el texto del tooltip a "¡Copiado!"
                copyButton.setToolTipText("¡Copiado!");

                // 3. Forzar la aparición del tooltip
                // Este es el punto crítico. Asegúrate de que las coordenadas sean correctas.
                // Usar (0,0) como coordenadas X, Y relativas al componente a veces no funciona bien
                // porque el ToolTipManager espera coordenadas donde el mouse "podría estar".
                // Es mejor usar el centro o cualquier punto dentro del botón.

                // Obtiene el punto en pantalla del botón (esquina superior izquierda)
                Point p = copyButton.getLocationOnScreen();
                // Calcula el centro del botón en coordenadas de pantalla.
                //int xScreen = p.x + copyButton.getWidth() / 2;
                //int yScreen = p.y + copyButton.getHeight() / 2;

                int xScreen = copyButton.getWidth() / 2;
                int yScreen = copyButton.getHeight() / 2;

                // Creamos un MouseEvent con las coordenadas del centro del botón.
                // Es crucial que 'source' sea el botón y el ID sea MOUSE_MOVED o MOUSE_ENTERED.
                MouseEvent fakeHoverEvent = new MouseEvent(
                        copyButton,                      // source: el componente que genera el evento
                        MouseEvent.MOUSE_MOVED,          // id: Tipo de evento (simula movimiento/entrada)
                        System.currentTimeMillis(),      // when: tiempo actual
                        0,                               // modifiers: sin modificadores de teclado/raton
                        xScreen,                         // x: coordenada X absoluta en pantalla
                        yScreen,                         // y: coordenada Y absoluta en pantalla
                        0,                               // clickCount: no es un clic
                        false                            // isPopupTrigger: no es un trigger de popup
                );

                // Disparamos el evento al ToolTipManager.
                // Esto hace que el ToolTipManager "piense" que el ratón entró en el botón
                // y muestre el tooltip con el nuevo texto "¡Copiado!".
                // Nota: mouseMoved es a menudo suficiente. mouseEntered es para casos específicos.
                ToolTipManager.sharedInstance().mouseMoved(fakeHoverEvent);

                // 4. Iniciar (o reiniciar) el temporizador para restaurar el tooltip
                hideTooltipTimer.restart();
            }
        });

        // Configuración adicional del ToolTipManager (puede ayudar si hay problemas)
        ToolTipManager.sharedInstance().setInitialDelay(100); // Aparece más rápido
        ToolTipManager.sharedInstance().setDismissDelay(5000); // Permanece visible 5 segundos
        ToolTipManager.sharedInstance().setReshowDelay(50); // Rápido para reaparecer

        pack();
        setLocationRelativeTo(null);
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
            new CopyButtonWithTooltipFixed().setVisible(true);
        });
    }
}