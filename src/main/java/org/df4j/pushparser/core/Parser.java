package org.df4j.pushparser.core;

import org.df4j.pushparser.json.JsonParserState;

import java.util.Stack;

public class Parser<T> implements TokenSink<T> {
    private ParserState state;
    private Stack<ParserState> stack = new Stack<ParserState>();
    private Object result;
    private T unparsed;
    
    @Override
    public void write(T t) {
        for (;;) {
            state.write(t);
            if (unparsed == null) {
                return;
            }
            t = unparsed;
            unparsed = null;
        }
    }

    protected void pushback(T t) {
        if (unparsed != null) {
            throw new IllegalStateException("too many unparsed tokens");
        }
        unparsed = t;
    }

    @Override
    public void close() {
        state.close();
    }

    protected void setState(JsonParserState state) {
        this.state = state;
    }

    protected void callState(JsonParserState newState) {
        stack.push(this.state);
        this.state = newState;
        newState.start();
    }

    protected void returnState(Object result) {
        this.result = result;
        this.state = stack.pop();
        this.state.setResult(result);
    }

    protected void popState() {
        state = stack.pop();
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}
