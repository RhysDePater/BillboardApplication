package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.View.HomeCard;
import BillboardControlPanel.View.ManageBillboardCard;
import com.sun.tools.javac.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HomeController{
    protected HomeCard homeCard;
    //private static String[][] bbTableResp;
    //public static String[][] bbTableData;

    public HomeController(){
        initView();
        initController(getHomeCard());
    }

    private void initView(){
        homeCard = new HomeCard();
    }

    private void initController(HomeCard homeCard) {
        homeCard.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
                ControllerHelper.returnMessage("return home");
            }
        });
        homeCard.getManageUser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getCurrentUserPrivs[0=createbillboard:1=editbillboard:2=schedule:3=editUser]
                if(MainController.getLoggedUserPrivs()[3].equalsIgnoreCase("1")){
                    ControllerHelper.refreshUsersTablePanel(); //run this to update before opening
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED USER MANAGEMENT PRIVILEGES");
                }

            }
        });
        homeCard.getManageBillboard().addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //RAYMOND u should implement this as per your specs
                if (MainController.getLoggedUserPrivs()[0].equalsIgnoreCase("1")) {
                    ControllerHelper.refreshBillBoardTablePanel();
                } else {
                    ControllerHelper.returnMessage("YOU ARE NOT GRANTED BILLBOARD MANAGEMENT PRIVILEGES");
                }
                //getBillTableData();
            }
        }));
    }

//    private static String[][] getBillTableData() {
//        try{
//            bbTableResp = ServerRequest.listBillboards(MainController.getSessionToken());
//            bbTableData = new String[bbTableResp.length - 1][bbTableResp[1].length];
//            for (int i = 1; i < bbTableResp.length; i++){
//                for(int k = 0; k < bbTableResp[i].length; k++){
//                    bbTableData[i-1][k] = bbTableResp[i][k];
//                }
//            }
//            for (int i = 0; i < bbTableData.length; i++){
//                for(int k = 0; k < bbTableData[i].length; k++){
//                    System.out.println(bbTableData[i][k]);
//                }
//            }
//
//        } catch(IOException e){
//
//        }
//        return bbTableData;
//    }

    //public String[][] getBBTableData(){return bbTableData;}
    public HomeCard getHomeCard() {
        return homeCard;
    }
}
