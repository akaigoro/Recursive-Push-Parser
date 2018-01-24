package org.df4j.pushparser.json;

import org.df4j.pushparser.core.Parser;
import org.df4j.pushparser.core.SyntaxError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonParser extends Parser<Token> {

    public JsonParser() {
        setState(new RootParserState());
    }

    static class KeyValue {
        public final String key;
        public final Object value;

        public KeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    class ValueParserState implements JsonParserState {

        @Override
        public void write(Token t) {
            switch (t.type) {
                case LeftBrace:
                    callState(new ObjectParserState());
                    break;
                case LeftBracket:
                    callState(new ArrayParserState());
                    break;
                case Value:
                    returnState(((ValueToken)t).value);
                    break;
                default:
                    pushback(t);
                    popState();
            }
        }

        @Override
        public void setResult(Object result) {
            returnState(result);
        }
    }

    class ArrayParserState implements JsonParserState {
        ArrayList<Object> array = new ArrayList<>();

        public void start() {
            callState(new ValueParserState());
        }

        @Override
        public void write(Token t) {
            switch (t.type) {
                case Comma:
                    callState(new ValueParserState());
                    break;
                case RightBracket:
                    returnState(array);
                    break;
                default:
                    throw new SyntaxError("unexpected token: "+t.toString());
            }
        }

        @Override
        public void setResult(Object result) {
            array.add(result);
        }
    }

    class KeyValueParserState implements JsonParserState {
        @Override
        public void write(Token t) {
            if (t.type != TokenType.Value) {
                throw new SyntaxError("unexpected token: "+t.toString());
            }
            Object key = ((ValueToken)t).value;
            if (!(key instanceof String)) {
                throw new SyntaxError("unexpected token: "+t.toString());
            }
            KeyValueParserState1 continuation = new KeyValueParserState1((String)key);
            setState(continuation);
        }
    }

    class KeyValueParserState1 implements JsonParserState {
        String key;

        KeyValueParserState1(String key) {
            this.key = key;
        }

        @Override
        public void write(Token t) {
            if (t.type != TokenType.Semicolon) {
                throw new SyntaxError("unexpected token: "+t.toString());
            }
            callState(new ValueParserState());
        }

        @Override
        public void setResult(Object value) {
            KeyValue res = new KeyValue(key, value);
            returnState(res);
        }
    }

    class ObjectParserState implements JsonParserState {
        Map<String, Object> map =  new HashMap<>();

        public void start() {
            callState(new KeyValueParserState());
        }

        @Override
        public void write(Token t) {
            switch (t.type) {
                case Comma:
                    callState(new KeyValueParserState());
                    break;
                case RightBrace:
                    returnState(map);
                    break;
                default:
                    throw new SyntaxError("unexpected token: "+t.toString());
            }
        }

        @Override
        public void setResult(Object result) {
            KeyValue res = (KeyValue)result;
            map.put(res.key, res.value);
        }
    }

    class RootParserState implements JsonParserState {
        @Override
        public void write(Token t) {
            if (getResult() != null) {
                throw new SyntaxError("unexpected token after the end of stream: "+t.toString());
            }
            switch (t.type) {
                case LeftBrace:
                    callState(new ObjectParserState());
                    break;
                case LeftBracket:
                    callState(new ArrayParserState());
                    break;
                    default:
                        throw new SyntaxError("unexpected token: "+t.toString());
            }
        }

        @Override
        public void close() {

        }

        @Override
        public void setResult(Object result) {
            JsonParser.this.setResult(result);
        }
    }
}
