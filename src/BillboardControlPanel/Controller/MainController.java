package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.MasterView;

import java.io.IOException;

public class MainController{
    private static LoginController loginController;
    private static HomeController homeController;
    private static ManageUserController manageUserController;
    private static MainView mainView;
    private static ManageBillboardController manageBillboardController;

    private static String[][] userData = {};
    private static String[] userColNames = {};
    private static String loggedUser;
    private static String[] loggedUserPrivs = {"1", "1","1","1"};
    private static String sessionToken;
    private static String[][] billData = {};

    public MainController(){
        initController();
        initView(loginController.getLoginCard());
    }

    private void initView(MasterView masterView){
        mainView = new MainView(masterView);
    }

    private void initController() {
        loginController = new LoginController();
        homeController = new HomeController();
        manageUserController = new ManageUserController();
        manageBillboardController = new ManageBillboardController();
    }

    public static void setUserData(){
        userData = ServerRequest.listUsers(sessionToken);
        userData = ServerRequest.removeHeaderFromDoubleArray(userData);
        System.out.println("set user data is called : " +userData[0][1]);
    }

    public static void setBillData(){
        try{
            billData = ServerRequest.listBillboards(MainController.getSessionToken());
            billData = ServerRequest.removeHeaderFromDoubleArray(billData);
        } catch(IOException e){
            System.out.println(billData[0][2]);
        }
    }

    public static void setUserColNames(){
        userColNames = ServerRequest.getFormatUserColumnNames(sessionToken);
        System.out.println("set user col is called : " + userColNames[0]);
    }

    public static void setLoggedUser(String user){
        loggedUser = user;
        System.out.println("set logged user to: " + loggedUser);
    }

    public static void setLoggedUserPrivs(){
        System.out.println("set logged user to: " + loggedUser);
    }


    public static void setSessionToken(String newSessionToken){
        sessionToken = newSessionToken;
        System.out.println("token set called: " + sessionToken);
    };
    //
    public static String[] getLoggedUserPrivs() {
        System.out.println(loggedUserPrivs[3]);
        return loggedUserPrivs;
    }

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static String getSessionToken(){return sessionToken;};

    public static String[][] getUserData(){
        return userData;
    }

    public static String[][] getBillData(){
        return billData;
    }

    public static String[] getUserColNames(){
        return userColNames;
    }

    public static MainView getMainView() {
        return mainView;
    }

    public static LoginController getLoginController() {
        return loginController;
    }

    public static HomeController getHomeController() {
        return homeController;
}

    public static ManageUserController getManageUserController() {
        return manageUserController;
    }

    public static ManageBillboardController getManageBillboardController() {return manageBillboardController;}
}
