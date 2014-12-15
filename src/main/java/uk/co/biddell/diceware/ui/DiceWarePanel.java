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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

import uk.co.biddell.core.ui.GridBagLayoutEx;
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
    private final JComboBox<Dictionary> dictionaryCombo = new JComboBox<Dictionary>(diceWare.getDictionaries());
    private final JComboBox<DiceWare.Type> typeCombo = new JComboBox<DiceWare.Type>(DiceWare.Type.values());
    private final JLabel spinnerLabel = new JLabel();
    private final SpinnerNumberModel passwordModel = new SpinnerNumberModel(16, 4, 100, 1);
    private final SpinnerNumberModel passphraseModel = new SpinnerNumberModel(6, 4, 25, 1);
    private final JSpinner spinner = new JSpinner();
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
        setLayout(new GridBagLayoutEx());
        setPreferredSize(new Dimension(480, 400));
        add(createTitleLabel("Dictionary"), "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:0 gridwidth:3");
        add(dictionaryCombo, "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:1 gridwidth:3");
        add(createTitleLabel("Type"), "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:2 gridwidth:3");
        add(typeCombo, "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:3 gridwidth:3");
        add(createTitleLabel("Options"), "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:4 gridwidth:3");
        add(spinnerLabel, "insets:8,8,8,8 fill:HORIZONTAL anchor:WEST gridx:0 gridy:5 gridwidth:1 weightx:1.0");
        add(spinner, "insets:8,8,8,8 fill:NONE anchor:WEST gridx:1 gridy:5 gridwidth:1");
        add(typeLabel, "insets:8,8,8,8 fill:NONE anchor:WEST gridx:0 gridy:6 gridwidth:1");
        final JScrollPane comp = new JScrollPane(passphrasePane);
        comp.setViewportBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(comp, "insets:8,8,8,8 fill:BOTH anchor:WEST gridx:0 gridy:7 gridwidth:3 weightx:1.0 weighty:1.0");
        add(helpButton, "insets:8,8,8,8 fill:NONE anchor:WEST gridx:0 gridy:8 gridwidth:1");
        add(copyButton, "insets:8,8,8,8 fill:NONE anchor:WEST gridx:1 gridy:8 gridwidth:1");
        add(nextButton, "insets:8,8,8,8 fill:NONE anchor:EAST gridx:2 gridy:8 gridwidth:1");
        dictionaryCombo.setRenderer(new DictionaryComboBoxRenderer());
        dictionaryCombo.addActionListener(this);
        typeCombo.setRenderer(new TypeComboBoxRenderer());
        typeCombo.addActionListener(this);
        spinner.getEditor().setEnabled(false);
        spinner.addChangeListener(this);
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

    private void create() {
        final DiceWare.Type type = (DiceWare.Type) typeCombo.getSelectedItem();
        if (type.getLengthType() == LengthType.WORD_LENGTH) {
            spinner.setModel(passphraseModel);
            spinnerLabel.setText("Number of words");
        } else {
            spinner.setModel(passwordModel);
            spinnerLabel.setText("Number of characters");
        }
        diceWords = diceWare.getDiceWords(type, (Integer) spinner.getValue());
        passphrasePane.setText(diceWords.toHTMLString());
    }

    //private final void createPassphrase() {
    //diceWords = diceWare.createPassphrase((Integer) spinner.getValue(), maximiseSecurityCheck.isSelected());
    //passphrasePane.setText(diceWords.toHTMLString());
    //        int entropy = ((int) (12.9F * numberOfWords.floatValue()));
    //        if (maximiseSecurityCheck.isSelected()) {
    //            entropy += 10;
    //        }
    //        securityTextArea.setText(
    //                "Your passphrase has an entropy of approximately "    + entropy + " bits and is " + actualLength + " characters in length.\n\n"
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
