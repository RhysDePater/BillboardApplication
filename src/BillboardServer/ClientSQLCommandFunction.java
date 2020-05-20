package BillboardServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientSQLCommandFunction {
// RunCommandCreateUser TO BE COMPLETED
    public static void RunCommandCreateUser(String userName, String passWord) throws IOException {
        String[] command = new String[3]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "createUserName";
        command[1] = "userName";
        command[2] = "password";
        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30);;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
        //oos.writeUTF("add user");
        //String[] string_array = {"createUser", "testusername", "testpassword", "1", "1", "1", "1"};
        //String[] string_array = {"deleteUser", "testusername"};
        //String[] string_array = {"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
        //String[] string_array = {"addSchedule", "billboardname", startDate.toString(), "120", "sessiontoken"};
        //String[] string_array = {"login", "admin", "p1ass"};
        //String xmlString = XMLStringCreator.createXmlString(); // Here, a string is created from an xml file so that it can be sent to and stored in the database.
        //String[] string_array = {"createBillboard", "BillboardName", xmlString}; // Command, billboard_name, xml_data
        //String[] string_array = {"deleteBillboard", "billboardname"};
        //String[] string_array = {"getBillboard", "billboardname"};
        //string_array = new String[]{"deleteSchedule", "billboardname", startDate.toString()};
        oos.writeObject(command);
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

    /**
     * Command for deleting a user from the database
     * @param userName Takes the username for the user to be deleted.
     * @throws IOException
     */

    public static void RunCommandDeleteUser(String userName) throws IOException {
        String[] command = new String[2]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "deleteUser";
        command[1] = userName;

        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(command);
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

    /**
     * Creates a billboard in the database.
     * Creates a string array from the parameters entered, with a command supplied by the method as the first element of the array.
     * This is then passed to Networking.Parse where switch cases determine which command to run, based of the first element of the array.
     * @param BillboardName The name of the billboard, as a string value.
     * @param xml_string The content of the xml file, parsed to a string. Allows contents of billboard to be sent to database.
     * @throws IOException
     */
    public static void RunCommandCreateBillboard(String BillboardName, String xml_string) throws IOException {
        String[] command = new String[3]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "createBillboard";
        command[1] = BillboardName;
        command[2] = xml_string;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(command);
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

    /**
     *  Deletes the billboard with the name entered.
     * @param BillboardName Name of the billboard to be deleted.
     * @throws IOException
     */
    public static void RunCommandDeleteBillboard(String BillboardName) throws IOException {
        String[] command = new String[2]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "deleteBillboard";
        command[1] = BillboardName;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(command);
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

    /**
     * Gets the billboard text from the database, from the row with the name entered as the BillboardName.
     * @param BillboardName Name of the billboard.
     * @throws IOException
     */
    public static void RunCommandGetBillboard(String BillboardName) throws IOException {
        String[] command = new String[2]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "getBillboard";
        command[1] = BillboardName;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(command);
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

    public static void RunCommandLogin(String userName, String password) throws IOException {
        String[] command = new String[3]; // Number of elements in the string for Networking.java switch statements.
        command[0] = "login";
        command[1] = userName;
        command[2] = password;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(command);
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

//    public static void main(String[] args) throws IOException {
//        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30);;
//        //the host and port here should be read from the network.props file.
//        Socket socket = new Socket (ReadFromNetworkPropsFile.readNetworkPropsHost(), ReadFromNetworkPropsFile.readNetworkPropsPort());
//        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
//        //oos.writeUTF("add user");
//        String[] string_array = {"createUser", "testusername", "testpassword", "1", "1", "1", "1"};
//        //String[] string_array = {"deleteUser", "testusername"};
//        //String[] string_array = {"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
//        //String[] string_array = {"addSchedule", "billboardname", startDate.toString(), "120", "sessiontoken"};
//        //String[] string_array = {"login", "admin", "p1ass"};
//        String xmlString = XMLStringCreator.createXmlString(); // Here, a string is created from an xml file so that it can be sent to and stored in the database.
//        //String[] string_array = {"createBillboard", "BillboardName", xmlString}; // Command, billboard_name, xml_data
//        //String[] string_array = {"deleteBillboard", "billboardname"};
//        //String[] string_array = {"getBillboard", "billboardname"};
//        //String[] string_array = {"deleteSchedule", "billboardname", startDate.toString()};
//        oos.writeObject(string_array);
//        oos.flush();
//        //oos.close();
//        //ois.close();
//        //socket.close();
//        for (;;){ // Now listen for a response
//            Object Response = null;
//            String[] StringArray;
//            try{
//                Response = ois.readObject();
//            }
//            catch(Exception e){
//                System.out.println(e.getMessage());
//            }
//            try{
//                StringArray = (String[])Response;
//            }
//            catch (Exception e ){
//                System.out.println(e.getMessage());
//                return;
//            }
//            System.out.println(StringArray[0]);
//            System.out.println(StringArray[1]);
//            System.out.println(StringArray[2]);
//            //System.out.println(StringArray[0]);
//        }
//    }

}
