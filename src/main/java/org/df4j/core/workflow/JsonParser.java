package org.df4j.core.workflow;

import org.df4j.core.workflow.token.Token;

public class JsonParser implements IJsonParser {
    private final IJsonScanner initState;
    private final IJsonScanner stringState;
    private final IJsonScanner objectState;
    private final IJsonScanner arrayState;

    {
        initState = new IJsonScanner() {
            @Override
            public void write(char c) {
            }

            @Override
            public void close() {

            }
        };

        objectState = new IJsonScanner() {
            @Override
            public void write(char c) {

            }

            @Override
            public void close() {

            }
        };

        arrayState = new IJsonScanner() {
            @Override
            public void write(char c) {

            }

            @Override
            public void close() {

            }
        };

        stringState = new IJsonScanner() {
            @Override
            public void write(char c) {

            }

            @Override
            public void close() {

            }
        };
    }

    private IJsonScanner state = initState;

    public void setState(IJsonScanner state) {
        this.state = state;
    }

    @Override
    public void write(Token t) {

    }

    @Override
    public void close() {

    }
}
