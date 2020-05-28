package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.HomeCard;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.MasterView;
import com.sun.tools.javac.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        homeCard.getManageBillboard().addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //RAYMOND u should implement this as per your specs
                if (MainController.getLoggedUserPrivs()[0].equalsIgnoreCase("1")) {
                    ControllerHelper.refreshBillBoardTablePanel();
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED BILLBOARD MANAGEMENT PRIVILEGES");
                }
                //getBillTableData();
            }
        }));
    }

    public HomeCard getHomeCard() {
        return homeCard;
    }
}
