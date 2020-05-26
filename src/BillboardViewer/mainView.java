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
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 2);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 20);

    public final String xmlText = "<billboard>\n" +
            "<message>\n" +
            "Billboard with message and picture with data attribute\n" +
            "</message>\n" +
            "<picture data=\"iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC\"/>\n" +
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
            helper.setMessageFont(messageLabel, this);
            messagePanel.add(messageLabel);
            this.getContentPane().add(messagePanel,BorderLayout.NORTH);
        }

        if(Picture != "") {
            JPanel picPanel = new JPanel();
            picPanel.setLayout(new GridBagLayout());
            URL url = new URL(Picture);
            BufferedImage myPicture = ImageIO.read(url);
            picLabel = new JLabel(new ImageIcon(myPicture));
            picPanel.add(picLabel);
            this.getContentPane().add(picPanel,BorderLayout.CENTER);
        }

        if(encodedPicture != "") {
            JPanel picPanel = new JPanel();
            picPanel.setLayout(new GridBagLayout());
            byte[] decodedPicture = Base64.getDecoder().decode(encodedPicture);
            BufferedImage myPicture = ImageIO.read(new ByteArrayInputStream(decodedPicture));
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
                helper.setInformationFont(messageLabel, infoLabel);
            }
            this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
        }
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
