package uk.co.biddell.diceware.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import uk.co.biddell.diceware.dictionaries.DiceWare;

/**
 * Created by biddster on 09/03/14.
 */
class TypeComboBoxRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(final JList< ? > list, final Object value, final int index,
            final boolean isSelected, final boolean cellHasFocus) {
        final DiceWare.Type type = ((DiceWare.Type) value);
        return super.getListCellRendererComponent(list, type.getDescription(), index, isSelected, cellHasFocus);
    }
}
