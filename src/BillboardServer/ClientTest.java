package BillboardServer;

import java.io.*;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket ( "localhost", 500);
        OutputStream MyOutputSteam = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(MyOutputSteam);
        //BufferedOutputStream bos = new BufferedOutputStream(MyOutputSteam);
        //oos.writeUTF("add user");
        String[] string_array = {"createUser", "testusername", "testpassword",  "testsalt"};
        oos.writeObject(string_array);
        //bos.close(); // Send all data which has been written (bos.flush)
        oos.close();
        socket.close();
    }
}
