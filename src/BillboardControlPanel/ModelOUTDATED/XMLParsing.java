//package BillboardControlPanel.Model;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.URL;
//import java.sql.ResultSet;
//import java.sql.SQLDataException;
//import java.sql.SQLException;
//import java.util.Base64;
//
//import static BillboardControlPanel.Controller.PreviewerController.xml_data;
//import static BillboardControlPanel.ModelOUTDATED.DBInteract.*;
//
//public class XMLParsing {
//    /**
//     * Queries server specifically for the XML_data and storing into a string for parsing.
//     * @param billboard_id stored on the server in the billboard table, and is unique.
//     * @return xmlstr, string containing queried XML data
//     * @throws SQLException
//     */
//    public static String getXMLData(String billboard_id) throws SQLException {
//        String xmlstr = "";
//        try {
//            //Query database with specific information for the result set
//            ResultSet rs = dbQueryCommand(selectTarget("billboard", "xml_data", "id", billboard_id));
//            //Move cursor forward in result set.
//            rs.next();
//            //Extract the XML data from the result set.
//            xmlstr = rs.getString("xml_data");
//            rs.close();
//        } catch(SQLDataException e){
//            System.out.println("This billboard ID doesn't exist.");
//        }
//        return xmlstr;
//    }
//
//    /**
//     * Receives XML file input, and converts the contents into a string for use in StrToXMLArray()
//     * @param directory the path to the XML file on the local storage.
//     * @return the reformatted string,
//     * This is for the input feature of the GUI.
//     */
//    public static String XMLFileToString(String directory) {
//        String xmlString = "";
//        TransformerFactory tf = TransformerFactory.newInstance();
//        try {
//            //Initiate new transformer instance to
//            Transformer transformer = tf.newTransformer();
//            //Direct to an existing XML file, then initiate the document factory and builder to make the doc.
//            File xmlFile = new File(directory);
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(xmlFile);
//            //preset the standalone to true, prevents it from appearing in the results
//            doc.setXmlStandalone(true);
//            //Initiate new StringWriter() instance to write the document as a String.
//            StringWriter writer = new StringWriter();
//            //Convert document information into a usable format for the StringWriter()
//            transformer.transform(new DOMSource(doc), new StreamResult(writer));
//            xmlString = writer.getBuffer().toString();
//        } catch (IOException | ParserConfigurationException | TransformerException e) {
//            System.out.println("XML file doesn't exist. Check file name.");
//        } catch(SAXException e){
//            System.out.println("No file specified. Check file's directory.");
//        }
//        return xmlString;
//    }
//
//    /**
//     * Function for creating a new XML file from a XML String for the export feature of the GUI.
//     * @param xmlString Input of XML data string. Either from server or created by the user.
//     * @param newFileDir Input of directory for where to create the xml file on the computer.
//     * @param newFileName Input of a name for the new XML file.
//     */
//    public static void StrToXMLFile(String xmlString, String newFileDir, String newFileName){
//        if (xmlString != "") {
//            try {
//                //Initiate transformer to convert doc into a StreamResult format.
//                TransformerFactory tf = TransformerFactory.newInstance();
//                Transformer transformer = tf.newTransformer();
//                //Initiate the factory and builder for the XML document to be created from the xml String.
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                //Create xml doc from xml string.
//                Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
//                //Create the XML file, using provided path and name.
//                FileOutputStream outPut = new FileOutputStream(new File(newFileDir + "/" + newFileName + ".xml"));
//                //Make the content to be placed into the new file from the created xml document.
//                transformer.transform(new DOMSource(doc), new StreamResult(outPut));
//            } catch (TransformerException | ParserConfigurationException | SAXException e) {
//                System.out.println("Provide the XML String.");
//            } catch (FileNotFoundException e) {
//                System.out.println("Provide both directory and name for new XML file.");
//            } catch (IOException e) {
//                System.out.println("Document failed to build. Check format.");
//            }
//        }
//        else{
//            System.out.println("Can't parse an empty String. This needs to be handled in the GUI.");
//        }
//    }
//
//    /**
//     *
//     * @param xmlString The input xml_data from server string to extract xml components
//     * @return A String[][] containing the XML components
//     * @throws ParserConfigurationException
//     */
//    public static String[][] StrToXMLArray(String xmlString) throws ParserConfigurationException {
//        String ar[][] = new String[4][3];
//        //Regex used to remove whitespace characters, specifically newlines in this case.
//        //factory.setIgnoringElementContentWhitespace(true); doesn't work here without a DOCType specified.
//        String reformatted = xmlString.replaceAll(">\\s*<", "><");
//        //Create the document builder
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        if (reformatted != "") {
//            try {
//                //Create the document by inputting the string
//                Document doc = builder.parse(new InputSource(new StringReader(reformatted)));
//                //Get the root node and extract its information to the array
//                Node rootNode = doc.getFirstChild();
//                ar[0][0] = rootNode.getNodeName();
//                if (rootNode.getAttributes().getNamedItem("background") == null) {
//                    ar[0][1] = null;
//                } else {
//                    ar[0][1] = rootNode.getAttributes().getNamedItem("background").getNodeValue();
//                }
//                //Get the root node's child nodes and extract their information to array
//                NodeList xmlComponents = rootNode.getChildNodes();
//                for (int i = 0; i < xmlComponents.getLength(); i++) {
//                    String tag = xmlComponents.item(i).getNodeName();
//                    if (tag == "message") {
//                        ar[1][0] = tag;
//                        ar[1][1] = xmlComponents.item(i).getTextContent();
//                        if (xmlComponents.item(i).getAttributes().getNamedItem("colour") == null) {
//                            ar[1][2] = null;
//                        } else {
//                            ar[1][2] = xmlComponents.item(i).getAttributes().getNamedItem("colour").getNodeValue();
//                        }
//                    } else if (tag == "picture") {
//                        ar[2][0] = tag;
//                        if (xmlComponents.item(i).getAttributes().getNamedItem("data") == null){
//                            ar[2][1] = xmlComponents.item(i).getAttributes().getNamedItem("url").getNodeValue();
//                        }
//                        else if (xmlComponents.item(i).getAttributes().getNamedItem("url") == null){
//                            ar[2][1] = xmlComponents.item(i).getAttributes().getNamedItem("data").getNodeValue();
//                        }
//                    } else if (tag == "information") {
//                        ar[3][0] = tag;
//                        ar[3][1] = xmlComponents.item(i).getTextContent();
//                        if (xmlComponents.item(i).getAttributes().getNamedItem("colour") == null) {
//                            ar[3][2] = null;
//                        } else {
//                            ar[3][2] = xmlComponents.item(i).getAttributes().getNamedItem("colour").getNodeValue();
//                        }
//                    } else {
//                        System.out.print("XML component " + tag + " not recognised.");
//                    }
//                }
//            } catch (IOException | SAXException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            System.out.println("Can't parse an empty string.");
//        }
////        for (int i = 0; i < 4; i++) {
////            for (int r = 0; r < 3; r++) {
////                System.out.println(ar[i][r]);
////            }
////        }
//        return ar;
//    }
//
//    public static BufferedImage Base64Handler(JFrame frame, String[][] arr){
//        String encodedImg = arr[2][1];
//        BufferedImage Img = null;
//        try{
//            byte[] decodedImg = Base64.getDecoder().decode(encodedImg);
//            Img = ImageIO.read(new ByteArrayInputStream(decodedImg));
//        } catch(IllegalArgumentException | IOException e){
//            JOptionPane.showMessageDialog(frame, "Please input picture as a URL or data encoded in base64.");
//        }
//        return Img;
//    }
//    public static BufferedImage UrlHandler(JFrame frame, String[][] arr){
//        String URL = arr[2][1];
//        BufferedImage Img = null;
//        try{
//            URL xmlURL = new URL(URL);
//            Img = ImageIO.read(xmlURL);
//        } catch(IllegalArgumentException | IOException e){
//            JOptionPane.showMessageDialog(frame, "Please input picture as a URL or data encoded in base64.");
//        }
//        return Img;
//    }
//}
//
