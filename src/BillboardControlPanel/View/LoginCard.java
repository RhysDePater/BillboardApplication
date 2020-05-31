package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;

public class LoginCard extends MasterView{

    private static JTextField userEmailTextField;
    private static JTextField passwordTextField;
    private static JButton btnLogIn;

    public LoginCard(){
        createNorthCard();
        createCenterCard();
        createSouthCard();
    }

    @Override
    protected JPanel createNorthCard(){
        northCard = ViewHelper.createPanel(Color.WHITE);
        return northCard;
    }

    private JPanel createCenterCard(){
        centerCard = ViewHelper.createPanel(Color.WHITE);
        centerCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/BillboardControlPanel/Images/loginTitle.jpg"));
        JLabel title = new JLabel("", SwingConstants.CENTER);
        title.setIcon(loginIcon);

        //LOGIN BUTTON
        gbc.gridwidth = 2;
        btnLogIn = ViewHelper.createButton("Log In");
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerCard.add(btnLogIn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        //WELCOME IMAGE
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerCard.add(title, gbc);

        //EMAIL LABEL
        JLabel userNameLabel = ViewHelper.createLabel("Email: ", ViewHelper.TEXT_FONT);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerCard.add(userNameLabel, gbc);
        //EMAIL FIELD
        userEmailTextField = ViewHelper.createTextField();
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerCard.add(userEmailTextField, gbc);

        //PASSWORD LABEL
        JLabel passwordLabel = ViewHelper.createLabel("Password: ", ViewHelper.TEXT_FONT);
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerCard.add(passwordLabel, gbc);
        //PASSWORD FIELD
        passwordTextField = ViewHelper.createPasswordField();
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 2;
        centerCard.add(passwordTextField, gbc);
        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.WHITE);
        return southCard;
    }

    public JButton getBtnLogIn() {
        return btnLogIn;
    }

    public JTextField getUserEmailTextField() {
        return userEmailTextField;
    }

    public JTextField getPasswordTextField() {
        return passwordTextField;
    }
}
