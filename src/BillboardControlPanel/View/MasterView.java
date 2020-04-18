package BillboardControlPanel.View;

import javax.swing.*;


public class MasterView {
    protected JFrame mainFrame;
    protected JPanel northCard;
    protected JPanel centerCard;
    protected JPanel southCard;
    protected JButton btnHome;

    public JFrame getMainFrame() {
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
