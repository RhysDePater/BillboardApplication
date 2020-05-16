package BillboardServer;
// To do
// Copy code to read from props file
// Copy/Create code to listen on a port
// Connect to database
// Get information being sent to port
// Send that information to the database
// Get the response from the database
// Send it back to the control panel

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException { // handle the exception properly

        Networking network = new Networking(12345);
        network.Listen();
    }
}
