package BillboardControlPanel.UnitTest;

import BillboardControlPanel.Model.DBInteract;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ModelTest {
    Connection connection;

    // DBInteract command functions are surrounded in try catch resulting in the instance connection being discarded at the end of the function;
//    @After
//    public void closeConnection() throws SQLException {
//        connection.close();
//    }
//
//    @After
//    public void deleteSingle(){
//        String command = DBInteract.commandTargetFromTable("Delete", "user", "email", "testEmail1");
//        DBInteract.dbExecuteCommand(command);
//    }


    @Test
    public void testConnection(){
        String command = DBInteract.createUser("testEmail1", "testPass", false, false, false, false);
        DBInteract.dbExecuteCommand(command);
    }

    @Test
    public void queryAll() throws SQLException {
        String command = DBInteract.selectAll("user");
        ResultSet rs = DBInteract.dbQueryCommand(command);
    }

//    @Test
//    public void querySingle() throws SQLException {
//        String command = DBInteract.commandTargetFromTable("SELECT *","user", "email", "testEmail1");
//        ResultSet rs = DBInteract.dbQueryCommand(command);
//    }

    @Test
    public void updateCol() throws SQLException {
        String command = DBInteract.updateColumn("user", "name", "newName2", "32");
        DBInteract.dbExecuteCommand(command);
    }







}
