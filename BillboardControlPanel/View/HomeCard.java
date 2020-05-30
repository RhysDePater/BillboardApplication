package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;

public class HomeCard extends MasterView{

    private static JButton btnManageBillboard;
    private static JButton btnManageUser;
    private static JButton btnManageSchedule;
    private static JButton btnChangePassword;

    public HomeCard(){
        createCenterCard();
        createSouthCard();
    }

    private JPanel createCenterCard(){
        centerCard = ViewHelper.createPanel(Color.white);
        JLabel titleLabel = ViewHelper.createLabel("Welcome", ViewHelper.TITLE_FONT);
        btnManageBillboard = ViewHelper.createButton("BillBoards");
        btnManageUser = ViewHelper.createButton("Users");
        btnManageSchedule = ViewHelper.createButton("Schedule");
        centerCard.add(titleLabel);
        centerCard.add(btnManageBillboard);
        centerCard.add(btnManageUser);
        centerCard.add(btnManageSchedule);
        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.gray);
        btnChangePassword = ViewHelper.createButton("set New Password");
        southCard.add(btnChangePassword);
        return southCard;
    }

    //
    public JButton getManageBillboard() {
        return btnManageBillboard;
    }

    public void setManageBillboard() {
        this.btnManageBillboard = btnManageBillboard;
    }

    public JButton getManageUser() {
        return btnManageUser;
    }

    public void setManageUser() {
        this.btnManageUser = btnManageUser;
    }

    public JButton getManageSchedule() {
        return btnManageSchedule;
    }

    public void setManageSchedule() {
        this.btnManageSchedule = btnManageSchedule;
    }

    public JButton getBtnChangePassword(){return btnChangePassword;}
}
