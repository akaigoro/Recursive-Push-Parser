package org.df4j.workflow;

import org.df4j.core.workflow.IJsonParser;
import org.df4j.core.workflow.JsonParser;
import org.df4j.core.workflow.JsonScanner;
import org.df4j.core.workflow.token.Token;
import org.junit.Test;

import java.util.ArrayList;

public class JsonScannerTest {
    ArrayList<Token> tokens  = new ArrayList<>();

    IJsonParser parser = new IJsonParser() {

        @Override
        public void write(Token t) {
            tokens.add(t);
        }
    };
    JsonScanner scanner = new JsonScanner(parser);

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
