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

public class xmlParser {

    public static String[] parseXML(String xmlString)  {
        final String filePath = "./src/BillboardViewer/xmlFiles/xmlFile.xml";

        String message = "";
        String picture  ="";
        String information="";

        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(xmlString);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(filePath));

            XPath xPath = XPathFactory.newInstance().newXPath();

            //get message data
            try {
                XPathExpression expression = xPath.compile("/billboard/message");
                Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
                message = node.getTextContent();
                System.out.println("Message: " + message);
            }
            catch(Exception e)
            {
                message="";
            }

            try {
                XPathExpression expression = xPath.compile("/billboard/picture[@url]");
                Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
                picture = node.getAttributes().getNamedItem("url").getNodeValue();
                System.out.println("picture: " + picture);
            }
            catch(Exception e)
            {
                picture="";
            }

            try {
                XPathExpression expression = xPath.compile("/billboard/information");
                Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
                information = node.getTextContent();
                System.out.println("Information: " + information);
            }
            catch(Exception e)
            {
                information="";
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException  exp) {
            return (new String[]{message, picture, information});
        }

        return (new String[]{message, picture, information});
    }
}
