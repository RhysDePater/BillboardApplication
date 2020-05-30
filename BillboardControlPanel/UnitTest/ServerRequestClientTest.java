package BillboardControlPanel.UnitTest;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.commons.util.StringUtils;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.Duration.ofMillis;

import static BillboardControlPanel.ServerUtilities.ServerRequestClient.*;
import static BillboardControlPanel.ServerUtilities.ReadNetworkProps.*;


class ServerRequestClientTest {
    //server constants
    private static Socket socket;
    private static int EXPECTED_PORT = 12345;
    private static String EXPECTED_HOST = "localhost";
    private static int TIMEOUT_DURATION = 2000;
    //login constants
    private static String loginUsername = "ADMIN";
    private static String loginPassword = "d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1";
    static String sessionToken;
    //new user constants
    private static String newUsername = "newUser1";
    private static String setNewHashPass = "a853eb9056f559430f61147acdd07c0a94630b93ac5baae39c64bb91034a46a6";
    private static String newPassword = "newPass1";
    private static int[] newUserPerm = {1,1,1,1};
    //billboard
    private static String newBillboard = "newBillboard";
    private static String newXmlData = "XMLDATA";


    @BeforeAll
    public static void getHostTest(){
        String host = getHost();
        Assertions.assertEquals(host, EXPECTED_HOST);
    }

    @BeforeAll
    public static void getPortTest(){
        int port = getPort();
        Assertions.assertEquals(port, EXPECTED_PORT);
    }

    @BeforeAll
    public static void loginTest(){
        String[] loginRes = login(loginUsername, loginPassword);
        sessionToken  = loginRes[1];
        //server has not setup a static session token to be used
    }

    //Make sure connection to server is true before running tests
    @BeforeAll
    public static void testServerConnection(){
        Assertions.assertTimeoutPreemptively(ofMillis(TIMEOUT_DURATION), () -> {
            socket = initServerConnect();
            socket.close();
        }, "Connection to server failed");
    }

    @AfterAll
    public static void logoutTest(){
        String[] res = logout(sessionToken);
        Assertions.assertTrue(res[0].equalsIgnoreCase("true"));
    }




    //Test with a simple Post to confirm that a response is received from the server
    @Test
    public void sendQueryTest() {
        String[] queryArray = {"Hello Server, Single Query res"}; //query to send to server
        String[] res = sendQuery(queryArray); //store response
        //res[0] returns response as string, if as res is returned then server successfully messaged
        Assertions.assertTrue(StringUtils.isNotBlank(res[0]));
    }

    //For get test if the response at res[0][0] is true then the command succeeded
    @Test
    public void listUsersTest(){
        String[][] res = listUsers(sessionToken); //query to send to server
        //res[0] returns response as string, if res[0] is returned then server successfully responded
        Assertions.assertEquals(res[0][0], "true");
    }

    @Test
    public void getColumnNamesTest(){
        String tableName = "user";
        String[][] res = getColumnNames(tableName, sessionToken);
//        for(int i = 0; i < res[1].length; i++){
//            System.out.println(res[1][i]);
//        }
        Assertions.assertEquals(res[0][0], "true");
    }

    @Test
    public void parseColumnNamesTest(){
        String[] formattedColumnData = getFormattedUserColumnNames(sessionToken);
        Assertions.assertNotNull(formattedColumnData);
    }

    @Test
    public void removeHeaderFromUserListTest(){
        String[][] userList =  removeHeaderFromDoubleArray(listUsers(sessionToken));
        Assertions.assertTrue(userList[0][0] != "true"); // this would be the header if it didnt work
    }

    //For post test if the response at res[0] is true then the command succeeded
    @Test
    public void createUserTest(){
        String[] postRes = createUser(newUsername, newPassword, newUserPerm[0], newUserPerm[1],newUserPerm[2], newUserPerm[3], sessionToken);
        Assertions.assertEquals(postRes[0], "true");
    }


    @Test
    public void changeUserPasswordTest(){
        String[] res = setUserPassword(newUsername, setNewHashPass, sessionToken );
        Assertions.assertTrue(res[0].equalsIgnoreCase("true"));
    }

    @Test
    public void deleteUserTest(){
        String[] postRes = deleteUser(newUsername, sessionToken);
        Assertions.assertEquals((postRes[0]), "true");
    }

    @Test
    public void getUserPermissionTest(){
        String[][] userPerm = getPermissions(loginUsername, sessionToken);
        Assertions.assertTrue(userPerm[0][0].equalsIgnoreCase("true"));
    }

    @Test
    public void getFormattedUserPermissionTest(){
        String[] userPerm = getFormattedUserPrivs(loginUsername, sessionToken);
        Assertions.assertNotNull(userPerm);
    }

    @Test
    @Order(4)
    public void createBillboardTest(){
        String[] res = createOrEditBillboard(newBillboard, newXmlData, sessionToken);
    }

    @Test
    @Order(5)
    public void createScheduleTest(){
        String[] res = ServerRequestClient.createSchedule(newBillboard, LocalDateTime.now(), 60, sessionToken);
        Assertions.assertTrue(res[0].equalsIgnoreCase("true"));
    }

    @Test
    @Order(6)
    public void listScheduleTest(){
        String[][] res = listSchedules(sessionToken);
        Assertions.assertTrue(res[0][0].equalsIgnoreCase("true"));
    }


    @Test
    @Order(6)
    public void deleteBillboardTest(){
        String[] res = deleteBillboard(newBillboard, sessionToken);
    }

    @Test
    public void deleteScheduleTest(){
//        String[] res = deleteSchedule(newBillboard, );
    }
}