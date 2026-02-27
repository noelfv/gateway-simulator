package com.bbva.gui.components;


import com.bbva.gui.utils.ComponentsUtil;
import lombok.Getter;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import static com.bbva.gui.utils.ComponentsUtil.BBVA_NAVY;

@Getter
public class JtreeOutputPanel extends JPanel {

    // Getters para configurar acciones desde fuera
    private final JTree resultTree;
    private final DefaultTreeModel treeModel;

    public JtreeOutputPanel(String title) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
        resultTree = ComponentsUtil.createJTree(treeModel);

        setLayout(new BorderLayout());
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), title);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        //setBorder(border);
        add(new JScrollPane(resultTree), BorderLayout.CENTER);

    }

}
