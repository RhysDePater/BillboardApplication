package BillboardControlPanel.UnitTest;

import BillboardControlPanel.Model.DBInteract;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;


public class ControllerTest {

    ResultSet resultSet;
    @Before
    public void getResultSet(){
        resultSet = DBInteract.dbQueryCommand(DBInteract.selectAll("user"));
    }

    @Test
    public void testGetColNames(){
        String[] toTest = DBInteract.getColNames("user");

    }

    @Test
    public void testGetUserInfo(){
        DBInteract.getUserInfo(DBInteract.selectAll("user"));
    }

    @Test
    public void checkMetaData(){
        ResultSet rs = DBInteract.dbQueryCommand(DBInteract.selectAll("billboard"));
        String[] colNames = DBInteract.getColNames("billboard");
        System.out.println("check");
        for (String string:colNames
             ) {
            System.out.println(string);
        }
    }

}
