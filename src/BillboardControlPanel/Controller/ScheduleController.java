package BillboardControlPanel.Controller;

import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.View.ScheduleCard;
import BillboardViewer.ClientUtilities.ServerRequest;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.xml.namespace.QName;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//todo
// fix returning the wrong error if the schedule cannot be added due to the billboard already being scheduled
// make it clear that the user input is in minutes, or hours, or seconds, and just multiply appropriately to get the seconds to pass to the server
public class ScheduleController {
    private ScheduleCard scheduleCard;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ScheduleController(){
        initView();
        initController(getScheduleCard());
    }

    public void initView(){
        scheduleCard = new ScheduleCard(MainController.getSunData(), MainController.getMonData(), MainController.getTueData(), MainController.getWedData(), MainController.getThuData(), MainController.getFriData(), MainController.getSatData());
    }

    public void initController(ScheduleCard scheduleCard){
        ControllerHelper.enableGlobalButtons(scheduleCard);
        scheduleCard.getBtnCreate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSchedule();
            }
        });
        scheduleCard.getTableSun().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableSun(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableMon().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableMon(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableTue().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableTue(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableWed().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableWed(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableThu().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableThu(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableFri().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableFri(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
        scheduleCard.getTableSat().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectDay(scheduleCard.getTableSat(), e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

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
    }

    private void createSchedule(){
        int action = ScheduleCard.createNewSchedule();
        switch (action){
            case JOptionPane.OK_OPTION:
                LocalDateTime dateTime  =null;
                String name = null;
                int duration = 0;
                int time_to_recur = 0;
                try{
                    String date = scheduleCard.getStartTime().getText();
                    dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e){
                    ControllerHelper.returnMessage("Must be valid date time yyyy-MM-dd HH:mm:ss");
                    createSchedule();
                    break;
                }
                try {
                    name = scheduleCard.getBillboardToSchedule().getText();
                    duration = Integer.parseInt(scheduleCard.getDuration().getText());
                } catch (Exception e){
                    ControllerHelper.returnMessage("name & duration cannot be null & duration must be int");
                    createSchedule();
                    break;
                }
                String stringReoccur = scheduleCard.getRecur().getText();
                if(stringReoccur.length() <=0){
                    time_to_recur =0;
                } else {
                    time_to_recur = Integer.parseInt(scheduleCard.getRecur().getText());
                }

                //logic to make sure scheudles dont dupe
                    //insert here
                //

                String[] res = ServerRequestClient.createSchedule(name, dateTime,duration, time_to_recur, MainController.getSessionToken());
                if(res[0].equalsIgnoreCase("false")){
                    ControllerHelper.returnMessage("bilboard name does not exist");
                    createSchedule();
                }
                break;
                case JOptionPane.CANCEL_OPTION:
                    break;
        }
        ControllerHelper.refreshScheduleTablePanel();
    }

    private void selectDay(JTable tableSelected, MouseEvent e){
        selectedCol = tableSelected.columnAtPoint(e.getPoint());
        if(e.getClickCount() == 2){
            String columnHeader = tableSelected.getColumnName(selectedCol);
//        String selectedUser = userTable.getValueAt(selectedRow, 1).toString(); //col 1 is username
            //check value of selected table element and return response
            switch (columnHeader){
                case ("Sun"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 0), columnHeader);
                    break;
                case ("Mon"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 1), columnHeader);
                    break;
                case ("Tue"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 2), columnHeader);
                    break;
                case ("Wed"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 3), columnHeader);
                    break;
                case ("Thu"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 4), columnHeader);
                    break;
                case ("Fri"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 5), columnHeader);
                    break;
                case ("Sat"):
                    scheduleCard.createUserViewAllBillboardSchedules(ControllerHelper.getScheduleForSingleDay(MainController.getScheduleData(), 6), columnHeader);
                    break;
            }
            ControllerHelper.refreshScheduleTablePanel();
        }
    }




    public ScheduleCard getScheduleCard() {
        return scheduleCard;
    }

    public void setSelectedCol(int newValue) {
        this.selectedCol = newValue;
    }

    public void setSelectedRow(int newValue) {
        this.selectedRow = newValue;

    }
}
