package uk.co.biddell.diceware.ui;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import uk.co.biddell.diceware.dictionaries.Dictionary;

/**
 * Created by biddster on 08/03/14.
 */
class DiceWareComboBoxModel extends DefaultComboBoxModel<Dictionary> {

    private static final long serialVersionUID = 4631471927361644405L;

    public DiceWareComboBoxModel(final Vector<Dictionary> dictionaries) {
        super(dictionaries);
    }
}
