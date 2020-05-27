package BillboardControlPanel.View;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.Controller.MainController;
import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ManageBillboardCard extends MasterView {
    private static JButton btnCreate;
    //    private static JButton deleteBillboard;
    private static JTable billboardTable;
    private static String[] colNames = new String[]{"User Name", "Billboard Name","XML Data", "Status"};
//    private static String[][] bbTableResp;
//    private static String[][] bbTableData;


    public ManageBillboardCard(String[][] colData){
        createNorthCard();
        createCenterCard(colData);
        createSouthCard();
    }

    private JPanel createNorthCard() {
        northCard = ViewHelper.createPanel(Color.gray);
        btnHome = ViewHelper.createButton("Home");
        northCard.add(btnHome);
        return northCard;
    }
    private JPanel createCenterCard(String[][] colData){
        centerCard = ViewHelper.createPanel(Color.white);
        billboardTable = ViewHelper.createJTable(colData, colNames);
        centerCard.add(new JScrollPane(billboardTable));
        return centerCard;
    }
    private JPanel createSouthCard() {
        southCard = new JPanel(new FlowLayout());
        btnCreate= ViewHelper.createButton("Create");
        southCard.add(btnCreate);

        JButton btnEdit = ViewHelper.createButton("Edit");
        southCard.add(btnEdit);

        JButton btnDelete = ViewHelper.createButton("Delete");
        southCard.add(btnDelete);
        return southCard;
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }


}
