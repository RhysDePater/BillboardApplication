package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;

public class LoginCard extends MasterView{

    private static JTextField userEmailTextField;
    private static JTextField passwordTextField;
    private static JButton btnLogIn;
    //
    private static JButton bypassLogIn;


    public LoginCard(){
        createNorthCard();
        createCenterCard();
        createSouthCard();
    }

    private JPanel createNorthCard(){
        northCard = ViewHelper.createPanel(Color.gray);
        return northCard;
    }

    private JPanel createCenterCard(){
        centerCard = ViewHelper.createPanel(Color.WHITE);
        JLabel title = ViewHelper.createLabel("***--Welcome to the Billboard Manager--***", ViewHelper.TITLE_FONT);
        JLabel passwordLabel = ViewHelper.createLabel("Password: ", ViewHelper.TEXT_FONT);
        JLabel userNameLabel = ViewHelper.createLabel("Email: ", ViewHelper.TEXT_FONT);
        userEmailTextField = ViewHelper.createTextField();
        passwordTextField = ViewHelper.createPasswordField();
        //
        centerCard.add(title);
        centerCard.add(userNameLabel);
        centerCard.add(userEmailTextField);
        centerCard.add(passwordLabel);
        centerCard.add(passwordTextField);
        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.gray);
        btnLogIn = ViewHelper.createButton("Log In");
        bypassLogIn = ViewHelper.createButton("bypass");
        southCard.add(btnLogIn);
        southCard.add(bypassLogIn);
        return southCard;
    }

    //



    public JButton getBtnLogIn() {
        return btnLogIn;
    }

    public JButton getBypassLogIn() {
        return bypassLogIn;
    }

    public JTextField getUserEmailTextField() {
        return userEmailTextField;
    }

    public JTextField getPasswordTextField() {
        return passwordTextField;
    }
}
