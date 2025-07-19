package com.bbva.gui.temp;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clase que representa la ventana de la calculadora
public class CalculatorDialog extends JDialog implements ActionListener {
    private JTextField display;
    private String currentOperation = "";
    private double firstOperand = 0;
    private boolean newNumber = true;

    public CalculatorDialog(JFrame parent) {
        super(parent, "Calculadora", true); // 'true' hace que el diálogo sea modal
        //setUndecorated(true);
        setSize(300, 400);
        setLocationRelativeTo(parent); // Centrar respecto a la ventana principal
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cierra solo este diálogo

        // Opcional: Borde para darle una sensación de componente "flotante"
        //getRootPane().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
        // Opcional: Un fondo que pueda coincidir o complementar el fondo de tu JFrame principal
        //setBackground(new Color(240, 240, 240)); // Un gris claro, similar al fondo de muchos JFrames

        // Display de la calculadora
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 24));
        add(display, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5)); // 5 filas, 4 columnas, espaciado

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C" // Botón para limpiar
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
            if (newNumber) {
                display.setText(command.equals(".") ? "0." : command);
                newNumber = false;
            } else if (command.equals(".") && display.getText().contains(".")) {
                // Ya contiene un punto, no hacer nada
            } else {
                display.setText(display.getText() + command);
            }
        } else if (command.equals("C")) {
            display.setText("0");
            currentOperation = "";
            firstOperand = 0;
            newNumber = true;
        } else if (command.equals("=")) {
            calculateResult();
            currentOperation = "";
            newNumber = true;
        } else { // Operadores (+, -, *, /)
            if (!currentOperation.isEmpty() && !newNumber) {
                calculateResult(); // Realiza la operación anterior si hay una pendiente
            }
            firstOperand = Double.parseDouble(display.getText());
            currentOperation = command;
            newNumber = true;
        }
    }

    private void calculateResult() {
        if (currentOperation.isEmpty()) {
            return;
        }

        double secondOperand = Double.parseDouble(display.getText());
        double result = 0;

        switch (currentOperation) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand == 0) {
                    display.setText("Error");
                    return;
                }
                result = firstOperand / secondOperand;
                break;
        }
        display.setText(String.valueOf(result));
        firstOperand = result; // El resultado se convierte en el primer operando para operaciones encadenadas
    }

    // O para diálogos personalizados:
  //  JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parentFrame));
//dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
}