package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.Model.DBInteract;
import BillboardControlPanel.View.ManageUserCard;

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
        initView();
        initController(getManageUserCard());
    }

    public void initView(){
        manageUserCard = new ManageUserCard();
    }

    public void initController(ManageUserCard manageUserCard){
        manageUserCard.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
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
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
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
        int action = ManageUserCard.createUserCreateInputBox();
        switch (action){
            case(JOptionPane.OK_OPTION):
                JRadioButton[] jBtnPermission = manageUserCard.getPermissionField();
                int[] permissions = new int[jBtnPermission.length];
                for(int i = 0; i < jBtnPermission.length; ++i){
                    if(jBtnPermission[i].isSelected()){
                        permissions[i] = 1;
                    } else{
                        permissions[i] = 0;
                    }
                }

                JTextField[] fieldArray= {
                        manageUserCard.getEmailField(),
                        manageUserCard.getPasswordField(),
                };

                String[] fieldStringArray = {
                    fieldArray[0].getText(),
                        fieldArray[1].getText()
                };

                DBInteract.dbExecuteCommand(DBInteract.createUser(
                        fieldStringArray[0],
                        fieldStringArray[1],
                        "salt"
                ));
                DBInteract.dbExecuteCommand(DBInteract.createPermission(permissions[0], permissions[1], permissions[2], permissions[3]));
                ControllerHelper.resetJTextFields(fieldArray);
                break;
            case(JOptionPane.CANCEL_OPTION):
                break;
        }
        ControllerHelper.refreshUsersTablePanel();
    }

    private void editUser(){
        if(selectedRow == -1 || selectedCol == -1){
            ControllerHelper.returnMessage("Please Select a User");
        }
        else if(selectedCol > 2) {
            String rowIdValue = manageUserCard.getUserTable().getValueAt(selectedRow, 0).toString();
            String colName = manageUserCard.getUserTable().getColumnName(selectedCol);
            String selectedValue = manageUserCard.getUserTable().getValueAt(selectedRow,selectedCol).toString();
            if(rowIdValue.equals(MainController.getLoginController().getCurrentUserID())){
                ControllerHelper.returnMessage("CANT EDIT OWN PRIVILEGES");
            } else{
                switch (selectedValue){
                    case("false"):
                        selectedValue = "1";
                        break;
                    case("true"):
                        selectedValue = "0";
                        break;
                }
                int action = ControllerHelper.confirmPopUp("Update " + colName + " to " + Boolean.valueOf(selectedValue).toString().toUpperCase() + "?");
                switch (action) {
                    case (JOptionPane.OK_OPTION):
                        DBInteract.dbExecuteCommand(DBInteract.updateColumn("permission", colName, selectedValue, rowIdValue));
                        break;
                    case (JOptionPane.CANCEL_OPTION):
                        break;
                }
            }
        }
        else if (selectedCol == 2){
            String rowValue = manageUserCard.getUserTable().getValueAt(selectedRow, 0).toString();
            String colName = manageUserCard.getUserTable().getColumnName(selectedCol);
            int action = ManageUserCard.createFrameTextInputBox(rowValue, colName);
            switch (action) {
                case (JOptionPane.OK_OPTION):
                    String newValue = manageUserCard.getUpdateTextField().getText();
                    DBInteract.dbExecuteCommand(DBInteract.updateColumn("user", colName, newValue, rowValue));
                    ControllerHelper.returnMessage("Edit User: " + rowValue + "| Column :  " + colName + "| New Value: " + newValue);
                    break;
                case (JOptionPane.CANCEL_OPTION):
                    break;
            }
        }
        else{
            ControllerHelper.returnMessage("Cannot edit id/email");
        }
        ControllerHelper.refreshUsersTablePanel();
    }

    private void deleteUser(){
        if(selectedRow == -1 || selectedCol == -1){
            ControllerHelper.returnMessage("Please Select a User");
        } else{
            String rowIdValue = manageUserCard.getUserTable().getValueAt(selectedRow, 0).toString();
            String rowEmailValue = manageUserCard.getUserTable().getValueAt(selectedRow, 1).toString();
            if(rowIdValue.equals(MainController.getLoginController().getCurrentUserID())) {
                ControllerHelper.returnMessage("CANT DELETE SELF");
            }
            else {
                int action = JOptionPane.showConfirmDialog(null, "DELETE USER " + rowIdValue,"ARE YOU SURE", JOptionPane.OK_CANCEL_OPTION);
                switch(action){
                    case(JOptionPane.OK_OPTION):
                        DBInteract.dbExecuteCommand(DBInteract.deleteInnerJoin("user", "permission", "id", "user_id", "username", rowEmailValue));
                        break;
                    case(JOptionPane.CANCEL_OPTION):
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
