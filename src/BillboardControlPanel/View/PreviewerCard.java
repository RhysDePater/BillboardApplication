package BillboardControlPanel.View;
import BillboardControlPanel.Helper.ViewHelper;
import BillboardControlPanel.Model.DBInteract;

import javax.swing.*;
import java.awt.*;

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
