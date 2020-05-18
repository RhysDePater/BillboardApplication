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
                System.out.println("New connection from:" + InitialisedSocket.getInetAddress());
                System.out.println("--------------------------------------------------------");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            ois = new ObjectInputStream(InitialisedSocket.getInputStream());
            oos = new ObjectOutputStream(InitialisedSocket.getOutputStream());
            Object data = ois.readObject();
            //ois.close();
            System.out.println("Incoming data is: " + data.getClass());
            Parse(data);
            //InitialisedSocket.close();
        }
    }

    /**
     * Takes the data from Listen() and translates it into commands to be run on the database, then runs them.
     * @param data , the data received from the control panel or viewer
     */
    private void Parse(Object data){
        boolean commandSucceeded = false;
        String optionalMessage = "";
        String responseData = "";
        String[] stringArray;
        try{
            stringArray = (String[])data;
        }
        catch (Exception e ){
            System.err.println(e.getMessage());
            return;
        }
        String command = stringArray[0];
        System.out.println("Command is: " + command);
        if(command.equals("createUser")) { // Creates a user and the associated permissions
            byte[] salt = GenerateSalt();
            //String placeHolderSalt = "11001";
            PreparedStatement QueryCreateUser = DBInteract.createUserPreparedStatement(stringArray[1], stringArray[2], salt);
            String QueryCreatePermission = DBInteract.createPermission(Integer.parseInt(stringArray[3]),
                    Integer.parseInt(stringArray[4]),
                    Integer.parseInt(stringArray[5]),
                    Integer.parseInt(stringArray[6]));
            System.out.println(QueryCreatePermission);
            System.out.println(QueryCreateUser.toString());
            try{
                QueryCreateUser.execute();
                DBInteract.dbExecuteCommand(QueryCreatePermission);
                commandSucceeded = true;
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else if(command.equals("deleteUser")) {
            boolean user_exists = false;
            String user_id = "";
            try{
                user_id = DBInteract.getUserId(stringArray[1]); // Get the id from the given username
                user_exists = true; // The above line will throw an exception is the user id doesn't exist for a username
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "User_id for supplied username not found: User doesn't exist";
            }
            if(user_exists){
                String QueryDeleteUser = DBInteract.deleteTarget("user", "id", user_id);
                String QueryDeletePermissions = DBInteract.deleteTarget("permission", "user_id", user_id);
                //String fullQuery = QueryDeleteUser + "; " + QueryDeletePermissions + ";"; // Executing the query on one line gave a syntax error for some reason
                // TODO
                // Change these to be a transaction so that they are guaranteed to both run together.
                // Otherwise it could be deleted from the user table without deleting the permissions
                System.out.println(QueryDeletePermissions);
                System.out.println(QueryDeleteUser);
                try{
                    DBInteract.dbExecuteCommand(QueryDeletePermissions);// This has to be run first so it deletes the foreign key user_id in permission
                    DBInteract.dbExecuteCommand(QueryDeleteUser);
                    commandSucceeded = true;
                }
                catch (SQLException e){
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }

        }
        else if(command.equals("login")){
            boolean user_exists = false;
            String password = "";
            try{
                password = DBInteract.getPassword(stringArray[1]);
                user_exists = true;
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "Password for supplied username not found: User doesn't exist";
            }
            if(user_exists){ // This code will only run if a password was found in the database
                if(password == null){
                    optionalMessage = "Username does not exist";
                }
                else if(password.equals(stringArray[2])){
                    commandSucceeded = true;
                    optionalMessage = "Password matches the supplied username";
                    responseData = "Session token placeholder";
                }
                else{
                    optionalMessage = "Password does not match the supplied username";
                    System.out.println(optionalMessage);
                }
            }

        }
        else if(command.equals("addSchedule")){
            //{"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
            boolean billboard_exists = false;
            String billboard_id ="";
            String user_id = "1";
            try{
                billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", stringArray[1]); // Get the billboard id from the billboard name
                billboard_exists = true;
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
            }
            if(billboard_exists){
                // Add the new schedule to the schedule table, if the billboard exists
                String QueryAddSchedule = DBInteract.addSchedule(user_id, billboard_id,stringArray[2],stringArray[3]);
                System.out.println(QueryAddSchedule);
                try{
                    DBInteract.dbExecuteCommand(QueryAddSchedule);
                    commandSucceeded = true;
                    optionalMessage = "Schedule successfully added";
                }
                catch (SQLException e){
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error adding schedule to the database";
                }
            }
        }
        // If the first element of the string array in clienttest.java is "createBillboard", then a prepared statement will be run.
        else if (command.equals("createBillboard")){
            PreparedStatement createBillboard = DBInteract.createBillboardPreparedStatement(stringArray[1], stringArray[2], stringArray[3], stringArray[4]);
            System.out.println(createBillboard.toString());
            try{
                createBillboard.execute();
                commandSucceeded = true;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                optionalMessage = "Error adding billboard to the database";
            }
        }
        else if (command.equals("deleteBillboard")){
            boolean billboard_exists = false;
            String billboard_id ="";
            try{
                billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", stringArray[1]); // Get the billboard id from the billboard name
                billboard_exists = true;
            }
            catch (SQLException e){
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
            }
            if(billboard_exists){
                String QueryDeleteBillboard = DBInteract.deleteTarget("billboard", "id", billboard_id);
                String QueryDeleteSchedule = DBInteract.deleteTarget("schedule", "billboard_id", billboard_id);
                // TODO
                // Change these to be a transaction so that they are guaranteed to both run together.
                // Otherwise it could be deleted from the billboard without deleting the schedule
                System.out.println(QueryDeleteSchedule);
                System.out.println(QueryDeleteBillboard);
                try{
                    DBInteract.dbExecuteCommand(QueryDeleteSchedule);// This has to be run first so it deletes the foreign key billboard_id in schedule
                    DBInteract.dbExecuteCommand(QueryDeleteBillboard);
                    commandSucceeded = true;
                }
                catch (SQLException e){
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }

        }

        SendBackData(commandSucceeded, responseData, optionalMessage);
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Sends a response back to the client in the form {"true/false (succeeded or failed)", "A result set or string if appropriate", "Optional message that could be displayed to the user/debugging purposes"}
     * status is the boolean describing the state the database operation
     * @param status
     * @param responseData
     * @param optionalMessage
     */
    private void SendBackData(boolean status, String responseData, String optionalMessage){
        String[] ServerResponse = {String.valueOf(status), responseData, optionalMessage};
        try{
            oos.writeObject(ServerResponse);
            oos.flush();
        }
        catch( Exception e){
            System.err.println(e.getMessage());
        }
    }

    private byte[] GenerateSalt() { // Returns a string which represents a random array of bytes with length 16
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }
}
