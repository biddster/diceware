/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.biddell;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author biddster
 */
public class FileBasedDictionary extends Dictionary {

    private final ArrayList<String> lines = new ArrayList<String>();

    public FileBasedDictionary(final String name, final String fileName) {
        super(name);
        try {
            final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
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
        return lines.get(index - 1);
    }

    public int getWordCount() {
        return lines.size();
    }

    public Iterator getIterator() {
        return lines.iterator();
    }

    public int getNumberOfThrowsRequired() {
        int count = 0;
        while (Math.pow(6, ++count) < lines.size()) {
        }
        return count;
    }

    public String getWord(final Random rand) {
        return lines.get(rand.nextInt(lines.size()));
    }
}
