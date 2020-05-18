package BillboardControlPanel.Controller;

import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.ManageUserCard;
import BillboardControlPanel.View.MasterView;

public class MainController{
    private static LoginController loginController;
    private static HomeController homeController;
    private static ManageUserController manageUserController;
    private static MainView mainView;
    private static ManageBillboardController manageBillboardController;

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
