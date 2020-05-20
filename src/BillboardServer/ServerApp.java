package BillboardServer;
import java.io.IOException;

public class ServerApp {
    // TODO
    // handle the exception properly
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Networking network = new Networking(ReadNetworkProps.getPort());
        network.Listen();
    }
}
