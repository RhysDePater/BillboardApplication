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
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 30);

    public final String xmlText ="<billboard background=\"#6800C0\">\n" +
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

        //TODO - setup the conditions for when the viewer is only passed certain elements
        if (Message != "" && (Picture != "" || encodedPicture !="") && Info != "")
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, BorderLayout.NORTH, 0.35);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.3, BorderLayout.CENTER, 0.3);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.3, BorderLayout.CENTER,0.3);
            }
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, BorderLayout.SOUTH, 0.35);
        }
        else if ((Picture != "" || encodedPicture !="") && Info != "")
        {
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, BorderLayout.CENTER, 0.67);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, BorderLayout.CENTER, 0.67);
            }
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, BorderLayout.SOUTH, 0.33);
        }
        else if (Message != "" && Info != "")
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, BorderLayout.NORTH, 0.5);

            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, BorderLayout.SOUTH,0.5);
        }
        else if (Message != "" && (Picture != "" || encodedPicture !="") )
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, BorderLayout.NORTH, 0.33);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, BorderLayout.CENTER, 0.67);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, BorderLayout.CENTER, 0.67);
            }
        }
        else if ( Info != "")
        {
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, BorderLayout.CENTER, 1);
        }
        else if ((Picture != "" || encodedPicture !=""))
        {
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, BorderLayout.CENTER, 1);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, BorderLayout.CENTER,1);
            }
        }
        else if (Message != "")
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, BorderLayout.CENTER, 1);
        }
    }

    private void drawInformation(String info, String message, String backGroundColour, String infoColour, JLabel messageLabel, String Position, double scale) {
        JTextArea infoLabel;
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoLabel = helper.JMultilineLabel(info, INFORMATION_FONT, this.getHeight(), this.getWidth());
        infoLabel.setForeground(Color.decode(infoColour));
        infoPanel.setBackground(Color.decode(backGroundColour));
        infoPanel.add(infoLabel);
        if(message !="")
        {
            helper.setInformationFont(messageLabel, infoLabel);
        }
        infoPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));
        this.getContentPane().add(infoPanel,Position);
    }

    private void drawDataPicture(String encodedPicture, String backGroundColour, double imageScale, String position, double panelScale) throws IOException {
        JLabel picLabel;
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        byte[] decodedPicture = Base64.getDecoder().decode(encodedPicture);
        BufferedImage myPicture = ImageIO.read(new ByteArrayInputStream(decodedPicture));
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
        picLabel = new JLabel(new ImageIcon(newImage));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
        this.getContentPane().add(picPanel,position);
    }

    private void drawURLPicture(String picture, String backGroundColour, double imageScale, String position, double panelScale) throws IOException {
        JLabel picLabel;
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        URL url = new URL(picture);
        BufferedImage myPicture = ImageIO.read(url);
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
        picLabel = new JLabel(new ImageIcon(newImage));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
        this.getContentPane().add(picPanel,position);
    }

    private JLabel drawMessage(String message, String backGroundColour, String messageColour, String position, double scale) {
        JLabel messageLabel;
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        messageLabel = helper.createLabel(message, MESSAGE_FONT);
        messageLabel.setForeground(Color.decode(messageColour));
        messagePanel.setBackground(Color.decode(backGroundColour));
        helper.setMessageFont(messageLabel, this);
        messagePanel.add(messageLabel);
        messagePanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));
        this.getContentPane().add(messagePanel,position);
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
