package BillboardServer.ServerLogic;

public class ServerVariables { // A class to be inherited by lots of other classes so the code can be split up while also having access to the same variables.
    protected static boolean commandSucceeded;
    static String sessionTokenFromClient; //Session tokens come from ServerRequest methods.
    static String optionalMessage;
    static String[] inboundData;
    static String outboundData;
    static String[] outboundData1D; // Used to store the for example column names to turn into a String[][] to be sent to the client
    static String[][] outboundData2D; // For functions that are required to return a String[][]



}
