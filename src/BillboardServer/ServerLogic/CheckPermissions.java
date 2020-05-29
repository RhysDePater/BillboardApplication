package BillboardServer.ServerLogic;
import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.SessionToken;

import java.sql.SQLException;
import java.util.HashMap;

import static BillboardServer.Misc.SessionToken.getUser;
import static BillboardServer.ServerLogic.ServerVariables.optionalMessage;
import static BillboardServer.ServerLogic.ServerVariables.sessionTokenFromClient;

/**
 * This function uses the session token entered to see if the currently logged in user has the permission for a function.
 * Uses case statements using the functionToCheckPermissionFor String.
 */
public class CheckPermissions {
    public static boolean checkUserPermissions(String sessionToken, String functionToCheckPermissionFor)  {
        ServerVariables.doesUserHavePermissions = false;
        String username = SessionToken.getUser(sessionToken);
        // Gets the user id from user table with the getUserID method in DBInteract. Then, use that id to
        // get the user's permissions using the getPermissions function in DBInteract.
        // Gets the current users id, then gets their permissions.
        String userID;
        String[] permissions;
        try{
            userID = DBInteract.getUserId(username);
            permissions = DBInteract.getPermissions(userID);
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        // Converts the String array of permissions to an int array, so the permissions can be checked in the case statements.
        int sizeOfPermissionsArray = permissions.length;
        int[] permissionsArrayOfInt = new int[sizeOfPermissionsArray];
        for (int i = 0; i<sizeOfPermissionsArray; i++){
            permissionsArrayOfInt[i] = Integer.parseInt(permissions[i]);
        }

        // The permissions and their position in the array are as follows: 0=create_billboard, 1=edit_billboard, 2=schedule_billboard, 3=edit_user.
        int create_billboard = permissionsArrayOfInt[0];
        int edit_billboard = permissionsArrayOfInt[1];
        int schedule_billboard = permissionsArrayOfInt[2];
        int edit_user = permissionsArrayOfInt[3];
        System.out.println("Current user permissions for command " + functionToCheckPermissionFor + " are: " + create_billboard + edit_billboard + schedule_billboard + edit_user);
        switch (functionToCheckPermissionFor){
            //----- UserFunctions-----//
            case "createUser":
            case "deleteUser":
            case "listUsers":
            case "editPermissions":
            case "getPermissions":
            case "setUserPassword": {
                if(edit_user == 1){
                    ServerVariables.doesUserHavePermissions = true;
                }
                break;
            }
            //----- Billboard and Schedule Functions-----//
            // 0=create_billboard, 1=edit_billboard, 2=schedule_billboard, 3=edit_user.
            case "createBillboard": {
                if (create_billboard == 1){
                    ServerVariables.doesUserHavePermissions = true;
                }
                break;
            }
            case "editBillboard": {
                if (edit_billboard == 1){
                    ServerVariables.doesUserHavePermissions = true;
                }
                break;
            }
            case "scheduleBillboard":
            case "removeBillboardFromSchedule": {
                if (schedule_billboard == 1){
                    ServerVariables.doesUserHavePermissions = true;
                }
                break;
            }
        }
        System.out.println("Can user perform action? " + ServerVariables.doesUserHavePermissions);
        return ServerVariables.doesUserHavePermissions;
    }

    // A specific case for trying to edit/delete billboard
    public static boolean checkBillboardPermissionsDeleteOrEdit(String billboard_id) {
        // Annoying permission check
        boolean currently_scheduled = false;
        boolean own_billboard = false;
        // See if the current user owns the billboard they're trying to edit or delete
        try{
            String userIdOfCurrentBillboard = DBInteract.getValue("user_id", "billboard", "id", billboard_id);
            if (userIdOfCurrentBillboard.equals(DBInteract.getUserId(getUser(sessionTokenFromClient)))) {
                own_billboard = true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        // See if the billboard is currently scheduled
        try{
            String scheduleIdOfCurrentBillboard = DBInteract.getValue("schedule_id", "billboard", "id", billboard_id);
            if (scheduleIdOfCurrentBillboard != null ) {
                currently_scheduled = true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        System.out.println("Billboard currently scheduled: " + currently_scheduled + ". Own billboard: " + own_billboard);
        // Permission check for the different conditions
        if(own_billboard && !currently_scheduled){
            if (CheckPermissions.checkUserPermissions(sessionTokenFromClient, "createBillboard")){
                return true;
            }
            else{
                optionalMessage = "User does not have permission to delete or edit their own billboard (requires create_billboard permission).";
            }
        }
        if (!own_billboard || currently_scheduled) {
            if (CheckPermissions.checkUserPermissions(sessionTokenFromClient, "editBillboard")){
                return true;
            }
            else{
                optionalMessage = "User does not have permission to delete or edit others (or scheduled) billboards (requires edit_billboard permission).";
            }
        }
        return false;
    }
}
