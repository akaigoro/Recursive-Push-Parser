package org.df4j.pushparser.json;

import org.df4j.pushparser.core.CharSink;
import org.df4j.pushparser.core.Scanner;
import org.df4j.pushparser.core.SyntaxError;
import org.df4j.pushparser.core.TokenSink;

public class JsonScanner extends Scanner {

    public JsonScanner(TokenSink parser) {
        super(parser);
        setState(initState);
    }

    private final CharSink initState = new rootScanner();
    private final CharSink stringBodyState = new StringBodyScanner();
    private final CharSink numberState = new NumberState();
    private final CharSink namedValueState = new NamedValueState();

    private class rootScanner implements CharSink {
        @Override
        public void write(char c) {
            TokenType type = TokenType.letterTypes.get(c);
            if (type != null) {
                Token t = new Token(type);
                writeToken(t);
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
                pushback(c);
            }
        }

        @Override
        public void close() {
            closeRokenStream();
        }
    }

    private class StringBodyScanner implements CharSink {
        StringBuilder sb = new StringBuilder();

        @Override
        public void write(char c) {
            if (c != '"') {
                sb.append(c);
            } else {
                String value = sb.toString();
                Token t = new ValueToken(value);
                writeToken(t);
                sb.delete(0, sb.length());
                setState(initState);
            }
        }
    }

    private class NumberState implements CharSink {
        int number = 0;

        @Override
        public void write(char c) {
            if (Character.isDigit(c)) {
                number=number*10+c-'0';
            } else {
                pushback(c);
                Token t = new ValueToken(number);
                number = 0;
                writeToken(t);
                setState(initState);
            }
        }
    }

    private class NamedValueState implements CharSink {
        StringBuilder sb = new StringBuilder();

        @Override
        public void write(char c) {
            if (Character.isLetter(c)) {
                sb.append(c);
            } else {
                pushback(c);
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
                writeToken(t);
                setState(initState);
            }
        }
    }
}
