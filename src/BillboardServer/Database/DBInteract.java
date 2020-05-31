package BillboardServer.Database;

import BillboardServer.Database.DBConnection;
import BillboardServer.Misc.SessionToken;
import BillboardServer.ServerLogic.ServerLogic;
import BillboardServer.ServerLogic.UserFunctions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;

import static BillboardServer.Misc.SessionToken.bytesToString;
import static java.lang.System.exit;

public class DBInteract {

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user ("
            + "id INT UNSIGNED AUTO_INCREMENT,"
            + "username VARCHAR(255) UNIQUE NOT NULL,"
            + "password VARCHAR(255) NOT NULL,"
            + "salt VARCHAR(255) NOT NULL," //CHANGE BACK TO VARBINARY(32) IF I CAN't GET SaltString To work.
            + "PRIMARY KEY (id),"
            + "UNIQUE KEY (username));";

    private static final String CREATE_PERMISSIONS_TABLE = "CREATE TABLE If NOT EXISTS permission("
            + "id INT UNSIGNED AUTO_INCREMENT,"
            + "user_id INT UNSIGNED NOT NULL,"
            + "create_billboard TINYINT(1) NOT NULL,"
            + "edit_billboard TINYINT(1) NOT NULL,"
            + "schedule_billboard TINYINT(1) NOT NULL,"
            + "edit_user TINYINT(1) NOT NULL,"
            + "PRIMARY KEY (id),"
            + "CONSTRAINT fk_permission_user FOREIGN KEY (user_id) REFERENCES user(id));";

    private static final String CREATE_BILLBOARD_TABLE = "CREATE TABLE IF NOT EXISTS billboard("
            + "id INT UNSIGNED AUTO_INCREMENT NOT NULL,"
            + "user_id INT UNSIGNED NOT NULL,"
            // Schedule id column used to be here
            + "billboard_name VARCHAR(255) UNIQUE,"
            + "xml_data MEDIUMTEXT NOT NULL,"
            + "status BOOLEAN NOT NULL DEFAULT false,"
            + "PRIMARY KEY (id),"
            + "CONSTRAINT fk_billboard_user FOREIGN KEY (user_id) REFERENCES user(id));";

    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS schedule("
            + "id INT UNSIGNED AUTO_INCREMENT NOT NULL,"
            + "user_id INT UNSIGNED NOT NULL,"
            + "billboard_id INT UNSIGNED NOT NULL,"
            + "start_time timestamp default current_timestamp,"
            + "duration INT UNSIGNED,"
            + "recur_after INT UNSIGNED default 0,"
            + "PRIMARY KEY (id),"
            + "CONSTRAINT fk_schedule_user FOREIGN KEY (user_id) REFERENCES user(id),"
            + "CONSTRAINT fk_schedule_billboard FOREIGN KEY (billboard_id) REFERENCES billboard (id));";

    // Based on material from cab302 week 9 assignment Q&A lecture.
    private static String hashedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // The hashing algorithm.
        byte[] hashedPassword = md.digest(password.getBytes());
        String hashedPasswordString = SessionToken.bytesToString(hashedPassword);
        return hashedPasswordString;
    }

    /**
     * Creates the database tables if they do not exist in the database. Also inserts an admin with a hashed + salted password.
     */
    // CREATES THE DATABASE AND IT'S TABLES, ALONGSIDE THE ADMIN
    public static void createDatabaseTables() {
        try {
            // Tables are created from strings of SQL commands.
            dbExecuteCommand(CREATE_USER_TABLE);
            dbExecuteCommand(CREATE_PERMISSIONS_TABLE);
            dbExecuteCommand(CREATE_BILLBOARD_TABLE);
            dbExecuteCommand(CREATE_SCHEDULE_TABLE);
            // Successfully creates an ADMIN user if none exists in the database.
            if (dbQueryForADMINUser("SELECT COUNT(id) FROM user WHERE username = 'ADMIN'") == 0){
                String hashedPassword = hashedPassword("pass"); // This is the admin's password.
                byte[] salt = UserFunctions.GenerateSalt();
                String saltString = SessionToken.bytesToString(salt);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                String hashAndSaltedPassword = bytesToString(md.digest((hashedPassword + saltString).getBytes()));

                dbExecuteCommand("INSERT INTO user VALUES (1, 'ADMIN','" + hashAndSaltedPassword + "','" + saltString + "');");
                dbExecuteCommand("INSERT INTO permission VALUES (1, 1, true, true, true, true);");
                System.out.println("Created ADMIN user.");
            }
        } catch (Exception e) {
            System.err.println("Fatal error: could not create tables");
            e.printStackTrace();
            exit(0);
        }
    }

    /**
     *
     * @param command Command entered to select the count of ID's with the username ADMIN in the database.
     * @return existenceOfAdmin as an int. If 0, then no user with ADMIN username exists, and therefore no admin exists for database.
     * @throws SQLException
     */
    public static int dbQueryForADMINUser(String command) throws SQLException {
        Connection connection = DBConnection.getInstance();
        Statement st = connection.createStatement();
        ResultSet existenceOfADMIN = st.executeQuery(command);
        st.close();
        existenceOfADMIN.next();
        return existenceOfADMIN.getInt(1);// Only returns one column, which is titled "COUNT(id)"; holds the number of users with username ADMIN.
    }

    //RUN SQL COMMAND FUNCTIONS
    /**
     * Runs an Execute command for any given 'valid' SQL command
     * @param command sql command to be executed
     */
    public static void dbExecuteCommand(String command) throws SQLException {
            Connection connection = DBConnection.getInstance();
            Statement st = connection.createStatement();
            // Method in st.execute gets the schema name from db.props. Ensures that the database with the name in that file is used.
            st.execute("USE " + BillboardServer.Database.DBConnection.getDatabaseName());
            st.execute(command);
            st.close();
    }

    /**
     * Runs a Query Command for any given 'valid' SQL command
     * @param command sql command to be executed
     * @return ResultSet containing queried data
     */
    public static ResultSet dbQueryCommand(String command) throws SQLException {
            Connection connection = DBConnection.getInstance();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(command);
            st.close();
            return rs;
        }


    //CREATE FUNCTIONS

    /**
     * SQL COMMAND to Create new user and Insert into database
     *
     * @param username  name input value
     * @param password  password input value
     * @param saltValue saltValue relative to user
     * @return ReturnType=String: Insert User command
     */
    public static String createUser(String username, String password, String saltValue) {
        return "INSERT INTO user (username, password, salt) VALUES (" +
                "'" + username + "'," +
                " '" + password + "'," +
                " " + saltValue + ")";
    }

    /**
     * SQL COMMAND to Create new user using prepared statements
     *
     * @param username  name input value
     * @param password  password input value
     * @param saltValue saltValue relative to user, as a byte array. This is why a prepared statement has to be used to insert the salt.
     * @return ReturnType=String: Insert User command
     */
    public static PreparedStatement createUserPreparedStatement(String username, String password, String saltValue) {
        String sql= "INSERT INTO user (username, password, salt) VALUES (" +
                "'" + username + "'," +
                " '" + password + "'," +
                " ?)";
        PreparedStatement ps = null;
        try{
            Connection connection =  DBConnection.getInstance(); // Get a new database connection
            ps = connection.prepareStatement(sql); // Create the prepared statement with the above string
            ps.setString(1, saltValue); // Add the salt value where there is a ?
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ps;
    }

    /**
     * Create user permissions IS CALLED DIRECTLY after create user
     *
     * @param create_billboard   tinyint value ? 0 : 1
     * @param edit_billboard     tinyint value ? 0 : 1
     * @param schedule_billboard tinyint value ? 0 : 1
     * @param edit_user          tinyint value ? 0 : 1
     * @return String command to create permission
     */
    public static String createPermission(int create_billboard, int edit_billboard, int schedule_billboard, int edit_user) {
        return "INSERT INTO permission (user_id, create_billboard, edit_billboard, schedule_billboard, edit_user) VALUES (LAST_INSERT_ID(), " +
                "'" + create_billboard + "'," +
                " '" + edit_billboard + "'," +
                "'" + schedule_billboard + "'," +
                " " + edit_user + ")";
    }

    public static String addSchedule(String user_id, String billboard_id, String start_time, String duration, String time_to_recur){
        return "INSERT INTO schedule (user_id, billboard_id, start_time, duration, recur_after) VALUES (" +
                user_id + ", " +
                "'" + billboard_id + "'," +
                " '" + start_time + "'," +
                " '" + duration + "'," +
                "'" + time_to_recur + "')";
    }

    /**
     * SQL CREATE COMMAND insert userPasswords' salt into database
     * Currently not in use because salt values are part of the user table
     * @param salt
     * @param userEmail
     * @return
     */
    public static String createSalt(byte[] salt, String userEmail) {
        String createSalt = "INSERT INTO salts (user_email, salt) VALUES ('" + userEmail + "','" + salt + "')";
        return createSalt;
    }


    /**
     * SQL command for adding a new billboard to the database.
     * NOTE: Although the data types for user_id and schedule_id are strings here, they are still listed as their actual data
     *      * types as they were set as in the models.sql file.
     * @param user_id user_id value.
     * @param xml_data xml_data is stored as a string.
     * @return returns the prepared statement string.
     */
    public static PreparedStatement createBillboardPreparedStatement(String user_id, String billboard_name, String xml_data) {
        String sql= "INSERT INTO billboard (user_id, billboard_name, xml_data) VALUES (" +
                "'" + user_id + "'," +
                " '" + billboard_name + "'," +
                " ?)";
        PreparedStatement ps = null;
        try{
            Connection connection =  DBConnection.getInstance(); // Get a new database connection
            ps = connection.prepareStatement(sql); // Create the prepared statement with the above string
            ps.setString(1, xml_data); // Add the xml string where there is a ?
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ps;
    }

    public static String setUserPassword(String username, String password, String saltValue){
        String sql = "UPDATE user SET password ='" + password + "', salt ='" + saltValue + "' WHERE username ='" + username + "';";
        return sql;
    }

    //SELECT FUNCTIONS
    /**
     * SQL QUERY to select all Rows from Table
     * @param table
     * @return ReturnType=String: Select query SQL command
     */
    public static String selectAll(String table){
        String select_query = "SELECT * from " + table;
        return select_query;
    }

    /**
     * SQL QUERY to select a limited amount of Rows from Table
     * @param table
     * @param limit how many rows to query
     * @return ReturnType=String: Select query SQL command
     */
    public static String selectAll(String table, int limit){
        String select_query = "SELECT * from " + table + " LIMIT " + limit;
        return select_query;
    }

    /**
     * SQL Query to select a specific row from a table
     * @param table
     * @param columnName Column to match
     * @param toMatch   String to match to database
     * @return ReturnType=String: Select query SQL command
     */
    public static String selectTarget(String table, String columnName, String toMatch){
        String selectQuery = "SELECT * from " + table + " WHERE " + columnName + "='" + toMatch +"'";
        return selectQuery;
    }

    /**
     * SQL Query to select a specific row and column from a table
     *
     * @param table
     * @param targetColumn the target column of info to be returned
     * @param columnName   Column to match
     * @param toMatch      String to match to database
     * @return ReturnType=String: Select query SQL command
     */
    public static String selectTarget(String table, String targetColumn, String columnName, String toMatch) {
        String selectQuery = "SELECT " + targetColumn + " from " + table + " WHERE " + columnName + "='" + toMatch + "'";
        return selectQuery;
    }

    /**
     * Inner joins user table and permission table and returns the joined table
     *
     * @return
     */
    public static String selectUserJoinPermission() {
        String selectQuery = "SELECT user.id, username, password, create_billboard, edit_billboard, schedule_billboard, edit_user FROM user INNER JOIN permission ON user.id = permission.user_id";
        return (selectQuery);
    }

    /**
     * Inner joins user table and permission table and returns the joined table
     *
     * @return
     */
    public static String selectUserJoinBillboard() {
        String selectQuery = "select username, billboard.billboard_name, billboard.xml_data, billboard.status FROM user INNER JOIN billboard ON user.id = billboard.user_id";
        return (selectQuery);
    }

    /**
     * Inner joins user table and permission table and returns the joined table
     *
     * @return
     */
    public static String selectScheduleJoinUserAndBillboard() {
        String selectQuery = "SELECT username, billboard.billboard_name, start_time, duration, recur_after from schedule INNER JOIN billboard on schedule.billboard_id = billboard.id INNER JOIN user on schedule.user_id = user.id";
        return (selectQuery);
    }

    /**
     * Selects a target from user inner joined permission and returns the target
     *
     * @param columnName column to match
     * @param toMatch    value to match
     * @return
     */
    public static String selectTargetUserJoinPermission(String columnName, String toMatch) {
        String selectQuery = "SELECT user.id, username, password, create_billboard, edit_billboard, schedule_billboard, edit_user FROM user INNER JOIN permission ON user.id = permission.user_id WHERE "
                + columnName + "='" + toMatch + "'";
        return (selectQuery);
    }

    //UPDATE FUNCTIONS

    /**
     * SQL UPDATE COMMAND update column from a given table using id as identifier
     *
     * @param tableName  table name to be updated
     * @param columnName column name to be updated
     * @param newValue   new value to be inserted
     * @param id         id of row
     * @return ReturnType=String:
     */
    public static String updateColumnWhereId(String tableName, String columnName, String newValue, String id) {
        String updateColumn = "UPDATE " + tableName + " SET " + columnName + "='" + newValue + "' WHERE id=" + id;
        return updateColumn;
    }

    public static PreparedStatement updateColumnWhereIdToNull(String tableName, String columnName, String id) {
        String updateColumn = "UPDATE " + tableName + " SET " + columnName + "=? WHERE id=" + id;
        PreparedStatement ps = null;
        try {
            Connection connection = DBConnection.getInstance(); // Get a new database connection
            ps = connection.prepareStatement(updateColumn); // Create the prepared statement with the above string
            ps.setString(1, null); // Add the salt value where there is a ?
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ps;
    }

    public static String updatePermission(String user_id, int create_billboard, int edit_billboard, int schedule_billboard, int edit_user ) {
        return "UPDATE permission SET create_billboard ='" + create_billboard
                + "', edit_billboard ='"  + edit_billboard
                + "', schedule_billboard ='" + schedule_billboard
                + "', edit_user ='" + edit_user
                + "' WHERE user_id=" + user_id;
    }

    public static String updatePermissionCreateBillboard(String user_id, String value ) {
        return "UPDATE permission SET create_billboard ='" + value
                + "' WHERE user_id=" + user_id;
    }

    public static String updatePermissionEditBillboard(String user_id, String value ){
        return "UPDATE permission SET edit_billboard ='" + value
                + "' WHERE user_id=" + user_id;
    }
    public static String updatePermissionScheduleBillboard(String user_id, String value ) {
        return "UPDATE permission SET schedule_billboard ='" + value
                + "' WHERE user_id=" + user_id;
    }

    public static String updatePermissionEditUser(String user_id, String value ) {
        return "UPDATE permission SET edit_user ='" + value
                + "' WHERE user_id=" + user_id;
    }


    //DELETE FUNCTIONS
    /**
     * SQL QUERY to DELETE all Rows from Table
     * @param table
     * @return ReturnType=String: Select query SQL command
     */
    public static String deleteAll(String table){
        String deleteQuery = "DELETE * from " + table;
        return deleteQuery;
    }

    /**
     * SQL DELETE COMMAND delete row by id
     * @param table
     * @param targetID  id of row to be deleted
     * @return
     */
    public static String deleteTarget(String table, String targetColumn, String targetID){
        return "DELETE from " + table + " WHERE " + targetColumn + "='" + targetID + "'";
    }

    /**
     * SQL DELETE COMMAND delete rows from tables with correlating tar
     *
     * @param table1
     * @param table2
     * @param table1ColName table1 col name to check
     * @param table2ColName table2 col name to check
     * @param target        target unique value
     * @return
     */
    public static String deleteInnerJoin(String table1, String table2, String table1ColName, String table2ColName, String targetColumn, String target) {
        String innerJoinDelete = "DELETE " + table1 + ", " + table2 + " FROM " + table1 + " INNER JOIN " + table2 + " ON" +
                " " + table2 + "." + table2ColName + "=" + table1 + "." + table1ColName + " WHERE " + table1 + "." + targetColumn + "='" + target + "'";
        //"DELETE user, permission FROM user INNER JOIN permission ON permission.user_id=user.id WHERE user.id='2'"
        return innerJoinDelete;
    }

    //GETS

    /**
     * Queries the user Table inner joined with permission table and returns results
     *
     * @param queryCommand TAKES A QUERY COMMAND FOR USER TABLE INNER JOINED WITH PERMISSIONS
     * @return ReturnType=String[][]: containing results from query
     */
    public static String[][] getUserData(String queryCommand) throws SQLException {
            ResultSet rs = dbQueryCommand(queryCommand);
            int rowCount = getRowCount(rs);
            int colCount = getColCount(rs);
            String[] colNames = getColNames(queryCommand);
            if (colNames == null){
                throw new SQLException("Internal server error, no column names found");
            }
            rs.first();
            String[][] userList = new String[rowCount][colCount];
            for (int i = 0; i < rowCount; ++i) {
                String id = Integer.toString(rs.getInt(colNames[0]));
                String username = rs.getString(colNames[1]);
                String password = rs.getString(colNames[2]);
                Boolean createBillboard = rs.getBoolean(colNames[3]);
                Boolean editAllBillboards = rs.getBoolean(colNames[4]);
                Boolean scheduleBillboards = rs.getBoolean(colNames[5]);
                Boolean editUsers = rs.getBoolean(colNames[6]);
                String[] colItem = new String[]{id, username, password, String.valueOf(createBillboard), String.valueOf(editAllBillboards), String.valueOf(scheduleBillboards), String.valueOf(editUsers)};
                for (int j = 0; j < colCount; ++j) {
                    userList[i][j] = colItem[j];
                }
                rs.next();
            }
                return userList;
    }

    /**
     * Gets the user who created a billboard and the billboard name plus other stuff
     *
     * @param queryCommand TAKES A QUERY COMMAND FROM selectUserJoinBillboard
     * @return ReturnType=String[][]: containing results from query
     */
    public static String[][] getBillboardData(String queryCommand) throws SQLException {
        ResultSet rs = dbQueryCommand(queryCommand);
        int rowCount = getRowCount(rs);
        int colCount = getColCount(rs);
        String[] colNames = getColNames(queryCommand);
        if (colNames == null){
            throw new SQLException("Internal server error, no column names found");
        }
        rs.first();
        String[][] billboardList = new String[rowCount][colCount];
        for (int i = 0; i < rowCount; ++i) {
            String username = rs.getString(colNames[0]);
            String billboard_name = rs.getString(colNames[1]);
            String xml_data = rs.getString(colNames[2]);
            String status = rs.getString(colNames[3]);
            String[] colItem = new String[]{username, billboard_name, xml_data, status};
            for (int j = 0; j < colCount; ++j) {
                billboardList[i][j] = colItem[j];
            }
            rs.next();
        }
        return billboardList;
    }

    /**
     * Gets the scheduled billboards, the user who created them, and the duration and start time
     *
     * @param queryCommand TAKES A QUERY COMMAND FROM selectScheduleJoinUserAndBillboard
     * @return ReturnType=String[][]: containing results from query
     */
    public static String[][] getScheduleData(String queryCommand) throws SQLException {
        ResultSet rs = dbQueryCommand(queryCommand);
        int rowCount = getRowCount(rs);
        int colCount = getColCount(rs);
        String[] colNames = getColNames(queryCommand);
        if (colNames == null){
            throw new SQLException("Internal server error, no column names found");
        }
        rs.first();
        String[][] billboardList = new String[rowCount][colCount];
        for (int i = 0; i < rowCount; ++i) {
            String username = rs.getString(colNames[0]);
            String billboard_name = rs.getString(colNames[1]);
            String start_time = rs.getTimestamp(colNames[2]).toString();
            String duration = rs.getString(colNames[3]);
            String recur_after = rs.getString(colNames[4]);
            String[] colItem = new String[]{username, billboard_name, start_time.substring(0,start_time.length()-2), duration, recur_after}; // timestamps store less then a second which is not part of our formatting, so cut if off
            for (int j = 0; j < colCount; ++j) {
                billboardList[i][j] = colItem[j];
            }
            rs.next();
        }
        return billboardList;
    }

    /**
     * Queries a table, parses resultSet metadata, returning column names
     *
     * @param commandToQuery an sql command to return the table which column names are to be retrieved from
     * @return ReturnType=String[]: containing column names
     */
    public static String[] getColNames(String commandToQuery) throws NullPointerException, SQLException {
            String formattedQuery = commandToQuery + " LIMIT 1";
            ResultSet rs = DBInteract.dbQueryCommand(formattedQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = getColCount(rs);
            String[] colNames = new String[colCount];
            for (int i = 1; i <= colCount; ++i) {
                colNames[i - 1] = rsmd.getColumnName(i);
            }
            return colNames;
    }

    /**
     * Gets just the password from the user table where the username equals whatever is provided to the function
     * Used to login using a given username
     */
    public static String getPassword(String username) throws SQLException {
        String getPasswordQuery = "SELECT password from user WHERE username = '" + username + "'";
        ResultSet rs = DBInteract.dbQueryCommand(getPasswordQuery);
        if (!rs.isBeforeFirst() ) { // If block executes if there is no data "isBeforeFirst returns false if the cursor is not before the first record or if there are no rows in the ResultSet."
            throw new SQLException ("No password with username: " + username);
        }
        rs.next();
        return rs.getString("password");
    }

    /**
     * Gets the user_id where the username equals whatever is provided to the function
     */
    public static String getUserId(String username) throws SQLException {
        String getPasswordQuery = "SELECT id from user WHERE username = '" + username + "'";
        ResultSet rs = DBInteract.dbQueryCommand(getPasswordQuery);
        if (!rs.isBeforeFirst() ) { // If block executes if there is no data "isBeforeFirst returns false if the cursor is not before the first record or if there are no rows in the ResultSet."
            throw new SQLException ("No user with username: " + username);
        }
        rs.next();
        return rs.getString("id");
    }

    /**
     * Gets the value in one column of a row where a different column (of the same row) has a value equal to the value provided to the function
     */
    public static String getValue(String target_column, String table_name, String filter_column, String filter_value) throws SQLException {
        String sqlGet = "SELECT " + target_column + " from " + table_name + " WHERE " + filter_column + " = '" + filter_value + "'";
        ResultSet rs = DBInteract.dbQueryCommand(sqlGet);
        if (!rs.isBeforeFirst() ) { // If block executes if there is no data "isBeforeFirst returns false if the cursor is not before the first record or if there are no rows in the ResultSet."
            throw new SQLException ("No value at " + target_column + " where : " + filter_column + " = " + filter_value);
        }
        rs.next();
        return rs.getString(target_column);
    }

    public static String[] getPermissions(String user_id) throws SQLException {
        String getPermissionsdQuery = "SELECT create_billboard, edit_billboard, schedule_billboard, edit_user from permission WHERE user_id = '" + user_id + "'";
        System.out.println(getPermissionsdQuery);
        ResultSet rs = DBInteract.dbQueryCommand(getPermissionsdQuery);
        int colCount = getColCount(rs);
        if (!rs.isBeforeFirst() ) { // If block executes if there is no data "isBeforeFirst returns false if the cursor is not before the first record or if there are no rows in the ResultSet."
            throw new SQLException ("User_id " + user_id + " has no permissions");
        }
        rs.next();
        return new String[]{rs.getString("create_billboard"), rs.getString("edit_billboard"), rs.getString("schedule_billboard"), rs.getString("edit_user")};
    }

    /**
     * Similar to getValue. Emulates the sql query: SELECT column FROM table WHERE column = value AND column2 = value2;
     */
    public static String getValueAnd(String target_column, String table_name, String filter_column, String filter_value, String filter_column2, String filter_value2) throws SQLException {
        String sqlGet = "SELECT " + target_column + " from " + table_name + " WHERE " + filter_column + " = '" + filter_value + "' AND " + filter_column2 + "= '" + filter_value2 + "'";
        ResultSet rs = DBInteract.dbQueryCommand(sqlGet);
        if (!rs.isBeforeFirst() ) { // If block executes if there is no data "isBeforeFirst returns false if the cursor is not before the first record or if there are no rows in the ResultSet."
            throw new SQLException ("No value at " + target_column + " where : " + filter_column + " = " + filter_value);
        }
        rs.next();
        return rs.getString(target_column);
    }

    //private functions
    /**
     * Gets table metadata(column count) from result set
     * @param resultSet result set to be counted
     * @return int: return the amount of columns in any given result set
     */
    private static int getColCount(ResultSet resultSet){
        int colCount;
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            colCount = rsmd.getColumnCount();
            return colCount;
        }catch (SQLException e){
            System.err.println(e.toString());
            return 0;
        }
    }

    /**
     * Gets table metadata(row count) from result set
     * @param resultSet result set to be counted
     * @return int: return the amount of rows in any given result set
     */
    private static int getRowCount(ResultSet resultSet){
        int rowCount;
        try {
            if(resultSet.last()){
                rowCount = resultSet.getRow();
                return rowCount;
            } else{
                return 0;
            }
        }catch (SQLException e){
            System.err.println(e);
            return 0;
        }
    }
    /////////////////////////////////////////______________________________
}
