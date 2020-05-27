package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.LoginCard;

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


    //LOGIN TOKEN AUTHENTICATION - TO BE DONE
    private void loginToken() {
        String usernameInput = loginCard.getUserEmailTextField().getText();
        String userPasswordInput = loginCard.getPasswordTextField().getText();
        //input error handling "NO username"
        if (usernameInput.length() <= 0) {
            ControllerHelper.returnMessage("please input a username");
            //input error handling "NO password"
        } else if (userPasswordInput.length() <= 0) {
            ControllerHelper.returnMessage("Password cannot be null");
        } else {
            String[] res = ServerRequest.login(usernameInput, userPasswordInput);
            System.out.println(res[0]);
            if(Boolean.parseBoolean(res[0]) == true){
                System.out.println("success");
                MainController.setSessionToken(res[1]);
                MainController.setUserData();
                MainController.setBillData();
                MainController.setUserColNames();
                MainController.setLoggedUser(usernameInput);
                MainController.setLoggedUserPrivs();
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            } else {
                System.out.println("Failure");
                ControllerHelper.returnMessage("Username and password do not match");
            }
        }
    }


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
            String[] res = ServerRequest.login(usernameInput, userPasswordInput);
            System.out.println(res[0]);
            if(Boolean.parseBoolean(res[0]) == true){
                System.out.println("success");
                MainController.setSessionToken(res[1]);
                MainController.setUserData();
                MainController.setBillData();
                MainController.setUserColNames();
                MainController.setLoggedUser(usernameInput);
                MainController.setLoggedUserPrivs();
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
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
