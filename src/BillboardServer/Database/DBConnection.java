package BillboardServer.Database;

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

        }
        catch (Exception e) {
            System.err.println("Fatal error: could not connect to the database.");
            System.out.println("Please make sure a compatible database is running and try again.");
            System.exit(0);
        }
    }

    public static Connection getInstance() {
        if(instance == null){
            new DBConnection();
        }
        return instance;
    }

    /**
     * Method for returning just the name of the database entered in the db.props file. Ensures that the database can be any name,
     *  and the program will still function as long as the name of the database created is the same as in the db.props file.
     * @return
     */
    public static String getDatabaseName() {
        Properties props = new Properties();
        FileInputStream input = null;
        String databaseName = "";
        try {
            input = new FileInputStream("./db.props");
            props.load(input);
            input.close();

            databaseName = props.getProperty("jdbc.schema");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databaseName;
    }
    }
