package BillboardViewer;

import BillboardViewer.helper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 3);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 20);

    public final String xmlText = "<billboard background=\"#6800C0\">\n" +
            "<message colour=\"#FF9E3F\">All custom colours</message>\n" +
            "<information colour=\"#3FFFC7\">All custom colours</information>\n" +
            "</billboard>";

    public mainView(String title) throws HeadlessException {
        super(title);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == ke.VK_ESCAPE) {
                    mainView.this.dispose();
                }
            }
        });
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent mouse) {
                if (mouse.getButton() == MouseEvent.BUTTON1)
                {
                    mainView.this.dispose();
                }
            }
        });

    }

    public void createGUI() throws IOException {
        String[] elements = xmlParser.parseXML(xmlText);
        String Message = elements[0];
        String Picture = elements[1];
        String Info = elements[2];
        String encodedPicture = elements[3];
        String backGroundColour = elements[4];
        String messageColour = elements[5];
        String infoColour = elements[6];


        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode(backGroundColour));


        JLabel messageLabel = null;
        JLabel picLabel = null;
        JTextArea infoLabel = null;

        if (Message != "") {
            messageLabel = drawMessage(Message, backGroundColour, messageColour);
        }

        if(Picture != "") {
            drawURLPicture(Picture, backGroundColour);
        }

        if(encodedPicture != "") {
            drawDataPicture(encodedPicture, backGroundColour);
        }

        if (Info != "") {
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel);
        }
    }

    private void drawInformation(String info, String message, String backGroundColour, String infoColour, JLabel messageLabel) {
        JTextArea infoLabel;
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoLabel = helper.JMultilineLabel(info, INFORMATION_FONT, this.getBounds().height, this.getBounds().width);
        infoLabel.setForeground(Color.decode(infoColour));
        infoPanel.setBackground(Color.decode(backGroundColour));
        infoPanel.add(infoLabel);
        if(message !="")
        {
            helper.setInformationFont(messageLabel, infoLabel);
        }
        this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
    }

    private void drawDataPicture(String encodedPicture, String backGroundColour) throws IOException {
        JLabel picLabel;
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        byte[] decodedPicture = Base64.getDecoder().decode(encodedPicture);
        BufferedImage myPicture = ImageIO.read(new ByteArrayInputStream(decodedPicture));
        picLabel = new JLabel(new ImageIcon(myPicture));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        this.getContentPane().add(picPanel,BorderLayout.CENTER);
    }

    private void drawURLPicture(String picture, String backGroundColour) throws IOException {
        JLabel picLabel;
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        URL url = new URL(picture);
        BufferedImage myPicture = ImageIO.read(url);
        picLabel = new JLabel(new ImageIcon(myPicture));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        this.getContentPane().add(picPanel,BorderLayout.CENTER);
    }

    private JLabel drawMessage(String message, String backGroundColour, String messageColour) {
        JLabel messageLabel;
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        messageLabel = helper.createLabel(message, MESSAGE_FONT);
        messageLabel.setForeground(Color.decode(messageColour));
        messagePanel.setBackground(Color.decode(backGroundColour));
        helper.setMessageFont(messageLabel, this);
        messagePanel.add(messageLabel);
        this.getContentPane().add(messagePanel,BorderLayout.NORTH);
        return messageLabel;
    }


    @Override
    public void run() {
        try {
            createGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
