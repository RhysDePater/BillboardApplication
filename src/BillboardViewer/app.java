package BillboardViewer;

import BillboardViewer.mainView;
import BillboardViewer.xmlParser;

import javax.swing.*;
import java.io.FileNotFoundException;

public class app {
    public static void main(String[] args) {
//        String xmlText = "<billboard>\n" +
//                "<message>Billboard with message, GIF and information</message>\n" +
//                "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download\"/>\n" +
//                "<information>\n" +
//                "This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.\n" +
//                "</information>\n" +
//                "</billboard>";
//
//        String[] elements = xmlParser.parseXML(xmlText);
//        System.out.println(elements[0]);

        SwingUtilities.invokeLater(new mainView("Exercise 1"));
    }
}
