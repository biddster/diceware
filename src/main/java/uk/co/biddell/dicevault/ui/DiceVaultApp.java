package uk.co.biddell.dicevault.ui;

import uk.co.biddell.core.ui.GridBagLayoutEx;
import uk.co.biddell.dicevault.model.Database;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class DiceVaultApp extends JFrame implements ChangeListener {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private DiceVaultApp() throws NoSuchAlgorithmException, IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("DiceVault Password Manager");
        setLocationRelativeTo(null);
        setLayout(new GridBagLayoutEx());
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("menu"));
        final JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("New"));
        add(toolBar, "gridx:0 gridy:0 weightx:1.0 weighty:0.0 fill:HORIZONTAL");
        add(tabbedPane, "gridx:0 gridy:1 weightx:1.0 weighty:1.0 fill:BOTH");
        tabbedPane.addChangeListener(this);
        setMinimumSize(new Dimension(640, 480));
        pack();
        addNewDatabase(new Database());
        addNewDatabase(new Database());
    }

    private void addNewDatabase(final Database database) {
        tabbedPane.addTab(database.getShortTitle(), new DiceVaultTab(database));
    }

    public static void main(final String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (final Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new DiceVaultApp().setVisible(true);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        final DiceVaultTab tab = (DiceVaultTab) tabbedPane.getSelectedComponent();
        this.setTitle(tab.getDatabase().getLongTitle());
    }
}
