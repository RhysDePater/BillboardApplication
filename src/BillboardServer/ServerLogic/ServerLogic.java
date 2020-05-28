package BillboardServer.ServerLogic;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static BillboardServer.Networking.SendBackData;

public class ServerLogic extends ServerVariables{
    /**
     * Takes the data from Listen() and translates it into commands to be run on the database, then runs them.
     * The main logic of the server
     * @param data the data received from the control panel or viewer
     */
    public static void Parse(Object data) throws NoSuchAlgorithmException {
        resetServerVariables();
        try{
            inboundData = (String[])data;
            System.out.println("Incoming data is: " + Arrays.toString(inboundData));
        }
        catch (Exception e ){
            System.err.println(e.getMessage());
            System.out.println("Incoming data is not a string array");
            return;
        }
        String command = inboundData[0];
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
            case "listUsers": {
                UserFunctions.listUsers();
                break;
            }
            case "editPermission": {
                UserFunctions.editPermissions();
                break;
            }
            case "getPermissions": {
                UserFunctions.getPermissions();
                break;
            }
            case "login": {
                UserFunctions.login();
                break;
            }
            case "logout":{
                UserFunctions.logout();
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
            case "listSchedules": {
                ScheduleFunctions.listSchedules();
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
            case "getBillboard": {
                BillboardFunctions.getBillboard();
                break;
            }
            case "listBillboards": {
                BillboardFunctions.listBillboards();
                break;
            }
            case "getColumns": {
                DatabaseFunctions.getColumns();
                break;
            }
            case "setUserPassword":{
                UserFunctions.setUserPassword();
                break;
            }
            case "getCurrentBillboard":{
                BillboardFunctions.getCurrentBillboard();
                break;
            }
        }
        SendBackData(commandSucceeded, outboundData, optionalMessage, outboundData2D, outboundData1D);
        System.out.println("--------------------------------------------------------");
    }

    private static void resetServerVariables(){
        commandSucceeded = false;
        optionalMessage = "";
        outboundData = "";
        if (outboundData2D != null){
            outboundData2D = null;
        }
        outboundData1D = null;
        inboundData = null;
        sessionTokenFromClient = "";
    }
}
