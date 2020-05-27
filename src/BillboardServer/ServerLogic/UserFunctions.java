package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.SessionToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import static BillboardServer.Misc.SessionToken.*;

public class UserFunctions extends ServerVariables{

    public static byte[] GenerateSalt() { // Returns a random array of bytes with length 16
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }

    public static void createUser() throws NoSuchAlgorithmException {
        byte[] salt = GenerateSalt();
        //String placeHolderSalt = "11001";
        sessionTokenFromClient = inboundData[7];
        hashedPassWordFromClient = inboundData[2];
        saltString = bytesToString(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // This is the hashed and salted password that is stored in the database.
        hashAndSaltedPassword = bytesToString(md.digest((hashedPassWordFromClient + saltString).getBytes()));
        if (!isSessionTokenValid(sessionTokenFromClient)) { //If the session token is valid, then the query can attempt to run.
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        PreparedStatement QueryCreateUser = DBInteract.createUserPreparedStatement(inboundData[1], hashAndSaltedPassword, saltString);
        String QueryCreatePermission = DBInteract.createPermission(Integer.parseInt(inboundData[3]), // The data comes in as part of a string array, so change it back to an int
                Integer.parseInt(inboundData[4]),
                Integer.parseInt(inboundData[5]),
                Integer.parseInt(inboundData[6]));
        System.out.println(QueryCreatePermission);
        System.out.println(QueryCreateUser.toString());

        try {
            QueryCreateUser.execute();
            DBInteract.dbExecuteCommand(QueryCreatePermission);
            commandSucceeded = true;
            optionalMessage = "User successfully created";
        } catch (
                SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Error adding user to database: User likely already exists";
        }
    }

    public static void deleteUser(){
        boolean user_exists = false;
        String user_id = "";
        String username = inboundData[1];
        sessionTokenFromClient = inboundData[2]; //Session tokens come from ServerRequest methods.
        if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        if (doesUserMatchSessionTokenFromClient(sessionTokenFromClient, username)){
            optionalMessage = "You cannot delete your own account.";
            return;
        }
            try {
                user_id = DBInteract.getUserId(username); // Get the id from the given username
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
                    optionalMessage = "User successfully deleted";
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Failed to delete user";
                }
            }
    }

    public static void listUsers() {
        if(!isSessionTokenValid(inboundData[1])){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        String getUserDataQuery = DBInteract.selectUserJoinPermission();
        System.out.println(getUserDataQuery);
        String[][] results;
        try{
             results = DBInteract.getUserData(getUserDataQuery);
             commandSucceeded = true;
             optionalMessage = "List of Users successfully returned";
             outboundData2D = results;
        }
        catch (SQLException e){
            System.out.println(e.toString());
            optionalMessage = "Failed to get user list:" + e.getMessage();
        }
    }

    public static void editPermissions(){
        String user_id = "";
        sessionTokenFromClient = inboundData[6];
        if(isSessionTokenValid(sessionTokenFromClient)) {
            try {
                user_id = DBInteract.getUserId(inboundData[1]); // Get the id from the given username
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "User_id for supplied username not found: User doesn't exist";
            }
            if (!user_id.equals("")) {
                String QueryEditPermission;
                if(Arrays.asList(inboundData).contains("-1")){ // Only a single permission is to be edited
                    QueryEditPermission = editSinglePermission(user_id);
                }
                else {
                    QueryEditPermission = DBInteract.updatePermission(user_id, Integer.parseInt(inboundData[2]), // The data comes in as part of a string array, so change it back to an int as the database stores tiny ints
                            Integer.parseInt(inboundData[3]),
                            Integer.parseInt(inboundData[4]),
                            Integer.parseInt(inboundData[5]));
                }
                System.out.println(QueryEditPermission);
                try {
                    DBInteract.dbExecuteCommand(QueryEditPermission);
                    commandSucceeded = true;
                    optionalMessage = "Permissions successfully changed";
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error changing permissions";
                }
            }
        }
        else if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }

    public static void getPermissions() {
        String user_id;
        sessionTokenFromClient = inboundData[2];
        if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        try{
            user_id = DBInteract.getUserId(inboundData[1]);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            optionalMessage = "Error getting the user id from the provided session token";
            outboundData1D = new String[]{""};
            return;
        }
        String[] getUserPermissions;
        try{
            getUserPermissions = DBInteract.getPermissions(user_id);
            commandSucceeded = true;
            optionalMessage = "List of permissions successfully returned";
            outboundData1D = getUserPermissions;
        }
        catch (SQLException e){
            System.out.println(e.toString());
            optionalMessage = "Failed to get permissions list:" + e.getMessage();
        }
    }

    private static String editSinglePermission(String user_id){
        if(!inboundData[2].equals("-1")){
            System.out.println(inboundData[2]);
            return DBInteract.updatePermissionCreateBillboard(user_id, inboundData[2]);
        }
        else if(!inboundData[3].equals("-1")){
            return DBInteract.updatePermissionEditBillboard(user_id, inboundData[3]);
        }
        else if(!inboundData[4].equals("-1")){
            return DBInteract.updatePermissionScheduleBillboard(user_id, inboundData[4]);
        }
        else if(!inboundData[5].equals("-1")){
            return DBInteract.updatePermissionEditUser(user_id, inboundData[5]);
        }
        return null;
    }

    public static void login() throws NoSuchAlgorithmException {
        String password = "";
        String username = inboundData[1];
        // Salt from the database.
        String stringOfSaltFromDatabase = "";
        hashedPassWordFromClient = inboundData[2];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try {
            password = DBInteract.getPassword(username);
            stringOfSaltFromDatabase = (DBInteract.getValue("salt", "user", "username", username));

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Password for supplied username not found: User doesn't exist";
        }

        hashAndSaltedPassword = bytesToString(md.digest((hashedPassWordFromClient + stringOfSaltFromDatabase).getBytes()));

        if (!password.equals("")) { // This code will only run if a password was found in the database
            if (password.equals(hashAndSaltedPassword)) {
                commandSucceeded = true;
                optionalMessage = "Password matches the supplied username";
                outboundData = createSessionToken(username);
            } else {
                optionalMessage = "Password does not match the supplied username: " + hashAndSaltedPassword;
            }
        }
    }

    public static void logout(){
        String sessionToken = inboundData[1];
        if(!isSessionTokenValid(sessionToken)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
        try{
            commandSucceeded = true;
            optionalMessage = "User successfully logged out.";
            sessionTokenStorage.remove(sessionToken);
            usersSessionTokens.remove(sessionToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
