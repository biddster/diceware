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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DictionaryTest {
    
    @Test
    public void test() {
        final Dictionary d = new FileBasedDictionary("/diceware8k.txt");
        assertEquals(8192, d.getWordCount());
        assertEquals("@", new InMemoryDictionary().getWord(8191));
        assertEquals("shear", d.getWord(63266));
        assertEquals(267753, new FileBasedDictionary("/sowpods.txt").getWordCount());
    }
    
    @Test
    public void testBuilder() {
        roll("", 3); //2nd parameter > 0
    }
    
    void roll(final String s, final int depth) {
        if (depth == 0) {
            System.out.println(s);
        } else {
            for (int i = 1; i <= 6; i++) {
                roll(s + String.valueOf(i), depth - 1);
            }
        }
    }
}
