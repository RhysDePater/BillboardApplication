package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;
import BillboardControlPanel.Model.DBInteract;

import javax.swing.*;
import java.awt.*;

public class ManageBillboardCard extends MasterView {
    //    private static JButton createBillboard;
//    private static JButton deleteBillboard;
    private static JTable billboardTable;
    private static String[] colNames = DBInteract.getColNames(DBInteract.selectAll("billboard"));
//    private static String[] colNames = {"id", "user_id", "schedule_id", "billboard_name", "status"};

    public ManageBillboardCard() {
        createNorthCard();
        createCenterCard();
        createSouthCard();
    }

    private JPanel createNorthCard() {
        northCard = ViewHelper.createPanel(Color.gray);
        btnHome = ViewHelper.createButton("Home");
        northCard.add(btnHome);
        return northCard;
    }
    private JPanel createCenterCard() {
        centerCard = ViewHelper.createPanel(Color.white);
        billboardTable = ViewHelper.createJTable((DBInteract.getBillboardData(DBInteract.selectAll("billboard"))), colNames);
        centerCard.add(new JScrollPane(billboardTable));
        return centerCard;
    }
    private JPanel createSouthCard() {
        southCard = new JPanel(new FlowLayout());
        JButton showPreview= ViewHelper.createButton("testPreview");
        southCard.add(showPreview);
        return southCard;
    }
}
