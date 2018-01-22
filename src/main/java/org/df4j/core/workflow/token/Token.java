package org.df4j.core.workflow.token;

public class Token {
    TokenType type;

    public Token(TokenType type) {
        this.type = type;
    }

    public Token() {
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
