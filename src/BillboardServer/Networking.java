package BillboardServer;

import BillboardServer.Database.DBInteract;
import BillboardServer.ServerLogic.ServerLogic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;


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

    public void Listen() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        System.out.println("Starting listening on port: " + port_number );
        for (;;) {
            try{
                InitialisedSocket = NetworkingSocket.accept(); // Accept the incoming connection
                System.out.println("New connection from: " + InitialisedSocket.getInetAddress());
                System.out.println("--------------------------------------------------------");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            try {
                ois = new ObjectInputStream(InitialisedSocket.getInputStream());
                oos = new ObjectOutputStream(InitialisedSocket.getOutputStream());
                Object data = ois.readObject();
                ServerLogic.Parse(data);
                // I don't know if these need to be closed every time
                ois.close();
                oos.close();
            }
            catch (EOFException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            InitialisedSocket.close();
        }
    }

    /**
     * Sends a response back to the client in the form {"true/false (succeeded or failed)", "A result set or string if appropriate", "Optional message that could be displayed to the user/debugging purposes"}
     * status is the boolean describing the state the database operation
     * @param responseDataSmall If the command was a get, such as get billboard content, then this will be the response
     * @param status If the command failed or not
     * @param optionalMessage Optional message for debugging/ to be shown to the user
     * @param outboundData1D Some functions return data as an array, which is stored in query results.
     */
    public static void SendBackData(boolean status, String responseDataSmall, String optionalMessage, String[][] responseData2D, String[] outboundData1D){
        String[] ServerResponse = {String.valueOf(status), responseDataSmall, optionalMessage};
        String[][] ServerResponse2D;
        if(responseData2D != null && responseData2D.length != 0){ // Compose a specific string[][] to send back to the client
            System.out.println(Arrays.deepToString(responseData2D));
            ServerResponse2D = new String[responseData2D.length + 1][responseData2D[0].length] ; // Add an extra row for the ServeResponse to go at ServerResponse[0]
            ServerResponse2D[0] = ServerResponse;
            for (int i = 0; i < responseData2D.length; i++ ){
                System.arraycopy(responseData2D[i], 0, ServerResponse2D[i + 1], 0, responseData2D[0].length); // Copy the main data to the ServerResponse2D
            }
            try{
                oos.writeObject(ServerResponse2D);
                oos.flush();
                System.out.println("Sent back: " + Arrays.deepToString(ServerResponse2D));
            }
            catch( Exception e){
                System.err.println(e.getMessage());
            }
        }
        else if(outboundData1D != null){ // Compose a specific string[][] to send back to the client
            ServerResponse2D = new String[2][outboundData1D.length] ; // Add an extra row for the ServeResponse to go at ServerResponse[0]
            ServerResponse2D[0] = ServerResponse;
            ServerResponse2D[1] = outboundData1D;
            try{
                oos.writeObject(ServerResponse2D);
                oos.flush();
                System.out.println("Sent back: " + Arrays.deepToString(ServerResponse2D));
            }
            catch( Exception e){
                System.err.println(e.getMessage());
            }
        }
        else{
            try{
                oos.writeObject(ServerResponse);
                oos.flush();
                System.out.println("Sent back: " + Arrays.toString(ServerResponse));
            }
            catch( Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}
