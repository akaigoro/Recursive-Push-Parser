package org.df4j.core.workflow;

import org.df4j.core.workflow.token.Token;

public interface IJsonParser {
    public void write(Token t);
    default public void close(){}
}
