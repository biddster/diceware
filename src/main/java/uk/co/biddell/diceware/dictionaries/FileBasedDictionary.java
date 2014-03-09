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
package uk.co.biddell.diceware.dictionaries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author biddster
 */
public class FileBasedDictionary extends Dictionary {

    private final ArrayList<String> lines = new ArrayList<String>();

    public FileBasedDictionary(final String name, final String fileName) throws IOException {
        super(name);
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName), "UTF-8"));
            String line = null;
            while ((line = lnr.readLine()) != null) {
                lines.add(line);
            }

        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lnr != null) lnr.close();
        }
    }

    public int getWordCount() {
        return lines.size();
    }

    public String getWord(final Random rand) {
        return lines.get(rand.nextInt(lines.size()));
    }
}
