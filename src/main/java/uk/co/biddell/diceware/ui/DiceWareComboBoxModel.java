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
