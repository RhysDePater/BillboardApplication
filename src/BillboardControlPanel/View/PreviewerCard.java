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

/**
 * Contains all GUI elements of the previewer using GridBagLayout.
 */
public class PreviewerCard {
    //Fonts for Message and Information
    private static final Font MsgFont = new Font("SansSerif", Font.BOLD, 3);
    private static final Font InfoFont = new Font("SansSerif", Font.BOLD, 30);
    //Billboard previewer (centerPanel components of the previewer)
    private static JLabel messageLabel = null;
    private static JLabel pictureLabel = null;
    private static JTextPane informationLabel = null;
    //The previewer container
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
    protected static JButton uploadBtn;
    //Preview Panel
    protected static JPanel centerPanel;
    //Whether picture input is URL or not
    protected static boolean isURL;
    //Picture related data
    protected static BufferedImage decodedImg;

    //Initate the previewer frame, listeners, and default colours.
    public static void PreviewerCard() {
        previewerFrame();
        PreviewerController.initDefaultCol();
        PreviewerController.initListeners();
    }

    /**
     * Initate the frame, and add the buttons to it, based on GridBagLayout and GridBagConstraints.
     */
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
        genBtn = colorUpBtn(southPanel, gbc, "Generate Preview", 1, 4);
        //Set everything below this to fill their grid horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Colour Buttons used to initiate the JColorChoosers for user input
        billJCCbtn = colorUpBtn(southPanel, gbc, "Background Colour", 2, 0);
        msgJCCbtn = colorUpBtn(southPanel, gbc, "Message Colour", 2, 1);
        infoJCCbtn = colorUpBtn(southPanel, gbc, "Information Colour", 2, 3);
        //Upload input data to server
        uploadBtn = colorUpBtn(southPanel, gbc, "Upload to Server", 2, 4);

        //DESIGNS THE SOUTH PANEL OF THE PREVIEWER
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

        //centerPanel.add(test);
        previewer.add(centerPanel, BorderLayout.CENTER);
        //centerPanel.setBackground(Color.decode(xml_data[0][1]));

        previewer.pack();
        previewer.setLocationRelativeTo(null);
        previewer.setVisible(true);
    }

    /**
     * The following Grid functions are used to style the southPanel of the previewer.
     * @param component JPanel southPanel, the main panel for the previewer.
     * @param c         Identify the GridBagConstraints for styling the label and field.
     * @param labelposx gridx position 0 for the name label
     * @param labelposy gridy position 0 for the name label
     * @param fieldposx gridx position 1 for the text field
     * @param fieldposy gridy position 0 for the text field
     */
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

    private static JButton colorUpBtn(JPanel component, GridBagConstraints c, String btnName, int CCposx, int CCposy) {
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

    /**
     * Changes the initial font of MESSAGE in order to fit it in the previewer.
     * @param label messageLabel, containing the text to render
     * @param component centerPanel, the previewer.
     */
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

        //This ensures that message strings shorter than 4 letters, especially p and q, fit the panel.
        String msg = xml_data[1][1];
        String pic = xml_data[2][1];
        String info = xml_data[3][1];

        //These statements ensure that message strings especially p and q, fit the panel.
        if (msg != null && pic == null && info == null && newFontSize >= 474){
            newFontSize = 473;
        }
        else if (msg != null && pic == null && info != null && newFontSize >= 269){
            newFontSize = 250;
        }
        if (msg != null && pic != null && info != null && newFontSize >= 200){
            newFontSize = 150;
        }

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
    }

    /**
     * This function finds the new required dimensions of the image and scaling it to the centerPanel.
     * @param height get the image height
     * @param width get the image width
     * @param scale rescale the image to fit
     * @param panel centerPanel previewer panel
     * @return the new dimensions to draw the image so that it fits
     */
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

    /**
     * Implements the MESSAGE components of the XML String to the previewer
     * @param position based on BorderLayout, either NORTH or CENTER depending on what components are provided.
     * @param scale The value to be scaled to inorder to fit the window.
     * @return
     */
    protected static JLabel drawMsg(String position, double scale){
        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new GridBagLayout());
        messageLabel = new JLabel(xml_data[1][1]);
        messageLabel.setFont(MsgFont);
        messageLabel.setForeground(Color.decode(xml_data[1][2]));
        msgPanel.setBackground(Color.decode(xml_data[0][1]));
        setMessageFont(messageLabel, centerPanel);
        msgPanel.add(messageLabel);
        msgPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int)((double)centerPanel.getBounds().height *scale)));
        centerPanel.add(msgPanel, position);
        return messageLabel;
    }

    /**
     * Implements the PICTURE component of the XML String to the previewer
     * @param position BORDERLAYOUT.CENTER
     * @param imageScale value for scalign the image to fit
     * @param scale value for changing the size of the label
     * @throws NullPointerException
     */
    protected static void drawPic(String position, double imageScale, double scale) throws NullPointerException{
        JPanel picPanel = new JPanel();
        pictureLabel = new JLabel();
        picPanel.setLayout(new GridBagLayout());
        picPanel.setBackground(Color.decode(xml_data[0][1]));
        //Check xml picture data, to see if it's a URL.
        try {
            URL xmlURL = new URL(xml_data[2][1]);
            xmlURL.toURI();
            isURL = true;
        } catch (MalformedURLException | URISyntaxException err) {
            isURL = false;
        }
        try {
            if (!isURL) {
                decodedImg = Base64Handler(previewer, xml_data);
                int[] imgDimensions = getImgDimensions(decodedImg.getHeight(), decodedImg.getWidth(), imageScale, centerPanel);
                Image newImage = decodedImg.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
                pictureLabel = new JLabel(new ImageIcon(newImage));
                picPanel.add(pictureLabel);
                picPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int) ((double) centerPanel.getBounds().height * scale)));
                centerPanel.add(picPanel, position);
            } else if (isURL) {
                BufferedImage URLImg = UrlHandler(previewer, xml_data);
                int[] imgDimensions = getImgDimensions(URLImg.getHeight(), URLImg.getWidth(), imageScale, centerPanel);
                Image newImage = URLImg.getScaledInstance(imgDimensions[1], imgDimensions[0], Image.SCALE_DEFAULT);
                pictureLabel = new JLabel(new ImageIcon(newImage));
                picPanel.add(pictureLabel);
                picPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int) ((double) centerPanel.getBounds().height * scale)));
                centerPanel.add(picPanel, position);
            }
        } catch(NullPointerException e){
            //Error caught, but do we need to do anything?
        }
    }

    /**
     * Function to rescale the JTextPane so that it fits in the previewer
     * @param text the INFORMATION string
     * @param font the font style for the INFORMATION
     * @param Height the height of the centerPanel (previewer)
     * @param Width the width of the centerPanel  (previewer)
     * @param scale value for resizing the information to display
     * @return
     */
    public static JTextPane JMultilineLabel(String text, Font font, int Height, int Width, double scale){

        JTextPane textArea = new JTextPane();
        textArea.setText(text);
        textArea.setFont(font);
        //handle the scale
        if (scale<=0.5)
        {
            textArea.setPreferredSize(new Dimension((int)((double)Width * 0.75),(int)((double)Height * scale)));
        }
        else {
            textArea.setPreferredSize(new Dimension((int) ((double) Width * 0.75), (int) ((double) Height * 0.50)));
        }

        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(font);

        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        return textArea;
    }


    //Adjusts the INFORMATION text size based on MESSAGE size so that information is always smaller than message.
    public static void setInformationFont(JLabel message, JTextPane info) {
        try {
            Font messageFont = message.getFont();
            Font infoFont = info.getFont();
            int infoFontSize = 0;

            infoFontSize = messageFont.getSize() / 2;

            info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
        } catch(NullPointerException e){

        }
    }

    /**
     * Set up the information components to render to the previewer screen
     * @param position BorderLayout.SOUTH, depending which components are available. If by itself it'll be CENTER.
     * @param scale value for scaling the information text panel
     */
    protected static void drawInfo(String position, double scale){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        informationLabel = JMultilineLabel(xml_data[3][1], InfoFont, centerPanel.getBounds().height, centerPanel.getBounds().width, scale);
        informationLabel.setForeground(Color.decode(xml_data[3][2]));
        infoPanel.setBackground((Color.decode(xml_data[0][1])));
        infoPanel.add(informationLabel);
        String message = xml_data[1][1];

        if(message.isEmpty() == false && message != null){
            setInformationFont(messageLabel, informationLabel);
        }
        infoPanel.setPreferredSize(new Dimension(centerPanel.getBounds().width, (int)((double)centerPanel.getBounds().height * scale)));
        centerPanel.add(infoPanel, position);
    }

    //GETS
    public static JPanel getCenterPanel(){ return centerPanel; }
    //Input fields
    public static JTextField getNameField(){ return nameField; }
    public static JTextField getMsgField(){ return msgField; }
    public static JTextField getPicField(){ return picField; }
    public static JTextArea getInfoField(){ return infoField; }

}