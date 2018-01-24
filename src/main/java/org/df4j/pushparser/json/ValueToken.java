package org.df4j.pushparser.json;

public class ValueToken extends Token {
    public final Object value;

    public ValueToken(Object value) {
        super(TokenType.Value);
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value="+value;
    }

}
