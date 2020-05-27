package BillboardServer.ClientUtilities;

import BillboardServer.Misc.SessionToken;

import java.rmi.ServerError;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ClientTest {

    // From week 10 assignment Q&A lecture.
    private static String bytesToString(byte[] hash) {
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    /**
     * USE THIS METHOD TO HASH THE PASSWORD BEING SENT FROM THE CLIENT. Used for hashing
     *  a password when either creating a user or logging
     * into an account.
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    // Based on material from cab302 week 9 assignment Q&A lecture.
    private static String hashedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // The hashing algorithm.
        byte[] hashedPassword = md.digest(password.getBytes());
        String hashedPasswordString = bytesToString(hashedPassword);
        return hashedPasswordString;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String sessionToken = "myToken";
        String xmlString;
        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30); // The format for date objects when scheduling
        String[] responseArray;
        String[][] responseArray2D;

        // Get xml data from a file, used for createOrEditBillboard
        try{
            xmlString = XMLStringCreator.createXmlString();
        }
        catch (Exception e){
            System.out.println("Xml file not found");
            return;
        }


        //--------------------------------------------------------------------------------------------------//

//        MessageDigest md = MessageDigest.getInstance("SHA-256"); // The hashing algorithm.
//        String input = "myPassword2";
//        byte[] hashedPassword = md.digest(input.getBytes());
//
//        System.out.println("Hashed password on client: " + bytesToString(hashedPassword));
//        String hashedPasswordString = bytesToString(hashedPassword);
//        // This hashed password is sent across to the server.
            String testPassword = hashedPassword("testPassword"); // USED TO TEST THAT HASHED PASSWORDS CAN BE AUTHENTICATED ON SERVER.
            String ADMINPassword = hashedPassword("pass");
//        MessageDigest md = MessageDigest.getInstance("SHA-256"); // The hashing algorithm.
//        String passwordTypedIntoControlPanel = "pass";
//        String newHashedPassword = bytesToString(md.digest(passwordTypedIntoControlPanel.getBytes()));
//        System.out.println("Hashed password being sent to server: " + newHashedPassword);

        // THE CONTROL PANEL NEEDS TO HASH ITS PASSWORD WHEN ENTERED LIKE ABOVE IN ORDER FOR THE SERVER TO AUTHENTICATE THE USER.
        //--------------------------------------------------------------------------------------------------//

        // These are the functions to call if you want the server to perform stuff on the database. Hopefully they are straightforward, but there are java doc style comments available.
        // See ServerRequest.sendQuery (and below) for what they return.
        try {
            //responseArray = ServerRequest.login("testusername", testPassword); // Password for this user is myPassword2
            responseArray = ServerRequest.login("ADMIN", ADMINPassword); // Password for this user is myPassword2
            //sessionToken = responseArray[1];
            sessionToken = responseArray[1];
            //responseArray = ServerRequest.createUser("testusername", testPassword, 1,1,1,1, sessionToken);
            //responseArray = ServerRequest.deleteUser("testusername", sessionToken);
            //responseArray = ServerRequest.createOrEditBillboard("my billboard", "example data", sessionToken);
            //responseArray = ServerRequest.deleteBillboard("my billboard", sessionToken);
            //responseArray = ServerRequest.getBillboard("BillboardMethodTest6", sessionToken);
            //responseArray = ServerRequest.addSchedule("BillboardMethodTest6", startDate, 120, sessionToken);
            //responseArray = ServerRequest.deleteSchedule("BillboardMethodTest6", startDate, sessionToken);
            //responseArray = ServerRequest.editAllPermissions("testusername", 0,0,0,0, sessionToken);
        }
        catch (IOException e){
            System.out.println("Could not access server");
            return; // Can't really do much if we can't access the server
        }

       // When an exception occurs the response array is null, so you will get an error if you try read it, otherwise it's fine
        System.out.println(responseArray[0]); // true or false if the sql query ran okay
        System.out.println(responseArray[1]); // Response string (Returned from some functions such as those which get stuff from the database, "" from others)
        System.out.println(responseArray[2]); // Optional user-friendly message (Not implemented for every function, it might just be "")
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        try{
            //responseArray = ServerRequest.editPermission("testusername", "edit_billboard", 0, sessionToken);
            //responseArray2D = ServerRequest.listUsers(sessionToken);
            //responseArray = ServerRequest.addSchedule("my billboard", startDate, 9290, sessionToken);
            //responseArray = ServerRequest.deleteBillboard("my billboard", sessionToken);
            //responseArray = ServerRequest.createOrEditBillboard("my billboard", "example data", sessionToken);
            responseArray2D = ServerRequest.listSchedules(sessionToken);
        }
        catch (IOException e){
            System.out.println("Could not access server");
            return; // Can't really do much if we can't access the server
        }
        System.out.println(Arrays.deepToString(responseArray2D)); // Entire output
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        try{
            responseArray2D = ServerRequest.getPermissions("1", sessionToken);
        }
        catch (IOException e){
            System.out.println("Could not access server");
            return; // Can't really do much if we can't access the server
        }
        System.out.println(Arrays.deepToString(responseArray2D)); // Entire output
    }
}

