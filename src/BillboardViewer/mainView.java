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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 3);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 30);

    public final String xmlText ="<billboard>\n" +
            "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download\"/>\n" +
            "<information>\n" +
            "Billboard with picture (with URL attribute) and information text only. The picture is now centred within the top 2/3 of the image and the information text is centred in the remaining space below the image.\n" +
            "</information>\n" +
            "</billboard>";

    public mainView(String title) throws HeadlessException {
        super(title);
        final Boolean[] Loop = {true};

        Thread timer = new Thread(() -> {
            while(Loop[0])
            {
                System.out.println("15 seconds?");
                getXMLElements();
                try {
                    Thread.sleep(15000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == ke.VK_ESCAPE) {
                    mainView.this.dispose();
                    Loop[0] = false;
                }
            }
        });
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent mouse) {
                if (mouse.getButton() == MouseEvent.BUTTON1)
                {
                    mainView.this.dispose();
                    Loop[0] = false;
                }
            }
        });
    }

    public void createGUI() {

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getXMLElements();
    }

    public void getXMLElements()
    {
        String[] elements = xmlParser.parseXML(xmlText);
        String Message = elements[0];
        String Picture = elements[1];
        String Info = elements[2];
        String encodedPicture = elements[3];
        String backGroundColour = elements[4];
        String messageColour = elements[5];
        String infoColour = elements[6];

        try {
            drawElements(Message, Picture, Info, encodedPicture, backGroundColour, messageColour, infoColour);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void drawElements(String Message, String Picture, String Info, String encodedPicture, String backGroundColour, String messageColour, String infoColour) throws IOException {
        getContentPane().setBackground(Color.decode(backGroundColour));


        JLabel messageLabel = null;
        JLabel picLabel = null;
        JTextArea infoLabel = null;

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
        JTextPane infoLabel;
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        //pass the scale through
        infoLabel = helper.JMultilineLabel(info, INFORMATION_FONT, this.getBounds().width);
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
        createGUI();
    }
}
