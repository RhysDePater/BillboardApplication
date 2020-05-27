package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.SessionToken;

import java.sql.SQLException;

import static BillboardServer.Misc.SessionToken.isSessionTokenValid;

public class ScheduleFunctions extends ServerVariables {
    public static void addSchedule(){
        // TODO
        // Add restrictions so the user can't enter a schedule which already exists
        // Currently if this is the case, the deleteSchedule won't know which one to delete, and just delete the first one
        // But, there shouldn't be a case where two of the same billboard want to start at the same time
        //{"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
        String billboard_id;
        String user_id;
        sessionTokenFromClient = inboundData[4];
        if(!isSessionTokenValid(sessionTokenFromClient)){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
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
        String QueryAddSchedule = DBInteract.addSchedule(user_id, billboard_id, inboundData[2], inboundData[3]);
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
            String updateSchedule = DBInteract.updateColumnWhereId("billboard", "schedule_id", schedule_id, billboard_id);
            DBInteract.dbExecuteCommand(updateSchedule);
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
        if (isSessionTokenValid(sessionTokenFromClient)) {
            // First see if the billboard actually exists
            try {
                billboard_id = DBInteract.getValue("id", "billboard", "billboard_name", inboundData[1]); // Get the billboard id from the billboard name
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                optionalMessage = "Billboard id for supplied Billboard name not found: Billboard doesn't exist";
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
                        commandSucceeded = true;
                        optionalMessage = "Schedule deleted successfully";
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        optionalMessage = "Error deleting the schedule: " + e.getMessage();
                    }
                }
            }
        } else if (!isSessionTokenValid(sessionTokenFromClient)) {
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
        }
    }

    public static void listSchedules(){
        if(!isSessionTokenValid(inboundData[1])){
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
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
            outboundData1D = new String[]{""};
            return;
        }
        commandSucceeded = true;
        optionalMessage = "List of schedules successfully returned";
        outboundData2D = results;
    }

}
