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
        switch (command) {
            case "createUser":  // Creates a user and the associated permissions
                byte[] salt = GenerateSalt();
                //String placeHolderSalt = "11001";
                PreparedStatement QueryCreateUser = DBInteract.createUserPreparedStatement(stringArray[1], stringArray[2], salt);
                String QueryCreatePermission = DBInteract.createPermission(Integer.parseInt(stringArray[3]),
                        Integer.parseInt(stringArray[4]),
                        Integer.parseInt(stringArray[5]),
                        Integer.parseInt(stringArray[6]));
                System.out.println(QueryCreatePermission);
                System.out.println(QueryCreateUser.toString());
                try {
                    QueryCreateUser.execute();
                    DBInteract.dbExecuteCommand(QueryCreatePermission);
                    commandSucceeded = true;
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case "deleteUser": {
                boolean user_exists = false;
                String user_id = "";
                try {
                    user_id = DBInteract.getUserId(stringArray[1]); // Get the id from the given username
                    user_exists = true; // The above line will throw an exception is the user id doesn't exist for a username
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "User_id for supplied username not found: User doesn't exist";
                }
                if (user_exists) {
                    String QueryDeleteUser = DBInteract.deleteTarget("user", "id", user_id);
                    String QueryDeletePermissions = DBInteract.deleteTarget("permission", "user_id", user_id);
                    //String fullQuery = QueryDeleteUser + "; " + QueryDeletePermissions + ";"; // Executing the query on one line gave a syntax error for some reason
                    // TODO
                    // Change these to be a transaction so that they are guaranteed to both run together.
                    // Otherwise it could be deleted from the user table without deleting the permissions
                    System.out.println(QueryDeletePermissions);
                    System.out.println(QueryDeleteUser);
                    try {
                        DBInteract.dbExecuteCommand(QueryDeletePermissions);// This has to be run first so it deletes the foreign key user_id in permission
                        DBInteract.dbExecuteCommand(QueryDeleteUser);
                        commandSucceeded = true;
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "login": {
                boolean user_exists = false;
                String password = "";
                try {
                    password = DBInteract.getPassword(stringArray[1]);
                    user_exists = true;
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Password for supplied username not found: User doesn't exist";
                }
                if (user_exists) { // This code will only run if a password was found in the database
                    if (password == null) {
                        optionalMessage = "Username does not exist";
                    } else if (password.equals(stringArray[2])) {
                        commandSucceeded = true;
                        optionalMessage = "Password matches the supplied username";
                        responseData = "Session token placeholder";
                    } else {
                        optionalMessage = "Password does not match the supplied username";
                        System.out.println(optionalMessage);
                    }
                }
                break;
            }
            case "addSchedule": {
                // TODO
                // Add restrictions so the user can't enter a schedule which already exists
                // Currently if this is the case, the deleteSchedule won't know which one to delete, and just delete the first one
                // But, there shouldn't be a case where two of the same billboard want to start at the same time
                //{"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
                boolean billboard_exists = false;
                String billboard_id = "";
                String user_id = "1";
                try {
                    billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", stringArray[1]); // Get the billboard id from the billboard name
                    billboard_exists = true;
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
                }
                if (billboard_exists) {
                    // Add the new schedule to the schedule table, if the billboard exists
                    String QueryAddSchedule = DBInteract.addSchedule(user_id, billboard_id, stringArray[2], stringArray[3]);
                    System.out.println(QueryAddSchedule);
                    try {
                        DBInteract.dbExecuteCommand(QueryAddSchedule);
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        optionalMessage = "Error adding schedule to the database";
                    }
                    // Add to the billboard table
                    String schedule_id = "";
                    try {
                        // Get the id of this new schedule
                        schedule_id = DBInteract.getValue("id", "schedule", "billboard_id", billboard_id);
                        // Finally, add the schedule number to the billboard table, this will link that billboard to a row in the schedule table
                        String updateSchedule = DBInteract.updateColumnWhereId("billboard", "schedule_id", schedule_id, billboard_id );
                        DBInteract.dbExecuteCommand(updateSchedule);
                        commandSucceeded = true;
                        optionalMessage = "Schedule added successfully";
                    }
                    catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error adding schedule to the database, schedule created, but billboard could not be linked to that schedule";
                    }
                }
                break;
            }
            case "deleteSchedule": {
                String billboard_id = "";
                String schedule_id = "";
                // First see if the billboard actually exists
                try {
                    billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", stringArray[1]); // Get the billboard id from the billboard name
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
                }
                if(!billboard_id.equals("")) {
                    // Then find the schedule id to see if a schedule exists for the billboard at that time
                    try {
                        schedule_id = DBInteract.getValueAnd("id", "schedule", "billboard_id", billboard_id, "start_time", stringArray[2]); // Get the schedule id from the billboard name and a time
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        optionalMessage = "Schedule id for supplied billboard name and time not found: Schedule doesn't exist";
                    }
                    // Finally, delete the entry from the database
                    if (!schedule_id.equals("")) {
                        String QueryDeleteSchedule = DBInteract.deleteTarget("schedule", "id", schedule_id); // Delete the row where the billboard name and time is as specified
                        try {
                            DBInteract.dbExecuteCommand(QueryDeleteSchedule);
                            commandSucceeded = true;
                            optionalMessage = "Schedule deleted successfully";
                        } catch (SQLException e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                            optionalMessage = "Error deleting the schedule: " + e.getMessage();
                        }
                    }
                }
                break;
            }

            // If the first element of the string array in clienttest.java is "createBillboard", then a prepared statement will be run.
            case "createBillboard":
                String user_id = "1";
                PreparedStatement createBillboard = DBInteract.createBillboardPreparedStatement(user_id, stringArray[1], stringArray[2]); // There should be no schedule at the start for the billboard
                System.out.println(createBillboard.toString());
                try {
                    createBillboard.execute();
                    commandSucceeded = true;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    optionalMessage = "Error adding billboard to the database";
                }
                break;
            case "deleteBillboard": {
                String billboard_id = "";
                try {
                    billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", stringArray[1]); // Get the billboard id from the billboard name
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
                }
                if (!billboard_id.equals("")) {
                    String QueryDeleteBillboard = DBInteract.deleteTarget("billboard", "id", billboard_id);
                    String QueryDeleteSchedule = DBInteract.deleteTarget("schedule", "billboard_id", billboard_id); // This will delete multiple rows if they match
                    // TODO
                    // Change these to be a transaction so that they are guaranteed to both run together.
                    // Otherwise it could be deleted from the billboard without deleting the schedule
                    // Also, figure out if there is a schedule for the billboard before trying to delete it
                    System.out.println(QueryDeleteSchedule);
                    System.out.println(QueryDeleteBillboard);
                    try {
                        DBInteract.dbExecuteCommand(QueryDeleteSchedule);// This has to be run first so it deletes the foreign key billboard_id in schedule
                        DBInteract.dbExecuteCommand(QueryDeleteBillboard);
                        commandSucceeded = true;
                        optionalMessage = "Billboard " + stringArray[1] + " deleted successfully";
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "getBillboard":
                try {
                    responseData = DBInteract.getValue("xml_data", "billboard", "billboard_name", stringArray[1]); // Get the billboard content from the billboard name
                    commandSucceeded = true;
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    responseData = "";
                    optionalMessage = "Billboard data for supplied Billboard name not found: Billboard doesn't exist";
                }
                break;
        }

        SendBackData(commandSucceeded, responseData, optionalMessage);
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Sends a response back to the client in the form {"true/false (succeeded or failed)", "A result set or string if appropriate", "Optional message that could be displayed to the user/debugging purposes"}
     * status is the boolean describing the state the database operation
     * @param status If the command failed or not
     * @param responseData If the command was a get, such as get billboard content, then this will be the response
     * @param optionalMessage Optional message for debugging/ to be shown to the user
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
