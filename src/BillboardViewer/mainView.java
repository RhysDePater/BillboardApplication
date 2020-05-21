package BillboardViewer;

import BillboardViewer.helper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 2);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 20);

    public final String xmlText = "<billboard>\n" +
            "<message>Billboard with message, GIF and information</message>\n" +
            "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download\"/>\n" +
            "<information>\n" +
            "This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.\n" +
            "</information>\n" +
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


        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel messageLabel = null;
        JLabel picLabel = null;
        JTextArea infoLabel = null;

        if (Message != "") {
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new GridBagLayout());
            messageLabel = helper.createLabel(Message, MESSAGE_FONT);
            setMessageFont(messageLabel);
            messagePanel.add(messageLabel);
            this.getContentPane().add(messagePanel,BorderLayout.NORTH);
        }

        if(Picture != "") {
            JPanel picPanel = new JPanel();
            picPanel.setLayout(new GridBagLayout());
            URL url = new URL(elements[1]);
            BufferedImage myPicture = ImageIO.read(url);
            picLabel = new JLabel(new ImageIcon(myPicture));
            picPanel.add(picLabel);
            this.getContentPane().add(picPanel,BorderLayout.CENTER);
        }

        if (Info != "") {
            JPanel infoPanel = new JPanel();
            this.getBounds();
            infoLabel = helper.JMultilineLabel(elements[2], INFORMATION_FONT, this.getBounds().height, this.getBounds().width);
            infoPanel.add(infoLabel);
            if(Message!="")
            {
                setInformationFont(messageLabel, infoLabel);
            }
            this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
        }
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
        System.out.println(newFontSize);
        int componentHeight = this.getHeight();
        System.out.println(componentHeight);

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);
        System.out.println(fontSizeToUse);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, newFontSize));
    }

    public void setInformationFont(JLabel message, JTextArea info)
    {
        Font messageFont = message.getFont();
        Font infoFont = info.getFont();
        int infoFontSize = 0;

        infoFontSize = messageFont.getSize() / 2;

        info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
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
