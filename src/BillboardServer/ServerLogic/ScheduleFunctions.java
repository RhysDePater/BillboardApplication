package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.SessionToken;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import static BillboardServer.Misc.SessionToken.isSessionTokenValid;

public class ScheduleFunctions extends ServerVariables {
    public static void addSchedule(){
        // TODO
        // Add restrictions so the user can't enter a schedule which already exists
        // Currently if this is the case, the deleteSchedule won't know which one to delete, and just delete the first one
        // But, there shouldn't be a case where two of the same billboard want to start at the same time
        String billboard_id;
        String user_id;
        sessionTokenFromClient = inboundData[5];
        if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        // Checks if the current user has the permission to add schedules
        if(!(CheckPermissions.checkUserPermissions(sessionTokenFromClient, "scheduleBillboard"))){
            optionalMessage = "User does not have permission to add a schedule (need schedule_billboard permission)";
            return;
        }
        try {
            billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", inboundData[1]); // Get the billboard id from the billboard name
            user_id = DBInteract.getUserId(SessionToken.getUser(sessionTokenFromClient)); // Get the user id
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
            return;
        }
        // Add the new schedule to the schedule table, if the billboard exists
        String QueryAddSchedule = DBInteract.addSchedule(user_id, billboard_id, inboundData[2], inboundData[3], inboundData[4]);
        System.out.println(QueryAddSchedule);
        try {
            DBInteract.dbExecuteCommand(QueryAddSchedule);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Error adding schedule to the database"; // Exception used to be thrown when you could only have one schedule per billboard
            return;
        }
        // Add to the billboard table
        //String schedule_id = "";
        try {
            // No need for a schedule id column, that's why all the code below is commented
            // Get the id of this new schedule and status
            // schedule_id = DBInteract.getValue("id", "schedule", "billboard_id", billboard_id);
            // Finally, add the schedule number to the billboard table, this will link that billboard to a row in the schedule table
            // String updateBillboard = DBInteract.updateColumnWhereId("billboard", "schedule_id", schedule_id, billboard_id);
            // DBInteract.dbExecuteCommand(updateBillboard);

            // add status
            String updateBillboardStatus = DBInteract.updateColumnWhereId("billboard", "status", "1", billboard_id);
            DBInteract.dbExecuteCommand(updateBillboardStatus);
            commandSucceeded = true;
            optionalMessage = "Schedule added successfully";
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Error adding schedule to the database, schedule created, but billboard could not be linked to that schedule";
        }
    }
    public static void deleteSchedule() {
        String billboard_id = "";
        String schedule_id = "";
        sessionTokenFromClient = inboundData[3];
        if (!isSessionTokenValid(sessionTokenFromClient)) {
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        // Checks if the current user has the permission to remove schedules
        if(!(CheckPermissions.checkUserPermissions(sessionTokenFromClient, "scheduleBillboard"))){
            optionalMessage = "User does not have permission to remove a schedule (need schedule_billboard permission)";
            return;
        }
        // First see if the billboard actually exists
        try {
            billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", inboundData[1]); // Get the billboard id from the billboard name
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
            return;
        }
        if (!billboard_id.equals("")) {
            // Then find the schedule id to see if a schedule exists for the billboard at that time
            try {
                schedule_id = DBInteract.getValueAnd("id", "schedule", "billboard_id", billboard_id, "start_time", inboundData[2]); // Get the schedule id from the billboard name and a time
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
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error deleting the schedule: " + e.getMessage();
                }
                // And also the data in billboard table linking to the recently deleted schedule.
                try {
                    // edit status
                    // see if the billboard has any other schedules
                    try {
                        DBInteract.getValue("id", "schedule", "billboard_id", billboard_id); // Will throw an error if nothing exists
                    }
                    catch (Exception e){ // No other schedules exits, this is fine
                        e.printStackTrace();
                        String updateBillboardStatus = DBInteract.updateColumnWhereId("billboard", "status", "0", billboard_id);
                        DBInteract.dbExecuteCommand(updateBillboardStatus);
                    }
                    commandSucceeded = true;
                    optionalMessage = "Schedule deleted successfully";
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                    optionalMessage = "Error deleting the schedule: " + e.getMessage();
                }
            }
        }
    }

    public static void listSchedules(){
        sessionTokenFromClient = inboundData[1];
        if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        // Checks if the current user has the permission to list schedules
        if(!(CheckPermissions.checkUserPermissions(sessionTokenFromClient, "scheduleBillboard"))){
            optionalMessage = "User does not have permission to list schedule (need schedule_billboard permission)";
            return;
        }

        String getScheduleDataQuery = DBInteract.selectScheduleJoinUserAndBillboard();
        System.out.println(getScheduleDataQuery);
        String[][] results;
        try{
            results = DBInteract.getScheduleData(getScheduleDataQuery);
        }
        catch (SQLException e){
            System.out.println(e.toString());
            optionalMessage = "Failed to get schedule list:" + e.getMessage();
            return;
        }
        System.out.println("RESULTS ARE " + Arrays.deepToString(results));
        commandSucceeded = true;
        optionalMessage = "List of schedules successfully returned";
        outboundData2D = results;
    }

}
