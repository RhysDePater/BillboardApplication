package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.MasterView;

public class MainController{
    private static LoginController loginController;
    private static HomeController homeController;
    private static ManageUserController manageUserController;
    private static MainView mainView;
    private static ManageBillboardController manageBillboardController;
    private static ScheduleController scheduleController;
    //
    private static String[][] userData = {};
    private static String[] userColNames = {};
    private static String[][] billData = {};
    private static String[][] scheduleData = {};
    //
    private static String[][] sunData = {};
    private static String[][] monData = {};
    private static String[][] tueData = {};
    private static String[][] wedData = {};
    private static String[][] thuData = {};
    private static String[][] friData = {};
    private static String[][] satData = {};
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
        scheduleController = new ScheduleController();
    }

    public static void setScheduleData() {
        scheduleData = ServerRequestClient.listSchedules(sessionToken);
        scheduleData = ServerRequestClient.removeHeaderFromDoubleArray(scheduleData);
    }
    public static String[][] getScheduleData() {
        try {
            return scheduleData;
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("array is empty: " + e);
            return null;
        }

    }

    public static void setSunData(){
        sunData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 0);
    }
    public static void setMonData(){
        monData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 1);
    }
    public static void setTueData(){
        tueData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 2);
    }
    public static void setWedData(){
        wedData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 3);
    }
    public static void setThuData(){
        thuData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 4);
    }
    public static void setFriData(){
        friData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 5);
    }
    public static void setSatData(){
        satData = ControllerHelper.getBillNamesFromScheduleSingleDay(scheduleData, 6);
    }

    public static String[][] getSunData(){return sunData;}
    public static String[][] getMonData(){return monData;}
    public static String[][] getTueData(){return tueData;}
    public static String[][] getWedData(){return wedData;}
    public static String[][] getThuData(){return thuData;}
    public static String[][] getFriData(){return friData;}
    public static String[][] getSatData(){return satData;}



    //SETS
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

    public static ScheduleController getScheduleController() {
        return scheduleController;
    }
}