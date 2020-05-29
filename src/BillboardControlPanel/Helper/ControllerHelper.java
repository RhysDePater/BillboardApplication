package BillboardControlPanel.Helper;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.Controller.MainController;
import BillboardControlPanel.View.MainView;
import BillboardControlPanel.View.ManageUserCard;
import BillboardControlPanel.View.MasterView;
import com.sun.tools.javac.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import java.awt.*;
import java.security.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

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
        MainController.setUserData();
        MainController.setUserColNames();
        MainController.getManageUserController().setSelectedCol(-1);
        MainController.getManageUserController().setSelectedRow(-1);
        MainController.getManageUserController().initView();
        MainController.getManageUserController().initController(MainController.getManageUserController().getManageUserCard());
        ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageUserController().getManageUserCard());
    }

    public static void refreshScheduleTablePanel(){
        MainController.setScheduleData();
        MainController.setAmountOfSchedulesPerDay();
        MainController.setSunData();
        MainController.setMonData();
        MainController.setTueData();
        MainController.setWedData();
        MainController.setThuData();
        MainController.setFriData();
        MainController.setSatData();
        MainController.getScheduleController().setSelectedCol(-1);
        MainController.getScheduleController().setSelectedRow(-1);
        MainController.getScheduleController().initView();
        MainController.getScheduleController().initController(MainController.getScheduleController().getScheduleCard());
        ControllerHelper.updateFrame(MainController.getMainView(), MainController.getScheduleController().getScheduleCard());
    }

    public static void refreshBillBoardTablePanel(){
        MainController.setBillData();
        MainController.getManageBillboardController().setSelectedCol(-1);
        MainController.getManageBillboardController().setSelectedRow(-1);
        MainController.getManageBillboardController().initView();
        MainController.getManageBillboardController().initController(MainController.getManageBillboardController().getManageBillboardCard());
        ControllerHelper.updateFrame(MainController.getMainView(), MainController.getManageBillboardController().getManageBillboardCard());
    }

    public static int confirmPopUp(String message){
        int action = JOptionPane.showConfirmDialog(null, message, "confirm", JOptionPane.OK_CANCEL_OPTION);
        return action;
    }

    public static void returnMessage(String message){
        JOptionPane.showMessageDialog(null, message, null, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This is a cheat way of implementing a parent for the controllers, it will add the functionality of global use buttons, in this case logout and home button
     * @param masterView current viewCard
     */
    public static void enableGlobalButtons(MasterView masterView){
        masterView.getBtnHome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ControllerHelper.updateFrame(MainController.getMainView(), MainController.getHomeController().getHomeCard());
            }
        });
        masterView.getBtnLogOut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load login panel
                int action = confirmPopUp("Logout?");
                switch (action){
                    case (JOptionPane.OK_OPTION):
                        //reset user variables
                        MainController.setLoggedUser("");
                        MainController.setSessionToken("");
                        ServerRequestClient.logout(MainController.getSessionToken());
                        ControllerHelper.updateFrame(MainController.getMainView(), MainController.getLoginController().getLoginCard());
                        break;
                    case (JOptionPane.CANCEL_OPTION):
                        break;
                }
            }
        });
    }

    public static Boolean checkConnection(){
            Boolean check = false;
            int timesFailed = 0;
            while(!check) {
                Socket socket = ServerRequestClient.initServerConnect();
                if(socket == null){
                    timesFailed++;
                } else {
                    try{
                        socket.close();
                        return true;
                    } catch (IOException e){
                        System.err.println(e.getMessage());
                    }
                }
                if(timesFailed == 1){
                    return false;
                }
            }
            return null;
    }

    //USER RELATED HELPERS

//    public static String[]

    public static String getDayOfDate(LocalDateTime date){
        Calendar calendar = Calendar.getInstance();
        Date formattedDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(formattedDate);
        String dayOfWeek = new SimpleDateFormat("EE").format(formattedDate);
        return dayOfWeek;
    }

    public static void setNewPassword(String userToUpdate, ManageUserCard manageUserCard, String sessionToken){
        int passwordBox = ManageUserCard.createFrameTextInputBox(userToUpdate, "password");
        switch (passwordBox) {
            case (JOptionPane.OK_OPTION):
                String newPassword = manageUserCard.getUpdateTextField().getText();
                String newHashedPassword = ControllerHelper.createSecurePassword(newPassword);
                ControllerHelper.confirmPopUp("Password changed to: " + newPassword);
                String[] res = ServerRequestClient.setUserPassword(userToUpdate, newHashedPassword, sessionToken);
                if(res[0].equalsIgnoreCase("false")){
                    System.out.println("failed");
                }
                break;
            case (JOptionPane.CANCEL_OPTION):
                break;
        }
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

    /**
     *
     * @param scheduleData
     * @return returns a list of billboard names for a given day
     */
    public static String[][] getBillNamesFromScheduleSingleDay(String[][] scheduleData, int dayToGet){
        String[][] scheduleDataForADay = getScheduleForSingleDay(scheduleData, dayToGet);
        ArrayList<String[]> arrayList = new ArrayList<>();
        //get billboard names from an array
        for(int i = 0; i < scheduleDataForADay.length; i++){
                String[] someArray= new String[]{scheduleDataForADay[i][1]};
                arrayList.add(someArray);
        }
        //remove duplicate values
        TreeSet<String[]> set = new TreeSet<String[]>(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Arrays.equals(o1, o2) ? 0 : 1;
            }
        });
        set.addAll(arrayList);
        //set as array list
        arrayList = new ArrayList<>(set);
        //convert from array list to array
        String[][] namesForADay= arrayList.toArray(new String[arrayList.size()][1]);
        return namesForADay;
    }

    /**
     *
     * @param scheduleData data to get schedules from
     * @param dayToGet starting from sunday values 0-6 to represent each day
     * @return
     */
    public static String[][] getScheduleForSingleDay(String[][] scheduleData, int dayToGet){
        ArrayList<String[]> daysScheduleList = new ArrayList<>();
        String[][] dayScheduleToReturn;

        for(int i = 0; i < scheduleData.length; i++){
            try{
                int dateIndex = 2;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(scheduleData[i][dateIndex], formatter);
                String day = ControllerHelper.getDayOfDate(dateTime);
            switch (dayToGet){
                case(0): //sunday
                    switch(day){
                        case ("Sun"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(1): //monday
                    switch(day){
                        case ("Mon"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(2): //tuesday
                    switch(day){
                        case ("Tue"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(3): //wednesday
                    switch(day){
                        case ("Wed"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(4): //thursday
                    switch(day){
                        case ("Thu"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(5): //friday
                    switch(day){
                        case ("Fri"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
                case(6): //saturday
                    switch(day){
                        case ("Sat"):
                            daysScheduleList.add(scheduleData[i]);
                            break;
                        default:
                            break;
                    }
                    break;
            }
            } catch (DateTimeParseException e){
                System.err.println("invalid date time format: " + e);
            }
        }
        dayScheduleToReturn = daysScheduleList.toArray(new String[daysScheduleList.size()][]);
        return dayScheduleToReturn;
    }

    public static String getAmountOfSchedulesPerDay(String[][] schedulesOnDay){
        return String.valueOf(schedulesOnDay.length) ;
    }
}
