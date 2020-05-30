package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;


public class ScheduleCard extends MasterView{

    private JTable calendar;
    private JButton btnCreate;
    private static String[] daysOfWeek = {"Sun", "Mon","Tue","Wed","Thu","Fri","Sat"};

    private static String[] scheduleHeader = {"user_id", "billboard_id", "start_time", "duration", "time to recur"};
    private static JTextField billboardToSchedule;
    private static JFormattedTextField startTime;
    private static JTextField duration;
    private static JTextField time_to_recur;
    private static  JTable tableSun;
    private static  JTable tableMon;
    private static  JTable tableTue;
    private static  JTable tableWed;
    private static  JTable tableThu;
    private static  JTable tableFri;
    private static  JTable tableSat;



    private static  JTable scheduleTable;

    public ScheduleCard(String[][] sundayData, String[][] mondayData, String[][] tuesdayData, String[][] wednesdayData,String[][] thursdayData,String[][] fridayData, String[][] saturdayData){
        createCenterCard(sundayData,mondayData,tuesdayData,wednesdayData,thursdayData,fridayData,saturdayData);
        createSouthCard();
    }



    private JPanel createCenterCard(String[][] sundayData, String[][] mondayData, String[][] tuesdayData, String[][] wednesdayData,String[][] thursdayData,String[][] fridayData, String[][] saturdayData){
        //

        String[][] sunday = sundayData;
        String[][] monday = mondayData;
        String[][] tuesday = tuesdayData;
        String[][] wednesday = wednesdayData;
        String[][] thursday = thursdayData;
        String[][] friday = fridayData;
        String[][] saturday = saturdayData;



        tableSun = ViewHelper.createJTable(sunday, new String[]{"Sun"});
        tableSun.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableMon = ViewHelper.createJTable(monday, new String[]{"Mon"});
        tableMon.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableTue = ViewHelper.createJTable(tuesday, new String[]{"Tue"});
        tableTue.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableWed = ViewHelper.createJTable(wednesday, new String[]{"Wed"});
        tableWed.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableThu = ViewHelper.createJTable(thursday, new String[]{"Thu"});
        tableThu.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableFri = ViewHelper.createJTable(friday, new String[]{"Fri"});
        tableFri.setPreferredScrollableViewportSize(tableSun.getPreferredSize());


        tableSat = ViewHelper.createJTable(saturday, new String[]{"Sat"});
        tableSat.setPreferredScrollableViewportSize(tableSun.getPreferredSize());



        centerCard = ViewHelper.createPanel(Color.WHITE);
        //contents of panel
        centerCard.setLayout(new GridLayout());
        centerCard.add(new JScrollPane(tableSun));
        centerCard.add(new JScrollPane(tableMon));
        centerCard.add(new JScrollPane(tableTue));
        centerCard.add(new JScrollPane(tableWed));
        centerCard.add(new JScrollPane(tableThu));
        centerCard.add(new JScrollPane(tableFri));
        centerCard.add(new JScrollPane(tableSat));
        return centerCard;
    }

    private JPanel createSouthCard(){
        southCard = ViewHelper.createPanel(Color.GRAY);
        southCard.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        btnCreate = ViewHelper.createButton("Create Schedule");
        JLabel toolTip = ViewHelper.createLabel("Double click cell to open full list of schedules for that day", ViewHelper.TEXT_FONT);
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        southCard.add(btnCreate);
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 0;
        c.gridx = 2;
        southCard.add(toolTip);
        return southCard;
    }

    public static int createNewSchedule(){
        JPanel jPanel = ViewHelper.createPanel(Color.white);
        //contents of panel
        JLabel billboardNameL = ViewHelper.createLabel("Billboard Name:", ViewHelper.TITLE_FONT);
        JLabel startTimeL = ViewHelper.createLabel("Start Time: ", ViewHelper.TEXT_FONT);
        JLabel durationL = ViewHelper.createLabel("Duration: ", ViewHelper.TEXT_FONT);
        JLabel time_to_recurL = ViewHelper.createLabel("Time until the billboard reoccurs: ", ViewHelper.TEXT_FONT);

        billboardToSchedule = ViewHelper.createTextField();
        startTime = ViewHelper.createDateTimeInputField();
        duration = ViewHelper.createTextField();
        time_to_recur = ViewHelper.createTextField();

        jPanel.add(billboardNameL);
        jPanel.add(billboardToSchedule);
        jPanel.add(startTimeL);
        jPanel.add(startTime);
        jPanel.add(durationL);
        jPanel.add(duration);
        jPanel.add(time_to_recurL);
        jPanel.add(time_to_recur);
        int action = JOptionPane.showConfirmDialog(null, jPanel, "Create schedule",JOptionPane.OK_CANCEL_OPTION);
        return action;
    }

    public static int createUserViewAllBillboardSchedules(String[][] scheduleTableContents, String columnHeader){
        JPanel jPanel = ViewHelper.createPanel(Color.white);
        //contents of panel
        scheduleTable = ViewHelper.createJTable(scheduleTableContents, scheduleHeader);
        jPanel.add(new JScrollPane(scheduleTable));
        int action = JOptionPane.showConfirmDialog(null, jPanel, columnHeader + ": schedules",JOptionPane.OK_CANCEL_OPTION);
        return action;
    }

//    public static int testBox(String[][] scheduleData){
//
//        int action = JOptionPane.showConfirmDialog(null, jPanel,  "day" + ": schedules",JOptionPane.OK_CANCEL_OPTION);
//        return action;
//    }


    //


    public JTable getTableSun() {
        return tableSun;
    }

    public JTable getTableMon() {
        return tableMon;
    }

    public JTable getTableFri() {
        return tableFri;
    }

    public JTable getTableSat() {
        return tableSat;
    }

    public JTable getTableThu() {
        return tableThu;
    }

    public JTable getTableTue() {
        return tableTue;
    }

    public JTable getTableWed() {
        return tableWed;
    }

    public JTextField getBillboardToSchedule() {
        return billboardToSchedule;
    }

    public JFormattedTextField getStartTime() {
        return startTime;
    }

    public JTextField getDuration() {
        return duration;
    }

    public JTextField getRecur() {
        return time_to_recur;
    }
    //
    public JButton getBtnCreate() {
        return btnCreate;
    }

    public JTable getCalendar() {
        return calendar;
    }

    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }
}
