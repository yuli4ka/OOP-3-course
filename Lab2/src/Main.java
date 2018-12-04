import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {

    public static List<Candy> saxParser(String filePath) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Candy> candyList = new ArrayList<>();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(new File(filePath), handler);
            candyList = handler.getCandyList();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return candyList;
    }

    public static void main(String[] args) {
        List<Candy> candyList;

        System.out.println("        --------------SAX PARSER--------------\n");
        candyList = saxParser("candies.xml");
        for(Candy candy : candyList)
            System.out.println(candy);

        System.out.println("\n\n\n        --------------StAX PARSER--------------\n");
        String fileName = "candies.xml";
        candyList = parseXML(fileName);
        for(Candy candy : candyList){
            System.out.println(candy.toString());
        }

        System.out.println("\n\n\n        --------------DOM PARSER--------------\n");
        candyList = domParser("candies.xml");
        for(Candy candy : candyList){
            System.out.println(candy.toString());
        }
    }

    public static List<Candy> domParser(String filePath){
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        List<Candy> candyList = new ArrayList<>();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Candy");

            for (int i = 0; i < nodeList.getLength(); i++) {
                candyList.add(getCandy(nodeList.item(i)));
            }

        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
        return candyList;
    }


    public static Candy getCandy(Node node) {
        Candy Cand = new Candy();
        Cand.setId(Integer.parseInt(node.getAttributes().item(0).getNodeValue()));
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            ArrayList<String> types;
            // name
            types = getTagValues("name", element);
            for (String type: types)
                Cand.setName(type);
            // energy
            types = getTagValues("energy", element);
            for (String type: types)
                Cand.setEnergy(Integer.parseInt(type));
            // type
            types = getTagValues("type", element);
            for (String type: types)
                Cand.setType(type);
            // production
            types = getTagValues("production", element);
            for (String type: types)
                Cand.setProduction(type);
            // ingredients
            types = getTagValues("ingredients", element);
            for (String type: types)
                Cand.setIngredients(type);
            // value
            types = getTagValues("value", element);
            Cand.setValueProtein(Integer.parseInt(types.get(0)));
            Cand.setValueFats(Integer.parseInt(types.get(1)));
            Cand.setValueCarbohydrates(Integer.parseInt(types.get(2)));
        }

        return Cand;
    }

    public static ArrayList<String> getTagValues(String tag, Element element) {
        ArrayList<String> types = new ArrayList<>();
        int num = element.getElementsByTagName(tag).getLength();

        if (tag.equals("value")) {
            types.add("0");
            types.add("0");
            types.add("0");
            for (int i = 0; i < num; i++) {
                NodeList nodeList = element.getElementsByTagName(tag).item(i).getChildNodes();
                Node node = (Node) nodeList.item(0);
                if (element.getElementsByTagName(tag).item(i).getAttributes().item(0).getNodeValue().equals("protein"))
                    types.set(0, node.getNodeValue());
                else if (element.getElementsByTagName(tag).item(i).getAttributes().item(0).getNodeValue().equals("fats"))
                    types.set(1, node.getNodeValue());
                else if (element.getElementsByTagName(tag).item(i).getAttributes().item(0).getNodeValue().equals("carbohydrates"))
                    types.set(2, node.getNodeValue());
            }
            return types;
        }

        for (int i = 0; i < num; i++) {
            NodeList nodeList = element.getElementsByTagName(tag).item(i).getChildNodes();
            Node node = (Node) nodeList.item(0);
            String tt = node.getNodeValue();
            int numAtr = element.getElementsByTagName(tag).item(i).getAttributes().getLength();
            for (int j = 0; j < numAtr; j++){
                tt += ("(" + element.getElementsByTagName(tag).item(i).getAttributes().item(j) + ")");
            }
            types.add(tt);
        }
        return types;
    }


    //------------------StAX----------------
    public static List<Candy> parseXML(String fileName) {
        List<Candy> candyList = new ArrayList<>();
        Candy Cand = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("Candy")){
                        Cand = new Candy();
                        //Get the 'id' attribute from Employee element
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        if(idAttr != null){
                            Cand.setId(Integer.parseInt(idAttr.getValue()));
                        }
                    }
                    //set the other varibles from xml elements
                    else if(startElement.getName().getLocalPart().equals("energy")){
                        xmlEvent = xmlEventReader.nextEvent();
                        Cand.setEnergy(Integer.parseInt(xmlEvent.asCharacters().getData()));
                    }else if(startElement.getName().getLocalPart().equals("name")){
                        xmlEvent = xmlEventReader.nextEvent();
                        Cand.setName(xmlEvent.asCharacters().getData());
                    }else if(startElement.getName().getLocalPart().equals("type")){
                        xmlEvent = xmlEventReader.nextEvent();
                        Cand.setType(xmlEvent.asCharacters().getData());
                    }else if(startElement.getName().getLocalPart().equals("production")){
                        xmlEvent = xmlEventReader.nextEvent();
                        Cand.setProduction(xmlEvent.asCharacters().getData());
                    }else if(startElement.getName().getLocalPart().equals("ingredients")){
                        xmlEvent = xmlEventReader.nextEvent();
                        String ingredients = xmlEvent.asCharacters().getData();
                        Iterator attributes = startElement.getAttributes();
                        boolean flag = true;
                        while (attributes.hasNext()) {
                            flag = false;
                            Attribute attribute = (javax.xml.stream.events.Attribute) (attributes.next());
                            String attributeSt = attribute.getValue();
                            if (ingredients.equals("chocolate") && attribute.getName().equals(new QName("chocolateType")))
                                Cand.setIngredients(ingredients+"(type: " + attributeSt + ")");
                            else if (ingredients.equals("chocolate") && attribute.getName().equals(new QName("mg")))
                                Cand.setIngredients(ingredients+"(" + attributeSt + "mg)");
                            else if (attribute.getName().equals(new QName("mg")))
                                Cand.setIngredients(ingredients+"(" + attributeSt + "mg)");
                        }
                        if (flag)
                            Cand.setIngredients(ingredients);
                    }else if(startElement.getName().getLocalPart().equals("value")){
                        xmlEvent = xmlEventReader.nextEvent();
                        String ingredients = xmlEvent.asCharacters().getData();
                        Iterator attributes = startElement.getAttributes();
                        Attribute attribute = (javax.xml.stream.events.Attribute) (attributes.next());
                        if (attribute.getValue().equals("carbohydrates"))
                            Cand.setValueCarbohydrates(Integer.parseInt(ingredients));
                        else if (attribute.getValue().equals("fats"))
                            Cand.setValueFats(Integer.parseInt(ingredients));
                        else if (attribute.getValue().equals("protein"))
                            Cand.setValueProtein(Integer.parseInt(ingredients));
                    }
                }
                //if Employee end element is reached, add employee object to list
                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();
                    if(endElement.getName().getLocalPart().equals("Candy")){
                        candyList.add(Cand);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return candyList;
    }

}
