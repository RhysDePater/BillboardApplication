package BillboardServer;
import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.ReadNetworkProps;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServerApp {
    // TODO
    // handle the exception properly
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        Networking network = new Networking(ReadNetworkProps.getPort());
        DBInteract.createDatabaseTables();
        network.Listen();
    }
}
