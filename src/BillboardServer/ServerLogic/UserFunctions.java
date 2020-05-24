package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static BillboardServer.Misc.SessionToken.createSessionToken;
import static BillboardServer.Misc.SessionToken.isSessionTokenValid;

public class UserFunctions extends ServerVariables{

    private static byte[] GenerateSalt() { // Returns a random array of bytes with length 16
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }

    public static void createUser() {
        byte[] salt = GenerateSalt();
        //String placeHolderSalt = "11001";
        sessionTokenFromClient = stringArray[7];
        PreparedStatement QueryCreateUser = DBInteract.createUserPreparedStatement(stringArray[1], stringArray[2], salt);
        String QueryCreatePermission = DBInteract.createPermission(Integer.parseInt(stringArray[3]), // The data comes in as part of a string array, so change it back to an int
                Integer.parseInt(stringArray[4]),
                Integer.parseInt(stringArray[5]),
                Integer.parseInt(stringArray[6]));
        System.out.println(QueryCreatePermission);
        System.out.println(QueryCreateUser.toString());
        // No need to check the session token here because this is only run when a user is created, so there wont be a session token for them
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
        sessionTokenFromClient = stringArray[2]; //Session tokens come from ServerRequest methods.
        if(isSessionTokenValid(sessionTokenFromClient)){
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
                    optionalMessage = "User successfully deleted";
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Failed to delete user";
                }
            }
        }
        else if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }
    public static void editPermissions(){
        String user_id = "";
        sessionTokenFromClient = stringArray[6];
        if(isSessionTokenValid(sessionTokenFromClient)) {
            try {
                user_id = DBInteract.getUserId(stringArray[1]); // Get the id from the given username
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "User_id for supplied username not found: User doesn't exist";
            }
            if (!user_id.equals("")) {
                String QueryEditPermission = DBInteract.updatePermission(user_id, Integer.parseInt(stringArray[2]), // The data comes in as part of a string array, so change it back to an int as the database stores tiny ints
                        Integer.parseInt(stringArray[3]),
                        Integer.parseInt(stringArray[4]),
                        Integer.parseInt(stringArray[5]));
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
    public static void login(){
        String password = "";
        try {
            password = DBInteract.getPassword(stringArray[1]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Password for supplied username not found: User doesn't exist";
        }
        if (!password.equals("")) { // This code will only run if a password was found in the database
            if (password.equals(stringArray[2])) {
                commandSucceeded = true;
                optionalMessage = "Password matches the supplied username";
                responseData = createSessionToken();
            } else {
                optionalMessage = "Password does not match the supplied username";
                System.out.println(optionalMessage);
            }
        }
    }
}
