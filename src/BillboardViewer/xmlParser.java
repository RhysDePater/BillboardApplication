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

            //get message data
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile("/billboard/message");
            Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
            message = node.getTextContent();
            System.out.println("Message: " + message);

            expression = xPath.compile("/billboard/picture[@url]");
            node = (Node) expression.evaluate(document, XPathConstants.NODE);
            picture = node.getAttributes().getNamedItem("url").getNodeValue();
            System.out.println("picture: " + picture );

            expression = xPath.compile("/billboard/information");
            node = (Node) expression.evaluate(document, XPathConstants.NODE);
            information = node.getTextContent();
            System.out.println("Information: " + information);
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | XPathExpressionException exp) {
            exp.printStackTrace();
        }

        return (new String[]{message, picture, information});
    }
}
