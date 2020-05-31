package BillboardControlPanel.Controller;

import BillboardControlPanel.ServerUtilities.ServerRequestClient;
import BillboardControlPanel.Helper.ControllerHelper;
import BillboardControlPanel.Model.XMLParsing;
import BillboardControlPanel.View.ManageBillboardCard;
import BillboardControlPanel.View.MasterView;
import BillboardControlPanel.View.PreviewerCard;
import static BillboardControlPanel.Controller.PreviewerController.xml_data;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;

/**
 * This class handles all ManageBillboardCard functionalities.
 * Includes calling the previewer, pulling user data from server, confirming user permissions.
 * Includes editing, exporting, importing, deleting billboards.
 * Displays billboards in a list.
 */
public class ManageBillboardController {

    private ManageBillboardCard manageBillboardCard;
    private static String[][] respArray;
    private int selectedRow = -1;
    private int selectedCol = -1;

    //Pull GUI information
    public ManageBillboardController(){
        initView();
        initController(getManageBillboardCard());
    }

    //Pull billboard data to display
    public void initView(){
            manageBillboardCard = new ManageBillboardCard(MainController.getBillData());
    }

    //Initiate all listeners for user interactions and respond accordingly
    public void initController(ManageBillboardCard manageBillboardCard) {
        ControllerHelper.enableGlobalButtons(manageBillboardCard);
        //CREATE
        manageBillboardCard.getBtnCreate().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Refreshing the privileges here is essential, as users with "edit user" privileges can encounter bugs.
                MainController.setLoggedUserPrivs(ServerRequestClient.getFormattedUserPrivs(MainController.getLoggedUser(), MainController.getSessionToken()));
                if (MainController.getLoggedUserPrivs()[0].equalsIgnoreCase("1")) {
                    PreviewerCard.PreviewerCard();
                } else {
                    ControllerHelper.returnMessage("You are not granted create billboard privileges.");
                }
            }
        });
        //TABLE
        manageBillboardCard.getBillTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = manageBillboardCard.getBillTable().rowAtPoint(e.getPoint());
                selectedCol = manageBillboardCard.getBillTable().columnAtPoint(e.getPoint());
            }
        });

        //EDIT
        manageBillboardCard.getBtnEdit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Refreshing the privileges here is essential, as users with "edit user" privileges can encounter bugs.
                MainController.setLoggedUserPrivs(ServerRequestClient.getFormattedUserPrivs(MainController.getLoggedUser(), MainController.getSessionToken()));
                if (selectedRow == -1 || selectedCol == -1){
                    JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Please select a billboard from the table.");
                }
                //Check to see if user has full edit permissions.
                else if (MainController.getLoggedUserPrivs()[1].equalsIgnoreCase("1")){
                    PreviewerCard.PreviewerCard();
                    String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                    String tableXMLData = manageBillboardCard.getBillTable().getValueAt(selectedRow, 2).toString();
                    try {
                        xml_data = XMLParsing.StrToXMLArray(tableXMLData);
                        xml_data[0][2] = bbName;
                        setFields();
                    } catch (ParserConfigurationException parse_error) {
                        JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Billboard table is currently empty.");
                    }
                }
                //check to see if user has create billboard permissions, if so did they create this billboard, is it scheduled?
                else if ((MainController.getLoggedUserPrivs()[0].equalsIgnoreCase("1"))
                        && (manageBillboardCard.getBillTable().getValueAt(selectedRow, 0).toString().equalsIgnoreCase(MainController.getLoggedUser()))
                        && (manageBillboardCard.getBillTable().getValueAt(selectedRow, 3).toString().equals("0"))){
                    PreviewerCard.PreviewerCard();
                    String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                    String tableXMLData = manageBillboardCard.getBillTable().getValueAt(selectedRow, 2).toString();
                    try {
                        xml_data = XMLParsing.StrToXMLArray(tableXMLData);
                        xml_data[0][2] = bbName;
                        setFields();
                    } catch (ParserConfigurationException parse_error) {
                        JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Billboard table is currently empty.");
                    }
                }
                else {
                    ControllerHelper.returnMessage("You are not granted privileges to edit this billboard. If you can create billboards, you can only edit your own if it's not scheduled.");
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
                    //Refreshing the privileges here is essential, as users with "edit user" privileges can encounter bugs.
                    MainController.setLoggedUserPrivs(ServerRequestClient.getFormattedUserPrivs(MainController.getLoggedUser(), MainController.getSessionToken()));
                    if (MainController.getLoggedUserPrivs()[1].equalsIgnoreCase("1")){
                        String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                        ServerRequestClient.deleteBillboard(bbName, MainController.getSessionToken());
                        ControllerHelper.refreshBillBoardTablePanel();
                    }
                    else if((MainController.getLoggedUserPrivs()[0].equalsIgnoreCase("1"))
                            && (manageBillboardCard.getBillTable().getValueAt(selectedRow, 0).toString().equalsIgnoreCase(MainController.getLoggedUser()))
                            && (manageBillboardCard.getBillTable().getValueAt(selectedRow, 3).toString().equals("0"))){
                        String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                        ServerRequestClient.deleteBillboard(bbName, MainController.getSessionToken());
                        ControllerHelper.refreshBillBoardTablePanel();
                    }
                    else {
                        ControllerHelper.returnMessage("You are not granted privileges to delete this billboard.");
                    }
                }

            }
        });
        //DELETE
        manageBillboardCard.getBtnImport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Select the XML File
                JFileChooser xml_chooser = new JFileChooser();
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                xml_chooser.setDialogTitle("Select XML File to import!");
                xml_chooser.setAcceptAllFileFilterUsed(false);
                xml_chooser.setFileFilter(xmlFilter);
                int rtnValue = xml_chooser.showOpenDialog(MasterView.getMainFrame());
                if(rtnValue == JFileChooser.APPROVE_OPTION){
                    String xmlFilePath = (xml_chooser.getSelectedFile().getAbsolutePath());
                    System.out.println(xmlFilePath);
                    //XML file parsing
                    String extractedXMLStr = XMLParsing.XMLFileToString(xmlFilePath);
                    System.out.println(extractedXMLStr);
                    PreviewerCard.PreviewerCard();
                    try {
                        xml_data = XMLParsing.StrToXMLArray(extractedXMLStr);
                        //Place data into String[][] further usage
                        setFields();
                    } catch(ParserConfigurationException parse_error){
                        JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Please import a correctly formatted XML file.");
                    }
                    ControllerHelper.refreshBillBoardTablePanel();
                }
            }
        });
        //EXPORT
        manageBillboardCard.getBtnExport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1 || selectedCol == -1){
                    JOptionPane.showMessageDialog(MasterView.getMainFrame(), "Please select a billboard from the table.");
                }
                else {
                    String bbName = manageBillboardCard.getBillTable().getValueAt(selectedRow, 1).toString();
                    String tableXMLData = manageBillboardCard.getBillTable().getValueAt(selectedRow, 2).toString();
                    JFileChooser xml_exporter = new JFileChooser();
                    xml_exporter.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int rtnValue = xml_exporter.showOpenDialog(MasterView.getMainFrame());
                    if(rtnValue == JFileChooser.APPROVE_OPTION){
                        String xmlFilePath = (xml_exporter.getSelectedFile().getAbsolutePath());
                        System.out.println(xmlFilePath);
                        XMLParsing.StrToXMLFile(tableXMLData, xmlFilePath, bbName);
                    }
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
        PreviewerController.billboardDrawer();
        PreviewerCard.getCenterPanel().validate();
    }

    public void setSelectedCol(int newValue) { this.selectedCol = newValue; }
    public void setSelectedRow(int newValue) { this.selectedRow = newValue; }

    public ManageBillboardCard getManageBillboardCard() { return manageBillboardCard; }

}