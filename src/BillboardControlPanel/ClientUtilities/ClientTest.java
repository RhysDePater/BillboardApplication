package BillboardControlPanel.ClientUtilities;

import java.time.LocalDateTime;
import java.io.*;

public class ClientTest {
    public static void main(String[] args) {
        String sessionToken ="myToken";
        String xmlString;
        LocalDateTime startDate = LocalDateTime.of(2015, 2, 20, 6, 30); // The format for date objects when scheduling
        String[] responseArray;

        // Get xml data from a file, used for createOrEditBillboard
        try{
            xmlString = XMLStringCreator.createXmlString();
        }
        catch (Exception e){
            System.out.println("Xml file not found");
            return;
        }

        // These are the functions to call if you want the server to perform stuff on the database. Hopefully they are straightforward, but there are java doc style comments available.
        // Session tokens aren't a thing at the moment so you can send whatever
        // See ServerRequest.sendQuery (and below) for what they return.
        try{
            responseArray = ServerRequest.createUser("testusername", "testpassword", 1,1,1,1, sessionToken);
            //responseArray = ServerRequest.deleteUser("testusername", sessionToken);
            //responseArray = ServerRequest.createOrEditBillboard("my billboard", "example data", xmlString);
            //responseArray = ServerRequest.deleteBillboard("my billboard", sessionToken);
            //responseArray = ServerRequest.getBillboard("BillboardMethodTest6", sessionToken);
            //responseArray = ServerRequest.login("ADMIN", "pass");
            //responseArray = ServerRequest.addSchedule("BillboardMethodTest6", startDate, 120, sessionToken);
            //responseArray = ServerRequest.deleteSchedule("BillboardMethodTest6", startDate, sessionToken);
            //responseArray = ServerRequest.editPermission("testusername", 0,0,0,0, sessionToken);
        }
        catch (IOException e){
            System.out.println("Could not access server");
            return; // Can't really do much if we can't access the server
        }

           // When an exception occurs the response array is null, so you will get an error if you try read it, otherwise it's fine
            System.out.println(responseArray[0]); // true or false if the sql query ran okay
            System.out.println(responseArray[1]); // Response string (Returned from some functions such as those which get stuff from the database, "" from others)
            System.out.println(responseArray[2]); // Optional user-friendly message (Not implemented for every function, it might just be "")

    }
}
