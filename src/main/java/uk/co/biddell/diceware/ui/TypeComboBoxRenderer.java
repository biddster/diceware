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
