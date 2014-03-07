package uk.co.biddell;

final class DiceWord {

    private static final Dictionary dictionary = new Dictionary("/sowpods.txt");

    static final String getDiceWord(final int n) {
        return dictionary.getWord(n);
    }

    static final char getPassphraseExtraSecurityChar(final int thirdRoll, final int fourthRoll) {
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

    static final char getPasswordSpecialChar(final int firstRoll, final int secondRoll) {
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

    static final char getPasswordRandomChar(final int firstRoll, final int secondRoll, final int thirdRoll) {
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
}
