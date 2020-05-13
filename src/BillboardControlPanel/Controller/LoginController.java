package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.Helper.ViewHelper;
import BillboardControlPanel.Model.DBInteract;
import BillboardControlPanel.View.LoginCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginController{
    protected LoginCard loginCard;
    private String currentUserID;
    private Boolean[] currentUserPrivs;

    public LoginController(){
        initView();
        initController(getLoginCard());
    }

    private void initView(){
        loginCard = new LoginCard();
    }

    private void initController(LoginCard loginCard) {
        //Action listener for login
        loginCard.getBtnLogIn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginToken();
            }
        });
        //to be removed
        loginCard.getBypassLogIn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[][] userInfo = DBInteract.getUserData(DBInteract.selectUserJoinPermission());
                currentUserID = userInfo[0][0];
                currentUserPrivs = ControllerHelper.checkUserPrivileges(currentUserID);
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
    }


    //LOGIN TOKEN AUTHENTICATION - TO BE DONE
    private void loginToken() {
        String username = loginCard.getUserEmailTextField().getText();
        String userPasswordInput = loginCard.getPasswordTextField().getText();
        //input error handling "NO username"
        if (username.length() <= 0) {
            ControllerHelper.returnMessage("please input a username");
            //input error handling "NO password"
        } else if (userPasswordInput.length() <= 0) {
            ControllerHelper.returnMessage("Password cannot be null");
        } else {
            //input error handling "invalid username"
            try {
                //userInfo[row of info][columns from row] -> userInfo[row][0=id:1=username: 2=password: 3=createBillboard: 4=editBillboard: 5=schedule: 6=editUser]
                String[][] userInfo = DBInteract.getUserData(DBInteract.selectTargetUserJoinPermission("username", username));
                String usernameDB = userInfo[0][1];
                String userPasswordDB = userInfo[0][2];
                //Successful login attempt username and password match
                if (usernameDB.equals(username) && userPasswordDB.equals(userPasswordInput)) {
                    ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
                    currentUserID = userInfo[0][0];
                    currentUserPrivs = ControllerHelper.checkUserPrivileges(currentUserID);
                    //input error handling "invalid username and password dont match"
                } else {
                    ControllerHelper.returnMessage("wrong password");
                    System.out.println("DB:" + usernameDB);
                    System.out.println("INPUT:" + username);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
                ControllerHelper.returnMessage("Username does not exist");
            }
        }
    }

    //GETS
    public String getCurrentUserID() {
        return currentUserID;
    }

    public Boolean[] getCurrentUserPrivs() {
        return currentUserPrivs;
    }

    public LoginCard getLoginCard(){
        return loginCard;
    }
}
