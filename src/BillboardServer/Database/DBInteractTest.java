package BillboardServer.Database;

import BillboardControlPanel.ModelOUTDATED.DBInteract;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;


class DBInteractTest extends DBInteract {

    static ResultSet rs;

    @BeforeAll
    public static void getResultSet() {
        rs = dbQueryCommand(selectUserJoinPermission());
    }

    @Test
    public void getUserInfo() {
        String[][] data = getUserData(selectUserJoinPermission());
    }

    @Test
    public void createNewPermission() {
//        dbExecuteCommand(createPermission(true,true,true,true));
    }
}