package BillboardServer;

import java.io.*;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket ( "localhost", 12345);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
        //oos.writeUTF("add user");
        String[] string_array = {"createUser", "testusername2", "testpassword2", "1", "1", "1", "1"};
        oos.writeObject(string_array);
        oos.flush();
        //bos.close(); // Send all data which has been written (bos.flush)
        //oos.close();
        //ois.close();
        //socket.close();

        for (;;){
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
            //System.out.println(StringArray[0]);
        }
    }
}
