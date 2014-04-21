/**
 * Copyright (C) 2014 Luke Biddell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.biddell.diceware.ui;

import java.awt.EventQueue;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.UIManager;

final class DiceWareApp extends JFrame {

    private static final long serialVersionUID = 1377971801674452004L;

    private DiceWareApp() throws NoSuchAlgorithmException, IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Diceware password generator");
        setLocationRelativeTo(null);
        add(new DiceWarePanel(getRootPane()));
        pack();
    }

    public static void main(final String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new DiceWareApp().setVisible(true);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
