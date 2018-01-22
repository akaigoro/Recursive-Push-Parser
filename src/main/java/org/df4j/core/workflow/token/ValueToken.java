package org.df4j.core.workflow.token;

import org.df4j.core.workflow.token.Token;

public class ValueToken extends Token {
    public final Object value;

    public ValueToken(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "value="+value;
    }

}
