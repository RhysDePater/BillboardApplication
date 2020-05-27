package BillboardControlPanel.Controller;

import BillboardControlPanel.ClientUtilities.ServerRequest;
import BillboardControlPanel.View.PreviewerCard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class PreviewerController extends PreviewerCard{
    public static String[][] xml_data = new String[4][3];

    //Colour related data
    protected static String msgcol_hex;
    protected static String infocol_hex;
    protected static String billcol_hex;
    //private static String[][] bbTableData;
    //private static String[][] bbTableResp;

    public static void initListeners(){
        //Default message colour to black
        xml_data[1][2] = "#000000";
        //Default information colour to black
        xml_data[3][2] =  "#000000";
        //Default background colour to white
        xml_data[0][1] = "#FFFFFF";
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
                xml_data[0][2] = nameField.getText();
                xml_data[1][1] = msgField.getText();
                xml_data[2][1] = picField.getText();
                xml_data[3][1] = infoField.getText();
//                for (int i = 0; i < 4; i++){
//                    for (int k = 0; k < 3; k++){
//                        System.out.println(xml_data[i][k]);
//                    }
//                }
                //getBillTableData();
                billboardDrawer();
                //drawMsg(BorderLayout.NORTH, 0.5);
                //drawPic(BorderLayout.CENTER, 0.5,1);
                //drawInfo(BorderLayout.CENTER, 1);


                centerPanel.validate();
                centerPanel.repaint();
            }
        });
        previewer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerPanel.validate();
                centerPanel.repaint();
            }
        });
    }
    private static void billboardDrawer(){
        if(xml_data[1][1] != "" && (xml_data[2][1] != "") &&  xml_data[3][1] != ""){
            drawMsg(BorderLayout.NORTH,0.35);
            drawPic(BorderLayout.CENTER, 0.3,0.3);
            drawInfo(BorderLayout.SOUTH, 0.35);
        }
        else if((xml_data[2][1] != "") && xml_data[3][1] != ""){
            drawPic(BorderLayout.CENTER, 0.5,0.67);
            drawInfo(BorderLayout.SOUTH, 0.33);
        }
        else if (xml_data[1][1] != "" && xml_data[3][1] != ""){
            drawMsg(BorderLayout.NORTH, 0.5);
            drawInfo(BorderLayout.SOUTH, 0.5);
        }
        else if (xml_data[1][1] != "" && xml_data[2][1] != ""){
            drawMsg(BorderLayout.NORTH, 0.33);
            drawPic(BorderLayout.CENTER, 0.5,0.67);
        }
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
}
