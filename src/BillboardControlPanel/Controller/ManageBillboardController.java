package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.PreviewerCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ManageBillboardController {

    private ManageBillboardCard manageBillboardCard;

    public ManageBillboardController(){
        initView();
        initController(getManageBillboardCard());
    }

    public void initView(){
        manageBillboardCard = new ManageBillboardCard(MainController.getBillData());
    }

    public void initController(ManageBillboardCard manageBillboardCard) {
        manageBillboardCard.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
        manageBillboardCard.getBtnCreate().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PreviewerCard.PreviewerCard();
            }
        });
    }

    public ManageBillboardCard getManageBillboardCard() {
        return manageBillboardCard;
    }
}
