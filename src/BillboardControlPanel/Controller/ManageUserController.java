package BillboardControlPanel.Controller;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.ManageUserCard;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ManageUserController {

    private ManageUserCard manageUserCard;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ManageUserController(){
        try{
            initView();
            initController(getManageUserCard());
        } catch (NullPointerException e){
            System.out.println(e);
        }

    }

    public void initView(){
            manageUserCard = new ManageUserCard(MainController.getUserData(), MainController.getUserColNames());
    }

    public void initController(ManageUserCard manageUserCard){
        ControllerHelper.enableGlobalButtons(manageUserCard);
        manageUserCard.getUserTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = manageUserCard.getUserTable().rowAtPoint(e.getPoint());
                selectedCol = manageUserCard.getUserTable().columnAtPoint(e.getPoint());
                if(e.getClickCount() == 2){
                    editUser();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        manageUserCard.getBtnDltUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        manageUserCard.getBtnCreateUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUser();
            }
        });
    }

    private void createUser(){
        //create input box
        int action = ManageUserCard.createUserCreateInputBox();
        switch (action){
            case(JOptionPane.OK_OPTION):
                //get values of permissions
                JRadioButton[] jBtnPermission = manageUserCard.getPermissionField();
                int[] permissions = new int[jBtnPermission.length];
                for(int i = 0; i < jBtnPermission.length; ++i){
                    if(jBtnPermission[i].isSelected()){
                        permissions[i] = 1;
                    } else{
                        permissions[i] = 0;
                    }
                }
                //get user and password values
                JTextField[] fieldArray= {
                        manageUserCard.getEmailField(),
                        manageUserCard.getPasswordField(),
                };
                //convert username and password to strings
                String[] fieldStringArray = {
                        fieldArray[0].getText(),
                        fieldArray[1].getText()
                };

                //fix this if time at end
                //check values are not null
                if (fieldStringArray[0].length() <= 0) {
                    ControllerHelper.returnMessage("user name is null");
                    createUser();
                    break;
                } else if (fieldStringArray[1].length() <= 0) {
                    ControllerHelper.returnMessage("Password is null");
                    createUser();
                    break;
                    //
                } else {
                    //hash password and send request to db to create user
                    String hashedPassword = ControllerHelper.createSecurePassword(fieldStringArray[1]);
                    ServerRequestClient.createUser( fieldStringArray[0],
                            hashedPassword, permissions[0], permissions[1], permissions[2], permissions[3], MainController.getSessionToken());
                    ControllerHelper.resetJTextFields(fieldArray);
                }
                break;
            case(JOptionPane.CANCEL_OPTION):
                break;
        }
        ControllerHelper.refreshUsersTablePanel();
    }

    private void editUser(){
        //get table values
        JTable userTable = manageUserCard.getUserTable();
        String columnHeader = userTable.getColumnName(selectedCol);
        String selectedUser = userTable.getValueAt(selectedRow, 1).toString(); //col 1 is username
        //check value of selected table element and return response
        if(selectedRow == 0){
            ControllerHelper.returnMessage("Primary Admin User Cannot be edited. This User must always have all permissions");
        } else {
            switch (columnHeader){
                case (""):
                    ControllerHelper.returnMessage("please select a user");
                    break;
                case ("id"):
                    ControllerHelper.returnMessage("cant edit id");
                    break;
                case ("username"):
                    ControllerHelper.returnMessage("cant edit username");
                    break;
                case ("password"):
                    ControllerHelper.setNewPassword(selectedUser, manageUserCard, MainController.getSessionToken());
                    break;
                default:
                    //check to prevent changing of own edit user privs
                    if( columnHeader.equalsIgnoreCase("edit_user") && selectedUser.equalsIgnoreCase(MainController.getLoggedUser())){
                        ControllerHelper.returnMessage("Administrators cannot remove edit their own 'edit users' privilege");
                        break;
                    } else {
                        //get updated permission values
                        String selectedValue = userTable.getValueAt(selectedRow,selectedCol).toString();
                        String boolValue = ""; // boolean cant parse 1 or 0 but database wont accept true or false as a boolean value becuase of an error with the updated mariadb client
                        switch (selectedValue){
                            case("false"):
                                selectedValue = "1";
                                boolValue = "true";
                                break;
                            case("true"):
                                selectedValue = "0";
                                boolValue = "false";
                                break;
                        }
                        //confirm input
                        int action = ControllerHelper.confirmPopUp("Update " + columnHeader + " to " + boolValue + "?");
                        switch (action) {
                            case (JOptionPane.OK_OPTION):
                                //update permissions
                                ServerRequestClient.editPermission(selectedUser, columnHeader, Integer.parseInt(selectedValue), MainController.getSessionToken());
                                break;
                            case (JOptionPane.CANCEL_OPTION):
                                break;
                        }
                    }
                    break;
            }
        }
        ControllerHelper.refreshUsersTablePanel();
    }

    private void deleteUser(){
        switch (selectedRow) {
            case (-1):
                ControllerHelper.returnMessage("Please Select a User");
                break;
            case (0):
                ControllerHelper.returnMessage("Admin user cannot be deleted");
                break;
            default:
                //get selected user
                String rowUsernameValue = manageUserCard.getUserTable().getValueAt(selectedRow, 1).toString();
                String rowEmailValue = manageUserCard.getUserTable().getValueAt(selectedRow, 1).toString();
                //check it is not self
                if (rowUsernameValue.equals(MainController.getLoggedUser())) {
                    ControllerHelper.returnMessage("CANT DELETE SELF");
                } else {
                    //confirmation popup
                    int action = JOptionPane.showConfirmDialog(null, "DELETE USER " + rowUsernameValue, "ARE YOU SURE", JOptionPane.OK_CANCEL_OPTION);
                    switch (action) {
                        case (JOptionPane.OK_OPTION):
                            //request server for delte of user
                            ServerRequestClient.deleteUser(rowEmailValue, MainController.getSessionToken());
                            break;
                        case (JOptionPane.CANCEL_OPTION):
                            break;
                    }
                }
        }
        ControllerHelper.refreshUsersTablePanel();
    }

    public ManageUserCard getManageUserCard() {
        return manageUserCard;
    }

    public void setSelectedCol(int newValue) {
        this.selectedCol = newValue;
    }

    public void setSelectedRow(int newValue) {
        this.selectedRow = newValue;

    }
}
