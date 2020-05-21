package BillboardControlPanel.UnitTest;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLDataException;   //Don't remove required for the commented tests below
import java.sql.SQLException;       //Don't remove required for the commented tests below
import static BillboardControlPanel.Model.DBInteract.*;
import static BillboardControlPanel.Model.XMLParsing.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Data Parsing related tests
 * Firstly testing the getXMLData() to get XML_data as a string for parsing input. Requiring a local database,
 * because dbQueryCommand queries a database; just hardcoded SQL commands isn't enough.
 * Second, tests for parsing string xml_data into two dimensional array.
 */
public class XMLParsingTests {
    static ResultSet rs;
    private final String expectedStr = "SELECT xml_data FROM billboard WHERE id = '1';";

    private final String QueryOnExisting = selectTarget("billboard", "xml_data", "id", "1");
    private final String QueryOnNonExisting = selectTarget("billboard", "xml_data", "id", "0");

    private final String msgOnly = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><billboard><message>Basic message-only billboard</message></billboard>";
    private final String urlPicOnly = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<billboard><picture url=\"https://cloudstor.aarnet.edu.au/plus/s/62e0uExNviPanZE/download\" /></billboard>";
    private final String all3Coloured = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#555555\">\n" +
            "    <message colour=\"#FFFFFF\">The information text is always smaller than the message text</message>\n" +
            "    <information colour=\"#DDDDDD\">The information text is always smaller than the message text</information>\n" +
            "</billboard>";
    private final String mixture = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <message>Default-coloured message</message>\n" +
            "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/X79GyWIbLEWG4Us/download\" />\n" +
            "    <information colour=\"#60B9FF\">Custom-coloured information text</information>\n" +
            "</billboard>";
    private final String mixtureSameLine ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><billboard><message>Default-coloured message</message><picture url=\"https://cloudstor.aarnet.edu.au/plus/s/X79GyWIbLEWG4Us/download\" /><information colour=\"#60B9FF\">Custom-coloured information text</information></billboard>";


    private final String msgXMLFile = "/Users/Ray/Desktop/CAB302/Practicals/CAB302Assignment/XMLs/1.xml";
    private final String nonFile = "/Users/Ray/Desktop/CAB302/Practicals/CAB302Assignment/XMLs/nothing.xml";
    private final String noSpec = "/Users/Ray/Desktop/CAB302/Practicals/CAB302Assignment/XMLs/";

    private final String newFileDir = "/Users/Ray/Desktop/CAB302/Practicals/CAB302Assignment/XMLs";
    private final String newFileName = "new2";
    private final String emptyStr = "";
    //Tests string format of SQL command from retrieving xml_data as a string
    @Test
    public void strFormat() {
        String actualStr = selectTarget("billboard", "xml_data", "id", "1");
        assertEquals(expectedStr, actualStr);
    }

    //Uncomment the following commented tests if connected to a local database from testing.
    //Testing to make getXMLData() using existing billboard ID
//    @Test
//    public void shouldReturn() throws SQLException {
//        rs = dbQueryCommand(QueryOnExisting);
//        rs.next();
//        String xmlstr = rs.getString("xml_data");
//        //System.out.println(xmlstr);
//        rs.close();
//    }
//
//    @Test
//    //Testing to make getXMLData() handle non-existing billboard ID
//    public void shouldReturnErr() throws SQLException {
//        try {
//            rs = dbQueryCommand(QueryOnNonExisting);
//            rs.next();
//            String xmlstr = rs.getString("xml_data");
//            //System.out.println(xmlstr);
//            rs.close();
//        } catch (SQLDataException e) {
//            //System.out.println("This billboard ID doesn't exist");
//        }
//    }
//
//    @Test
//    //Testing the getXMLData function using ID 1 and 0(doesn't exist)
//    public void billboardID1() throws SQLException {
//        getXMLData("1");
//    }
//
//    @Test
//    public void billboardID0() throws SQLException {
//        getXMLData("0");
//    }
//
//    //Testing -1 ID, that shouldn't exist as long as billboard creation doesn't allow negative numbers.
//    @Test
//    public void billboardIDNeg1() throws SQLException {
//        getXMLData("-1");
//    }

    //Parsing tests from String into String[][]
    @Test
    public void parseMsgOnly() throws ParserConfigurationException {
        StrToXMLArray(msgOnly);
    }
    //This can be handled by wrapping method in an if-else statement, check whether is string is populate or not.
    @Test
    public void noString() throws ParserConfigurationException {
            StrToXMLArray("");
    }
    @Test
    public void parseURLOnly() throws ParserConfigurationException {
        StrToXMLArray(urlPicOnly);
    }

    //The below two tests only passes if reformatted with Regex first.
    @Test
    public void colouredOnly() throws ParserConfigurationException {
        StrToXMLArray(all3Coloured);
    }
    @Test
    public void Mixture() throws ParserConfigurationException {
        StrToXMLArray(mixture);
    }

    //Below test works only if the parsed string is formatted on one line.
    @Test
    public void MixtureSameLine() throws ParserConfigurationException {
        StrToXMLArray(mixtureSameLine);
    }

    //Test for converting input XML file to XML string for use in StrToXMLArray() mainly for Importing.
    //This test can be used to form the function XMLFileToStr();
    @Test
    public void shouldRtnStr(){
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();

            File xmlFile = new File(msgXMLFile);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            //preset the standalone to true, prevents it from appearing in the results
            doc.setXmlStandalone(true);

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String xmlString = writer.getBuffer().toString();
            String reformatted = xmlString.replaceAll(">\\s*<", "><");
            assertEquals(msgOnly, reformatted);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //Tests the made function from above, but without reformatting the XML string as that's done by StrToXMLArray().
    @Test
    public void shouldRtnStr1(){
        String xmlStr = XMLFileToString(msgXMLFile);
        System.out.println(xmlStr);
    }
    //Passing in non-existent file.
    @Test
    public void shouldRtnErr() {
        String xmlStr = XMLFileToString(nonFile);
    }
    //Not specifying the file or not giving any directory.
    @Test
    public void noFileHere(){
        String xmlStr = XMLFileToString(noSpec);
    }

    //Test below is for creating XML file from XML string. The test is used to make the StrToXMLFile().
    //This outputs a new XML file to the root of the project folder.
    @Test
    public void newFile() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(msgOnly)));
        FileOutputStream outPut = new FileOutputStream(new File(newFileDir + "/" + newFileName));
        transformer.transform(new DOMSource(doc), new StreamResult(outPut));
    }
    //Below tests are for StrToXMLFile()
    @Test
    public void emptyStrTests(){

        if (emptyStr != ""){
            StrToXMLFile(emptyStr, newFileDir, "name");
        }
        else{
            System.out.println("Can't parse an empty String. This needs to be handled in the GUI.");
        }
    }
    //The test below directs the FileOutPut to "/" + "nam.xml", removing the slash in the method
    // creates the file in the root project directory. Ensure directory is selected in GUI.
    @Test
    public void noSuchDir(){
        StrToXMLFile(msgOnly, "", "name");
    }

    //Test below creates a xml file named .xml. This needs to be managed in the GUI.
    @Test
    public void noName(){
        StrToXMLFile(msgOnly, newFileDir, emptyStr);
    }
}