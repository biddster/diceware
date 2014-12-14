package uk.co.biddell.dicevault.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class DiceVaultApp extends JFrame {

    private DiceVaultApp() throws NoSuchAlgorithmException, IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("DiceVault Password Manager");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new JMenu("menu"));
        final JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton("button"));
        add(toolBar, BorderLayout.NORTH);
        final JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("tab", new DiceVaultTab());
        pack();
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
}
