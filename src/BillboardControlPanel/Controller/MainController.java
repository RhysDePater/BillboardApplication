package BillboardControlPanel.Controller;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.MasterView;

public class MainController{
    private static LoginController loginController;
    private static HomeController homeController;
    private static ManageUserController manageUserController;
    private static MainView mainView;
    private static ManageBillboardController manageBillboardController;

    //
    private static String[][] userData = {};
    private static String[] userColNames = {};
    private static String[][] billData = {};
    //
    private static String loggedUser;
    private static String[] loggedUserPrivs;
    private static String sessionToken;
    //


    public MainController(){
        //this is a server connection test to run before launching application, bandaid check, implement better later
//        if(!ControllerHelper.checkConnection()) {
//            ControllerHelper.returnMessage("cannot connect to server");
//            System.exit(1);
//        }
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


    //this is a mess we should of had a MasterView similar to our view to handle all these variables
    //but its to late to change or structure now, so lets stick with it, we can look at changing if we have enough time :O
    //sets
    public static void setUserData(){
        userData = ServerRequestClient.listUsers(sessionToken);
        userData = ServerRequestClient.removeHeaderFromDoubleArray(userData);
    }

    public static void setBillData(){
        billData = ServerRequestClient.listBillboards(sessionToken);
        billData = ServerRequestClient.removeHeaderFromDoubleArray(billData);

    }

    public static void setUserColNames(){
        if(userColNames.length <= 0){
            userColNames = ServerRequestClient.getFormattedUserColumnNames(sessionToken);
        }
    }

    public static void setLoggedUser(String user){
        loggedUser = user;
    }

    public static void setLoggedUserPrivs(String[] newPrivileges){
        loggedUserPrivs = newPrivileges;
    }

    public static void setSessionToken(String newSessionToken){
        sessionToken = newSessionToken;
    };

    //GETS
    public static String[] getLoggedUserPrivs() {
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