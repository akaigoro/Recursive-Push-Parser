package org.df4j.pushparser.json;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class JsonParserTest {
    JsonParser parser = new JsonParser();
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
        Object res = parser.getResult();
        Assert.assertTrue(res instanceof ArrayList);
        ArrayList array = (ArrayList)res;
        Assert.assertEquals(0, array.size());
    }

    @Test
    public void arrayTest1() {
        toScanner("[null]");
        Object res = parser.getResult();
        Assert.assertTrue(res instanceof ArrayList);
        ArrayList array = (ArrayList)res;
        Assert.assertEquals(1, array.size());
        Assert.assertNull(array.get(0));
    }

    @Test
    public void objectTest() {
        toScanner("{ \"abc\":true, \"2\":\"false\", \"123\": 123}");
        Object res = parser.getResult();
        Assert.assertTrue(res instanceof Map);
        Map map = (Map)res;
        Assert.assertEquals(3, map.size());
        Object v2 = map.get("2");
        Assert.assertEquals("false", v2);
    }

}
