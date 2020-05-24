package BillboardControlPanel.Helper;

import BillboardControlPanel.Controller.MainController;
import BillboardControlPanel.ModelOUTDATED.DBInteract;
import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.MasterView;

import javax.swing.*;
import java.awt.*;
import java.security.*;

public class ControllerHelper {


    public static MainView updateFrame(MainView mainView, MasterView masterView){
        mainView.getMainFrame().getContentPane().removeAll();
        mainView.getMainFrame().getContentPane().add(masterView.getNorthCard(), BorderLayout.NORTH);
        mainView.getMainFrame().getContentPane().add(masterView.getCenterCard(), BorderLayout.CENTER);
        mainView.getMainFrame().getContentPane().add(masterView.getSouthCard(), BorderLayout.SOUTH);
        mainView.getMainFrame().validate();
        mainView.getMainFrame().repaint();
        return mainView;
    }

    public static void resetJTextFields(JTextField[] jTextField){
        for (JTextField item: jTextField) {
            item.setText("");
        }
    }

    public static void refreshUsersTablePanel(){
        MainController.getManageUserController().setSelectedCol(-1);
        MainController.getManageUserController().setSelectedRow(-1);
        MainController.getManageUserController().initView();
        MainController.getManageUserController().initController(MainController.getManageUserController().getManageUserCard());
        ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageUserController().getManageUserCard());
    }

    public static int confirmPopUp(String message){
        int action = JOptionPane.showConfirmDialog(null, message, "confirm", JOptionPane.OK_CANCEL_OPTION);
        return action;
    }

    public static void returnMessage(String message){
        JOptionPane.showMessageDialog(null, message, null, JOptionPane.INFORMATION_MESSAGE);
    }

    //USER RELATED HELPERS
    public static Boolean[] checkUserPrivileges(String userID){
        int amountOfPrivliges = 4;
        Boolean[] boolPrivilges = new Boolean[amountOfPrivliges];
        String[][] userInfo = DBInteract.getUserData(DBInteract.selectTargetUserJoinPermission("user.id", userID));
        //userInfo[row of info][columns from row] -> userInfo[row][0=id:1=email: 2=password: 3=createbillboard: 4=editbillboard: 5=scheduel: 6=edituser]
        for(int i =0; i < amountOfPrivliges; ++i){
            boolPrivilges[i] = Boolean.valueOf(userInfo[0][i + 3]);
        }
        return boolPrivilges;
    }

    public static String createSecurePassword(String passwordToHash) {
        String securePassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; ++i) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            securePassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return securePassword;
    }

    public static byte[] createSalt(String userEmailInput){
        try{
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            DBInteract.dbExecuteCommand(DBInteract.createSalt(salt, userEmailInput));
            return salt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getSalt(String userEmailInput){
        return null;
    }
}
