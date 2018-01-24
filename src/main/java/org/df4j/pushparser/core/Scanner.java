package org.df4j.pushparser.core;

public class Scanner<T> implements CharSink {
    private final TokenSink<T> parser;
    private char unparsed;
    private CharSink state;

    public Scanner(TokenSink<T> parser) {
        this.parser = parser;
    }

    protected void setState(CharSink state) {
        this.state = state;
    }

    protected void pushback(char c) {
        if (unparsed != 0) {
            throw new IllegalStateException("too many unparsed tokens");
        }
        unparsed = c;
    }

    @Override
    public void write(char c) {
        for (; ; ) {
            state.write(c);
            if (unparsed == 0) {
                return;
            }
            c = unparsed;
            unparsed = 0;
        }
    }

    protected void closeRokenStream() {
        parser.close();
    }

    protected void writeToken(T t) {
        parser.write(t);
    }

    @Override
    public void close() {
        state.close();
    }
}

