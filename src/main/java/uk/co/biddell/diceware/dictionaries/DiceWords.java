package uk.co.biddell.diceware.dictionaries;

import java.util.ArrayList;

/**
 * Created by biddster on 09/03/14.
 */
public class DiceWords {

    private final ArrayList<String> words = new ArrayList<String>();

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
        html.append("<html><body>");
        for (int i = 0; i < words.size(); ++i) {
            // Append the word to our formatted output in alternate colours so the dice words
            // are easily seen and hopefully remembered.
            html.append("<font color=\"").append(i % 2 == 0 ? "green" : "blue").append("\">");
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
        for (final String word : words) {
            sb.append(word);
        }
        return sb.toString();
    }
}
