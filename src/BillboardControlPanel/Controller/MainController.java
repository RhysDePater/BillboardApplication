package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
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
    private static String loggedUser;
    private static String[] loggedUserPrivs;
    private static String sessionToken;
    //
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


    //this is a mess we should of had a MasterView similar to our view to handle all these variables
    //but its to late to change or structure now, so lets stick with it, we can look at changing if we have enough time :O
    //sets
    public static void setUserData(){
        userData = ServerRequest.listUsers(sessionToken);
        userData = ServerRequest.removeHeaderFromDoubleArray(userData);
//        System.out.println("set user data is called : " + userData[0][1]);
    }

    public static void setBillData(){
        billData = ServerRequest.listBillboards(sessionToken);
        billData = ServerRequest.removeHeaderFromDoubleArray(billData);
        //System.out.println(billData[0][1]);
    }

    public static void setUserColNames(){
        System.out.println(userColNames.length);
        if(userColNames.length <= 0){
            userColNames = ServerRequest.getFormattedUserColumnNames(sessionToken);
            System.out.println("set user col is called");
        }
    }

    public static void setLoggedUser(String user){
        loggedUser = user;
//        System.out.println("set logged user to: " + loggedUser);
    }

    public static void setLoggedUserPrivs(){
        loggedUserPrivs = ServerRequest.getFormattedUserPrivs(loggedUser, sessionToken);
//        for (String priv: loggedUserPrivs
//        ) {
//            System.out.println("priv: " + priv);
//        }
    }

    public static void setSessionToken(String newSessionToken){
        sessionToken = newSessionToken;
//        System.out.println("token set called: " + sessionToken);
    };

    //GETS
    public static String[] getLoggedUserPrivs() {
        for (String priv: loggedUserPrivs
             ) {
                System.out.println(priv);
        }
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
//        System.out.println(billData[0][0]);
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