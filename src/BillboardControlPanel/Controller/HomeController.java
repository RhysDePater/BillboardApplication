package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.View.HomeCard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class stores functionality for the HomeCard buttons. Includes changing passwords functionality.
 * Allows managing billboard, managing schedule, and mangaging user cards to be pulled for display, along
 * with their required data and functionalities.
 */
public class HomeController{
    protected HomeCard homeCard;

    public HomeController(){
        initView();
        initController(getHomeCard());
    }

    private void initView(){
        homeCard = new HomeCard();
    }

    private void initController(HomeCard homeCard) {
        ControllerHelper.enableGlobalButtons(homeCard);
        //CHANGE PASSWORD BUTTON FUNCTIONALITY
        homeCard.getBtnChangePassword().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!MainController.getLoggedUser().equalsIgnoreCase("Admin")){
                    ControllerHelper.setNewPassword(MainController.getLoggedUser(), MainController.getManageUserController().getManageUserCard(), MainController.getSessionToken());
                } else {
                    ControllerHelper.returnMessage("Primary Admin User Cannot be edited.");
                }

            }
        });
        //MANAGE USERS BUTTON FUNCTIONALITY
        homeCard.getManageUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getCurrentUserPrivs[0=createbillboard:1=editbillboard:2=schedule:3=editUser]
                if(MainController.getLoggedUserPrivs()[3].equalsIgnoreCase("1")){
                    ControllerHelper.refreshUsersTablePanel(); //run this to update before opening
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED USER MANAGEMENT PRIVILEGES");
                }

            }
        });



//        System.out.println(billData.length);
//        for(int i = 0; i < billData.length; i++){
//            for (int k = 0; k < billData[i].length; k++){
//                System.out.println(billData[i][k]);
//            }
//        }

        //MANAGE BILLBOARDS BUTTON FUNCTIONALITY
        homeCard.getManageBillboard().addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[][] billData = ServerRequestClient.listBillboards(MainController.getSessionToken());
                if (billData[1][0]!="") {
                    ControllerHelper.refreshBillBoardTablePanel();
                } else {
                    MainController.getManageBillboardController().initView();
                    MainController.getManageBillboardController().initController(MainController.getManageBillboardController().getManageBillboardCard());
                    ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageBillboardController().getManageBillboardCard());
                }
            }
        }));
        //MANAGE SCHEDULES BUTTON FUNCTIONALITY
        homeCard.getManageSchedule().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MainController.getLoggedUserPrivs()[2].equalsIgnoreCase("1")){
                    ControllerHelper.refreshScheduleTablePanel();
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED SCHEDULE MANAGEMENT PRIVILEGES");
                }
            }
        });
    }

    public HomeCard getHomeCard() {
        return homeCard;
    }
}
