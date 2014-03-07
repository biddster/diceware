package uk.co.biddell;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;

public final class DiceWareApplet extends JApplet {

    private static final long serialVersionUID = -1281784563863009669L;

    public DiceWareApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, NoSuchAlgorithmException {
        final DiceWarePanel panel = new DiceWarePanel(getRootPane());
        add(panel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (final Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        SwingUtilities.updateComponentTreeUI(panel);
    }
}
