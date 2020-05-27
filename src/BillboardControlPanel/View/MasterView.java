package BillboardControlPanel.View;

import BillboardControlPanel.Controller.MainController;
import com.sun.tools.javac.Main;

import javax.swing.*;


public class MasterView {
    protected static JFrame mainFrame;
    protected JPanel northCard;
    protected JPanel centerCard;
    protected JPanel southCard;
    protected JButton btnHome;

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public JPanel getNorthCard() {
        return northCard;
    }

    public JPanel getCenterCard() {
        return centerCard;
    }

    public JPanel getSouthCard() {
        return southCard;
    }

    public JButton getBtnHome(){
        return btnHome;
    }

}
