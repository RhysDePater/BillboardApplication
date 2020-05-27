package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static BillboardServer.Misc.SessionToken.getUser;
import static BillboardServer.Misc.SessionToken.isSessionTokenValid;

public class BillboardFunctions extends ServerVariables{

    public static void createBillboard(){
        String billboard_id = "";
        sessionTokenFromClient = inboundData[3];
        String user_id;
        try{
            user_id = DBInteract.getUserId(getUser(sessionTokenFromClient));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            optionalMessage = "Error getting the user id from the provided session token";
            return;
        }
        if (isSessionTokenValid(sessionTokenFromClient)) {
            // First see if the billboard actually exists
            try {
                billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", inboundData[1]); // Get the billboard id from the billboard name
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            if (billboard_id.equals("")) { // Create billboard because no billboard exists
                PreparedStatement createBillboard = DBInteract.createBillboardPreparedStatement(user_id, inboundData[1], inboundData[2]); // There should be no schedule at the start for the billboard
                System.out.println(createBillboard.toString());
                try {
                    createBillboard.execute();
                    commandSucceeded = true;
                    optionalMessage = "Billboard successfully added";
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    optionalMessage = "Error adding billboard to the database";
                }
            } else { // Modify the existing billboard
                String modifyBillboard = DBInteract.updateColumnWhereId("billboard", "xml_data", inboundData[2], billboard_id); // There should be no schedule at the start for the billboard
                System.out.println(modifyBillboard);
                try {
                    DBInteract.dbExecuteCommand(modifyBillboard);
                    optionalMessage = "Billboard " + inboundData[1] + " modified successfully";
                    commandSucceeded = true;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    optionalMessage = "Error modifying existing billboard";
                }
            }
        }
        else if (!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }

    public static void deleteBillboard() {
        String billboard_id = "";
        sessionTokenFromClient = inboundData[2];
        if (isSessionTokenValid(sessionTokenFromClient)) {
            try {
                billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", inboundData[1]); // Get the billboard id from the billboard name
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
                    optionalMessage = "Billboard " + inboundData[1] + " deleted successfully";
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error deleting billboard";
                }
            }
        }
        else if (!isSessionTokenValid(sessionTokenFromClient)) {
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }

    public static void getBillboard(){
        sessionTokenFromClient = inboundData[2];
        if (isSessionTokenValid(sessionTokenFromClient)) {
            try {
                outboundData = DBInteract.getValue("xml_data", "billboard", "billboard_name", inboundData[1]); // Get the billboard content from the billboard name
                commandSucceeded = true;
                optionalMessage = "Billboard data for supplied Billboard successfully found";
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                outboundData = "";
                optionalMessage = "Billboard data for supplied Billboard name not found: Billboard doesn't exist";
            }
        }
        else if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }
    public static void listBillboards() {
        if(!isSessionTokenValid(inboundData[1])){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        String getBillboardDataQuery = DBInteract.selectUserJoinBillboard();
        System.out.println(getBillboardDataQuery);
        String[][] results;
        try{
            results = DBInteract.getBillboardData(getBillboardDataQuery);
        }
        catch (SQLException e){
            System.out.println(e.toString());
            optionalMessage = "Failed to get billboard list:" + e.getMessage();
            return;
        }
        commandSucceeded = true;
        optionalMessage = "List of billboards successfully returned";
        outboundData2D = results;
    }

}
