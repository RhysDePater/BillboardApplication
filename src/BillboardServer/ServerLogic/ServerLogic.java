package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static BillboardServer.Misc.SessionToken.isSessionTokenValid;
import static BillboardServer.Misc.SessionToken.createSessionToken;
import static BillboardServer.Networking.SendBackData;

public class ServerLogic extends ServerVariables{


    /**
     * Takes the data from Listen() and translates it into commands to be run on the database, then runs them.
     * The main logic of the server
     * @param data the data received from the control panel or viewer
     */
    public static void Parse(Object data){

        resetServerVariables();
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
            case "createUser": { // Creates a user and the associated permissions
                UserFunctions.createUser();
                break;
            }
            case "deleteUser": {
                UserFunctions.deleteUser();
                break;
            }
            case "editPermission": {
                UserFunctions.editPermissions();
                break;
            }
            case "login": {
                UserFunctions.login();
                break;
            }
            case "addSchedule": {
                ScheduleFunctions.addSchedule();
                break;
            }
            case "deleteSchedule": {
                ScheduleFunctions.deleteSchedule();
                break;
            }
            case "createBillboard": { // If the first element of the string array in clienttest.java is "createBillboard", then a prepared statement will be run.
                BillboardFunctions.createBillboard(); // Will modify a billboard if the billboard name already exists
                break;
            }
            case "deleteBillboard": {
                BillboardFunctions.deleteBillboard();
                break;
            }
            case "getBillboard":
                BillboardFunctions.getBillboard();
                break;
        }
        SendBackData(commandSucceeded, responseData, optionalMessage);
        System.out.println("--------------------------------------------------------");
    }

    private static void resetServerVariables(){
         commandSucceeded = false;
        optionalMessage = "";
        responseData = "";
        stringArray = null;
        sessionTokenFromClient = "";
    }
}
