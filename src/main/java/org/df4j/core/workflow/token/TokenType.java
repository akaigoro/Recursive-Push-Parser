package org.df4j.core.workflow.token;

import java.util.HashMap;

public enum TokenType {
    leftBrace('{'),
    rightBrace('}'),
    leftBracket('['),
    rightBracket(']'),
    comma(','),
    semicolon(':');

    private final char c;

    TokenType(char c) {
        this.c = c;
    }

    public static final HashMap<Character, TokenType> letterTypes = new HashMap<Character, TokenType>();
    static {
        for (TokenType type : TokenType.values()) {
            letterTypes.put(type.c, type);
        }
    }
}
