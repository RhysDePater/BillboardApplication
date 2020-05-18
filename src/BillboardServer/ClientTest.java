package BillboardServer;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30);;
        //the host and port here should be read from the network.props file.
        Socket socket = new Socket ( "localhost", 12345);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
        //oos.writeUTF("add user");
        //String[] string_array = {"createUser", "testusername", "testpassword", "1", "1", "1", "1"};
        //String[] string_array = {"deleteUser", "testusername"};
        //String[] string_array = {"addSchedule", "billboardname", "2015-02-20T06:30", "120", "sessiontoken"};
        //String[] string_array = {"addSchedule", "billboardname", startDate.toString(), "120", "sessiontoken"};
        //String[] string_array = {"login", "admin", "p1ass"};
        String xmlString = XMLStringCreator.createXmlString(); // Here, a string is created from an xml file so that it can be sent to and stored in the database.
        //String[] string_array = {"createBillboard", "1", "23", "BillboardName", xmlString}; // Command,  user_id, schedule_id, billboard_name, xml_data
        String[] string_array = {"deleteBillboard", "billboardname1"};
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
}
