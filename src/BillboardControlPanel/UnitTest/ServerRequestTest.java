import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.commons.util.StringUtils;

import java.net.Socket;
import static java.time.Duration.ofMillis;

import static BillboardControlPanel.ClientUtilities.ServerRequest.*;
import static BillboardControlPanel.ClientUtilities.ReadNetworkProps.*;


class ServerRequestTest {
    private static Socket socket;
    private static int EXPECTED_PORT = 12345;
    private static String EXPECTED_HOST = "localhost";
    private static int TIMEOUT_DURATION = 2000;

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

    //Make sure connection to server is true before running tests
    @BeforeAll
    public static void testServerConnection(){
        Assertions.assertTimeoutPreemptively(ofMillis(TIMEOUT_DURATION), () -> {
            socket = initServerConnect();
            socket.close();
        }, "Connection to server failed");
    }

    //Test with a simple Post to confirm it works
    @Test
    public void sendQueryTest() {
        String[] queryArray = {"Hello Server"}; //query to send to server
        String[] res = sendQuery(queryArray); //store response
        //res[0] returns response as string, if as res is returned then server successfully messaged
        Assertions.assertTrue(StringUtils.isNotBlank(res[0]));
    }


    //Testing for all gets to server needs to be implemented however we don't have these yet

}