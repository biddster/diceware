package uk.co.biddell.dicevault.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class DiceVaultTab extends JPanel {

    public DiceVaultTab() {
        super();
        setLayout(new BorderLayout());
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JTree(), new JLabel("label"));
        add(splitPane, BorderLayout.CENTER);
    }
}
