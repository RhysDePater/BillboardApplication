package BillboardViewer;

import BillboardViewer.helper;
import BillboardViewer.ClientUtilities.ServerRequest;

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


    public mainView(String title) throws HeadlessException {
        super(title);
        final Boolean[] Loop = {true};


        Thread timer = new Thread(() -> {
            while(Loop[0])
            {
                //get the data and draw it on screen
                try {
                    //get the xmldata for the billboard that should be displayed from the server
                    String[] xmlData = ServerRequest.getCurrentBillboard();
                    System.out.println(xmlData[1]);
                    //send the xml string the xmlparser to be drawn on screen
                    getXMLElements(xmlData[1]);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }

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
        try {
            String[] xmlData = ServerRequest.getCurrentBillboard();
            System.out.println(xmlData[1]);
            getXMLElements(xmlData[1]);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void getXMLElements(String xmlText)
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
            this.getContentPane().add(null,BorderLayout.NORTH);
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
            this.getContentPane().add(null,BorderLayout.CENTER);
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
            this.getContentPane().add(null,BorderLayout.SOUTH);
        }
        else if ( Info != "")
        {
            drawInformation(Info, Message, backGroundColour, infoColour, messageLabel, BorderLayout.CENTER, 1);
        }
        else if ((Picture != "" || encodedPicture !=""))
        {
            this.getContentPane().add(null,BorderLayout.NORTH);
            if (Picture != "")
            {
                drawURLPicture(Picture, backGroundColour, 0.5, BorderLayout.CENTER, 1);
            }
            else
            {
                drawDataPicture(encodedPicture, backGroundColour, 0.5, BorderLayout.CENTER,1);
            }
            this.getContentPane().add(null,BorderLayout.SOUTH);
        }
        else if (Message != "")
        {
            this.getContentPane().add(null,BorderLayout.NORTH);
            messageLabel = drawMessage(Message, backGroundColour, messageColour, BorderLayout.CENTER, 1);
            this.getContentPane().add(null,BorderLayout.SOUTH);
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
