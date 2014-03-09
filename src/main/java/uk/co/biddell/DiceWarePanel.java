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
package uk.co.biddell;

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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

final class DiceWarePanel extends JPanel implements ChangeListener, ActionListener, ClipboardOwner {

    private static final long serialVersionUID = 1019231115266636856L;
    private final static String[] securityText = new String[] {
            "Four words are breakable with a hundred or so PCs.",
            "Five words are only breakable by an organization with a large budget.",
            "Six words appear unbreakable for the near future, but may be within the range of large organizations by around 2014.",
            "Seven words and longer are unbreakable with any known technology, but may be within the range of large organizations by around 2030.",
            "Eight words and more should be completely secure through 2050."
    };
    private final JRadioButton passwordRadio = new JRadioButton("Create password");
    private final JRadioButton passphraseRadio = new JRadioButton("Create passphrase");
    private final JComboBox<Dictionary> dictionaryCombo = new JComboBox<Dictionary>(new DiceWareComboBoxModel(DiceWord.getDictionaries()));
    private final JLabel spinnerLabel = new JLabel();
    private final SpinnerNumberModel passwordModel = new SpinnerNumberModel(16, 4, 100, 1);
    private final SpinnerNumberModel passphraseModel = new SpinnerNumberModel(7, 4, 100, 1);
    private final JSpinner spinner = new JSpinner();
    private final JTextPane passphrasePane = new JTextPane();
    private final Random rand = SecureRandom.getInstance("SHA1PRNG");
    private final JCheckBox maximiseSecurityCheck = new JCheckBox("Maximise security");
    private final JButton nextButton = new JButton();
    private final JButton copyButton = new JButton("Copy to clipboard");
    private final TitledBorder border = BorderFactory.createTitledBorder("");
    private final JTextArea securityTextArea = new JTextArea(5, 50);
    private final Clipboard clipboard;
    private StringBuilder passPhrase;

    DiceWarePanel(final JRootPane rootPane) throws NoSuchAlgorithmException {
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Dictionary"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        add(dictionaryCombo, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        add(passphraseRadio, gbc);
        gbc.gridx = 1;
        add(passwordRadio, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(spinnerLabel, gbc);
        gbc.gridx++;
        add(spinner, gbc);
        gbc.gridx++;
        add(maximiseSecurityCheck, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.EAST;
        add(nextButton, gbc);
        // gbc.gridy++;
        // gbc.gridx = 3;
        // add(copyButton, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        add(passphrasePane, gbc);
        gbc.weightx = 0.0;
        gbc.gridx = 3;
        add(copyButton, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 4;
        add(securityTextArea, gbc);
        final ButtonGroup group = new ButtonGroup();
        group.add(passwordRadio);
        group.add(passphraseRadio);
        passwordRadio.addActionListener(this);
        passphraseRadio.addActionListener(this);
        dictionaryCombo.setRenderer(new DiceWareComboBoxModel.DictionaryComboBoxRenderer());
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
    public final void stateChanged(final ChangeEvent ce) {
        if (passphraseRadio.isSelected()) {
            createPassphrase();
        } else {
            createPassword();
        }
    }

    @Override
    public final void actionPerformed(final ActionEvent ae) {
        if (ae.getSource() == passphraseRadio) {
            border.setTitle("Passphrase");
            spinner.setModel(passphraseModel);
            nextButton.setText("Next passphrase");
            spinnerLabel.setText("Number of dice words");
            createPassphrase();
        } else if (ae.getSource() == passwordRadio) {
            border.setTitle("Password");
            spinner.setModel(passwordModel);
            nextButton.setText("Next password");
            spinnerLabel.setText("Length of password");
            createPassword();
        } else if (ae.getSource() == nextButton || ae.getSource() == maximiseSecurityCheck) {
            if (passphraseRadio.isSelected()) {
                createPassphrase();
            } else {
                createPassword();
            }
        } else if (ae.getSource() == copyButton) {
            clipboard.setContents(new StringSelection(passPhrase.toString()), this);
        } else if (ae.getSource() == dictionaryCombo) {
            DiceWord.setDictionary((Dictionary) dictionaryCombo.getSelectedItem());
            nextButton.doClick();
        }
    }

    @Override
    public final void lostOwnership(final Clipboard arg0, final Transferable arg1) {
        // nothing to do
    }

    private String getDiceWord() {
        // Throw the dice 5 times and build up our selection criteria,
        //        final StringBuilder currentWord = new StringBuilder(5);
        //        for (int j = 0; j < 5; ++j) {
        //            currentWord.append(throwDie());
        //        }
        //        final Integer wordNumber = Integer.valueOf(currentWord.toString());
        //        // Now get our actual dice word
        //        return DiceWord.getDiceWord(wordNumber);
        return DiceWord.getDictionary().getWord(rand);
    }

    private void createPassword() {
        final Integer passwordLength = (Integer) spinner.getValue();
        if (maximiseSecurityCheck.isSelected()) {
            // The maximise security check is selected so we are just going to get n random
            // password chars.
            passPhrase = new StringBuilder(passwordLength);
            final StringBuilder formattedPassPhrase = new StringBuilder((passwordLength + 1) * 32);
            formattedPassPhrase.append("<html><body><font color=\"blue\">");
            for (int i = 0; i < passwordLength; ++i) {
                char letter;
                do {
                    // Do while we don't get a space for this char as we don't want spaces.
                    letter = DiceWord.getPasswordRandomChar(throwDie(), throwDie(), throwDie());
                } while (letter == ' ');
                passPhrase.append(letter);
                formattedPassPhrase.append(normalise(String.valueOf(letter)));
            }
            formattedPassPhrase.append("</font></body></html>");
            passphrasePane.setText(formattedPassPhrase.toString());
            final int entropy = (int) (6.55F * passwordLength.floatValue());
            securityTextArea.setText(
                    "Your password has an entropy of approximately " + entropy + " bits and is " + passwordLength + " characters in length.\n\n");
        } else {
            // Build up a list of words until we have matched or exceeded the requested password length
            int actualLength = 0;
            final ArrayList<String> words = new ArrayList<String>();
            while (actualLength < passwordLength) {
                final String word = getDiceWord();
                words.add(word);
                actualLength += word.length();
            }
            passPhrase = new StringBuilder(passwordLength * 6);
            // Now pick out a word to capitalise and a word to have a special char randomly inserted.
            final int capitaliseWord;
            final int specialCharWord;
            // If we're truncating the last word, don't select it as the word to insert the special char into.
            if (actualLength > passwordLength) {
                specialCharWord = rand.nextInt(words.size() - 1);
                capitaliseWord = rand.nextInt(words.size() - 1);
            } else {
                specialCharWord = rand.nextInt(words.size());
                capitaliseWord = rand.nextInt(words.size());
            }
            final StringBuilder formattedPassPhrase = new StringBuilder((passwordLength + 1) * 32);
            formattedPassPhrase.append("<html><body>");
            for (int i = 0; i < words.size(); ++i) {
                String word = words.get(i);
                // Append the word to our formatted output in alternate colours so the dice words
                // are easily seen and hopefully remembered.
                formattedPassPhrase.append("<font color=\"").append(i % 2 == 0 ? "green" : "blue").append("\">");
                if (i == capitaliseWord) {
                    // This is our special word where we capitalise the first char.
                    final char[] wordChars = word.toCharArray();
                    wordChars[0] = Character.toUpperCase(wordChars[0]);
                    word = String.valueOf(wordChars);
                }
                if (i == specialCharWord) {
                    // This is our special word. Pick a random char within the word to be replaced with
                    // our random special char.
                    word = addExtraSecurityToWord(word);
                }
                // If the word is too long we chop the end off.
                if ((passPhrase.length() + word.length()) > passwordLength) {
                    word = word.substring(0, passwordLength - passPhrase.length());
                }
                passPhrase.append(word);
                formattedPassPhrase.append(normalise(word)).append("</font>");
            }
            formattedPassPhrase.append("</body></html>");
            passphrasePane.setText(formattedPassPhrase.toString());
            // TODO - we make a guess at the entropy. Must do this properly.
            int entropy = ((int) (12.9F * passwordLength.floatValue()));
            if (maximiseSecurityCheck.isSelected()) {
                entropy += 10;
            }
            securityTextArea.setText(
                    "Your password has an entropy of approximately TODO bits and is " + passPhrase.length() + " characters in length.\n\n");
        }
    }

    private void createPassphrase() {
        final Integer numberOfWords = (Integer) spinner.getValue();
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>(numberOfWords);
        for (int i = 0; i < numberOfWords; ++i) {
            final String word = getDiceWord();
            words.add(word);
            actualLength += word.length();
        }
        if (actualLength < 14) {
            // Less than 14 is not recommended according to diceware so throw again
            createPassphrase();
        } else {
            passPhrase = new StringBuilder(numberOfWords * 6);
            int extraSecurityWord = -1;
            if (maximiseSecurityCheck.isSelected()) {
                // If we maximise security we replace a char in a word whose index is selected here.
                extraSecurityWord = rand.nextInt(numberOfWords);
            }
            final StringBuilder formattedPassPhrase = new StringBuilder((numberOfWords + 1) * 32);
            formattedPassPhrase.append("<html><body>");
            for (int i = 0; i < words.size(); ++i) {
                String word = words.get(i);
                // Append the word to our formatted output in alternate colours so the dice words
                // are easily seen and hopefully remembered.
                formattedPassPhrase.append("<font color=\"").append(i % 2 == 0 ? "green" : "blue").append("\">");
                if (i == extraSecurityWord) {
                    // This is our special word. Pick a random char within the word to be replaced with
                    // our random special char.
                    word = addExtraSecurityToWord(word);
                }
                passPhrase.append(word);
                formattedPassPhrase.append(normalise(word)).append("</font>");
            }
            formattedPassPhrase.append("</body></html>");
            passphrasePane.setText(formattedPassPhrase.toString());
            int entropy = ((int) (12.9F * numberOfWords.floatValue()));
            if (maximiseSecurityCheck.isSelected()) {
                entropy += 10;
            }
            securityTextArea.setText(
                    "Your passphrase has an entropy of approximately " + entropy + " bits and is " + actualLength + " characters in length.\n\n"
                            + securityText[(numberOfWords >= 8 ? 4 : (numberOfWords - 4))]
            );
        }
    }

    private String addExtraSecurityToWord(final String word) {
        final int extraSecurityWordLetter = rand.nextInt(word.length());
        final char securityChar = DiceWord.getPassphraseExtraSecurityChar(throwDie(), throwDie());
        final char[] wordChars = word.toCharArray();
        wordChars[extraSecurityWordLetter] = securityChar;
        return String.valueOf(wordChars);
    }

    private String normalise(final String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }

    private int throwDie() {
        return rand.nextInt(6) + 1;
    }
}