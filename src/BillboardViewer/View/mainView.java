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

/**
 * mainView is the viewer itself and is where everything is displayed
 * @author Rhys De Pater
 * @version 1.0.0
 * @since 2020-05-01
 */
public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 3);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 30);

    /**
     * Constructor, also creates the timer thread and event listeners
     * @param title
     * @throws HeadlessException
     */
    public mainView(String title) throws HeadlessException {
        super(title);

        //boolean used for breaking the timer thread
        final Boolean[] Loop = {true};

        //Thread to check the server for a new billboard every 15 seconds
        Thread timer = new Thread(() -> {
            while(Loop[0])
            {
                //update the viewer
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

        //event listen for the escape key
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == ke.VK_ESCAPE) {
                    //close the viewer
                    mainView.this.dispose();
                    //break the timer thread out of sleep, so that the app can close right away
                    timer.interrupt();
                    //stop the timer thread
                    Loop[0] = false;
                }
            }
        });
        //event listen for mouse click
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent mouse) {
                if (mouse.getButton() == MouseEvent.BUTTON1)
                {
                    //close the viewer
                    mainView.this.dispose();
                    //break the timer thread out of sleep, so that the app can close right away.
                    timer.interrupt();
                    //stop the timer thread
                    Loop[0] = false;
                }
            }
        });
    }

    /**
     * function for creating the frame
     */
    public void createGUI() {
        //set the frame to be the screen size
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //get rid of the window border
        setUndecorated(true);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * function for getting the xml data from the server and then pulling the elements from that
     * and sending them to be drawn
     */
    public void getXMLElements()
    {
        //intialise the data
        String[] xmlData = null;

        try {
            //get what should be displayed from the server
            xmlData = ServerRequest.getCurrentBillboard();
            System.out.println(xmlData[1]);
        }
        //if the application can't talk to the server
        catch(Exception e)
        {
            try {
                //tell the user the viewer couldn't connect to the server
                drawElements("Unable to connect to billboard server.", "", "", "", "#FFFFFF", "#000000", "");
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }

        //parse the xml data that was taken from the server assign each array section to a variable
        String[] elements = xmlParser.parseXML(xmlData[1]);
        String Message = elements[0];
        String Picture = elements[1];
        String Info = elements[2];
        String encodedPicture = elements[3];
        String backGroundColour = elements[4];
        String messageColour = elements[5];
        String infoColour = elements[6];

        try {
            //draw the elements to the screen
            drawElements(Message, Picture, Info, encodedPicture, backGroundColour, messageColour, infoColour);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Draws the elements on screen based on what has data and what is an empty string
     * @param Message
     * @param Picture
     * @param Info
     * @param encodedPicture
     * @param backGroundColour
     * @param messageColour
     * @param infoColour
     * @throws IOException
     */
    public void drawElements(String Message, String Picture, String Info, String encodedPicture, String backGroundColour, String messageColour, String infoColour) throws IOException {
        //set the frame background colour
        getContentPane().setBackground(Color.decode(backGroundColour));

        //setup the panels that will be shown in the north center and south region
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridBagLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridBagLayout());

        JLabel messageLabel = null;
        JLabel picLabel = null;
        JTextArea infoLabel = null;

        //draw the elements on screen according to what was given by the xml

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
            drawMessage("", backGroundColour, messageColour, northPanel, 0);
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

        //add the panel to the frame
        this.getContentPane().add(northPanel,BorderLayout.NORTH);
        this.getContentPane().add(centerPanel,BorderLayout.CENTER);
        this.getContentPane().add(southPanel,BorderLayout.SOUTH);

        //set the frame background colour
        getContentPane().setBackground(Color.decode(backGroundColour));
        //makes sure everything that was added to screen is visible
        setVisible(true);
    }

    /**
     * Draw the info section on screen in the panel passed to it with a size based on scale
     * @param info
     * @param message
     * @param backGroundColour
     * @param infoColour
     * @param messageLabel
     * @param infoPanel
     * @param scale
     */
    private void drawInformation(String info, String message, String backGroundColour, String infoColour, JLabel messageLabel, JPanel infoPanel, double scale) {
        //create the label
        JTextPane infoLabel;
        infoLabel = helper.JMultilineLabel(info, INFORMATION_FONT);

        //set label text colour
        infoLabel.setForeground(Color.decode(infoColour));
        //set the panel background
        infoPanel.setBackground(Color.decode(backGroundColour));
        //add the text to the panel
        infoPanel.add(infoLabel);
        if(message !="")
        {
            //set the text size
            helper.setInformationFont(messageLabel, infoLabel);
        }
        //scale the panel to the desired size on screen
        infoPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));

        //scale the label to the desired size on screen while not exceeding 50% height
        if (scale<=0.5) {
            infoLabel.setPreferredSize(new Dimension((int) ((double) this.getBounds().width * 0.75), (int) ((double) this.getBounds().height * scale)));
        }
        else{
            infoLabel.setPreferredSize(new Dimension((int) ((double) this.getBounds().width * 0.75), (int) ((double) this.getBounds().height * 0.5)));
        }
    }

    /**
     * function for drawing the data picture with provided base 64 encoded image data
     * it is also sized according to image scale and places in pic panel with panel scale size
     * @param encodedPicture
     * @param backGroundColour
     * @param imageScale
     * @param picPanel
     * @param panelScale
     * @throws IOException
     */
    private void drawDataPicture(String encodedPicture, String backGroundColour, double imageScale, JPanel picPanel, double panelScale) throws IOException {
        //set label that will hold the picture
        JLabel picLabel;

        //decode the data given using base64
        byte[] decodedPicture = Base64.getDecoder().decode(encodedPicture);
        //create a buffered image using the decoded data
        BufferedImage myPicture = ImageIO.read(new ByteArrayInputStream(decodedPicture));

        //get the picture height and width according to the scale provided
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);

        //create an image by scaling the bufferedImage previously made with the new height and width found
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
        //add the image to the label
        picLabel = new JLabel(new ImageIcon(newImage));
        //set panel background
        picPanel.setBackground(Color.decode(backGroundColour));

        //add the picture to the panel and then set the panel size
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
    }

    /**
     * function for drawing the url picture with provided url to an image
     * it is also sized according to image scale and places in pic panel with panel scale size
     * @param picture
     * @param backGroundColour
     * @param imageScale
     * @param picPanel
     * @param panelScale
     * @throws IOException
     */
    private void drawURLPicture(String picture, String backGroundColour, double imageScale, JPanel picPanel, double panelScale) throws IOException {
        //set the label and url
        JLabel picLabel;
        URL url = new URL(picture);

        //create buffered image using url
        BufferedImage myPicture = ImageIO.read(url);

        //get the picture height and width according to the scale provided
        int[] imgDimensions = helper.getImgDimensions(myPicture.getHeight(),myPicture.getWidth(), imageScale, this);

        //create an image by scaling the bufferedImage previously made with the new height and width found
        Image newImage = myPicture.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);

        //add the image to the label
        picLabel = new JLabel(new ImageIcon(newImage));
        //set panel background
        picPanel.setBackground(Color.decode(backGroundColour));
        //add picture to panel and set panel size
        picPanel.add(picLabel);
        picPanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height * panelScale)));
    }

    /**
     * draw message in panel messagePanel scaled to scale
     * @param message
     * @param backGroundColour
     * @param messageColour
     * @param messagePanel
     * @param scale
     * @return
     */
    private JLabel drawMessage(String message, String backGroundColour, String messageColour, JPanel messagePanel, double scale) {
        //create the message label
        JLabel messageLabel;
        messageLabel = helper.createLabel(message, MESSAGE_FONT);

        //set message text colour
        messageLabel.setForeground(Color.decode(messageColour));
        //set panel background colour
        messagePanel.setBackground(Color.decode(backGroundColour));

        //add the label to the panel and set panel size
        messagePanel.add(messageLabel);
        messagePanel.setSize(this.getBounds().width, (int)((double)this.getBounds().height *scale));
        messagePanel.setPreferredSize(new Dimension(this.getBounds().width, (int)((double)this.getBounds().height *scale)));

        //set the text size to max possible size
        helper.setMessageFont(messageLabel, messagePanel);
        return messageLabel;
    }


    @Override
    public void run() {
        createGUI();
        getXMLElements();
    }
}
