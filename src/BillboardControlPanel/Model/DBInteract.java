package BillboardControlPanel.Model;

import java.sql.*;

public class DBInteract {

    //RUN SQL COMMAND FUNCTIONS
    /**
     * Runs an Execute command for any given 'valid' SQL command
     * @param command sql command to be executed
     */
    public static void dbExecuteCommand(String command){
        try {
            Connection connection = DBConnection.getInstance();
            Statement st = connection.createStatement();
            st.execute("USE cab302");
            st.execute(command);
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    };

    /**
     * Runs a Query Command for any given 'valid' SQL command
     * @param command sql command to be executed
     * @return ResultSet containing queried data
     */
    public static ResultSet dbQueryCommand(String command){
        try {
            Connection connection =  DBConnection.getInstance();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(command);
            st.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    };


    //CREATE FUNCTIONS
    /**
     * SQL COMMAND to Create new user and Insert into database
     * @param email
     * @param password
     * @param createBillboard
     * @param editAllBillboards
     * @param scheduleBillboards
     * @param editUsers
     * @return ReturnType=String: Insert User command
     */
    public static String createUser(String email, String password, Boolean createBillboard, Boolean editAllBillboards, Boolean scheduleBillboards, Boolean editUsers){
        String createUser = "INSERT INTO user (email, password, create_billboards, edit_billboard, schedule_billboards, edit_users) VALUES (" +
                "'" + email + "'," +
                " '" + password + "'," +
                " " + createBillboard + "," +
                " " + editAllBillboards + "," +
                " " + scheduleBillboards + "," +
                " " + editUsers + ")";
        return createUser;
    }

    /**
     * SQL CREATE COMMAND insert userPasswords' salt into database
     * @param salt
     * @param userEmail
     * @return
     */
    public static String createSalt(byte[] salt, String userEmail){
        String createSalt = "INSERT INTO salts (user_email, salt) VALUES ('"+ userEmail +"','"+salt+"')";
        return createSalt;
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
     * @param table
     * @param targetColumn the target column of info to be returned
     * @param columnName Column to match
     * @param toMatch   String to match to database
     * @return ReturnType=String: Select query SQL command
     */
    public static String selectTarget(String table, String targetColumn ,String columnName, String toMatch){
        String selectQuery = "SELECT " + targetColumn + " from " + table + " WHERE " + columnName + "='" + toMatch +"'";
        return selectQuery;
    }

    //UPDATE FUNCTIONS
    /**
     * SQL UPDATE COMMAND update column from a given table using id as identifier
     * @param tableName table name to be updated
     * @param columnName column name to be updated
     * @param newValue new value to be inserted
     * @param id    id of row
     * @return ReturnType=String:
     */
    public static String updateColumn(String tableName, String columnName, String newValue, String id){
        String updateColumn = "UPDATE "+ tableName + " SET " + columnName + "=" + newValue + " WHERE id=" + id;
        return updateColumn;
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
    public static String deleteTarget(String table, String targetID){
        String deleteQuery = "DELETE from " + table + " WHERE id='" + targetID +"'";
        return deleteQuery;
    }

    /**
     * SQL DELETE COMMAND delete rows from tables with correlating target
     * @param table1
     * @param table2
     * @param table1ColName table1 col name to check
     * @param table2ColName table2 col name to check
     * @param target target unique value
     * @return
     */
    public static String deleteInnerJoin(String table1, String table2, String table1ColName, String table2ColName, String target){
        String innerJoinDelete = "DELETE " +table1+ ", " +table2+ " FROM "+table1+" INNER JOIN " +table2+ " ON" +
                " " +table2 + "." + table2ColName+ "=" +table1 + "." + table1ColName+ " WHERE "+table1 + "." + table1ColName+ "='"+target+"'";
        //"DELETE user, salts FROM user INNER JOIN salts ON salts.user_email=user.email WHERE user.email='11'"
        return innerJoinDelete;
    }


    //GETS
    /**
     * Queries the user Table and returns results
     * @param queryCommand command defining what to return from user table
     * @return ReturnType=String[][]: containing results from query
     */
    public static String[][] getUserInfo(String queryCommand){
        try{
            ResultSet rs = DBInteract.dbQueryCommand(queryCommand);
            int rowCount = getRowCount(rs);
            int colCount = getColCount(rs);
            String[] colNames = getColNames("user");
            rs.first();
            String[][] userList = new String[rowCount][colCount];
            for(int i=0; i < rowCount; ++i){
                String id = Integer.toString(rs.getInt(colNames[0]));
                String email = rs.getString(colNames[1]);
                String password = rs.getString(colNames[2]);
                Boolean createBillboard = rs.getBoolean(colNames[3]);
                Boolean editAllBillboards = rs.getBoolean(colNames[4]);
                Boolean scheduleBillboards = rs.getBoolean(colNames[5]);
                Boolean editUsers = rs.getBoolean(colNames[6]);
                String[] colItem = new String[]{id, email, password, String.valueOf(createBillboard), String.valueOf(editAllBillboards), String.valueOf(scheduleBillboards), String.valueOf(editUsers)};
                for(int j=0; j< colCount; ++j){
                    userList[i][j] = colItem[j];
                }
                rs.next();
            }
            return userList;
        } catch (SQLException e){
            System.err.println(e);
            return null;
        }
    }

    /**
     * Queries a table, parses resultSet metadata, returning column names
     * @param table
     * @return ReturnType=String[]: containing column names
     */
    public static String[] getColNames(String table){
        try {
            ResultSet rs = DBInteract.dbQueryCommand(DBInteract.selectAll(table, 1));
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = getColCount(rs);
            String[] colNames = new String[colCount];
            for(int i = 1; i <= colCount; ++i){
                colNames[i - 1] = rsmd.getColumnName(i);
            }
            return colNames;
        }catch (SQLException e){
            System.err.println(e);
            return null;
        }
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
