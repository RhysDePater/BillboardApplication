package BillboardControlPanel.UnitTest;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
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

    //Tests string format of SQL command from retrieving xml_data as a string
    @Test
    public void strFormat() {
        String actualStr = selectTarget("billboard", "xml_data", "id", "1");
        assertEquals(expectedStr, actualStr);
    }

    //Testing to make getXMLData() using existing billboard ID
    @Test
    public void shouldReturn() throws SQLException {
        rs = dbQueryCommand(QueryOnExisting);
        rs.next();
        String xmlstr = rs.getString("xml_data");
        //System.out.println(xmlstr);
        rs.close();
    }

    @Test
    //Testing to make getXMLData() handle non-existing billboard ID
    public void shouldReturnErr() throws SQLException {
        try {
            rs = dbQueryCommand(QueryOnNonExisting);
            rs.next();
            String xmlstr = rs.getString("xml_data");
            //System.out.println(xmlstr);
            rs.close();
        } catch (SQLDataException e) {
            //System.out.println("This billboard ID doesn't exist");
        }
    }

    @Test
    //Testing the getXMLData function using ID 1 and 0(doesn't exist)
    public void billboardID1() throws SQLException {
        getXMLData("1");
    }

    @Test
    public void billboardID0() throws SQLException {
        getXMLData("0");
    }

    //Testing -1 ID, that shouldn't exist as long as billboard creation doesn't allow negative numbers.
    @Test
    public void billboardIDNeg1() throws SQLException {
        getXMLData("-1");
    }

    //Parsing tests from String into String[][]
    @Test
    public void parseMsgOnly() throws ParserConfigurationException {
        StrToXMLArray(msgOnly);
    }
//    @Test
//    public void noString() throws ParserConfigurationException, SAXParseException {
//            StrToXMLArray("");
//    }
    @Test
    public void parseURLOnly() throws ParserConfigurationException {
        StrToXMLArray(urlPicOnly);
    }

    //The below two tests, fails to read properly as they're on separated lines. Have to reconfigure parser.
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
}