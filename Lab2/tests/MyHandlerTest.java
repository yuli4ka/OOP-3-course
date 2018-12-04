import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.List;

import static org.junit.Assert.*;

public class MyHandlerTest {
    private MyHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new MyHandler();
    }

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testGetCandyList() {
        Candy candy;
        List<Candy> candyList = null;
        assertEquals(candyList, handler.getCandyList());
    }

    @Test
    public void testStartElement() throws SAXException {
        handler.startElement("", "", "Candy", null);
        handler.startElement("", "", "value", null);
    }

    @Test
    public void testEndElements() throws SAXException {
        //handler.endElement("", "", "Candy");
    }

    @Test (expected = NullPointerException.class)
    public void testCharactersEnergy() throws SAXException {
        handler.startElement("", "", "energy", null);
        char[] ch = new char[1];
        ch[0] = '8';
        handler.characters(ch, 0, 1);
//        handler.startElement("", "", "Candy", null);
//        handler.startElement("", "", "value", null);
    }

    @Test (expected = NullPointerException.class)
    public void testCharactersProduction() throws SAXException {
        handler.startElement("", "", "production", null);
        char[] ch = new char[1];
        ch[0] = '8';
        handler.characters(ch, 0, 1);
    }

    @Test (expected = NullPointerException.class)
    public void testCharactersType() throws SAXException {
        handler.startElement("", "", "type", null);
        char[] ch = new char[1];
        ch[0] = '8';
        handler.characters(ch, 0, 1);
    }

    @Test (expected = NullPointerException.class)
    public void testCharactersIngredients() throws SAXException {
        handler.startElement("", "", "ingredients", null);
        char[] ch = new char[1];
        ch[0] = '8';
        handler.characters(ch, 0, 1);
    }

    @Test (expected = NullPointerException.class)
    public void testCharactersName() throws SAXException {
        handler.startElement("", "", "name", null);
        char[] ch = new char[1];
        ch[0] = '8';
        handler.characters(ch, 0, 1);
    }
}