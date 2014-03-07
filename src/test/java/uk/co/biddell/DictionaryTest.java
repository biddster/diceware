package uk.co.biddell;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DictionaryTest {
    
    @Test
    public void test() {
        final Dictionary d = new Dictionary("/diceware8k.txt");
        assertEquals(8192, d.getWordCount());
        assertEquals("vowel", new InMemoryDictionary().getWord(8191));
        assertEquals("vowel", d.getWord(63266));
        assertEquals(267753, new Dictionary("/sowpods.txt").getWordCount());
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
