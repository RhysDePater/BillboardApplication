package BillboardServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadFromNetworkPropsFile {
    private static int portNum;
    // Method for reading the port number from the network.props file.
    public static int readNetworkProps() {
        Properties props = new Properties();
        FileInputStream input = null;

        try {
            input = new FileInputStream("./network.props");
            props.load(input);
            input.close();

            String portNumber = props.getProperty("port");
            int typeCastedPortNum = Integer.parseInt(portNumber);
            //Testing typecasted port number.
            System.out.println("Testing typcasted port number:" + typeCastedPortNum);
            portNum = typeCastedPortNum; // Assigns the typecasted port number

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return portNum;
    }
}
