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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.biddell.diceware.dictionaries.DiceWare;
import uk.co.biddell.diceware.dictionaries.DiceWare.LengthType;
import uk.co.biddell.diceware.dictionaries.DiceWords;
import uk.co.biddell.diceware.dictionaries.Dictionary;

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
    private final JComboBox<Dictionary> dictionaryCombo = new JComboBox<Dictionary>(new DiceWareComboBoxModel(
            diceWare.getDictionaries()));
    private final JComboBox<DiceWare.Type> typeCombo = new JComboBox<DiceWare.Type>(DiceWare.Type.values());
    private final JLabel spinnerLabel = new JLabel();
    private final SpinnerNumberModel passwordModel = new SpinnerNumberModel(16, 4, 100, 1);
    private final SpinnerNumberModel passphraseModel = new SpinnerNumberModel(6, 4, 100, 1);
    private final JSpinner spinner = new JSpinner();
    private final JTextPane passphrasePane = new JTextPane();
    private final JButton nextButton = new JButton();
    private final JButton copyButton = new JButton("Copy to clipboard");
    private final JLabel typeLabel = createTitleLabel("");
    private final JTextArea securityTextArea = new JTextArea(5, 50);
    private final Clipboard clipboard;
    private DiceWords diceWords;

    DiceWarePanel(final JRootPane rootPane) throws IOException, NoSuchAlgorithmException {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(480, 480));
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        add(createTitleLabel("Dictionary"), gridBagConstraints);
        gridBagConstraints.gridy++;
        add(dictionaryCombo, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(createTitleLabel("Type"), gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridwidth = 3;
        add(typeCombo, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(createTitleLabel("Options"), gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 1.0;
        add(spinnerLabel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.gridx++;
        add(spinner, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        add(typeLabel, gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridwidth = 3;
        final JScrollPane comp = new JScrollPane(passphrasePane);
        comp.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(comp, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridheight = 0;
        //add(securityTextArea, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        add(copyButton, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(nextButton, gridBagConstraints);
        dictionaryCombo.setRenderer(new DictionaryComboBoxRenderer());
        dictionaryCombo.addActionListener(this);
        typeCombo.setRenderer(new TypeComboBoxRenderer());
        typeCombo.addActionListener(this);
        spinner.getEditor().setEnabled(false);
        spinner.addChangeListener(this);
        nextButton.addActionListener(this);
        copyButton.addActionListener(this);
        passphrasePane.setContentType("text/html");
        passphrasePane.setEditable(false);
        passphrasePane.setBorder(null);
        securityTextArea.setEditable(false);
        securityTextArea.setWrapStyleWord(true);
        securityTextArea.setFont(securityTextArea.getFont().deriveFont(Font.ITALIC));
        securityTextArea.setLineWrap(true);
        securityTextArea.setBorder(BorderFactory.createTitledBorder("Security information"));
        rootPane.getRootPane().setDefaultButton(nextButton);
        clipboard = getToolkit().getSystemClipboard();
        // Hide this until I can get it accurate.
        securityTextArea.setVisible(false);
        dictionaryCombo.setSelectedIndex(0);
    }

    private JLabel createTitleLabel(final String text) {
        final JLabel label = new JLabel(text);
        final Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), Font.BOLD, labelFont.getSize() + 2));
        return label;
    }

    @Override
    public final void stateChanged(final ChangeEvent changeEvent) {
        create();
    }

    @Override
    public final void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == dictionaryCombo || actionEvent.getSource() == typeCombo) {
            create();
        } else if (actionEvent.getSource() == nextButton) {
            create();
        } else if (actionEvent.getSource() == copyButton) {
            clipboard.setContents(new StringSelection(diceWords.toString()), this);
        } else if (actionEvent.getSource() == dictionaryCombo) {
            diceWare.setDictionary((Dictionary) dictionaryCombo.getSelectedItem());
            nextButton.doClick();
        }
    }

    private void create() {
        final DiceWare.Type type = (DiceWare.Type) typeCombo.getSelectedItem();
        if (type.getLengthType() == LengthType.WORD_LENGTH) {
            typeLabel.setText("Passphrase");
            spinner.setModel(passphraseModel);
            nextButton.setText("Next passphrase");
            spinnerLabel.setText("Number of words");
        } else {
            typeLabel.setText("Password");
            spinner.setModel(passwordModel);
            nextButton.setText("Next password");
            spinnerLabel.setText("Number of characters");
        }
        passphrasePane.setText(diceWare.getDiceWords(type, (Integer) spinner.getValue()).toHTMLString());
    }

    //private final void createPassphrase() {
    //diceWords = diceWare.createPassphrase((Integer) spinner.getValue(), maximiseSecurityCheck.isSelected());
    //passphrasePane.setText(diceWords.toHTMLString());
    //        int entropy = ((int) (12.9F * numberOfWords.floatValue()));
    //        if (maximiseSecurityCheck.isSelected()) {
    //            entropy += 10;
    //        }
    //        securityTextArea.setText(
    //                "Your passphrase has an entropy of approximately " + entropy + " bits and is " + actualLength + " characters in length.\n\n"
    //                        + securityText[(numberOfWords >= 8 ? 4 : (numberOfWords - 4))]
    //        );
    //}
    //private final void createPassword() {
    //diceWords = diceWare.createPassword((Integer) spinner.getValue(), maximiseSecurityCheck.isSelected());
    //passphrasePane.setText(diceWords.toHTMLString());
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
    //}
    @Override
    public final void lostOwnership(final Clipboard clipboard, final Transferable transferable) {
        // nothing to do
    }
}