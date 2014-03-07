package uk.co.biddell;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;

public final class DiceWare extends JFrame {

    private static final long serialVersionUID = 1377971801674452004L;

    DiceWare() throws NoSuchAlgorithmException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Diceware password generator");
        setLocationRelativeTo(null);
        add(new DiceWarePanel(getRootPane()));
        pack();
    }

    public static final void main(final String[] args) throws Exception {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new DiceWare().setVisible(true);
                } catch (final NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
