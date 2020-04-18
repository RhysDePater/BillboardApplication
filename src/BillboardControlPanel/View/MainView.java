package BillboardControlPanel.View;

import javax.swing.*;
import java.awt.*;

public class MainView extends MasterView implements Runnable{

    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    public MainView(MasterView initialCard){
        mainFrame = new JFrame("Billboard Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.getContentPane().add(initialCard.getNorthCard(), BorderLayout.NORTH);
        mainFrame.getContentPane().add(initialCard.getCenterCard(), BorderLayout.CENTER);
        mainFrame.getContentPane().add(initialCard.getSouthCard(), BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    @Override
    public void run() {

    }
}
