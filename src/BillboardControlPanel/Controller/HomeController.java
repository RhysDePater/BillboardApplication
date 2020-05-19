package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.HomeCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        homeCard.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
                ControllerHelper.returnMessage("return home");
            }
        });
        homeCard.getManageUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getCurrentUserPrivs[0=createbillboard:1=editbillboard:2=schedule:3=editUser]
                if (MainController.getLoginController().getCurrentUserPrivs()[3]) {
                    ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageUserController().getManageUserCard());
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED USER MANAGEMENT PRIVILEGES");
                }

            }
        });
        homeCard.getManageBillboard().addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MainController.getLoginController().getCurrentUserPrivs()[0]) {
                    ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageBillboardController().getManageBillboardCard());
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED USER MANAGEMENT PRIVILEGES");
                }
            }
        }));
    }

    public HomeCard getHomeCard() {
        return homeCard;
    }
}
