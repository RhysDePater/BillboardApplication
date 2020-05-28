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
        btnHome = ViewHelper.createButton("Home");
        logOut = ViewHelper.createButton("Log-out");
        northCard.add(btnHome);
        northCard.add(logOut);
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
