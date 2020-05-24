package BillboardViewer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;


public class xmlParser {

    public static String[] parseXML(String xmlString)  {
        final String filePath = "./src/BillboardViewer/xmlFiles/xmlFile.xml";

        String message = "";
        String picture  ="";
        String encodedPicture ="";
        String information="";
        String backGroundColour = "";
        String messageColour = "";
        String infoColour = "";

        writeXMLFile(xmlString, filePath);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(filePath));

            try {
                Node node = helper.xmlNode("/billboard[@background]", document);
                backGroundColour = node.getAttributes().getNamedItem("background").getNodeValue();
                System.out.println("background: " + backGroundColour);
            }
            catch(Exception e)
            {
                backGroundColour="#FFFFFF";
            }

            //get message data
            try {
                Node node = helper.xmlNode("/billboard/message", document);
                message = node.getTextContent();
                System.out.println("Message: " + message);
            }
            catch(Exception e)
            {
                message="";
            }

            try {
                Node node = helper.xmlNode("/billboard/message[@colour]", document);
                messageColour = node.getAttributes().getNamedItem("colour").getNodeValue();
                System.out.println("message colour: " + messageColour);
            }
            catch(Exception e)
            {
                messageColour="#000000";
            }

            try {
                Node node = helper.xmlNode("/billboard/picture[@url]", document);
                picture = node.getAttributes().getNamedItem("url").getNodeValue();
                System.out.println("picture: " + picture);
            }
            catch(Exception e)
            {
                picture="";
            }

            try {
                Node node = helper.xmlNode("/billboard/picture[@data]", document);
                encodedPicture = node.getAttributes().getNamedItem("data").getNodeValue();
                System.out.println("picture: " + encodedPicture);
            }
            catch(Exception e)
            {
                encodedPicture ="";
            }

            try {
                Node node = helper.xmlNode("/billboard/information", document);
                information = node.getTextContent();
                System.out.println("Information: " + information);
            }
            catch(Exception e)
            {
                information="";
            }

            try {
                Node node = helper.xmlNode("/billboard/information[@colour]", document);
                infoColour = node.getAttributes().getNamedItem("colour").getNodeValue();
                System.out.println("information colour: " + infoColour);
            }
            catch(Exception e)
            {
                infoColour="#000000";
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException  exp) {
            return (new String[]{message, picture, information, encodedPicture, backGroundColour, messageColour, infoColour});
        }

        return (new String[]{message, picture, information, encodedPicture, backGroundColour, messageColour, infoColour});
    }

    public static void writeXMLFile(String xml, String filePath)
    {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(xml);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
