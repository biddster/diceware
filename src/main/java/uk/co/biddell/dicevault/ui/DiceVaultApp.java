package uk.co.biddell.dicevault.ui;

import uk.co.biddell.core.ui.GridBagLayoutEx;
import uk.co.biddell.core.ui.Launcher;
import uk.co.biddell.dicevault.model.Database;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class DiceVaultApp extends JFrame implements ChangeListener {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public DiceVaultApp() throws NoSuchAlgorithmException, IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("DiceVault Password Manager");
        setLocationRelativeTo(null);
        setLayout(new GridBagLayoutEx());
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("menu"));
        final JToolBar toolBar = new JToolBar();
        toolBar.add(new NewDatabaseAction());
        add(toolBar, "gridx:0 gridy:0 weightx:1.0 weighty:0.0 fill:HORIZONTAL");
        add(tabbedPane, "gridx:0 gridy:1 weightx:1.0 weighty:1.0 fill:BOTH");
        tabbedPane.addChangeListener(this);
        setMinimumSize(new Dimension(640, 480));
        pack();
    }

    private void addNewDatabase(final Database database) {
        tabbedPane.addTab(database.getShortTitle(), new DiceVaultTab(database));
    }

    @Override
    public final void stateChanged(final ChangeEvent e) {
        final DiceVaultTab tab = (DiceVaultTab) tabbedPane.getSelectedComponent();
        this.setTitle(tab.getDatabase().getLongTitle());
    }

    private final class NewDatabaseAction extends AbstractAction {

        public NewDatabaseAction() {
            super("New Database");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            addNewDatabase(new Database());
        }
    }

    public static void main(final String[] args) throws Exception {
        Launcher.launch(DiceVaultApp.class);
    }
}
