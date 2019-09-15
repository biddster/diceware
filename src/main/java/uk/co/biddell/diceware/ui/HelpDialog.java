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

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpDialog extends JDialog implements HyperlinkListener {

    private static final long serialVersionUID = -401393967255989080L;

    public HelpDialog() throws IOException {
        super();
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        editorPane.setEditable(false);
        editorPane.setPage(this.getClass().getResource("/help.html"));
        editorPane.addHyperlinkListener(this);
        //Put the editor pane in a scroll pane.
        final JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(editorScrollPane);
        this.setSize(new Dimension(640, 480));
        this.setPreferredSize(this.getSize());
        this.pack();
    }

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
