package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
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
                String[][] userInfo = DBInteract.getUserInfo(DBInteract.selectTarget("user", "email", "ADMIN"));
                currentUserID = userInfo[0][0];
                currentUserPrivs = ControllerHelper.checkUserPrivileges(currentUserID);
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
    }


    //LOGIN TOKEN AUTHENTICATION - TO BE DONE
    private void loginToken(){
        String userEmailInput = loginCard.getUserEmailTextField().getText();
        String userPasswordInput = loginCard.getPasswordTextField().getText();
        //userInfo[row of info][columns from row] -> userInfo[row][0=id:1=email: 2=password: 3=createBillboard: 4=editBillboard: 5=schedule: 6=editUser]
        String[][] userInfo = DBInteract.getUserInfo(DBInteract.selectTarget("user", "email", userEmailInput));
        String userEmailDB = userInfo[0][1];
        String userPasswordDB = userInfo[0][2];



        if(userEmailDB.equals(userEmailInput) && userPasswordDB.equals(userPasswordInput)){
            ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            currentUserID = userInfo[0][0];
            currentUserPrivs = ControllerHelper.checkUserPrivileges(currentUserID);
        } else{
            ControllerHelper.returnMessage("wrong password/username");
            System.out.println("DB:" +  userEmailDB);
            System.out.println("INPUT:" + userEmailInput);
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
