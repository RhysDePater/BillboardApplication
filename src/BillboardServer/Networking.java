package BillboardServer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


/**
 * The Networking constructor sets up the Socket
 * Listen is an infinite for loop which accepts incoming connections and messages as an ObjectInputStream.
 * The server expects a String array which is {Command, param1, param2, param3, ...} as appropriate
 * Such as: {createUser, username, password}
 */

public class Networking {
    static ServerSocket NetworkingSocket;
    static Socket InitialisedSocket;
    static int port_number;
    public Networking(int port) throws IOException {
        port_number = port;
        try {
            NetworkingSocket = new ServerSocket(port); // Make the port dynamic
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
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            InputStream MyInputStream = InitialisedSocket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(MyInputStream);
            Object command = ois.readObject();
            System.out.println("byte read:" + command.toString());
            Parse(command);
            InitialisedSocket.close();
        }
    }

    /**
     * Takes the data from Listen() and translates it into commands to be run on the database
     * @param data
     */

    private void Parse(Object data) {
        String[] stringArray;
        try{
            stringArray = (String[])data;
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
            return;
        }
        String command = stringArray[0];
        System.out.println(command);
        System.out.println(stringArray[1]);
        /*
        if(command != "createUser")
        {
            System.out.println(true);
        }*/


        if(command.equals("ExecuteSQL")){
            //DBInteract.dbExecuteCommand(data.substring(10,-1));
        }
        else if(command.equals("createUser")) {
            //int salt = GenerateSalt();
            String placeHolderSalt = "11001";
            String sqlcommand = DBInteract.createUser(stringArray[1], stringArray[2], placeHolderSalt);
            System.out.println(sqlcommand);
            DBInteract.dbExecuteCommand(sqlcommand);
        }
    }

    private void SendBackData(){

    }


    private byte[] GenerateSalt(){
        final Random r = new SecureRandom();
        byte[] salt = new byte[64];
        r.nextBytes(salt);
        return salt;
    }
}
