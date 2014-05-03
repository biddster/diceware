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
