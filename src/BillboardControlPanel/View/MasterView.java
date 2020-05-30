package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;


public class MasterView {
    protected static JFrame mainFrame;
    protected JPanel northCard;
    protected JPanel centerCard;
    protected JPanel southCard;
    private JButton btnHome;
    private JButton logOut;

    public MasterView(){
        createNorthCard();
    }

    protected JPanel createNorthCard(){
        northCard = ViewHelper.createPanel(Color.gray);
        northCard.setLayout( new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        btnHome = ViewHelper.createButton("Home");
        logOut = ViewHelper.createButton("Log-out");
        northCard.add(btnHome, c);
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 0;
        c.gridx = 2;
        northCard.add(logOut, c);
        return northCard;
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public JPanel getNorthCard() {
        return northCard;
    }

    public JPanel getCenterCard() {
        return centerCard;
    }

    public JPanel getSouthCard() {
        return southCard;
    }

    public JButton getBtnHome(){
        return btnHome;
    }

    public JButton getBtnLogOut(){return logOut;}

}
