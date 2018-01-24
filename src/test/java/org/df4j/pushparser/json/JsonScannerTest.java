package org.df4j.pushparser.json;

import org.df4j.pushparser.core.TokenSink;
import org.junit.Test;

import java.util.ArrayList;

public class JsonScannerTest {
    ArrayList<Token> tokens  = new ArrayList<>();

    JsonScanner scanner = new JsonScanner(new TokenSink<Token>() {

        @Override
        public void write(Token t) {
            tokens.add(t);
        }

        @Override
        public void close() {

        }
    });

    private void toScanner(String s) {
        for (int k=0; k<s.length(); k++) {
            char c = s.charAt(k);
            scanner.write(c);
        }
        scanner.close();
    }

    @Test
    public void emptyArrayTest() {
        toScanner("[]");
        Object t0 = tokens.get(0);
        Object t1 = tokens.get(1);
    }

    @Test
    public void arrayTest1() {
        toScanner("[null]");
        Object t0 = tokens.get(0);
        Object t1 = tokens.get(1);
    }

    @Test
    public void arrayTest() {
        toScanner("[ \"abc\":true,false, 123,]");
        Object t0 = tokens.get(0);
        Object t1 = tokens.get(1);
    }
}
