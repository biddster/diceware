package uk.co.biddell.diceware.ui;

import uk.co.biddell.diceware.dictionaries.Dictionary;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by biddster on 08/03/14.
 */
class DiceWareComboBoxModel extends DefaultComboBoxModel<Dictionary> {

	private static final long serialVersionUID = 4631471927361644405L;

	public DiceWareComboBoxModel(final Vector<Dictionary> dictionaries) {
        super(dictionaries);
    }

}
