package BillboardServer;

import java.io.*;

// This class is used to read from an xml file, and create a String out of it. This is so it can be stored in the database.
public class XMLStringCreator {
    private static String xmlStringFromXmlFile = ""; // The string that stores the content of the xml file.
    public static String createXmlString() throws FileNotFoundException {
        // Variable for holding the contents of the file in the while loop.
        ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
        InputStream in = new BufferedInputStream(new FileInputStream("./14.xml"));
        int lineCount;
        try {
            while ((lineCount = in.read()) != -1)
                out.write(lineCount);
            in.close();

            // Stores the result from the ByteOutputStream called "out" to the string xmlStringFromXmlFile.
            xmlStringFromXmlFile = (new String(out.toByteArray()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return xmlStringFromXmlFile;

    }
}
