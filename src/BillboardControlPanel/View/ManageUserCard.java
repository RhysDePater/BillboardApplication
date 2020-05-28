package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;


public class ManageUserCard extends MasterView {

    private static JButton btnDltUser;
    private static JButton btnCreateUser;
    private static JTable userTable;

    private static JTextField emailField;
    private static JPasswordField passwordField;
    private static JRadioButton[] permissionField;

    private static JTextField updateTextField;

    public ManageUserCard(String[][] colData, String[] colNames){
        createCenterCard(colData, colNames);
        createSouthCard();
    }

    private JPanel createCenterCard(String[][] colData, String[] colNames){
        centerCard = ViewHelper.createPanel(Color.white);
        JLabel titleLabel = ViewHelper.createLabel("Manage Users", ViewHelper.TITLE_FONT);
        //get user table to display
        userTable = ViewHelper.createJTable(colData, colNames);
        centerCard.add(titleLabel);
        centerCard.add(ViewHelper.createLabel("Double click to edit cell", ViewHelper.TEXT_FONT));
        centerCard.add(new JScrollPane(userTable));
        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.gray);
        btnDltUser = ViewHelper.createButton("Delete User");
        btnCreateUser = ViewHelper.createButton("Create User");
        southCard.add(btnCreateUser);
        southCard.add(btnDltUser);
        return southCard;
    }

    public static int createFrameTextInputBox(String id, String columnName){
        JPanel panel = ViewHelper.createPanel(Color.WHITE);
        JLabel jLabel = new JLabel("new " + columnName);
        updateTextField = ViewHelper.createTextField();
        panel.add(jLabel);
        panel.add(updateTextField);
        int jOptionPane = JOptionPane.showConfirmDialog(null, panel, "User Edit: " + id + "| COLUMN: " + columnName, JOptionPane.OK_CANCEL_OPTION);
        return jOptionPane;
    }

    public static int createUserCreateInputBox(){
        JPanel jPanel = ViewHelper.createPanel(Color.white);
        //contents of panel
        JLabel titleLabel = ViewHelper.createLabel("Create a new Account", ViewHelper.TITLE_FONT);
        JLabel emailLabel = ViewHelper.createLabel("Email: ", ViewHelper.TEXT_FONT);
        JLabel passwordLabel = ViewHelper.createLabel("Password: ", ViewHelper.TEXT_FONT);
        JLabel permissionLabel = ViewHelper.createLabel("Permission: ", ViewHelper.TEXT_FONT);
        emailField = ViewHelper.createTextField();
        passwordField = ViewHelper.createPasswordField();
        permissionField = new JRadioButton[]{
                new JRadioButton("Create Billboards", false),
                new JRadioButton("Edit All Billboards", false),
                new JRadioButton("Schedule Billboards", false),
                new JRadioButton("Edit Users", false)
        };
        //
        jPanel.add(titleLabel);
        jPanel.add(emailLabel);
        jPanel.add(emailField);
        jPanel.add(passwordLabel);
        jPanel.add(passwordField);
        jPanel.add(permissionLabel);
        for (JRadioButton button: permissionField) {
            jPanel.add(button);
        }
        int action = JOptionPane.showConfirmDialog(null, jPanel, "Create User",JOptionPane.OK_CANCEL_OPTION);
        return action;
    }

    public JButton getBtnDltUser() {
        return btnDltUser;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public JTextField getUpdateTextField() {
        return updateTextField;
    }

    public JButton getBtnCreateUser() {
        return btnCreateUser;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JRadioButton[] getPermissionField() {
        return permissionField;
    }



}
