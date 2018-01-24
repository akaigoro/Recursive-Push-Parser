package org.df4j.pushparser.core;

public interface CharSink {

    void write(char c);

    default void close() {
        throw new SyntaxError("unexpected end of input file");
    }
}
