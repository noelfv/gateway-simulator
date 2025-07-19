package com.bbva.gui.temp;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import javax.swing.tree.TreePath;

public class SimpleJTreeExample extends JFrame {

    public SimpleJTreeExample() {
        setTitle("Ejemplo de JTree con Subnodos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centrar la ventana

        // 1. Crear el nodo raíz del árbol
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Sistema de Archivos");

        // 2. Crear los nodos principales (primer nivel)
        DefaultMutableTreeNode documentos = new DefaultMutableTreeNode("Documentos");
        DefaultMutableTreeNode imagenes = new DefaultMutableTreeNode("Imágenes");
        DefaultMutableTreeNode musica = new DefaultMutableTreeNode("Música");
        DefaultMutableTreeNode videos = new DefaultMutableTreeNode("Videos");

        // 3. Añadir los nodos principales al nodo raíz
        top.add(documentos);
        top.add(imagenes);
        top.add(musica);
        top.add(videos);

        // 4. Crear subnodos para "Documentos"
        DefaultMutableTreeNode informes = new DefaultMutableTreeNode("Informes");
        DefaultMutableTreeNode cartas = new DefaultMutableTreeNode("Cartas");
        DefaultMutableTreeNode facturas = new DefaultMutableTreeNode("Facturas");
        documentos.add(informes);
        documentos.add(cartas);
        documentos.add(facturas);

        // 5. Crear subnodos para "Imágenes"
        DefaultMutableTreeNode fotosVacaciones = new DefaultMutableTreeNode("Vacaciones 2024");
        DefaultMutableTreeNode fotosFamilia = new DefaultMutableTreeNode("Familia");
        DefaultMutableTreeNode capturasPantalla = new DefaultMutableTreeNode("Capturas");
        imagenes.add(fotosVacaciones);
        imagenes.add(fotosFamilia);
        imagenes.add(capturasPantalla);

        // 6. Crear sub-subnodos para "Vacaciones 2024"
        fotosVacaciones.add(new DefaultMutableTreeNode("Playa.jpg"));
        fotosVacaciones.add(new DefaultMutableTreeNode("Montaña.png"));
        fotosVacaciones.add(new DefaultMutableTreeNode("Ciudad.gif"));

        // 7. Crear subnodos para "Música"
        musica.add(new DefaultMutableTreeNode("Rock"));
        musica.add(new DefaultMutableTreeNode("Pop"));
        musica.add(new DefaultMutableTreeNode("Clásica"));

        // 8. Crear el JTree con el nodo raíz
        JTree tree = new JTree(top);

        // 9. Opcional: Expandir algunos nodos por defecto para que no aparezca totalmente colapsado
        // tree.expandRow(0); // Expande el nodo raíz
        // tree.expandRow(1); // Expande "Documentos"
        // tree.expandRow(5); // Expande "Vacaciones 2024" (depende del orden y si se ha expandido antes)

        // En lugar de expandir por fila, puedes expandir por ruta (más robusto)
        /*tree.expandPath(new JTree.TreePath(documentos.getPath()));
        tree.expandPath(new JTree.TreePath(fotosVacaciones.getPath()));*/

        tree.expandPath(new TreePath(documentos.getPath()));
        tree.expandPath(new TreePath(fotosVacaciones.getPath()));

        // 10. Añadir el JTree a un JScrollPane para que sea desplazable si el contenido es grande
        JScrollPane scrollPane = new JScrollPane(tree);

        // 11. Añadir el JScrollPane al content pane del JFrame
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Establecer el Look and Feel (opcional, pero mejora la apariencia)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new SimpleJTreeExample().setVisible(true);
        });
    }
}