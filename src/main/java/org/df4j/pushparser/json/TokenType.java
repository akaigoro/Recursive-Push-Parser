package org.df4j.pushparser.json;

import java.util.HashMap;

public enum TokenType {
    LeftBrace('{'),
    RightBrace('}'),
    LeftBracket('['),
    RightBracket(']'),
    Comma(','),
    Semicolon(':'),
    Value(' ');

    private final char c;

    TokenType(char c) {
        this.c = c;
    }

    public static final HashMap<Character, TokenType> letterTypes = new HashMap<Character, TokenType>();
    static {
        for (TokenType type : TokenType.values()) {
            if (type != Value) {
                letterTypes.put(type.c, type);
            }
        }
    }
}
