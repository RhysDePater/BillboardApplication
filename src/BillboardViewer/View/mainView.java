package BillboardViewer.View;

import BillboardViewer.HelpFiles.helper;
import BillboardViewer.ClientUtilities.ServerRequest;
import BillboardViewer.HelpFiles.xmlParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 3);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 30);


    public mainView(String title) throws HeadlessException {
        super(title);

        final Boolean[] Loop = {true};

        //Thread to check the server for a new billboard every 15 seconds
        Thread timer = new Thread(() -> {
            while(Loop[0])
            {
                getXMLElements();

                //sleep for 15 seconds
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
                    //break the timer thread out of sleep, so that the app can close right away
                    timer.interrupt();
                    //stop the timer thread
                    Loop[0] = false;
                }
            }
        });
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent mouse) {
                if (mouse.getButton() == MouseEvent.BUTTON1)
                {
                    mainView.this.dispose();
                    //break the timer thread out of sleep, so that the app can close right away.
                    timer.interrupt();
                    //stop the timer thread
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
    }


    public void getXMLElements()
    {
        String[] xmlData = null;

        try {
            xmlData = ServerRequest.getCurrentBillboard();
            System.out.println(xmlData[1]);
        }
        catch(Exception e)
        {
            try {
                drawElements("Unable to connect to billboard server.", "", "", "", "#FFFFFF", "#000000", "");
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }

        String[] elements = xmlParser.parseXML(xmlData[1]);
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

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridBagLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridBagLayout());

        JLabel messageLabel = null;
        JLabel picLabel = null;
        JTextArea infoLabel = null;

        if (Message != "" && (Picture != "" || encodedPicture !="") && Info != "")
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, northPanel, 0.35);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.3, centerPanel, 0.3);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.3, centerPanel,0.3);
            }
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, southPanel, 0.35);
        }
        else if ((Picture != "" || encodedPicture !="") && Info != "")
        {
            //drawMessage("", backGroundColour, messageColour, BorderLayout.NORTH, 0);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, centerPanel, 0.67);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5,centerPanel, 0.67);
            }
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, southPanel, 0.33);
        }
        else if (Message != "" && Info != "")
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, northPanel, 0.5);
            drawMessage("", backGroundColour, messageColour, centerPanel, 0);
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, southPanel,0.5);
        }
        else if (Message != "" && (Picture != "" || encodedPicture !="") )
        {
            messageLabel = drawMessage(Message, backGroundColour, messageColour, northPanel, 0.33);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, centerPanel, 0.67);
                System.out.println("Im in");
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, centerPanel, 0.67);
            }
            drawMessage("", backGroundColour, messageColour, southPanel, 0);
        }
        else if ( Info != "")
        {
            drawMessage("", backGroundColour, messageColour, northPanel, 0);
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, centerPanel, 1);
            drawMessage("", backGroundColour, messageColour, southPanel, 0);
        }
        else if ((Picture != "" || encodedPicture !=""))
        {
            drawMessage("", backGroundColour, messageColour, northPanel, 0);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, centerPanel, 1);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, centerPanel,1);
            }
            drawMessage("", backGroundColour, messageColour, southPanel, 0);
        }
        else if (Message != "")
        {
            drawMessage("", backGroundColour, messageColour, northPanel, 0);
            drawMessage(Message, backGroundColour, messageColour, centerPanel, 1);
            drawMessage("", backGroundColour, messageColour, southPanel, 0);
        }
        else
        {
            drawMessage("", backGroundColour, messageColour, northPanel, 0);
            drawMessage("", backGroundColour, messageColour, centerPanel, 0);
            drawMessage("", backGroundColour, messageColour, southPanel, 0);
        }

        this.getContentPane().add(northPanel,BorderLayout.NORTH);
        this.getContentPane().add(centerPanel,BorderLayout.CENTER);
        this.getContentPane().add(southPanel,BorderLayout.SOUTH);
        setVisible(true);
    }


    private void drawInformation(String info, String message, String backGroundColour, String infoColour, JLabel messageLabel, JPanel infoPanel, double scale) {
        JTextPane infoLabel;
        //pass the scale through
        infoLabel = helper.JMultilineLabel(info, INFORMATION_FONT);
        infoLabel.setForeground(Color.decode(infoColour));
        infoPanel.setBackground(Color.decode(backGroundColour));
        infoPanel.add(infoLabel);
        if(message !="")
        {
            helper.setInformationFont(messageLabel, infoLabel);
        }
        infoPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));

        if (scale<=0.5) {
            infoLabel.setPreferredSize(new Dimension((int) ((double) this.getBounds().width * 0.75), (int) ((double) this.getBounds().height * scale)));
        }
        else{
            infoLabel.setPreferredSize(new Dimension((int) ((double) this.getBounds().width * 0.75), (int) ((double) this.getBounds().height * 0.5)));
        }
    }

    private void drawDataPicture(String encodedPicture, String backGroundColour, double imageScale, JPanel picPanel, double panelScale) throws IOException {
        JLabel picLabel;
        byte[] decodedPicture = Base64.getDecoder().decode(encodedPicture);
        BufferedImage myPicture = ImageIO.read(new ByteArrayInputStream(decodedPicture));
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
        picLabel = new JLabel(new ImageIcon(newImage));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
    }

    private void drawURLPicture(String picture, String backGroundColour, double imageScale, JPanel picPanel, double panelScale) throws IOException {
        JLabel picLabel;
        URL url = new URL(picture);
        BufferedImage myPicture = ImageIO.read(url);
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
        picLabel = new JLabel(new ImageIcon(newImage));
        picPanel.setBackground(Color.decode(backGroundColour));
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
    }

    private JLabel drawMessage(String message, String backGroundColour, String messageColour, JPanel messagePanel, double scale) {
        JLabel messageLabel;
        messageLabel = helper.createLabel(message, MESSAGE_FONT);
        messageLabel.setForeground(Color.decode(messageColour));
        messagePanel.setBackground(Color.decode(backGroundColour));
        helper.setMessageFont(messageLabel, this);
        messagePanel.add(messageLabel);
        messagePanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));
        return messageLabel;
    }


    @Override
    public void run() {
        createGUI();
        getXMLElements();
    }
}
