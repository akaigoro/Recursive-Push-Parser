package org.df4j.core.workflow;

import org.df4j.core.workflow.token.Token;
import org.df4j.core.workflow.token.TokenType;
import org.df4j.core.workflow.token.ValueToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class JsonScanner implements IJsonScanner{
    final IJsonParser parser;

    public JsonScanner(IJsonParser parser) {
        this.parser = parser;
    }

    private final IJsonScanner initState;
    private final IJsonScanner namedValueState;
    private final IJsonScanner stringBodyState;
    private final IJsonScanner numberState;

    {
        initState = new IJsonScanner() {
            @Override
            public void write(char c) {
                TokenType type = TokenType.letterTypes.get(c);
                if (type != null) {
                    Token t = new Token(type);
                    parser.write(t);
                } else {
                    if (c =='"') {
                        setState(stringBodyState);
                        return;
                    } else if (Character.isDigit(c)) {
                        setState(numberState);
                    } else if (Character.isLetter(c)) {
                        setState(namedValueState);
                    } else if (Character.isSpaceChar(c)) {
                        return;
                    } else {
                        throw new SyntaxError("unexpected character: "+c);
                    }
                    state.write(c);
                }
            }

            @Override
            public void close() {
                parser.close();
            }
        };

        stringBodyState = new IJsonScanner() {
            StringBuilder sb = new StringBuilder();

            @Override
            public void write(char c) {
                if (c != '"') {
                    sb.append(c);
                } else {
                    String value = sb.toString();
                    Token t = new ValueToken(value);
                    parser.write(t);
                    sb.delete(0, sb.length());
                    setState(initState);
                }
            }
        };

        numberState = new IJsonScanner() {
            int number = 0;

            @Override
            public void write(char c) {
                if (Character.isDigit(c)) {
                    number=number*10+c-'0';
                } else {
                    Token t = new ValueToken(number);
                    number = 0;
                    parser.write(t);
                    setState(initState);
                    state.write(c);
                }
            }
        };

        namedValueState = new IJsonScanner() {
            StringBuilder sb = new StringBuilder();

            @Override
            public void write(char c) {
                if (Character.isLetter(c)) {
                    sb.append(c);
                } else {
                    Object value;
                    String str = sb.toString();
                    switch (str) {
                        case "true":
                            value = Boolean.TRUE;
                            break;
                        case "false":
                            value = Boolean.FALSE;
                            break;
                        case "null":
                            value = null;
                            break;
                        default: throw new SyntaxError("unexpected word: "+str);
                    }
                    sb.delete(0, sb.length());
                    Token t = new ValueToken(value);
                    parser.write(t);
                    setState(initState);
                    state.write(c);
                }
            }
        };

    }

    IJsonScanner state = initState;

    void setState(IJsonScanner state) {
        this.state = state;
    }

    @Override
    public void write(char c) {
        state.write(c);
    }

    @Override
    public void close() {
        state.close();
    }
}
