package BillboardControlPanel.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Initiates mainFrame and renders the GUI to it
 */
public class MainView extends MasterView implements Runnable{

    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    public MainView(MasterView initialCard){
        mainFrame = new JFrame("Billboard Manager");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.getContentPane().add(initialCard.getNorthCard(), BorderLayout.NORTH);
        mainFrame.getContentPane().add(initialCard.getCenterCard(), BorderLayout.CENTER);
        mainFrame.getContentPane().add(initialCard.getSouthCard(), BorderLayout.SOUTH);

        //Exit confirmation
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int retVal = JOptionPane.showConfirmDialog(mainFrame, "Exit Program?",
                        "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
                if (retVal == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
            }
        });
        //Centers window startup position
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setVisible(true);
    }

    @Override
    public void run() {

    }
}
