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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;

final class DiceWareApp extends JFrame {

    private static final long serialVersionUID = 1377971801674452004L;

    private DiceWareApp() throws NoSuchAlgorithmException, IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Diceware password generator");
        setLocationRelativeTo(null);
        add(new DiceWarePanel(getRootPane()));
        pack();
    }

    private static void setupOnAppleDeviceIfDetected() {
        try {
            final Class<?> aClass = DiceWareApp.class.getClassLoader().loadClass("com.apple.eawt.Application");
            final Method getApplication = aClass.getMethod("getApplication");
            final Object application = getApplication.invoke(null);
            final Method setDockIconImage = aClass.getMethod("setDockIconImage", Image.class);
            setDockIconImage.invoke(application, new ImageIcon(DiceWareApp.class.getClassLoader().getResource("images/icon.png")).getImage());
        } catch (final Exception e) {
            // Nothing to do here, we're probably not on an apple platform.
        }
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
                    setupOnAppleDeviceIfDetected();
                    new DiceWareApp().setVisible(true);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
