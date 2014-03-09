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

import java.util.Vector;

final class DiceWord {

    private static final Vector<Dictionary> dictionaries = new Vector<Dictionary>();
    private static Dictionary dictionary;

    static {
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
    static Vector<Dictionary> getDictionaries() {
        return dictionaries;
    }


    static String getDiceWord(final int n) {
        return dictionary.getWord(n);
    }

    static char getPassphraseExtraSecurityChar(final int thirdRoll, final int fourthRoll) {
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

    static char getPasswordSpecialChar(final int firstRoll, final int secondRoll) {
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

    static char getPasswordRandomChar(final int firstRoll, final int secondRoll, final int thirdRoll) {
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

    public static Dictionary getDictionary() {
        return dictionary;
    }

    public static void setDictionary(Dictionary dict) {
        dictionary = dict;
    }
}
