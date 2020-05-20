package BillboardViewer;

import BillboardViewer.helper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 70);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final int MIN_FONT_SIZE=3;
    public static final int MAX_FONT_SIZE=240;
    Graphics g;

    public final String xmlText = "<billboard>\n" +
            "<message>Billboard with message, GIF and information this is more text for testing</message>\n" +
            "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download\"/>\n" +
            "<information>\n" +
            "This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.\n" +
            "</information>\n" +
            "</billboard>";

    public mainView(String title) throws HeadlessException {
        super(title);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {  // handler
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

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        JLabel messageLabel = helper.createLabel(elements[0], MESSAGE_FONT);
        setMessageFont(messageLabel);
        messagePanel.add(messageLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        JLabel infoLabel = helper.createLabel(elements[2], INFORMATION_FONT);
        infoPanel.add(infoLabel);

        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        URL url = new URL(elements[1]);
        BufferedImage myPicture = ImageIO.read(url);
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picPanel.add(picLabel);

        this.getContentPane().add(messagePanel,BorderLayout.NORTH);
        this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
        this.getContentPane().add(picPanel,BorderLayout.CENTER);
    }

    public void setMessageFont(JLabel label)
    {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        System.out.println(stringWidth);
        int componentWidth = this.getWidth();
        System.out.println(componentWidth);

// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;
        System.out.println(widthRatio);

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = this.getHeight();

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
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
