package BillboardControlPanel.Controller;

import BillboardControlPanel.View.PreviewerCard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static BillboardControlPanel.ClientUtilities.ServerRequest.createOrEditBillboard;
import static BillboardControlPanel.Helper.ControllerHelper.refreshBillBoardTablePanel;


public class PreviewerController extends PreviewerCard{
    public static String[][] xml_data = new String[4][3];

    //Colour related data
    protected static String msgcol_hex;
    protected static String infocol_hex;
    protected static String billcol_hex;
    protected static String newXMLStr;
    protected static String msg;
    protected static String pic;
    protected static String info;
    protected static String bkgrndCol;
    protected static String msgCol;
    protected static String infoCol;

    public static void initDefaultCol(){
        //Default message colour to black
        xml_data[1][2] = "#000000";
        //Default information colour to black
        xml_data[3][2] =  "#000000";
        //Default background colour to white
        xml_data[0][1] = "#FFFFFF";
    }

    public static void initListeners(){
        msgJCCbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color msgRGB = JColorChooser.showDialog(previewer, "Message Colour Chooser", Color.BLACK);
                int r = msgRGB.getRed();
                int g = msgRGB.getGreen();
                int b = msgRGB.getBlue();
                msgcol_hex = String.format("#%02x%02x%02x", r, g, b);
                xml_data[1][2] = msgcol_hex;
                System.out.println("Colour = " + msgRGB + " " + r + " " + g + " " + b + " " + msgcol_hex);
            }
        });
        infoJCCbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color infoRGB = JColorChooser.showDialog(previewer, "Message Colour Chooser", Color.BLACK);
                int r = infoRGB.getRed();
                int g = infoRGB.getGreen();
                int b = infoRGB.getBlue();
                infocol_hex = String.format("#%02x%02x%02x", r, g, b);
                xml_data[3][2] = infocol_hex;
                System.out.println("Colour = " + infoRGB + " " + r + " " + g + " " + b + " " + infocol_hex);
            }
        });
        billJCCbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color billRGB = JColorChooser.showDialog(previewer, "Message Colour Chooser", Color.WHITE);
                int r = billRGB.getRed();
                int g = billRGB.getGreen();
                int b = billRGB.getBlue();
                billcol_hex = String.format("#%02x%02x%02x", r, g, b);
                xml_data[0][1] = billcol_hex;
                System.out.println("Colour = " + billRGB + " " + r + " " + g + " " + b + " " + billcol_hex);
            }
        });
        genBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.removeAll();

                getAllFields();
//                for (int i = 0; i < 4; i++){
//                    for (int k = 0; k < 3; k++){
//                        System.out.println(xml_data[i][k]);
//                    }
//                }
                billboardDrawer();
                centerPanel.validate();
                centerPanel.repaint();
            }
        });
//        previewer.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                centerPanel.validate();
//                centerPanel.repaint();
//            }
//        });
        uploadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAllFields();
                if (xml_data[0][2].isEmpty() || xml_data[0][2] == null){
                    JOptionPane.showMessageDialog(previewer, "Please give this billboard a name.");
                }
                else if((xml_data[1][1].isEmpty() || xml_data[1][1] == null) && (xml_data[2][1].isEmpty() || xml_data[2][1] == null) &&
                        (xml_data[3][1].isEmpty() || xml_data[3][1] == null)){
                    JOptionPane.showMessageDialog(previewer, "Please enter at least a message, picture or information.");
                }
                else {
                    String createdXMLStr = xmlStringer();
                    createOrEditBillboard(xml_data[0][2], createdXMLStr, MainController.getSessionToken());
                    previewer.dispose();
                    refreshBillBoardTablePanel();
                }
            }
        });
    }

    private static void getAllFields(){
        xml_data[0][2] = nameField.getText();
        xml_data[1][1] = msgField.getText();
        xml_data[2][1] = picField.getText();
        xml_data[3][1] = infoField.getText();
    }

    protected static void billboardDrawer(){
        msg = xml_data[1][1];
        pic = xml_data[2][1];
        info = xml_data[3][1];
        centerPanel.setBackground(Color.decode(xml_data[0][1]));

        if((msg.isEmpty() == false && msg != null) && (pic.isEmpty() == false && pic != null) && (info.isEmpty() == false && info != null)){
            drawMsg(BorderLayout.NORTH,0.35);
            drawPic(BorderLayout.CENTER, 0.3,0.3);
            drawInfo(BorderLayout.SOUTH, 0.35);
            System.out.println("1");
        }
        else if((pic.isEmpty() == false && pic != null) && (info.isEmpty() == false && info != null)){
            drawPic(BorderLayout.CENTER, 0.5,0.67);
            drawInfo(BorderLayout.SOUTH, 0.33);
            System.out.println("2");
        }
        else if ((msg.isEmpty() == false && msg != null) && (info.isEmpty() == false && info != null)){
            drawMsg(BorderLayout.NORTH, 0.5);
            drawInfo(BorderLayout.SOUTH, 0.5);
            System.out.println("3");
        }
        else if ((msg.isEmpty() == false && msg != null) && (pic.isEmpty() == false && pic != null)){
            drawMsg(BorderLayout.NORTH, 0.33);
            drawPic(BorderLayout.CENTER, 0.5,0.67);
            System.out.println("4");
        }
        else if (info.isEmpty() == false && info != null){
            drawInfo(BorderLayout.CENTER, 1);
            System.out.println("5");
        }
        else if (pic.isEmpty() == false && pic != null){
            drawPic(BorderLayout.CENTER, 0.5,1);
            System.out.println("6");
        }
        else if (msg.isEmpty() == false && msg != null){
            drawMsg(BorderLayout.CENTER, 1);
            System.out.println("7");
        }
    }

    private static String xmlStringer(){
        msg = xml_data[1][1];
        pic = xml_data[2][1];
        info = xml_data[3][1];
        bkgrndCol = xml_data[0][1];
        msgCol = xml_data[1][2];
        infoCol = xml_data[3][2];
        String msgComp = "";
        String picComp = "";
        String infoComp = "";
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String bbComp1 = "<billboard background=\"" + bkgrndCol + "\">";
        String bbComp2 = "</billboard>";
        if (msg.isEmpty() == false && msg != null){
            msgComp = "<message colour=\"" + msgCol + "\">" + msg + "</message>";
        }
        if (pic.isEmpty() == false && pic != null){
            if (isURL){
                picComp = "<picture url=\"" + pic + "\"/>";
            }
            else if (!isURL){
                picComp = "<picture data=\"" + pic + "\"/>";
            }
        }
        else {
            picComp ="";
        }


        if (info.isEmpty() == false && msg != null){
            infoComp = "<information colour=\"" + infoCol + "\">" + info + "</information>";
        }

        newXMLStr = header + bbComp1 + msgComp + picComp + infoComp + bbComp2;
        System.out.println(newXMLStr);
        return newXMLStr;
    }
}
