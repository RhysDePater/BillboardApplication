package BillboardServer;

import com.sun.jdi.BooleanValue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


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
        System.out.println("Starting listening on port:" + port_number );
        for (; ; ) {
            // stuff
            try{
                InitialisedSocket = NetworkingSocket.accept(); // Accept the incoming connection
                System.out.println("New connection to:" + InitialisedSocket.getInetAddress());
                System.out.println("--------------------------------------------------------");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            ois = new ObjectInputStream(InitialisedSocket.getInputStream());
            oos = new ObjectOutputStream(InitialisedSocket.getOutputStream());
            Object data = ois.readObject();
            //ois.close();
            System.out.println("Data: " + data.getClass());
            Parse(data);
            //InitialisedSocket.close();
        }
    }

    /**
     * Takes the data from Listen() and translates it into commands to be run on the database, then runs them
     * @param data , the data received from the control panel
     */
    private void Parse(Object data){
        boolean commandSucceeded = false;
        String[] stringArray;
        try{
            stringArray = (String[])data;
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
            return;
        }
        String command = stringArray[0];
        System.out.println("Command is: " + command);
        if(command.equals("createUser")) { // Creates a user and the associated permissions
            byte[] salt = GenerateSalt();
            //String placeHolderSalt = "11001";
            PreparedStatement createUser = DBInteract.createUserPreparedStatement(stringArray[1], stringArray[2], salt);
            String createPermission = DBInteract.createPermission(Integer.parseInt(stringArray[3]),
                    Integer.parseInt(stringArray[4]),
                    Integer.parseInt(stringArray[5]),
                    Integer.parseInt(stringArray[6]));
            System.out.println(createPermission);
            System.out.println(createUser.toString());
            try{
                createUser.execute();
                DBInteract.dbExecuteCommand(createPermission);
                commandSucceeded = true;
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        // If the first element of the string array in clienttest.java is "createBillboard", then a prepared statement will be run.
        else if (command.equals("createBillboard")){
            PreparedStatement createBillboard = DBInteract.createBillboardPreparedStatement(stringArray[1], stringArray[2], stringArray[3]);
            System.out.println(createBillboard.toString());

            try{
                createBillboard.execute();
                commandSucceeded = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        SendBackData(command, commandSucceeded);
    }

    private void SendBackData( String command, boolean status){
        String[] ServerResponse = {command + " succeeded: " + status};
        try{
            oos.writeObject(ServerResponse);
            oos.flush();
        }
        catch( Exception e){
            System.out.println(e.getMessage());
        }
    }


    private byte[] GenerateSalt() { // Returns a string which represents a random array of bytes with length 16
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }
}
