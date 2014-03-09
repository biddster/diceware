package uk.co.biddell;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by biddster on 08/03/14.
 */
class DiceWareComboBoxModel extends DefaultComboBoxModel<Dictionary> {

    public DiceWareComboBoxModel(final Vector<Dictionary> dictionaries) {
        super(dictionaries);
    }

    public static class DictionaryComboBoxRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final Dictionary d = ((Dictionary) value);
            final String label = d.getName() + " (" + d.getWordCount() + " words)";
            return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
        }
    }

}
