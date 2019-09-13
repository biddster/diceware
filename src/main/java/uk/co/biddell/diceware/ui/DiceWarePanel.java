/**
 * Copyright (C) 2014 Luke Biddell
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.biddell.diceware.ui;

import uk.co.biddell.diceware.dictionaries.DiceWare;
import uk.co.biddell.diceware.dictionaries.DiceWare.LengthType;
import uk.co.biddell.diceware.dictionaries.DiceWords;
import uk.co.biddell.diceware.dictionaries.Dictionary;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

final class DiceWarePanel extends JPanel implements ChangeListener, ActionListener, ClipboardOwner, DocumentListener {

    private static final long serialVersionUID = 1019231115266636856L;
    private final static String[] securityText = new String[]{
            "Four words are breakable with a hundred or so PCs.",
            "Five words are only breakable by an organization with a large budget.",
            "Six words appear unbreakable for the near future, but may be within the range of large organizations by around 2014.",
            "Seven words and longer are unbreakable with any known technology, but may be within the range of large organizations by around 2030.",
            "Eight words and more should be completely secure through 2050."
    };
    private final Preferences preferences = Preferences.userRoot().node("uk/co/biddell/diceware");
    private final DiceWare diceWare = new DiceWare();
    private final JComboBox<Dictionary> dictionaryCombo = new JComboBox<Dictionary>(diceWare.getDictionaries());
    private final JComboBox<DiceWare.Type> typeCombo = new JComboBox<DiceWare.Type>(DiceWare.Type.values());
    private final JLabel spinnerLabel = new JLabel();
    private final SpinnerNumberModel passwordModel = new SpinnerNumberModel(16, 4, 100, 1);
    private final SpinnerNumberModel passphraseModel = new SpinnerNumberModel(6, 4, 25, 1);
    private final JSpinner spinner = new JSpinner();
    private final JLabel separatorLabel = new JLabel("Separator");
    private final JTextField separatorField = new JTextField("");
    private final JTextPane passphrasePane = new JTextPane();
    private final JButton helpButton = createImageButton("Help", "/images/help.png");
    private final JButton nextButton = createImageButton("Next", "/images/arrow_right.png");
    private final JButton copyButton = createImageButton("Copy to clipboard", "/images/page_white_copy.png");
    private final JLabel typeLabel = createTitleLabel("Result");
    private final JTextArea securityTextArea = new JTextArea(5, 50);
    private final HelpDialog helpDialog = new HelpDialog();
    private final Clipboard clipboard;
    private DiceWords diceWords;

    DiceWarePanel(final JRootPane rootPane) throws IOException, NoSuchAlgorithmException {
        loadPreferences();
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(480, 400));
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        add(createTitleLabel("Dictionary"), gridBagConstraints);
        gridBagConstraints.gridy++;
        add(dictionaryCombo, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(createTitleLabel("Type"), gridBagConstraints);
        gridBagConstraints.gridy++;
        add(typeCombo, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(createTitleLabel("Options"), gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridy++;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.0;
        add(spinnerLabel, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(spinner, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(separatorLabel, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(separatorField, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        add(typeLabel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridwidth = 4;
        final JScrollPane comp = new JScrollPane(passphrasePane);
        comp.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(comp, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridheight = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        add(helpButton, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        add(copyButton, gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(nextButton, gridBagConstraints);
        dictionaryCombo.setRenderer(new DictionaryComboBoxRenderer());
        dictionaryCombo.addActionListener(this);
        typeCombo.setRenderer(new TypeComboBoxRenderer());
        typeCombo.addActionListener(this);
        spinner.getEditor().setEnabled(false);
        spinner.addChangeListener(this);
        separatorField.getDocument().addDocumentListener(this);
        helpButton.addActionListener(this);
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

    private JButton createImageButton(final String text, final String imagePath) {
        return new JButton(text, new ImageIcon(this.getClass().getResource(imagePath)));
    }

    @Override
    public final void stateChanged(final ChangeEvent changeEvent) {
        create();
    }

    @Override
    public final void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == typeCombo || actionEvent.getSource() == nextButton) {
            create();
        } else if (actionEvent.getSource() == copyButton) {
            clipboard.setContents(new StringSelection(diceWords.toString()), this);
        } else if (actionEvent.getSource() == dictionaryCombo) {
            diceWare.setDictionary((Dictionary) dictionaryCombo.getSelectedItem());
            create();
        } else if (actionEvent.getSource() == helpButton) {
            helpDialog.setVisible(!helpDialog.isVisible());
        }
    }

    @Override
    public final void lostOwnership(final Clipboard clipboard, final Transferable transferable) {
        // nothing to do
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update();
    }

    private void create() {
        final DiceWare.Type type = (DiceWare.Type) typeCombo.getSelectedItem();
        String separator = "";
        if (type.getLengthType() == LengthType.WORD_LENGTH) {
            spinner.setModel(passphraseModel);
            spinnerLabel.setText("Number of words");
            separatorLabel.setVisible(true);
            separatorField.setVisible(true);
            separator = separatorField.getText();
        } else {
            separatorLabel.setVisible(false);
            separatorField.setVisible(false);
            spinner.setModel(passwordModel);
            spinnerLabel.setText("Number of characters");
        }
        diceWords = diceWare.getDiceWords(type, (Integer) spinner.getValue());
        passphrasePane.setText(diceWords.toHTMLString(separator));
        savePreferences();
    }

    private void update() {
        passphrasePane.setText(diceWords.toHTMLString(separatorField.getText()));
        savePreferences();
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

    private void loadPreferences() {
        System.out.println(preferences.get("type", "0"));
        dictionaryCombo.setSelectedIndex(Integer.parseInt(preferences.get("dictionary", "0")));
        typeCombo.setSelectedIndex(Integer.parseInt(preferences.get("type", "0")));
        passwordModel.setValue(Integer.parseInt(preferences.get("password_length", "16")));
        passphraseModel.setValue(Integer.parseInt(preferences.get("passphrase_length", "6")));
        separatorField.setText(preferences.get("separator", ""));
    }

    private void savePreferences() {
        System.out.println(Integer.toString(typeCombo.getSelectedIndex()));
        preferences.put("dictionary", Integer.toString(dictionaryCombo.getSelectedIndex()));
        preferences.put("type", Integer.toString(typeCombo.getSelectedIndex()));
        preferences.put("password_length", Integer.toString((Integer) passwordModel.getValue()));
        preferences.put("passphrase_length", Integer.toString((Integer) passphraseModel.getValue()));
        preferences.put("separator", separatorField.getText());
    }

}
