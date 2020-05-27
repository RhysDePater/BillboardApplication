package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.PreviewerCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ManageBillboardController {

    private ManageBillboardCard manageBillboardCard;
    private int selectedRow = -1;
    private int selectedCol = -1;

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
                ControllerHelper.refreshBillBoardTablePanel();
                PreviewerCard.PreviewerCard();
            }
        });
    }

    public void setSelectedCol(int newValue) {
        this.selectedCol = newValue;
    }

    public void setSelectedRow(int newValue) {
        this.selectedRow = newValue;

    }
    public ManageBillboardCard getManageBillboardCard() {
        return manageBillboardCard;
    }
}
