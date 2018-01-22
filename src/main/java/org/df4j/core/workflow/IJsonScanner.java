package org.df4j.core.workflow;

public interface IJsonScanner {
    public void write(char c);
    default public void close() {
        throw new SyntaxError("unexpected end of input file");
    }
}
