package BillboardServer.ClientUtilities;

import BillboardServer.ReadNetworkProps;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * This class has lots of methods for requesting the server to perform actions
 * Most methods create a string array where the first element is the command, and the rest the function params.
 * This array is the main method of communication between the client and the server. A string array is sent, and a string array is returned.
 * The way the string array is sent should be abstracted out by just using the provided methods
 * See the comment for sendQuery below for information on the array which is returned (This is not finalised, suggestions are welcome)
 */
public class ServerRequest {

    /**
     * Sends a given string array in a specific format, then listens for and returns the result
     * This is called from all methods below, and you do not need to called this directly
     * @param queryArray The array to be sent to the server
     *
     * @return Null if the server timed out, otherwise a string array which is formatted:
     *      "true" or "false" (Did the command run successfully on the server or not? This describes the state the database operation) it's a sting representation of a bool,
     *      "A string of results" Not many functions use this currently, getBillboard does as an example, it would return the xml string from the database. For a function like delete user, this will just be "".
     *      "Optional message that could be displayed to the user/debugging purposes", Just print this to the console for greater verbosity
     * See the SendBackData function on the server for more the code responsible
     *
     * @throws IOException This is thrown from lots of the networking functions, probably going to happen if the server isn't up, or network props file doesn't exist and a weird socket is made
     */
    public static String[] sendQuery(String[] queryArray) throws IOException{
        Socket socket = new Socket (ReadNetworkProps.getHost(), ReadNetworkProps.getPort());
        socket.setSoTimeout(4000); // Set a two second timeout on read operations. After two seconds of nothing being read, an exception will be thrown
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(queryArray);
        oos.flush();
        // Now listen for a response, no loop is required because there will only be a single response from the server for now, then return the results
        Object Response = null;
        try{
            Response = ois.readObject();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        try{
            return (String[])Response;
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Creates a user in the database, and associated permissions in the permission table
     * @param username username for the new user
     * @param password  password for the new user (currently just send it plaintext)
     * @param create_billboard 0 or 1 for disabled or enabled
     * @param edit_billboard 0 or 1 for disabled or enabled
     * @param schedule_billboard 0 or 1 for disabled or enabled
     * @param edit_user 0 or 1 for disabled or enabled
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[] createUser(String username, String password, Integer create_billboard, Integer edit_billboard, Integer schedule_billboard, Integer edit_user, String sessionToken) throws IOException {
        String[] command = {"createUser", username, password, create_billboard.toString(), edit_billboard.toString(), schedule_billboard.toString(), edit_user.toString()};
        return sendQuery(command);
    }

    /**
     * Deletes a user from the database, and associated permissions
     * @param username username of the user to delete
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  deleteUser(String username, String sessionToken) throws IOException {
        String[] command = {"deleteUser", username};
        return sendQuery(command);
    }

    /**
     * Creates a billboard if none exists, edits existing billboard otherwise.
     * @param billboardName The name of the billboard, as a string value.
     * @param xml_string The xml data which you wish to assign to the billboard
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  createOrEditBillboard(String billboardName, String xml_string, String sessionToken) throws IOException {
        String[] command = {"createBillboard", billboardName, xml_string, sessionToken};
        return sendQuery(command);
    }

    /**
     * Gets the value under xml_data for the provided billboardName.
     * This will be at index 1 of the results ie. second position in array
     * @param billboardName Name of the billboard.
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  getBillboard(String billboardName, String sessionToken) throws IOException {
        String[] command = {"getBillboard", billboardName, sessionToken};
        return sendQuery(command);
    }

    /**
     *  Deletes the billboard with the name entered.
     * @param billboardName Name of the billboard to be deleted
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  deleteBillboard(String billboardName, String sessionToken) throws IOException {
        String[] command = {"deleteBillboard", billboardName, sessionToken};
        return sendQuery(command);
    }

    /**
     * Adds one schedule to the database for one billboard
     * @param billboardName The billboard to associate the schedule with
     * @param startTime When the billboard should start displaying, currently a LocalDateTime object
     * @param duration Duration in seconds to be displayed from the startTime
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  addSchedule(String billboardName, LocalDateTime startTime, Integer duration, String sessionToken) throws IOException {
        String[] command = {"addSchedule", billboardName, startTime.toString(), duration.toString(), sessionToken};
        return sendQuery(command);
    }

    /**
     *  Deletes a schedule from the database which matches the billboard name and time.
     *  This may delete multiple schedules if they match
     *  (there should probably be restriction, server or client side or both, so you cant schedule two billboards at the exact same time for the exact same length)
     * @param billboardName Name of the billboard the schedule is for
     * @param startTime Time the billboard is scheduled to start
     * @param sessionToken A session token so the server can authenticate the request
     * @return See ServerRequest.sendQuery
     */
    public static String[]  deleteSchedule(String billboardName, LocalDateTime startTime, String sessionToken) throws IOException {
        String[] command = {"deleteSchedule", billboardName, startTime.toString(), sessionToken};
        return sendQuery(command);
    }

    /**
     * Edit permissions associated with a username
     * @param username The user to edit permissions for
     * @param create_billboard 0 or 1 for disabled or enabled
     * @param edit_billboard 0 or 1 for disabled or enabled
     * @param schedule_billboard 0 or 1 for disabled or enabled
     * @param edit_user 0 or 1 for disabled or enabled
     * @return See ServerRequest.sendQuery
     */
    public static String[]  editPermission(String username, Integer create_billboard, Integer edit_billboard, Integer schedule_billboard, Integer edit_user, String sessionToken) throws IOException {
        String[] command = {"editPermission", username, create_billboard.toString(), edit_billboard.toString(), schedule_billboard.toString(), edit_user.toString(), sessionToken};
        return sendQuery(command);
    }

    /**
     * Allows you to login to access other functions
     * @param username The username the password is for
     * @param password The password, which has to match the stored password (only plaintext right now)
     * @return See ServerRequest.sendQuery
     */
    public static String[]  login(String username, String password) throws IOException {
        String[] command = {"login", username, password};
        return sendQuery(command);
    }
}


// Just ignore all this, it's from testing
/*
    public static void main(String[] args) throws IOException {
        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30);;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadNetworkProps.readNetworkPropsHost(), ReadNetworkProps.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
        //oos.writeUTF("add user");
        String[] string_array = {"createUser", "testusername", "testpassword", "1", "1", "1", "1"};
        //String[] string_array = {"deleteUser", "testusername"};
        //String[] string_array = {"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
        //String[] string_array = {"addSchedule", "billboardname", startDate.toString(), "120", "sessiontoken"};
        //String[] string_array = {"login", "admin", "p1ass"};
        String xmlString = XMLStringCreator.createXmlString(); // Here, a string is created from an xml file so that it can be sent to and stored in the database.
        //String[] string_array = {"createBillboard", "BillboardName", xmlString}; // Command, billboard_name, xml_data
        //String[] string_array = {"deleteBillboard", "billboardname"};
        //String[] string_array = {"getBillboard", "billboardname"};
        //String[] string_array = {"deleteSchedule", "billboardname", startDate.toString()};
        oos.writeObject(string_array);
        oos.flush();
        //oos.close();
        //ois.close();
        //socket.close();
        for (;;){ // Now listen for a response
            Object Response = null;
            String[] StringArray;
            try{
                Response = ois.readObject();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            try{
                StringArray = (String[])Response;
            }
            catch (Exception e ){
                System.out.println(e.getMessage());
                return;
            }
            System.out.println(StringArray[0]);
            System.out.println(StringArray[1]);
            System.out.println(StringArray[2]);
            //System.out.println(StringArray[0]);
        }
    }
*/

