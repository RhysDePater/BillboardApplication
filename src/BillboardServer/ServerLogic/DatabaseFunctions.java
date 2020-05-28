package BillboardServer.ServerLogic;

import BillboardServer.Database.DBInteract;

import java.sql.SQLException;
import java.util.Arrays;

import static BillboardServer.Misc.SessionToken.isSessionTokenValid;

public class DatabaseFunctions extends ServerVariables{

    public static void getColumns() {
        sessionTokenFromClient = inboundData[2];
        String table = inboundData[1];
        if (!isSessionTokenValid(sessionTokenFromClient)) { //If the session token is valid, then the query can attempt to run.
            optionalMessage = "Session token is invalid or expired. The user will need to log in again.";
            return;
        }
        try {
            String[] ColumnNames = DBInteract.getColNames("SELECT * FROM " + table);
            System.out.println(Arrays.toString(ColumnNames));
            commandSucceeded = true;
            outboundData1D = ColumnNames; // Assign it to a server variable
            optionalMessage = "Columns successfully return for: " + table;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            optionalMessage = "Error getting column names: " + e.getMessage();
        }
    }
}
