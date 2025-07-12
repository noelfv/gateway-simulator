package org.example.gui;

import org.example.gui.panels.ConvertitTramaViewerPanel;
import org.example.gui.panels.GeneratePanelGpt;
import org.example.gui.panels.ParseViewerPanel;
import org.example.gui.panels.GenerarTramaViewerPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MastercardParserGUI5 extends JFrame {

    private final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
    private JMenuBar menuBar;
    private JMenu parseMenu;
    private JMenu conversionMenu;
    private JMenuItem parseMenuItem;
    private JMenuItem generarTramaMenuItem;
    private JMenuItem convertirTramaMenuItem;

    public MastercardParserGUI5() {
        initializeComponents();
        actionsMenu();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();

        // Crear menú "Parse"
        parseMenu = new JMenu("Menu");
        parseMenuItem = new JMenuItem("Parsear mensaje");
        parseMenu.add(parseMenuItem);

        // Crear menú "Conversión" con submenús
        conversionMenu = new JMenu("Conversion");
        generarTramaMenuItem = new JMenuItem("Generar trama");
        convertirTramaMenuItem = new JMenuItem("Convertir trama");
        conversionMenu.add(convertirTramaMenuItem);
        conversionMenu.add(generarTramaMenuItem);
        

        // Agregar menús a la barra
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);


        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);

        //ver si va
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JPanel(), BorderLayout.CENTER);
    }


    private void actionsMenu() {

        // Configurar los listeners para los menús
        parseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                ParseViewerPanel parserPanel = new ParseViewerPanel();
                parserPanel.setInputText(SAMPLE_MESSAGE);
                getContentPane().add(parserPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        generarTramaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                //getContentPane().add(new GenerarTramaViewerPanel(), BorderLayout.CENTER);
                getContentPane().add(new GeneratePanelGpt(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });

        convertirTramaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                ConvertitTramaViewerPanel parserPanel = new ConvertitTramaViewerPanel();
                getContentPane().add(parserPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
    }


    public static void main(String[] args) {
        // Establece el Look and Feel ANTES de crear cualquier componente Swing
        //FlatLightLaf.setup(); // O el tema que prefieras
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                    }

                } catch (Exception e) {
                    // Usar el look and feel por defecto
                }

                MastercardParserGUI5 gui = new MastercardParserGUI5();
                gui.setVisible(true);
            }
        });
    }
}