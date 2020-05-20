package BillboardControlPanel.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import static BillboardControlPanel.Model.DBInteract.*;

public class XMLParsing {
    /**
     * Queries server specifically for the XML_data and storing into a string for parsing.
     * @param billboard_id stored on the server in the billboard table, and is unique.
     * @return xmlstr, string containing queried XML data
     * @throws SQLException
     */
    public static String getXMLData(String billboard_id) throws SQLException {
        String xmlstr = "";
        try {
            ResultSet rs = dbQueryCommand(selectTarget("billboard", "xml_data", "id", billboard_id));
            rs.next();
            xmlstr = rs.getString("xml_data");
            //System.out.println(xmlstr);
            rs.close();
            //return xmlstr;
        } catch(SQLDataException e){
            System.out.println("This billboard ID doesn't exist.");
        }
        //System.out.println(xmlstr);
        return xmlstr;
    }

    /**
     *
     * @param xmlString The input xml_data from server string to extract xml components
     * @return A String[][] containing the XML components
     * @throws ParserConfigurationException
     */
    public static String[][] StrToXMLArray(String xmlString) throws ParserConfigurationException {
        String ar[][] = new String[4][3];
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        try{
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            Node rootNode = doc.getFirstChild();
            ar[0][0] = rootNode.getNodeName();
            if(rootNode.getAttributes().getNamedItem("background") == null){
                ar[0][1] = null;
            }
            else{
                ar[0][1] = rootNode.getAttributes().getNamedItem("background").getNodeValue();
            }
            NodeList xmlComponents = rootNode.getChildNodes();
            for (int i = 0; i < xmlComponents.getLength(); i++){
                String tag = xmlComponents.item(i).getNodeName();
                if (tag == "message"){
                    ar[1][0] = tag;
                    ar[1][1] = xmlComponents.item(i).getTextContent();
                    if(xmlComponents.item(i).getAttributes().getNamedItem("colour") == null){
                        ar[1][2] = null;
                    }
                    else{
                        ar[1][2] = xmlComponents.item(i).getAttributes().getNamedItem("colour").getNodeValue();;
                    }
                }
                else if (tag == "picture"){
                    ar[2][0] = tag;
                    ar[2][1] = xmlComponents.item(i).getAttributes().getNamedItem("url").getNodeValue();
                    //System.out.print(tag + " = " + picture);
                }
                else if (tag == "information"){
                    ar[3][0] = tag;
                    ar[3][1] = xmlComponents.item(i).getTextContent();
                    if(xmlComponents.item(i).getAttributes().getNamedItem("colour") == null){
                        ar[3][2] = null;
                    }
                    else{
                        ar[3][2] = xmlComponents.item(i).getAttributes().getNamedItem("colour").getNodeValue();;
                    }
                }
                else
                {
                    System.out.print("XML component " + tag +  " not recognised.");
                }
            }
        }catch (IOException | SAXException e){
            e.printStackTrace();
        }
        return ar;
    }
}

