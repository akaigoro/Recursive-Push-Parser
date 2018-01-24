package org.df4j.pushparser.core;

public interface ParserState<T> extends TokenSink<T> {

    default void start() {}

    default void setResult(Object result) {
        throw new UnsupportedOperationException();
    }
}
