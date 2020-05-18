package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.ManageUserCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageBillboardController {

    private ManageBillboardCard manageBillboardCard;

    public ManageBillboardController(){
        initView();
        initController(getManageBillboardCard());
    }

    public void initView(){
        manageBillboardCard = new ManageBillboardCard();
    }

    public void initController(ManageBillboardCard manageBillboardCard) {
        manageBillboardCard.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
    }

    public ManageBillboardCard getManageBillboardCard() {
        return manageBillboardCard;
    }
}
