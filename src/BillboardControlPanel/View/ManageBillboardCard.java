package BillboardControlPanel.View;

import BillboardControlPanel.Helper.ViewHelper;

import javax.swing.*;
import java.awt.*;

public class ManageBillboardCard extends MasterView {
    private static JButton btnCreate;
    private static JButton btnEdit;
    private static JButton btnDelete;
    private static JButton btnImport;
    private static JButton btnExport;
    private static JTable billboardTable;

    private static String[] colNames = new String[]{"User Name", "Billboard Name","XML Data", "Status"};

    public ManageBillboardCard(String[][] colData){
        createCenterCard(colData);
        createSouthCard();
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

        btnEdit = ViewHelper.createButton("Edit");
        southCard.add(btnEdit);

        btnDelete = ViewHelper.createButton("Delete");
        southCard.add(btnDelete);

        btnImport = ViewHelper.createButton("Import");
        southCard.add(btnImport);

        btnExport = ViewHelper.createButton("Export");
        southCard.add(btnExport);
        return southCard;
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnImport() { return btnImport; }
    public JButton getBtnExport() {return btnExport; }
    public JTable getBillTable(){ return billboardTable; }

}
