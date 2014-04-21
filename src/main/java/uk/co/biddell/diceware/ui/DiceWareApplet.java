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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class DiceWareApplet extends JApplet {

    private static final long serialVersionUID = -1281784563863009669L;

    public DiceWareApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, NoSuchAlgorithmException, IOException {
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
