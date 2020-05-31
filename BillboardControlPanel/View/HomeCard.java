package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;

import static BillboardControlPanel.Helper.ViewHelper.TITLE_FONT;

/**
 * GridBagLayout styled home screen. Contains components to render to screen when
 * home card is called. NOTE: This contains an image from the Image folder. DO NOT
 * REMOVE OR CHANGE THE DIRECTORY.
 */
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
        centerCard = ViewHelper.createPanel(Color.WHITE);
        centerCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        //Pull the image for the local Image folder and store as an ImageIcon.
        ImageIcon welcomeIcon = new ImageIcon(getClass().getResource("/BillboardControlPanel/Images/welcomeImg.jpg"));

        JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont((TITLE_FONT));
        titleLabel.setIcon(welcomeIcon);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        //WELCOME TITLE
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerCard.add(titleLabel, gbc);

        //MANAGE BILLBOARD
        btnManageBillboard = ViewHelper.createButton("Manage BillBoards");
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerCard.add(btnManageBillboard, gbc);

        //MANAGE USERS
        btnManageUser = ViewHelper.createButton("Manage Users");
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerCard.add(btnManageUser, gbc);

        //MANAGE SCHEDULES
        btnManageSchedule = ViewHelper.createButton("Manage Schedule");
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerCard.add(btnManageSchedule, gbc);

        //CHANGE PASSWORD
        btnChangePassword = ViewHelper.createButton("Set New Password");
        gbc.gridx = 0;
        gbc.gridy = 4;
        centerCard.add(btnChangePassword, gbc);

        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.WHITE);
        return southCard;
    }

    //GETS
    public JButton getManageBillboard() { return btnManageBillboard; }
    public JButton getManageUser() { return btnManageUser; }
    public JButton getManageSchedule() { return btnManageSchedule; }
    public JButton getBtnChangePassword(){return btnChangePassword;}
}
