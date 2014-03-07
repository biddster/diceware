/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.biddell;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author lbiddell2
 */
public class Dictionary {

    private final File file;
    private final ArrayList lines = new ArrayList();

    public Dictionary(final String fileName) {
        try {
            file = new File(this.getClass().getResource(fileName).toURI());
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            String line = null;
            while ((line = lnr.readLine()) != null) {
                lines.add(line);
            }
            lnr.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getWord(int n) {
        final int index = n & 0X1fff;
        return (String) lines.get(index - 1);
    }

    public int getWordCount() {
        return lines.size();
    }

    public Iterator getIterator() {
        return lines.iterator();
    }

    public int getNumberOfThrowsRequired() {
        //        int count = lines.size();
        //        int times = 0;
        //        while (count % 6 == 0) {
        //            count /= 6;
        //            times++;
        //        }
        //        return times;
        int count = 0;
        while (Math.pow(6, ++count) < lines.size()) {
        }
        return count;
    }

    public String getWord(final Random rand) {
        return (String) lines.get(rand.nextInt(lines.size()));
    }
}
