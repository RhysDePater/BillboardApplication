package BillboardServer.Database;

import BillboardControlPanel.Helper.ControllerHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {

    private static Connection instance = null;


    private DBConnection(){
        Properties props = new Properties();
        FileInputStream input = null;

        try{
            input = new FileInputStream("./db.props");
            props.load(input);
            input.close();

            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            instance = DriverManager.getConnection(url + "/" +  schema, username, password);

        } catch(SQLException sqle) {
            System.err.print(sqle);
            ControllerHelper.returnMessage("Cannot connect");
        } catch(FileNotFoundException fnfe){
            System.err.println(fnfe);
            ControllerHelper.returnMessage("file not found");
        } catch(IOException ex) {
            System.err.println(ex);
        }
    }

    public static Connection getInstance() {
        if(instance == null){
            new DBConnection();
        }
        return instance;
    }

}
