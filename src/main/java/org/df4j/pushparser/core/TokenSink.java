package org.df4j.pushparser.core;

public interface TokenSink<T> {

    void write(T t);

    default void close() {
        throw new SyntaxError("unexpected end of input token stream");
    }

}
