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
package uk.co.biddell.core.ui;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GridBagLayoutEx extends GridBagLayout {

    private static final Logger log = Logger.getLogger(GridBagLayoutEx.class.getName());
    private static final long serialVersionUID = 5303976710331305433L;
    private static final Pattern[] patterns = new Pattern[] {
            Pattern.compile("(fill):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(anchor):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(gridx):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(gridy):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(gridwidth):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(gridheight):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(weightx):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(weighty):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(ipadx):([^\\s]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(ipady):([^\\s]+)", Pattern.CASE_INSENSITIVE)
    };
    private static final Pattern insetsPattern = Pattern.compile("insets:(\\d+).(\\d+).(\\d+).(\\d+)",
            Pattern.CASE_INSENSITIVE);

    @Override
    public final void addLayoutComponent(final Component comp, final Object constraints) {
        try {
            final GridBagConstraints gbc = new GridBagConstraints();
            for (final Pattern pattern : patterns) {
                final Matcher m = pattern.matcher((String) constraints);
                if (m.find()) {
                    // First have a look and see if the value is a static constant ie there's a field representing it.
                    // This would be something like gbc.gridx = GridBagConstraints.REMAINDER
                    final Field f = gbc.getClass().getField(m.group(1).toLowerCase());
                    try {
                        final Field value = gbc.getClass().getField(m.group(2).toUpperCase());
                        if (f.getType() == int.class) {
                            f.setInt(gbc, value.getInt(gbc));
                        } else if (f.getType() == double.class) {
                            f.setDouble(gbc, value.getDouble(gbc));
                        }
                    } catch (final NoSuchFieldException nsfe) {
                        // There's no field for this value so we will attempt to set it ourselves.
                        if (f.getType() == int.class) {
                            f.setInt(gbc, Integer.parseInt(m.group(2)));
                        } else if (f.getType() == double.class) {
                            f.setDouble(gbc, Double.parseDouble(m.group(2)));
                        }
                    }
                }
            }
            final Matcher m = insetsPattern.matcher((String) constraints);
            if (m.find()) {
                gbc.insets = new Insets(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
            }
            super.addLayoutComponent(comp, gbc);
        } catch (final Exception e) {
            log.severe("Exception adding component [" + comp + "] with constraints [" + constraints + "]");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}