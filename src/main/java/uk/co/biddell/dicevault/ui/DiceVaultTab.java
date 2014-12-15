package uk.co.biddell.dicevault.ui;

import uk.co.biddell.core.ui.GridBagLayoutEx;
import uk.co.biddell.dicevault.model.Database;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class DiceVaultTab extends JPanel {

    private final Database database;

    public DiceVaultTab(Database database) {
        super();
        this.database = database;
        setLayout(new GridBagLayoutEx());
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JTree(), new JLabel("label"));
        add(splitPane, "gridx:0 gridy:0 weightx:1.0 weighty:1.0 fill:BOTH");
    }

    public final Database getDatabase() {
        return database;
    }
}
