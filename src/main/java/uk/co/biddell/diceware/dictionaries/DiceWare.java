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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public final class DiceWare {

    private final Random rand = SecureRandom.getInstance("SHA1PRNG");
    private final Vector<Dictionary> dictionaries = new Vector<Dictionary>();
    private Dictionary dictionary;

    public DiceWare() throws IOException, NoSuchAlgorithmException {
        dictionaries.add(new FileBasedDictionary("DiceWare 8K", "/diceware8k.txt"));
        dictionaries.add(new FileBasedDictionary("DiceWare Beale", "/beale.txt"));
        dictionaries.add(new FileBasedDictionary("Scrabble SOWPODS", "/sowpods.txt"));
        dictionary = dictionaries.get(0);
    }

    /**
     * Returns a vector of the available dictionaries. A Vector? Well combobox models like Vectors
     * and I don't think we'll get caught out by it being synchronised.
     * @return
     */
    public Vector<Dictionary> getDictionaries() {
        return dictionaries;
    }

    char getPassphraseExtraSecurityChar(final int thirdRoll, final int fourthRoll) {
        final char[] passphraseExtraSecurityChars[] = {
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
        return passphraseExtraSecurityChars[fourthRoll - 1][thirdRoll - 1];
    }

    public char getPasswordSpecialChar(final int firstRoll, final int secondRoll) {
        final char[] passwordSpecialChars[] = {
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
        return passwordSpecialChars[secondRoll - 1][firstRoll - 1];
    }

    char getPasswordRandomChar(final int firstRoll, final int secondRoll, final int thirdRoll) {
        final char[] passwordCharsRoll1Or2[] = {
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
        final char[] passwordCharsRoll3Or4[] = {
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
        final char[] passwordCharsRoll5Or6[] = {
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
    //    private String getDiceWare() {
    // Throw the dice 5 times and build up our selection criteria,
    //        final StringBuilder currentWord = new StringBuilder(5);
    //        for (int j = 0; j < 5; ++j) {
    //            currentWord.append(throwDie());
    //        }
    //        final Integer wordNumber = Integer.valueOf(currentWord.toString());
    //        // Now get our actual dice word
    //        return diceWare.getDiceWare(wordNumber);
    //        return diceWare.getDictionary().getWord(rand);
    //    }

    public DiceWords createPassword(final int passwordLength, final boolean maximiseSecurity) {
        final DiceWords diceWords = new DiceWords();
        if (maximiseSecurity) {
            // The maximise security check is selected so we are just going to get n random
            // password chars.
            for (int i = 0; i < passwordLength; ++i) {
                char letter;
                do {
                    // Do while we don't get a space for this char as we don't want spaces.
                    letter = getPasswordRandomChar(throwDie(), throwDie(), throwDie());
                } while (letter == ' ');
                diceWords.append(letter);
            }
        } else {
            // Build up a list of words until we have matched or exceeded the requested password length
            int actualLength = 0;
            final ArrayList<String> words = new ArrayList<String>();
            while (actualLength < passwordLength) {
                final String word = dictionary.getWord(rand);
                words.add(word);
                actualLength += word.length();
            }
            // Now pick out a word to capitalise and a word to have a special char randomly inserted.
            final int capitaliseWord;
            final int specialCharWord;
            // If we're truncating the last word, don't select it as the word to insert the special char into.
            if (actualLength > passwordLength) {
                specialCharWord = rand.nextInt(words.size() - 1);
                capitaliseWord = rand.nextInt(words.size() - 1);
            } else {
                specialCharWord = rand.nextInt(words.size());
                capitaliseWord = rand.nextInt(words.size());
            }
            for (int i = 0; i < words.size(); ++i) {
                String word = words.get(i);
                // Append the word to our formatted output in alternate colours so the dice words
                // are easily seen and hopefully remembered.
                if (i == capitaliseWord) {
                    // This is our special word where we capitalise the first char.
                    final char[] wordChars = word.toCharArray();
                    wordChars[0] = Character.toUpperCase(wordChars[0]);
                    word = String.valueOf(wordChars);
                }
                if (i == specialCharWord) {
                    // This is our special word. Pick a random char within the word to be replaced with
                    // our random special char.
                    word = addExtraSecurityToWord(word);
                }
                // If the word is too long we chop the end off.
                final int length = diceWords.getLength();
                if ((length + word.length()) > passwordLength) {
                    word = word.substring(0, passwordLength - length);
                }
                diceWords.append(word);
            }
        }
        return diceWords;
    }

    public DiceWords createPassphrase(final int numberOfWords, final boolean maximiseSecurity) {
        final DiceWords diceWords = new DiceWords();
        int actualLength = 0;
        final ArrayList<String> words = new ArrayList<String>(numberOfWords);
        for (int i = 0; i < numberOfWords; ++i) {
            final String word = dictionary.getWord(rand);
            words.add(word);
            actualLength += word.length();
        }
        if (actualLength < 14) {
            // Less than 14 is not recommended according to diceware so throw again
            createPassphrase(numberOfWords, maximiseSecurity);
        } else {
            int extraSecurityWord = -1;
            if (maximiseSecurity) {
                // If we maximise security we replace a char in a word whose index is selected here.
                extraSecurityWord = rand.nextInt(numberOfWords);
            }
            for (int i = 0; i < words.size(); ++i) {
                String word = words.get(i);
                // Append the word to our formatted output in alternate colours so the dice words
                // are easily seen and hopefully remembered.
                if (i == extraSecurityWord) {
                    // This is our special word. Pick a random char within the word to be replaced with
                    // our random special char.
                    word = addExtraSecurityToWord(word);
                }
                diceWords.append(word);
            }
        }
        return diceWords;
    }

    private String addExtraSecurityToWord(final String word) {
        final int extraSecurityWordLetter = rand.nextInt(word.length());
        final char securityChar = getPassphraseExtraSecurityChar(throwDie(), throwDie());
        final char[] wordChars = word.toCharArray();
        wordChars[extraSecurityWordLetter] = securityChar;
        return String.valueOf(wordChars);
    }

    private int throwDie() {
        return rand.nextInt(6) + 1;
    }
}
