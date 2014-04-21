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
package uk.co.biddell.diceware.dictionaries;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class DictionaryTest {

    @Test
    public void test() throws IOException {
        final Dictionary d = new FileBasedDictionary("", "/diceware.txt");
        System.out.println(d.getEntropy(1));
        System.out.println(BigInteger.valueOf(7776).bitLength());
        System.out.println(BigDecimal.valueOf(7776.0).toBigInteger().bitLength());
        System.out.println(1 + Math.floor(Math.log(7776) / Math.log(2)));
        System.out.println(BigDecimal.valueOf(Math.log(7776) / Math.log(2)).toBigInteger().bitLength());
        assertEquals(7776, d.getWordCount());
    }
    //    @Test
    //    public void testBuilder() {
    //        roll("", 3); //2nd parameter > 0
    //    }
    //
    //    void roll(final String s, final int depth) {
    //        if (depth == 0) {
    //            System.out.println(s);
    //        } else {
    //            for (int i = 1; i <= 6; i++) {
    //                roll(s + String.valueOf(i), depth - 1);
    //            }
    //        }
    //    }
}
