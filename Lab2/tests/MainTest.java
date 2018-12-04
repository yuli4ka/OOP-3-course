import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    private Main myMain;

    @Before
    public void setUp() throws Exception {
        myMain = new Main();
    }

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testStAXParser() {
        String fileName = "candiTest.xml";
        List<Candy> candyList = myMain.parseXML(fileName);
        assertEquals("Candy:: ID: " + "30"+";\n        Name: " + "name" +
                ";\n        Type: " + "type" + ";\n        Energy: " + "123" +
                "kkal;\n        Ingredients: " + "water" + ";\n        Production: "
                + "prod" +
                ";\n        Value:\n            Carbohydrates: " + "20"
                + "g\n            Protein: " + "45"
                + "g\n            Fats: " + "32" + "g\n", candyList.get(0).toString());
    }

    @Test
    public void testSAXParser() {
        String fileName = "candiTest.xml";
        List<Candy> candyList = myMain.saxParser(fileName);
        assertEquals("Candy:: ID: " + "30"+";\n        Name: " + "name" +
                ";\n        Type: " + "type" + ";\n        Energy: " + "123" +
                "kkal;\n        Ingredients: " + "water" + ";\n        Production: "
                + "prod" +
                ";\n        Value:\n            Carbohydrates: " + "20"
                + "g\n            Protein: " + "45"
                + "g\n            Fats: " + "32" + "g\n", candyList.get(0).toString());
    }

    @Test
    public void testDomParser() {
        String fileName = "candiTest.xml";
        List<Candy> candyList = myMain.domParser(fileName);
        assertEquals("Candy:: ID: " + "30"+";\n        Name: " + "name" +
                ";\n        Type: " + "type" + ";\n        Energy: " + "123" +
                "kkal;\n        Ingredients: " + "water" + ";\n        Production: "
                + "prod" +
                ";\n        Value:\n            Carbohydrates: " + "20"
                + "g\n            Protein: " + "45"
                + "g\n            Fats: " + "32" + "g\n", candyList.get(0).toString());
    }
}