import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {

    //List to hold Employees object
    private List<Candy> candyList = null;
    private Candy candy = null;


    //getter method for employee list
    public List<Candy> getCandyList() {
        return candyList;
    }

    private boolean bEnergy = false;
    private boolean bName = false;
    private boolean bType = false;
    private boolean bIngredients = false;
    private boolean bProduction =  false;
    private boolean bCarbohydrates = false;
    private boolean bProtein = false;
    private boolean bFats = false;
    private String mg = "";
    private String chocolateType = "";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("Candy")) {
            //create a new Candy and put it in Map
            String id = "0";
            if (attributes != null)
                id = attributes.getValue("id");
            //initialize Candy object and set id attribute
            candy = new Candy();
            candy.setId(Integer.parseInt(id));

            //initialize list
            if (candyList == null)
                candyList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("name")) {
            //set boolean values for fields, will be used in setting Candy variables
            bName = true;
        } else if (qName.equalsIgnoreCase("energy")) {
            bEnergy = true;
        } else if (qName.equalsIgnoreCase("type")) {
            bType = true;
        } else if (qName.equalsIgnoreCase("ingredients")) {
            bIngredients = true;
            if (attributes != null)
                mg = attributes.getValue("mg");
            if (attributes != null)
                chocolateType = attributes.getValue("chocolateType");
        } else if (qName.equalsIgnoreCase("production")) {
            bProduction = true;
        } else if (qName.equalsIgnoreCase("value")) {
            String typeValue = "";
            if (attributes != null)
                typeValue = attributes.getValue("type");
            if (typeValue.equals("protein"))
                bProtein = true;
            else if (typeValue.equals("fats"))
                bFats = true;
            else if (typeValue.equals("carbohydrates"))
                bCarbohydrates = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Candy")) {
            //add Candy object to list
            candyList.add(candy);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bEnergy) {
            candy.setEnergy(Integer.parseInt(new String(ch, start, length)));
            bEnergy = false;
        } else if (bName) {
            candy.setName(new String(ch, start, length));
            bName = false;
        } else if (bIngredients) {
            if (mg != null)
                candy.setIngredients(new String(ch, start, length) + "(" + mg + "mg)");
            else if (chocolateType != null)
                candy.setIngredients(new String(ch, start, length) + "(type: " + chocolateType + ")");
            else
                candy.setIngredients(new String(ch, start, length));
            bIngredients = false;
        } else if (bType) {
            candy.setType(new String(ch, start, length));
            bType = false;
        } else if (bProduction) {
            candy.setProduction(new String(ch, start, length));
            bProduction = false;
        } else if (bCarbohydrates) {
            candy.setValueCarbohydrates(Integer.parseInt(new String(ch, start, length)));
            bCarbohydrates = false;
        } else if (bFats) {
        candy.setValueFats(Integer.parseInt(new String(ch, start, length)));
            bFats = false;
        } else if (bProtein) {
            candy.setValueProtein(Integer.parseInt(new String(ch, start, length)));
            bProtein = false;
        }
    }
}
