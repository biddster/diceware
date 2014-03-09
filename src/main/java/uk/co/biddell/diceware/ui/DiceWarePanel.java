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

import uk.co.biddell.diceware.dictionaries.DiceWare;
import uk.co.biddell.diceware.dictionaries.DiceWords;
import uk.co.biddell.diceware.dictionaries.Dictionary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

final class DiceWarePanel extends JPanel implements ChangeListener, ActionListener, ClipboardOwner {

    private static final long serialVersionUID = 1019231115266636856L;
    private final static String[] securityText = new String[] {
            "Four words are breakable with a hundred or so PCs.",
            "Five words are only breakable by an organization with a large budget.",
            "Six words appear unbreakable for the near future, but may be within the range of large organizations by around 2014.",
            "Seven words and longer are unbreakable with any known technology, but may be within the range of large organizations by around 2030.",
            "Eight words and more should be completely secure through 2050."
    };
    private final DiceWare diceWare = new DiceWare();
    private final JRadioButton passwordRadio = new JRadioButton("Create password");
    private final JRadioButton passphraseRadio = new JRadioButton("Create passphrase");
    private final JComboBox<Dictionary> dictionaryCombo = new JComboBox<Dictionary>(new DiceWareComboBoxModel(diceWare.getDictionaries()));
    private final JLabel spinnerLabel = new JLabel();
    private final SpinnerNumberModel passwordModel = new SpinnerNumberModel(16, 4, 100, 1);
    private final SpinnerNumberModel passphraseModel = new SpinnerNumberModel(5, 4, 100, 1);
    private final JSpinner spinner = new JSpinner();
    private final JTextPane passphrasePane = new JTextPane();
    private final JCheckBox maximiseSecurityCheck = new JCheckBox("Maximise security");
    private final JButton nextButton = new JButton();
    private final JButton copyButton = new JButton("Copy to clipboard");
    private final TitledBorder border = BorderFactory.createTitledBorder("");
    private final JTextArea securityTextArea = new JTextArea(5, 50);
    private final Clipboard clipboard;
    private DiceWords diceWords;

    DiceWarePanel(final JRootPane rootPane) throws NoSuchAlgorithmException, IOException {
        setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(new JLabel("Dictionary"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 3;
        add(dictionaryCombo, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.gridwidth = 1;
        add(passphraseRadio, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        add(passwordRadio, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 1;
        add(spinnerLabel, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(spinner, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(maximiseSecurityCheck, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(nextButton, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridwidth = 3;
        add(passphrasePane, gridBagConstraints);
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.gridx = 3;
        add(copyButton, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridwidth = 4;
        add(securityTextArea, gridBagConstraints);
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(passwordRadio);
        buttonGroup.add(passphraseRadio);
        passwordRadio.addActionListener(this);
        passphraseRadio.addActionListener(this);
        dictionaryCombo.setRenderer(new DictionaryComboBoxRenderer());
        dictionaryCombo.addActionListener(this);
        spinner.getEditor().setEnabled(false);
        spinner.addChangeListener(this);
        maximiseSecurityCheck.addActionListener(this);
        nextButton.addActionListener(this);
        copyButton.addActionListener(this);
        passphrasePane.setBorder(border);
        passphrasePane.setContentType("text/html");
        passphrasePane.setEditable(false);
        securityTextArea.setEditable(false);
        securityTextArea.setWrapStyleWord(true);
        securityTextArea.setFont(securityTextArea.getFont().deriveFont(Font.ITALIC));
        securityTextArea.setLineWrap(true);
        securityTextArea.setBorder(BorderFactory.createTitledBorder("Security information"));
        rootPane.getRootPane().setDefaultButton(nextButton);
        clipboard = getToolkit().getSystemClipboard();
        passphraseRadio.doClick();
        // Hide this until I can get it accurate.
        securityTextArea.setVisible(false);
    }

    @Override
    public final void stateChanged(final ChangeEvent changeEvent) {
        if (passphraseRadio.isSelected()) {
            createPassphrase();
        } else {
            createPassword();
        }
    }

    @Override
    public final void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == passphraseRadio) {
            border.setTitle("Passphrase");
            spinner.setModel(passphraseModel);
            nextButton.setText("Next passphrase");
            spinnerLabel.setText("Number of dice words");
            createPassphrase();
        } else if (actionEvent.getSource() == passwordRadio) {
            border.setTitle("Password");
            spinner.setModel(passwordModel);
            nextButton.setText("Next password");
            spinnerLabel.setText("Length of password");
            createPassword();
        } else if (actionEvent.getSource() == nextButton || actionEvent.getSource() == maximiseSecurityCheck) {
            if (passphraseRadio.isSelected()) {
                createPassphrase();
            } else {
                createPassword();
            }
        } else if (actionEvent.getSource() == copyButton) {
            clipboard.setContents(new StringSelection(diceWords.toString()), this);
        } else if (actionEvent.getSource() == dictionaryCombo) {
            diceWare.setDictionary((Dictionary) dictionaryCombo.getSelectedItem());
            nextButton.doClick();
        }
    }

    private final void createPassphrase() {
        diceWords = diceWare.createPassphrase((Integer) spinner.getValue(), maximiseSecurityCheck.isSelected());
        passphrasePane.setText(diceWords.toHTMLString());
        //        int entropy = ((int) (12.9F * numberOfWords.floatValue()));
        //        if (maximiseSecurityCheck.isSelected()) {
        //            entropy += 10;
        //        }
        //        securityTextArea.setText(
        //                "Your passphrase has an entropy of approximately " + entropy + " bits and is " + actualLength + " characters in length.\n\n"
        //                        + securityText[(numberOfWords >= 8 ? 4 : (numberOfWords - 4))]
        //        );
    }

    private final void createPassword() {
        diceWords = diceWare.createPassword((Integer) spinner.getValue(), maximiseSecurityCheck.isSelected());
        passphrasePane.setText(diceWords.toHTMLString());
        //        final int entropy = (int) (6.55F * passwordLength.floatValue());
        //        securityTextArea.setText(
        //                "Your password has an entropy of approximately " + entropy + " bits and is " + passwordLength + " characters in length.\n\n");
        //
        //        passphrasePane.setText(formattedPassPhrase.toString());
        //        // TODO - we make a guess at the entropy. Must do this properly.
        //        int entropy = ((int) (12.9F * passwordLength.floatValue()));
        //        if (maximiseSecurityCheck.isSelected()) {
        //            entropy += 10;
        //        }
        //        securityTextArea.setText(
        //                "Your password has an entropy of approximately TODO bits and is " + diceWords.length() + " characters in length.\n\n");
    }

    @Override
    public final void lostOwnership(final Clipboard clipboard, final Transferable transferable) {
        // nothing to do
    }
}