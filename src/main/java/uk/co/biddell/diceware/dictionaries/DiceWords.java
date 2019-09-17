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
package uk.co.biddell.diceware.dictionaries;

import java.util.ArrayList;

/**
 * Created by biddster on 09/03/14.
 */
public class DiceWords {

    private final ArrayList<String> words = new ArrayList<String>();
    private String separator;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void append(final String word) {
        words.add(word);
    }

    public void append(final char character) {
        words.add(String.valueOf(character));
    }

    public int getLength() {
        int length = 0;
        for (final String word : words) {
            length += word.length();
        }
        return length;
    }

    public String toHTMLString() {
        final StringBuilder html = new StringBuilder((words.size() + 1) * 32);
        html.append("<html><body style=\"background-color: #ffffff; font-weight: bold;font-size: 11px;padding: 8px;font-family: courier;\">");
        for (int i = 0; i < words.size(); ++i) {
            // Append the word to our formatted output in alternate colours so the dice words
            // are easily seen and hopefully remembered.
            if (i > 0) {
                html.append(separator);
            }
            html.append("<font color=\"").append(i % 2 == 0 ? "#0b61a4" : "#423e3e").append("\">");
            html.append(normalise(words.get(i))).append("</font>");
        }
        html.append("</body></html>");
        return html.toString();
    }

    private String normalise(final String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(words.get(i));
        }

        return sb.toString();
    }
}
