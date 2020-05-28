package BillboardServer.ServerLogic;
import BillboardServer.Database.DBInteract;
import BillboardServer.Misc.SessionToken;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * This function uses the session token entered to see if the currently logged in user has the permission for a function.
 * Uses case statements using the functionToCheckPermissionFor String.
 */
public class CheckPermissions {
    public static boolean checkUserPermissions(String sessionToken, String functionToCheckPermissionFor) throws SQLException {
        ServerVariables.doesUserHavePermissions = false;
        HashMap<String, String> usersSessionTokens1 = SessionToken.usersSessionTokens;
        String username = usersSessionTokens1.get(sessionToken);
        // Gets the user id from user table with the getUserID method in DBInteract. Then, use that id to
        // get the user's permissions using the getPermissions function in DBInteract.
        // Gets the current users id, then gets their permissions.
        String userID = DBInteract.getUserId(username);
        String[] permissions = DBInteract.getPermissions(userID);
        // Converts the String array of permissions to an int array, so the permissions can be checked in the case statements.
        int sizeOfPermissionsArray = permissions.length;
        int[] permissionsArrayOfInt = new int[sizeOfPermissionsArray];
        for (int i = 0; i<sizeOfPermissionsArray; i++){
            permissionsArrayOfInt[i] = Integer.parseInt(permissions[i]);
        }

        // The permissions and their position in the array are as follows: 0=create_billboard, 1=edit_billboard, 2=schedule_billboard, 3=edit_user.
        switch (functionToCheckPermissionFor){
            //----- UserFunctions-----//
            case "createUser": { //Seems that this case works.
                if(permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            case "deleteUser": { // Works.
                if(permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            case "listUsers": { // Works.
                if(permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            case "editPermissions": { // Works
                if (permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            case "getPermissions": { // Works.
                if (permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            case "setUserPassword": { // Works.
                if (permissionsArrayOfInt[3] == 1){
                    ServerVariables.doesUserHavePermissions = true;
                    break;
                }
            }
            //-----End of UserFunctions-----//

        }
        return ServerVariables.doesUserHavePermissions;
    }
}
