package uk.co.biddell.diceware.ui;

import uk.co.biddell.diceware.dictionaries.Dictionary;

import javax.swing.*;
import java.awt.*;

/**
 * Created by biddster on 09/03/14.
 */
public class DictionaryComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final Dictionary d = ((Dictionary) value);
        final String label = d.getName() + " (" + d.getWordCount() + " words)";
        return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
    }
}
