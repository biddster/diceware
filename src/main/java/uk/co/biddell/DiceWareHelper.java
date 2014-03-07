package uk.co.biddell;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DiceWareHelper {

    private final SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
    private static final List<String> diceWords8k;
    static {
        // Load in the diceware 8k word list.
        final ArrayList<String> tmp = new ArrayList<String>(8192);
        try {
            final LineNumberReader br = new LineNumberReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("diceware8k.txt")));
            String diceWord;
            while ((diceWord = br.readLine()) != null) {
                tmp.add(diceWord);
            }
            br.close();
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        diceWords8k = Collections.unmodifiableList(tmp);
    }
    private static final char[] passphraseExtraSecurityChars[] = {
            {
                    '~', '!', '#', '$', '%', '^'
            }, {
                    '&', '*', '(', ')', '-', '='
            }, {
                    '+', '[', ']', '\\', '{', '}'
            }, {
                    ':', ';', '"', '\'', '<', '>'
            }, {
                    '?', '/', '0', '1', '2', '3'
            }, {
                    '4', '5', '6', '7', '8', '9'
            }
    };
    private static final char[] randomSpecialCharacters[] = {
            {
                    '!', '@', '#', '$', '%', '^'
            }, {
                    '&', '*', '(', ')', '-', '='
            }, {
                    '+', '[', ']', '{', '}', '\\'
            }, {
                    '|', '`', ';', ':', '\'', '"'
            }, {
                    '<', '>', '/', '?', '.', ','
            }, {
                    '~', '_', '3', '5', '7', '9'
            }
    };
    private static final char[] randomCharactersRoll1or2[] = {
            {
                    'A', 'B', 'C', 'D', 'E', 'F'
            }, {
                    'G', 'H', 'I', 'J', 'K', 'L'
            }, {
                    'M', 'N', 'O', 'P', 'Q', 'R'
            }, {
                    'S', 'T', 'U', 'V', 'W', 'X'
            }, {
                    'Y', 'Z', '0', '1', '2', '3'
            }, {
                    '4', '5', '6', '7', '8', '9'
            }
    };
    private static final char[] randomCharactersRoll3or4[] = {
            {
                    'a', 'b', 'c', 'd', 'e', 'f'
            }, {
                    'g', 'h', 'i', 'j', 'k', 'l'
            }, {
                    'm', 'n', 'o', 'p', 'q', 'r'
            }, {
                    's', 't', 'u', 'v', 'w', 'x'
            }, {
                    'y', 'z', '~', '_', ' ', ' '
            }, {
                    ' ', ' ', ' ', ' ', ' ', ' '
            }
    };
    private static final char[] randomCharactersRoll5or6[] = {
            {
                    '!', '@', '#', '$', '%', '^'
            }, {
                    '&', '*', '(', ')', '-', '='
            }, {
                    '+', '[', ']', '{', '}', '\\'
            }, {
                    '|', '`', ';', ':', '\'', '"'
            }, {
                    '<', '>', '/', '?', '.', ','
            }, {
                    ' ', ' ', ' ', ' ', ' ', ' '
            }
    };
    private static final char[] randomDecimalNumbers[] = {
            {
                    '1', '2', '3', '4', '5', '*'
            }, {
                    '6', '7', '8', '9', '0', '*'
            }, {
                    '1', '2', '3', '4', '5', '*'
            }, {
                    '6', '7', '8', '9', '0', '*'
            }, {
                    '1', '2', '3', '4', '5', '*'
            }, {
                    '6', '7', '8', '9', '0', '*'
            }
    };
    private static final char[] randomHexadecimalNumbers[] = {
            {
                    '0', '1', '2', '3', '4', '5'
            }, {
                    '6', '7', '8', '9', 'A', 'B'
            }, {
                    'C', 'D', 'E', 'E', '0', '1'
            }, {
                    '2', '3', '4', '5', '6', '7'
            }, {
                    '8', '9', 'A', 'B', 'C', 'D'
            }, {
                    'E', 'F', '*', '*', '*', '*'
            }
    };
    // Note that I've added the first column here myself. This is because we're using the diceware 8k
    // list which has words with 1 character. This way we either insert before or after the only character.
    // http://world.std.com/~reinhold/dicewarefaq.html#characterinaword
    private static final char[] randomCharacterInWord[] = {
            {
                    '1', '1', '1', '1', '1', '1'
            }, {
                    '0', '2', '2', '2', '2', '2'
            }, {
                    '1', '0', '3', '3', '3', '3'
            }, {
                    '0', '1', '0', '4', '4', '4'
            }, {
                    '1', '2', '*', '0', '5', '5'
            }, {
                    '0', '0', '*', '*', '0', '6'
            }
    };

    /**
     * This class is not thread-safe by virtue of the fact that the SecureRandom used
     * internally may or may not be thread-safe.
     * <a href="">http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6498354</a>
     * @throws NoSuchAlgorithmException
     */
    public DiceWareHelper() throws NoSuchAlgorithmException {
        // Seed the random as per OWASP
        // http://www.owasp.org/index.php/Using_the_Java_Cryptographic_Extensions
        // Note that a call to nextBytes when the random is unseeded causes it to seed itself
        rand.nextBytes(new byte[512]);
    }

    /**
     * Creates a string with the given number of characters. The returned string comprises
     * of uppercase letters and numbers as specified here
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#lettersnumbers">http://world.std.com/~reinhold/dicewarefaq.html#lettersnumbers</a>.
     * Each random character adds 5.17 bits of entropy. 
     * @param numberOfDigits
     * @return a String e.g. UBCV84BNT0U6RZY 
     */
    public final String createRandomLettersAndNumbers(final int numberOfCharacters) {
        final StringBuilder sb = new StringBuilder(numberOfCharacters);
        for (int i = 0; i < numberOfCharacters; ++i) {
            final int firstRoll = throwDie();
            final int secondRoll = throwDie();
            sb.append(randomCharactersRoll1or2[secondRoll - 1][firstRoll - 1]);
        }
        return sb.toString();
    }

    /**
     * Creates a random string containing the requested number of digits as specified
     * here <a href="http://world.std.com/~reinhold/dicewarefaq.html#decimal">http://world.std.com/~reinhold/dicewarefaq.html#decimal</a>.
     * Each random digit adds 3.3 bits of entropy. 
     * @param numberOfDigits
     * @return a string e.g. 938017764811504795
     */
    public final String createRandomDecimalNumber(final int numberOfDigits) {
        final StringBuilder sb = new StringBuilder(numberOfDigits);
        for (int i = 0; i < numberOfDigits; ++i) {
            char number;
            do {
                // Do while we don't get a * for this char.
                final int firstRoll = throwDie();
                final int secondRoll = throwDie();
                number = randomDecimalNumbers[secondRoll - 1][firstRoll - 1];
            } while (number == '*');
            sb.append(number);
        }
        return sb.toString();
    }

    /**
     * Creates a string containing the requested number of characters as specified here
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#randomstrings">http://world.std.com/~reinhold/dicewarefaq.html#randomstrings</a>.
     * Each random character adds 6.55 bits of entropy.
     * @param numberOfCharacters
     * @return a string e.g. J7v&lt;e:q5h`8`dDA
     */
    public final String createRandomCharacters(final int numberOfCharacters) {
        final StringBuilder sb = new StringBuilder(numberOfCharacters);
        for (int i = 0; i < numberOfCharacters; ++i) {
            char letter;
            do {
                final int firstRoll = throwDie();
                final int secondRoll = throwDie();
                final int thirdRoll = throwDie();
                // Do while we don't get a space for this char as we don't want spaces.
                if (firstRoll == 1 || firstRoll == 2) {
                    letter = randomCharactersRoll1or2[thirdRoll - 1][secondRoll - 1];
                } else if (firstRoll == 3 || firstRoll == 4) {
                    letter = randomCharactersRoll3or4[thirdRoll - 1][secondRoll - 1];
                } else {
                    letter = randomCharactersRoll5or6[thirdRoll - 1][secondRoll - 1];
                }
            } while (letter == ' ');
            sb.append(letter);
        }
        return sb.toString();
    }

    /**
     * Creates a string containing the requested number of hex characters as specified here
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#hexadecimal">http://world.std.com/~reinhold/dicewarefaq.html#hexadecimal</a>.
     * Each random character adds 4 bits of entropy.
     * @param numberOfCharacters
     * @return a string e.g. 495C84E9720837
     */
    public final String createRandomHexadecimalNumber(final int numberOfDigits) {
        final StringBuilder sb = new StringBuilder(numberOfDigits);
        for (int i = 0; i < numberOfDigits; ++i) {
            char number;
            do {
                final int firstRoll = throwDie();
                final int secondRoll = throwDie();
                // Do while we don't get a * for this char.
                number = randomHexadecimalNumbers[secondRoll - 1][firstRoll - 1];
            } while (number == '*');
            sb.append(number);
        }
        return sb.toString();
    }

    // 
    /**
     * Creates login password suitable for *nix style logins where the password file is shadowed and multiple login
     * attempts are monitored. See here 
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#login">http://world.std.com/~reinhold/dicewarefaq.html#login</a>
     * for more details. Note that the returned password may be less than maxLength.
     * @param maxLength - the maximum size the password must not exceed. It's better and more secure to use createWindowsLoginPassword
     * @return a string e.g. davis;live
     */
    public final String createLoginPassword(final int maxLength) {
        final StringBuilder sb = new StringBuilder(16);
        sb.append(getDiceWord());
        sb.append(createRandomSpecialCharacter());
        sb.append(getDiceWord());
        if (sb.length() > maxLength) {
            sb.setLength(maxLength);
        }
        return sb.toString();
    }

    // 
    /**
     * Creates a login password suitable for a Windows login as specified here
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#windowscaution">http://world.std.com/~reinhold/dicewarefaq.html#windowscaution</a>.
     * This method doesn't follow the diceware recommendation exactly as it truncates if the
     * generated password is longer than the requested length. The diceware page makes no
     * recommendation regarding this.
     * @param passwordLength
     * @return a string e.g. virgo%dadaGe
     */
    public final String createWindowsLoginPassword(final int passwordLength) {
        // Build up a list of words until we have matched or exceeded the requested password length
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>();
        while (actualLength < passwordLength) {
            final String word = getDiceWord();
            words.add(word);
            actualLength += word.length();
        }
        final StringBuilder password = new StringBuilder(passwordLength * 6);
        // Now pick out a word to capitalise and a word to have a special char randomly inserted.
        final int capitaliseWordIndex = rand.nextInt(words.size());
        int specialCharWordIndex;
        do {
            // Repeat until the two are different.
            specialCharWordIndex = rand.nextInt(words.size());
        } while (capitaliseWordIndex == specialCharWordIndex);
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);
            // Append the word to our formatted output in alternate colours so the dice words
            // are easily seen and hopefully remembered.
            if (i == capitaliseWordIndex) {
                // This is our special word where we capitalise the first char.
                final char[] wordChars = word.toCharArray();
                wordChars[0] = Character.toUpperCase(wordChars[0]);
                word = String.valueOf(wordChars);
            } else if (i == specialCharWordIndex) {
                word = insertCharacterRandomlyIntoWord(word, createRandomSpecialCharacter());
            }
            password.append(word);
        }
        // If the word is too long we chop the end off. This isn't strict diceware but it's close.
        if (password.length() > passwordLength) {
            password.setLength(passwordLength);
        }
        return password.toString();
    }

    /**
     * Creates a passphrase as per the diceware specification detailed here
     * <a href="http://world.std.com/~reinhold/diceware.html">http://world.std.com/~reinhold/diceware.html</a>.
     * By enabling addExtraSecurity, a randomly chosen special character is randomly inserted into a randomly chosen word. 
     * Each word yields 12.9 bits of entropy. addExtraSecurity adds approx 10 bits of entropy.
     * @param numberOfWords
     * @param addExtraSecurity
     * @return a string e.g. hunchholtmhnobislehitchtulsa
     */
    public final String createPassphrase(final int numberOfWords, final boolean addExtraSecurity) {
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>(numberOfWords);
        for (int i = 0; i < numberOfWords; ++i) {
            final String word = getDiceWord();
            words.add(word);
            actualLength += word.length();
        }
        if (actualLength < 14) {
            // Less than 14 is not recommended according to diceware so throw again by calling ourself
            return createPassphrase(numberOfWords, addExtraSecurity);
        }
        final StringBuilder passPhrase = new StringBuilder(numberOfWords * 6);
        int extraSecurityWordIndex = -1;
        if (addExtraSecurity) {
            // If we maximise security we insert a char in a word whose index is selected here.
            extraSecurityWordIndex = rand.nextInt(numberOfWords);
        }
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);
            // Append the word to our formatted output in alternate colours so the dice words
            // are easily seen and hopefully remembered.
            if (i == extraSecurityWordIndex) {
                final int firstRoll = throwDie();
                final int secondRoll = throwDie();
                final char securityChar = passphraseExtraSecurityChars[secondRoll - 1][firstRoll - 1];
                word = insertCharacterRandomlyIntoWord(word, securityChar);
            }
            passPhrase.append(word);
        }
        return passPhrase.toString();
    }

    /**
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#specialcharacters">http://world.std.com/~reinhold/dicewarefaq.html#specialcharacters</a>
     * @return
     */
    private final char createRandomSpecialCharacter() {
        final int firstRoll = throwDie();
        final int secondRoll = throwDie();
        return randomSpecialCharacters[secondRoll - 1][firstRoll - 1];
    }

    /**
     * <a href="http://world.std.com/~reinhold/dicewarefaq.html#characterinaword">http://world.std.com/~reinhold/dicewarefaq.html#characterinaword</a>
     * @return
     */
    private final String insertCharacterRandomlyIntoWord(final String word, final char characterToInsert) {
        char characterIndex;
        do {
            final int firstRoll = throwDie();
            // Do while we don't get a * for this char.
            characterIndex = randomCharacterInWord[firstRoll - 1][word.length() - 1];
        } while (characterIndex == '*');
        final StringBuilder sb = new StringBuilder(word.length() + 1);
        sb.append(word);
        // System.out.println("word [" + word + "] char [" + characterToInsert + "]");
        sb.insert(Character.digit(characterIndex, 10), characterToInsert);
        return sb.toString();
    }

    /**
     * Simulate a dice throw using secure random.
     * @return a number between 1 and 6 inclusive
     */
    private final int throwDie() {
        return rand.nextInt(6) + 1;
    }

    private final String getDiceWord() {
        // Throw the dice 5 times and build up our selection criteria,
        final StringBuilder currentWord = new StringBuilder(5);
        for (int j = 0; j < 5; ++j) {
            currentWord.append(throwDie());
        }
        // Now get our actual dice word
        return diceWords8k.get(Integer.parseInt(currentWord.toString()) & 0X1fff);
    }

    public static final void main(final String[] args) throws NoSuchAlgorithmException {
        final DiceWareHelper dwh = new DiceWareHelper();
        System.out.println(dwh.createPassphrase(7, false));
        System.out.println(dwh.createPassphrase(9, true));
        System.out.println(dwh.createLoginPassword(10));
        System.out.println(dwh.createWindowsLoginPassword(12));
        System.out.println(dwh.createRandomCharacters(15));
        System.out.println(dwh.createRandomLettersAndNumbers(15));
        System.out.println(dwh.createRandomDecimalNumber(18));
        System.out.println(dwh.createRandomHexadecimalNumber(14));
        System.out.println(dwh.createRandomSpecialCharacter());
    }
}
