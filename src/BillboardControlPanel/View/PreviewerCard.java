package BillboardControlPanel.View;

import javax.swing.*;

public class PreviewerCard extends MasterView{
    public PreviewerCard(){
        createNorthCard();
        createCenterCard();
        createSouthCard();
    }

    private JPanel createNorthCard() {
        return northCard;
    }

    private JPanel createCenterCard() {
        return centerCard;
    }
    private JPanel createSouthCard() {
        return southCard;
    }
}
