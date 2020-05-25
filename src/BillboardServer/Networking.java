package BillboardServer;

import BillboardServer.Database.DBInteract;
import BillboardServer.ServerLogic.ServerLogic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;


/**
 * The Networking constructor sets up the Socket
 * Listen is an infinite for loop which accepts incoming connections and messages as an ObjectInputStream.
 * The server expects a String array which is {Command, param1, param2, param3, ...}
 * Such as: {createUser, username, password(hashed), createbillboard, editbillboard, schedulebillboard, edituser}
 */
public class Networking {
    static ServerSocket NetworkingSocket;
    static Socket InitialisedSocket;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    static int port_number;

    public Networking(int port) {
        port_number = port;
        try {
            NetworkingSocket = new ServerSocket(port);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void Listen() throws IOException, ClassNotFoundException {
        System.out.println("Starting listening on port: " + port_number );
        for (;;) {
            try{
                InitialisedSocket = NetworkingSocket.accept(); // Accept the incoming connection
                System.out.println("New connection from: " + InitialisedSocket.getInetAddress());
                System.out.println("--------------------------------------------------------");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            ois = new ObjectInputStream(InitialisedSocket.getInputStream());
            oos = new ObjectOutputStream(InitialisedSocket.getOutputStream());
            Object data = ois.readObject();
            System.out.println("Incoming data is: " + data.getClass());
            ServerLogic.Parse(data);
            // I don't know if these need to be closed every time
            ois.close();
            oos.close();
            InitialisedSocket.close();
        }
    }

    /**
     * Sends a response back to the client in the form {"true/false (succeeded or failed)", "A result set or string if appropriate", "Optional message that could be displayed to the user/debugging purposes"}
     * status is the boolean describing the state the database operation
     * @param status If the command failed or not
     * @param responseData If the command was a get, such as get billboard content, then this will be the response
     * @param optionalMessage Optional message for debugging/ to be shown to the user
     */
    public static void SendBackData(boolean status, String responseData, String optionalMessage){
        String[] ServerResponse = {String.valueOf(status), responseData, optionalMessage};
        try{
            oos.writeObject(ServerResponse);
            oos.flush();
        }
        catch( Exception e){
            System.err.println(e.getMessage());
        }
    }

    public static void SendBackGetData(boolean status, String responseData, String optionalMessage){
        String[] ServerResponse = {String.valueOf(status), responseData, optionalMessage};
        try{
            oos.writeObject(ServerResponse);
            oos.flush();
        }
        catch( Exception e){
            System.err.println(e.getMessage());
        }
    }
}
