package BillboardControlPanel.View;
import BillboardControlPanel.Controller.PreviewerController;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static BillboardControlPanel.Controller.PreviewerController.xml_data;
import static BillboardControlPanel.Helper.ViewHelper.createButton;
import static BillboardControlPanel.Model.XMLParsing.Base64Handler;
import static BillboardControlPanel.Model.XMLParsing.UrlHandler;

public class PreviewerCard {
    //Fonts for Message and Information
    private static final Font MsgFont = new Font("SansSerif", Font.BOLD, 3);
    private static final Font InfoFont = new Font("SansSerif", Font.BOLD, 30);
    //messageLabel is used for the Billboard previewer.
    private static JLabel messageLabel = null;

    protected static JFrame previewer;
    //Billboard Name components + background colour
    private static JLabel nameLabel;
    protected static JTextField nameField;
    protected static JButton billJCCbtn;
    //Message components
    private static JLabel msgLabel;
    protected static JTextField msgField;
    protected static JButton msgJCCbtn;
    //Picture components
    private static JLabel picLabel;
    protected static JTextField picField;
    protected static JButton infoJCCbtn;
    //Information components
    private static JLabel infoLabel;
    protected static JTextArea infoField;
    //Generate preview and Upload to server buttons
    protected static JButton genBtn;
    private static JButton uploadBtn;
    //Preview Panel
    protected static JPanel centerPanel;
    //Whether picture input is URL or not
    private static boolean isURL;
    //Picture related data
    protected static BufferedImage decodedImg;

    public static void PreviewerCard() {
        previewerFrame();
        PreviewerController.initListeners();
    }

    public static void previewerFrame() {
        //Initialise the previewer frame.
        previewer = new JFrame("Previewer");
        previewer.setPreferredSize(new Dimension(1280, 800));
        previewer.setResizable(false);
        previewer.getContentPane().setLayout(new BorderLayout());

        //Initialise South Panel and its components in the previewer using GridBagLayout.
        JPanel southPanel = new JPanel();
        previewer.getContentPane().add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Handle input data to generate a preview in the Center Panel.
        genBtn = colorBtn(southPanel, gbc, "Generate Preview", 1, 4);
        //Set everything below this to fill their grid horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Colour Buttons
        billJCCbtn = colorBtn(southPanel, gbc, "Background Colour", 2, 0);
        msgJCCbtn = colorBtn(southPanel, gbc, "Message Colour", 2, 1);
        infoJCCbtn = colorBtn(southPanel, gbc, "Information Colour", 2, 3);
        //Upload input data to server
        uploadBtn = colorBtn(southPanel, gbc, "Upload to Server", 2, 4);

        //billboard name
        billNameGrid(southPanel, gbc, 0, 0, 1, 0);
        //message
        msgGrid(southPanel, gbc, 0, 1, 1, 1);
        //picture
        picGrid(southPanel, gbc, 0, 2, 1, 2);
        //information
        infoGrid(southPanel, gbc, 0, 3, 1, 3);

        //Center Panel used to show the Preview
        centerPanel = new JPanel(new BorderLayout());
        previewer.add(centerPanel, BorderLayout.CENTER);
        //centerPanel.setBackground()

        previewer.pack();
        previewer.setLocationRelativeTo(null);
        previewer.setVisible(true);
    }

    private static void billNameGrid(JPanel component, GridBagConstraints c, int labelposx, int labelposy, int fieldposx, int fieldposy) {
        nameLabel = new JLabel("Billboard Name: ");
        nameField = new JTextField();
        c.weightx = 0.0;
        c.gridx = labelposx;
        c.gridy = labelposy;
        component.add(nameLabel, c);
        c.weightx = 1.0;
        c.gridx = fieldposx;
        c.gridy = fieldposy;
        component.add(nameField, c);
    }

    private static void msgGrid(JPanel component, GridBagConstraints c, int labelposx, int labelposy, int fieldposx, int fieldposy) {
        msgLabel = new JLabel("Message: ");
        msgField = new JTextField();
        c.weightx = 0.0;
        c.gridx = labelposx;
        c.gridy = labelposy;
        component.add(msgLabel, c);
        c.weightx = 1.0;
        c.gridx = fieldposx;
        c.gridy = fieldposy;
        component.add(msgField, c);
    }

    private static JButton colorBtn(JPanel component, GridBagConstraints c, String btnName, int CCposx, int CCposy) {
        JButton btn = createButton(btnName);
        c.gridx = CCposx;
        c.gridy = CCposy;
        component.add(btn, c);
        return btn;
    }

    private static void picGrid(JPanel component, GridBagConstraints c, int labelposx, int labelposy, int fieldposx, int fieldposy) {
        picLabel = new JLabel("Picture URL or data: ");
        picField = new JTextField();
        c.weightx = 0.0;
        c.gridx = labelposx;
        c.gridy = labelposy;
        component.add(picLabel, c);
        c.weightx = 1.0;
        c.gridx = fieldposx;
        c.gridy = fieldposy;
        component.add(picField, c);
    }

    private static void infoGrid(JPanel component, GridBagConstraints c, int labelposx, int labelposy, int fieldposx, int fieldposy) {
        infoLabel = new JLabel("Information: ");
        infoField = new JTextArea();
        c.weightx = 0.0;
        c.gridx = labelposx;
        c.gridy = labelposy;
        component.add(infoLabel, c);
        c.weightx = 1.0;
        c.gridx = fieldposx;
        c.gridy = fieldposy;
        component.add(infoField, c);
    }

    public static void setMessageFont(JLabel label, JPanel component)
    {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = component.getWidth();


        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = component.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
        System.out.println(label.getFontMetrics(labelFont).stringWidth(labelText));
    }

    public static int[] getImgDimensions(int height, int width, double scale, JPanel panel)
    {
        int newHeight;
        int newWidth;

        if(height >= width)
        {
            double ratio = (double)width/(double)height;
            newHeight = (int) ((double) panel.getHeight() * scale);
            newWidth = (int) ((double) newHeight * ratio);
        }
        else
        {
            double ratio = (double)height/(double)width;
            newWidth = (int) ((double) panel.getWidth() * scale);
            newHeight = (int) ((double) newWidth * ratio);
        }
        return (new int[]{newHeight, newWidth});
    }

    protected static JLabel drawMsg(String position, double scale){
        JPanel msgPanel = new JPanel();
        messageLabel = new JLabel(xml_data[1][1]);
        messageLabel.setFont(MsgFont);
        setMessageFont(messageLabel, centerPanel);
        msgPanel.add(messageLabel);
        messageLabel.setForeground(Color.decode(xml_data[1][2]));
        msgPanel.setBackground(Color.decode(xml_data[0][1]));
        msgPanel.setLayout(new GridBagLayout());
        msgPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int)((double)centerPanel.getBounds().height *scale)));
        centerPanel.add(msgPanel, position);
        return messageLabel;
    }

    protected static void drawPic(String position, double imageScale, double scale) throws NullPointerException{
        JPanel picPanel = new JPanel();
        JLabel picLabel = new JLabel();
        picPanel.setLayout(new GridBagLayout());
        picPanel.setBackground(Color.decode(xml_data[0][1]));
        //Check xml picture data, to see if it's a URL.
        try {
            URL xmlURL = new URL(xml_data[2][1]);
            xmlURL.toURI();
            isURL = true;
        } catch (MalformedURLException | URISyntaxException err) {
            isURL = false;
            //err.printStackTrace();
        }
        try {
            if (!isURL) {
                decodedImg = Base64Handler(previewer, xml_data);
                int[] imgDimensions = getImgDimensions(decodedImg.getHeight(), decodedImg.getWidth(), imageScale, centerPanel);
                Image newImage = decodedImg.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
                picLabel = new JLabel(new ImageIcon(newImage));
                picPanel.setBackground(Color.decode(xml_data[0][1]));
                picPanel.add(picLabel);
                picPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int) ((double) centerPanel.getBounds().height * scale)));
                centerPanel.add(picPanel, position);
            } else if (isURL) {
                BufferedImage URLImg = UrlHandler(previewer, xml_data);
                int[] imgDimensions = getImgDimensions(URLImg.getHeight(), URLImg.getWidth(), imageScale, centerPanel);
                Image newImage = URLImg.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
                picLabel = new JLabel(new ImageIcon(newImage));
                picPanel.setBackground(Color.decode(xml_data[0][1]));
                picPanel.add(picLabel);
                picPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int) ((double) centerPanel.getBounds().height * scale)));
                centerPanel.add(picPanel, position);
            }
        } catch(NullPointerException e){
            //Error caught, but do we need to do anything?
        }
    }

    //IMPORT THIS FROM BILLBOARD VIEWER'S HELPER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static JTextPane JMultilineLabel(String text, Font font, int Height, int Width){

        JTextPane textArea = new JTextPane();
        textArea.setText(text);
        textArea.setFont(font);
        //handle the scale
        textArea.setSize((int)((double)Width * 0.75), Integer.MAX_VALUE);
        textArea.setPreferredSize(new Dimension((int)((double)Width * 0.75),textArea.getPreferredSize().height));

        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(font);
//        textArea.setWrapStyleWord(true);
//        textArea.setLineWrap(true);

        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        return textArea;
    }
    //IMPORT THIS FROM BILLBOARD VIEWER'S HELPER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static void setInformationFont(JLabel message, JTextPane info) {
        try {
            Font messageFont = message.getFont();
            Font infoFont = info.getFont();
            int infoFontSize = 0;

            infoFontSize = messageFont.getSize() / 2;

            info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
            System.out.println("uh oh");
        } catch(NullPointerException e){
            //System.out.println("uh oh");
        }
    }

    protected static void drawInfo(String position, double scale){
        JTextPane infoLabel;
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoLabel = JMultilineLabel(xml_data[3][1], InfoFont, centerPanel.getBounds().height, centerPanel.getBounds().width);
        infoLabel.setForeground(Color.decode(xml_data[3][2]));
        infoPanel.setBackground((Color.decode(xml_data[0][1])));
        infoPanel.add(infoLabel);
        String message = xml_data[1][1];

        if(message.isEmpty() == false && message != null){
            setInformationFont(messageLabel, infoLabel);
        }
        infoPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int)((double)centerPanel.getBounds().height * scale)));
        centerPanel.add(infoPanel, position);
    }
}


