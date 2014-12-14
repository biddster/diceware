package uk.co.biddell.core.ui;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public abstract class Launcher {

    public static void launch(final Class<? extends JFrame> app) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (final Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        setupOnAppleDeviceIfDetected();
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    app.newInstance().setVisible(true);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void setupOnAppleDeviceIfDetected() {
        try {
            final Class<?> aClass = Launcher.class.getClassLoader().loadClass("com.apple.eawt.Application");
            final Method getApplication = aClass.getMethod("getApplication");
            final Object application = getApplication.invoke(null);
            final Method setDockIconImage = aClass.getMethod("setDockIconImage", Image.class);
            setDockIconImage.invoke(application, new ImageIcon(Launcher.class.getClassLoader().getResource("images/icon.png")).getImage());
        } catch (final Exception e) {
            // Nothing to do here, we're probably not on an apple platform.
        }
    }
}
