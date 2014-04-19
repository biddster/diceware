/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
