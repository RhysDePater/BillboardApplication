package BillboardControlPanel.Controller;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.LoginCard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController{
    protected LoginCard loginCard;


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
                bypassLogin();
            }
        });
    }

    /**
     * Login and create a login token, handles all login related erros
     */
    private void loginToken() {
        //get input values
        String usernameInput = loginCard.getUserEmailTextField().getText();
        String userPasswordInput = loginCard.getPasswordTextField().getText();
        //hash password
        String hashedPassword = ControllerHelper.createSecurePassword(userPasswordInput);
        //input error handling "NO username"
        if (usernameInput.length() <= 0) {
            ControllerHelper.returnMessage("please input a username");
            //input error handling "NO password"
        } else if (userPasswordInput.length() <= 0) {
            ControllerHelper.returnMessage("Password cannot be null");
        } else {
            //on valid values request login token from server
            String[] res = ServerRequestClient.login(usernameInput, hashedPassword);
            //on success
            if(Boolean.parseBoolean(res[0]) == true){
                System.out.println("Login success");
                //user
                MainController.setSessionToken(res[1]);
                MainController.setLoggedUser(usernameInput);
                MainController.setLoggedUserPrivs(ServerRequestClient.getFormattedUserPrivs(MainController.getLoggedUser(), MainController.getSessionToken()));
                //update
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
                ControllerHelper.resetJTextFields(new JTextField[]{loginCard.getUserEmailTextField(), loginCard.getPasswordTextField()});
            } else {
                //on failure
                System.out.println("Login Failure");
                ControllerHelper.returnMessage("Username and password do not match");
            }
        }
    }

    //DEV FUNC REMOVE LATER
    private void bypassLogin(){
        String usernameInput = "Admin";
        String userPasswordInput = "d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1";
        //input error handling "NO username"
        if (usernameInput.length() <= 0) {
            ControllerHelper.returnMessage("please input a username");
            //input error handling "NO password"
        } else if (userPasswordInput.length() <= 0) {
            ControllerHelper.returnMessage("Password cannot be null");
        } else {
            String[] res = ServerRequestClient.login(usernameInput, userPasswordInput);
            System.out.println(res[0]);
            if(Boolean.parseBoolean(res[0]) == true){
                System.out.println("success");
                //user
                MainController.setSessionToken(res[1]);
                MainController.setLoggedUser(usernameInput);
                MainController.setLoggedUserPrivs(ServerRequestClient.getFormattedUserPrivs(MainController.getLoggedUser(), MainController.getSessionToken()));
                //data
                //MainController.setScheduleData();
                MainController.setUserData();
                MainController.setBillData();
                MainController.setUserColNames();
                //MainController.setAmountOfSchedulesPerDay();
                //update
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
                ControllerHelper.resetJTextFields(new JTextField[]{loginCard.getUserEmailTextField(), loginCard.getPasswordTextField()});
            } else {
                System.out.println("Failure");
                ControllerHelper.returnMessage("Username and password do not match");
            }
        }
    }
    //GETS


    public LoginCard getLoginCard(){
        return loginCard;
    }
}
