package BillboardServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads from the network.props file to retrieve the host or port number.
 */

public class ReadNetworkProps {
    private static int portNum;
    private static String host;
    // Method for reading the port number from the network.props file.
    public static int getPort() {
        Properties props = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("./network.props");
            props.load(input);
            input.close();

            String portNumber = props.getProperty("port");
            int typeCastedPortNum = Integer.parseInt(portNumber);
            portNum = typeCastedPortNum; // Assigns the typecasted port number

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return portNum;
    }

    // Reads the value of the host property in network.props. Allows clients to connect to the server.
    public static String getHost(){
        Properties props = new Properties();
        FileInputStream input;

        try {
            input = new FileInputStream("./network.props");
            props.load(input);
            input.close();

            String hostName = props.getProperty("host");
            host = hostName; // Assigns the host to the return String.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return host;
    }
}
