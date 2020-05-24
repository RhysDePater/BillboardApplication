package BillboardServer.Database;

import BillboardControlPanel.Model.DBConnection;

import java.sql.*;

public class DBInteract {

    //RUN SQL COMMAND FUNCTIONS
    /**
     * Runs an Execute command for any given 'valid' SQL command
     * @param command sql command to be executed
     */
    public static void dbExecuteCommand(String command) throws SQLException {
            Connection connection = DBConnection.getInstance();
            Statement st = connection.createStatement();
            st.execute("USE cab302");
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
    public static PreparedStatement createUserPreparedStatement(String username, String password, byte[] saltValue) {
        String sql= "INSERT INTO user (username, password, salt) VALUES (" +
                "'" + username + "'," +
                " '" + password + "'," +
                " ?)";
        PreparedStatement ps = null;
        try{
            Connection connection =  DBConnection.getInstance(); // Get a new database connection
            ps = connection.prepareStatement(sql); // Create the prepared statement with the above string
            ps.setBytes(1, saltValue); // Add the salt value where there is a ?
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

    public static String addSchedule(String user_id, String billboard_id, String start_time, String duration){
        return "INSERT INTO schedule (user_id, billboard_id, start_time, duration) VALUES (" +
                user_id + ", " +
                "'" + billboard_id + "'," +
                " '" + start_time + "'," +
                "'" + duration + "')";
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

    public static String updatePermission(String user_id, int create_billboard, int edit_billboard, int schedule_billboard, int edit_user ) {
        return "UPDATE permission SET create_billboard ='" + create_billboard
                + "', edit_billboard ='"  + edit_billboard
                + "', schedule_billboard ='" + schedule_billboard
                + "', edit_user ='" + edit_user
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
    public static String[][] getUserData(String queryCommand) throws NullPointerException {
        try {
            ResultSet rs = dbQueryCommand(queryCommand);
            int rowCount = getRowCount(rs);
            int colCount = getColCount(rs);
            String[] colNames = getColNames(queryCommand);
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
        } catch (SQLException e){
            System.out.println(e);
            return null;
        }

    }

    /**
     * Queries a table, parses resultSet metadata, returning column names
     *
     * @param commandToQuery an sql command to return the table which column names are to be retrieved from
     * @return ReturnType=String[]: containing column names
     */
    public static String[] getColNames(String commandToQuery) throws NullPointerException {
        try {
            String formattedQuery = commandToQuery + " LIMIT 1";
            ResultSet rs = DBInteract.dbQueryCommand(formattedQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = getColCount(rs);
            String[] colNames = new String[colCount];
            for (int i = 1; i <= colCount; ++i) {
                colNames[i - 1] = rsmd.getColumnName(i);
            }
            return colNames;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
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
            System.err.println(e);
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
