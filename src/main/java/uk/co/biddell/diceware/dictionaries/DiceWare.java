/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package uk.co.biddell.diceware.dictionaries;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public final class DiceWare {

    private final Random rand = SecureRandom.getInstance("SHA1PRNG");
    private final Vector<Dictionary> dictionaries = new Vector<Dictionary>();
    private Dictionary dictionary;

    public static enum LengthType {
        WORD_LENGTH,
        CHARACTER_LENGTH;
    }

    public static enum Type {
        PASSPHRASE("Passphrase", LengthType.WORD_LENGTH),
        PASSPHRASE_EXTRA_SECURITY("Passphrase", LengthType.WORD_LENGTH),
        WINDOWS_PASSWORD("Passphrase", LengthType.CHARACTER_LENGTH),
        MAXIMUM_SECURITY_PASSWORD("Passphrase", LengthType.CHARACTER_LENGTH),
        RANDOM_LETTERS_AND_NUMBERS("Passphrase", LengthType.CHARACTER_LENGTH),
        RANDOM_DECIMAL_NUMBERS("Passphrase", LengthType.CHARACTER_LENGTH),
        RANDOM_HEXADECIMAL_NUMBERS("Passphrase", LengthType.CHARACTER_LENGTH);

        private String description;
        private LengthType lengthType;

        private Type(final String description, final LengthType lengthType) {
            this.description = description;
            this.lengthType = lengthType;
        }

        public String getDescription() {
            return description;
        }

        public LengthType getLengthType() {
            return lengthType;
        };
    }

    public DiceWare() throws IOException, NoSuchAlgorithmException {
        dictionaries.add(new FileBasedDictionary("DiceWare - United Kingdon", "/beale.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - United States", "/diceware.txt"));
        // dictionaries.add(new FileBasedDictionary("DiceWare - German",
        // "/diceware_german.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Spanish", "/DW-Espanol-1.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Japanese", "/diceware_jp.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Dutch", "/DicewareDutch.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Polish", "/dicelist-pl.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Swedish", "/diceware-sv.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare - Russian", "/diceware.ru.txt"));
        dictionary = dictionaries.get(0);
    }

    /**
     * Returns a vector of the available dictionaries. A Vector? Well combobox models like Vectors and I don't think
     * we'll get caught out by it being synchronised.
     *
     * @return
     */
    public Vector<Dictionary> getDictionaries() {
        return dictionaries;
    }

    private char getPassphraseExtraSecurityChar(final int thirdRoll, final int fourthRoll) {
        final char[] passphraseExtraSecurityChars[] = {
                {
                        '~',
                        '!',
                        '#',
                        '$',
                        '%',
                        '^'
                },
                {
                        '&',
                        '*',
                        '(',
                        ')',
                        '-',
                        '='
                },
                {
                        '+',
                        '[',
                        ']',
                        '\\',
                        '{',
                        '}'
                },
                {
                        ':',
                        ';',
                        '"',
                        '\'',
                        '<',
                        '>'
                },
                {
                        '?',
                        '/',
                        '0',
                        '1',
                        '2',
                        '3'
                },
                {
                        '4',
                        '5',
                        '6',
                        '7',
                        '8',
                        '9'
                }
        };
        return passphraseExtraSecurityChars[fourthRoll - 1][thirdRoll - 1];
    }

    private char getPasswordSpecialChar(final int firstRoll, final int secondRoll) {
        final char[] passwordSpecialChars[] = {
                {
                        '!',
                        '@',
                        '#',
                        '$',
                        '%',
                        '^'
                },
                {
                        '&',
                        '*',
                        '(',
                        ')',
                        '-',
                        '='
                },
                {
                        '+',
                        '[',
                        ']',
                        '{',
                        '}',
                        '\\'
                },
                {
                        '|',
                        '`',
                        ';',
                        ':',
                        '\'',
                        '"'
                },
                {
                        '<',
                        '>',
                        '/',
                        '?',
                        '.',
                        ','
                },
                {
                        '~',
                        '_',
                        '3',
                        '5',
                        '7',
                        '9'
                }
        };
        return passwordSpecialChars[secondRoll - 1][firstRoll - 1];
    }

    private char getPasswordRandomChar(final int firstRoll, final int secondRoll, final int thirdRoll) {
        final char[] passwordCharsRoll1Or2[] = {
                {
                        'A',
                        'B',
                        'C',
                        'D',
                        'E',
                        'F'
                },
                {
                        'G',
                        'H',
                        'I',
                        'J',
                        'K',
                        'L'
                },
                {
                        'M',
                        'N',
                        'O',
                        'P',
                        'Q',
                        'R'
                },
                {
                        'S',
                        'T',
                        'U',
                        'V',
                        'W',
                        'X'
                },
                {
                        'Y',
                        'Z',
                        '0',
                        '1',
                        '2',
                        '3'
                },
                {
                        '4',
                        '5',
                        '6',
                        '7',
                        '8',
                        '9'
                }
        };
        final char[] passwordCharsRoll3Or4[] = {
                {
                        'a',
                        'b',
                        'c',
                        'd',
                        'e',
                        'f'
                },
                {
                        'g',
                        'h',
                        'i',
                        'j',
                        'k',
                        'l'
                },
                {
                        'm',
                        'n',
                        'o',
                        'p',
                        'q',
                        'r'
                },
                {
                        's',
                        't',
                        'u',
                        'v',
                        'w',
                        'x'
                },
                {
                        'y',
                        'z',
                        '~',
                        '_',
                        ' ',
                        ' '
                },
                {
                        ' ',
                        ' ',
                        ' ',
                        ' ',
                        ' ',
                        ' '
                }
        };
        final char[] passwordCharsRoll5Or6[] = {
                {
                        '!',
                        '@',
                        '#',
                        '$',
                        '%',
                        '^'
                },
                {
                        '&',
                        '*',
                        '(',
                        ')',
                        '-',
                        '='
                },
                {
                        '+',
                        '[',
                        ']',
                        '{',
                        '}',
                        '\\'
                },
                {
                        '|',
                        '`',
                        ';',
                        ':',
                        '\'',
                        '"'
                },
                {
                        '<',
                        '>',
                        '/',
                        '?',
                        '.',
                        ','
                },
                {
                        ' ',
                        ' ',
                        ' ',
                        ' ',
                        ' ',
                        ' '
                }
        };
        if (firstRoll == 1 || firstRoll == 2) {
            return passwordCharsRoll1Or2[thirdRoll - 1][secondRoll - 1];
        } else if (firstRoll == 3 || firstRoll == 4) {
            return passwordCharsRoll3Or4[thirdRoll - 1][secondRoll - 1];
        } else {
            return passwordCharsRoll5Or6[thirdRoll - 1][secondRoll - 1];
        }
    }

    public void setDictionary(final Dictionary dict) {
        dictionary = dict;
    }

    public DiceWords getDiceWords(final Type type, final int length) {
        switch (type) {
        case PASSPHRASE:
            return createPassphrase(length, false);
        case PASSPHRASE_EXTRA_SECURITY:
            return createPassphrase(length, true);
        case WINDOWS_PASSWORD:
            return createWindowsPassword(length);
        case MAXIMUM_SECURITY_PASSWORD:
            return createMaximumSecurityPassword(length);
        case RANDOM_LETTERS_AND_NUMBERS:
            // TODO - implement me
            break;
        case RANDOM_DECIMAL_NUMBERS:
            // TODO - implement me
            break;
        case RANDOM_HEXADECIMAL_NUMBERS:
            // TODO - implement me
            break;
        }
        final DiceWords diceWords = new DiceWords();
        diceWords.append("Not");
        diceWords.append("implemented");
        diceWords.append("yet");
        return null;
    }

    private String getDiceWord() {
        // Throw the dice 5 times and build up our selection criteria,
        final StringBuilder currentWord = new StringBuilder(5);
        for (int j = 0; j < 5; ++j) {
            currentWord.append(throwDie());
        }
        // Now get our actual dice word
        return dictionary.getWord(currentWord.toString());
    }

    private final DiceWords createWindowsPassword(final int length) {
        // Build up a list of words until we have matched or exceeded the
        // requested password length
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>();
        while (actualLength < length) {
            final String word = getDiceWord();
            words.add(word);
            actualLength += word.length();
        }
        // Now pick out a word to capitalise and a word to have a special
        // char randomly inserted.
        final int capitaliseWord;
        final int specialCharWord;
        // If we're truncating the last word, don't select it as the word to
        // insert the special char into.
        if (actualLength > length) {
            specialCharWord = rand.nextInt(words.size() - 1);
            capitaliseWord = rand.nextInt(words.size() - 1);
        } else {
            specialCharWord = rand.nextInt(words.size());
            capitaliseWord = rand.nextInt(words.size());
        }
        final DiceWords diceWords = new DiceWords();
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);
            // Append the word to our formatted output in alternate colours
            // so the dice words
            // are easily seen and hopefully remembered.
            if (i == capitaliseWord) {
                // This is our special word where we capitalise the first
                // char.
                final char[] wordChars = word.toCharArray();
                wordChars[0] = Character.toUpperCase(wordChars[0]);
                word = String.valueOf(wordChars);
            }
            if (i == specialCharWord) {
                // This is our special word. Pick a random char within the
                // word to be replaced with
                // our random special char.
                // TODO - we should be using a dice roll to determine the letter within the word
                final int extraSecurityWordLetter = rand.nextInt(word.length());
                final char securityChar = getPasswordSpecialChar(throwDie(), throwDie());
                final char[] wordChars = word.toCharArray();
                wordChars[extraSecurityWordLetter] = securityChar;
                word = String.valueOf(wordChars);
            }
            // If the word is too long we chop the end off.
            final int currentLength = diceWords.getLength();
            if ((currentLength + word.length()) > length) {
                word = word.substring(0, length - currentLength);
            }
            diceWords.append(word);
        }
        return diceWords;
    }

    private final DiceWords createMaximumSecurityPassword(final int length) {
        final DiceWords diceWords = new DiceWords();
        // The maximise security check is selected so we are just going to
        // get n random password chars.
        for (int i = 0; i < length; ++i) {
            char letter;
            do {
                // Do while we don't get a space for this char as we don't
                // want spaces.
                letter = getPasswordRandomChar(throwDie(), throwDie(), throwDie());
            } while (letter == ' ');
            diceWords.append(letter);
        }
        return diceWords;
    }

    private DiceWords createPassphrase(final int numberOfWords, final boolean maximiseSecurity) {
        final DiceWords diceWords = new DiceWords();
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>(numberOfWords);
        for (int i = 0; i < numberOfWords; ++i) {
            final String word = getDiceWord();
            words.add(word);
            actualLength += word.length();
        }
        if (actualLength < 14) {
            // Less than 14 is not recommended according to diceware so throw
            // again, should probably try this a few times.
            return createPassphrase(numberOfWords, maximiseSecurity);
        } else {
            int extraSecurityWord = -1;
            if (maximiseSecurity) {
                // If we maximise security we replace a char in a word whose
                // index is selected here.
                extraSecurityWord = rand.nextInt(numberOfWords);
            }
            for (int i = 0; i < words.size(); ++i) {
                String word = words.get(i);
                if (i == extraSecurityWord) {
                    // This is our special word. Pick a random char within the
                    // word to be replaced with our random special char.
                    // TODO - we should be using a dice roll to determine the letter within the word
                    final int extraSecurityWordLetter = rand.nextInt(word.length());
                    final char securityChar = getPassphraseExtraSecurityChar(throwDie(), throwDie());
                    final char[] wordChars = word.toCharArray();
                    wordChars[extraSecurityWordLetter] = securityChar;
                    word = String.valueOf(wordChars);
                }
                diceWords.append(word);
            }
        }
        return diceWords;
    }

    private int throwDie() {
        return rand.nextInt(6) + 1;
    }
}
