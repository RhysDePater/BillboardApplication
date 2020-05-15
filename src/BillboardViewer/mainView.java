package BillboardViewer;

import BillboardViewer.helper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class mainView extends JFrame implements Runnable{
    public static final Font MESSAGE_FONT = new Font("SansSerif", Font.BOLD, 40);
    public static final Font INFORMATION_FONT = new Font("SansSerif", Font.BOLD, 20);

    public mainView(String title) throws HeadlessException {
        super(title);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {  // handler
                if(ke.getKeyCode() == ke.VK_ESCAPE) {
                    System.out.println("escaped ?");
                    mainView.this.dispose();
                } else {
                    System.out.println("not escaped");
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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        JLabel messageLabel = helper.createLabel("This is the message section", MESSAGE_FONT);
        messagePanel.add(messageLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        JLabel infoLabel = helper.createLabel("This is the Information section", INFORMATION_FONT);
        infoPanel.add(infoLabel);

        JPanel picPanel = new JPanel();
        picPanel.setLayout(new GridBagLayout());
        BufferedImage myPicture = ImageIO.read(new File("src/BillboardViewer/sad.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picPanel.add(picLabel);

        this.getContentPane().add(messagePanel,BorderLayout.NORTH);
        this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
        this.getContentPane().add(picPanel,BorderLayout.CENTER);
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
