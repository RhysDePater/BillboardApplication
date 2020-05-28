package BillboardControlPanel.Controller;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.Model.XMLParsing;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.MasterView;
import BillboardControlPanel.View.PreviewerCard;
import static BillboardControlPanel.Controller.PreviewerController.xml_data;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;


public class ManageBillboardController {

    private ManageBillboardCard manageBillboardCard;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ManageBillboardController(){
        initView();
        initController(getManageBillboardCard());
    }

    public void initView(){
        manageBillboardCard = new ManageBillboardCard(MainController.getBillData());
    }

    public void initController(ManageBillboardCard manageBillboardCard) {
        ControllerHelper.enableGlobalButtons(manageBillboardCard);
        manageBillboardCard.getBtnCreate().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //ControllerHelper.refreshBillBoardTablePanel();
                PreviewerCard.PreviewerCard();
            }
        });
        manageBillboardCard.getBillTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = manageBillboardCard.getBillTable().rowAtPoint(e.getPoint());
                selectedCol = manageBillboardCard.getBillTable().columnAtPoint(e.getPoint());
            }
        });

        manageBillboardCard.getBtnEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1 || selectedCol == -1){
                    JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Please select a billboard from the table.");
                }
                else {
                    PreviewerCard.PreviewerCard();
                    String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                    String tableXMLData = manageBillboardCard.getBillTable().getValueAt(selectedRow, 2).toString();
                    try {
                        xml_data = XMLParsing.StrToXMLArray(tableXMLData);
                        xml_data[0][2] = bbName;

                        for (int i = 0; i < 4; i++){
                            for (int k = 0; k < 3; k++){
                            System.out.println(PreviewerController.xml_data[i][k]);
                            }
                        }
                        setFields();
                    } catch (ParserConfigurationException parse_error) {
                        parse_error.printStackTrace();////////////////////////////////////////////////////CHANGE LATER
                    }
                }
            }
        });
        manageBillboardCard.getBtnDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1 || selectedCol == -1){
                    JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Please select a billboard from the table.");
                }
                else {
                    String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                    ServerRequestClient.deleteBillboard(bbName, MainController.getSessionToken());
                    ControllerHelper.refreshBillBoardTablePanel();
                }

            }
        });
    }

    private static void setFields(){
        //Set the default colours if no colour is provided by the xml String.
        if ( xml_data[1][2] == null) //msg
        {
            xml_data[1][2] = "#000000";
        }
        if (xml_data[3][2] == null) //info
        {
            xml_data[3][2] = "#000000";
        }
        if (xml_data[0][1] == null) //bkgrnd
        {
            xml_data[0][1] = "#FFFFFF";
        }

        //Input fields
        PreviewerCard.getNameField().setText(xml_data[0][2]);
        PreviewerCard.getMsgField().setText(xml_data[1][1]);
        PreviewerCard.getPicField().setText(xml_data[2][1]);
        PreviewerCard.getInfoField().setText(xml_data[3][1]);
        //Center Panel Components
        //PreviewerCard.getCenterPanel().setBackground(Color.decode(xml_data[0][1]));
        PreviewerController.billboardDrawer();
        //PreviewerCard.getMessageLabel().setText("hi");
        //PreviewerCard.getInformationLabel().setText(xml_data[3][1]);


        //PreviewerCard.getMessageLabel().setForeground(Color.decode(xml_data[1][2]));
        //PreviewerCard.getInformationLabel().setForeground(Color.decode(xml_data[3][2]));
    }

    public void setSelectedCol(int newValue) { this.selectedCol = newValue; }
    public void setSelectedRow(int newValue) { this.selectedRow = newValue; }

    public ManageBillboardCard getManageBillboardCard() { return manageBillboardCard; }

}
