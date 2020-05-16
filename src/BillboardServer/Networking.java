package BillboardServer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/**
 * Does networking
 */

public class Networking {
    static ServerSocket NetworkingSocket;
    static Socket InitialisedSocket;
    public Networking(int port) throws IOException {
        try {
            NetworkingSocket = new ServerSocket(port); // Make the port dynamic
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void Listen() throws IOException {
        for (; ; ) {
            // stuff
            try{
                InitialisedSocket = NetworkingSocket.accept(); // Accept the incoming connection
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            InputStream MyInputStream = InitialisedSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(MyInputStream);
            System.out.println("byte read:" + ois.readUTF());
            InitialisedSocket.close();
        }
    }
}
