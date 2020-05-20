package BillboardServer;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        // Note: can only run one ClientSQLCommandFunction method at a time at the moment. All currently listed examples function.
        String xmlString = XMLStringCreator.createXmlString();
        ClientSQLCommandFunction.RunCommandDeleteUser("testusername");
        //ClientSQLCommandFunction.RunCommandCreateBillboard("BillboardMethodTest6", xmlString);
        //ClientSQLCommandFunction.RunCommandDeleteBillboard("BillboardMethodTest2");
        //ClientSQLCommandFunction.RunCommandGetBillboard("BillboardMethodTest6");
        //ClientSQLCommandFunction.RunCommandLogin("ADMIN", "pass");

        }
    }

