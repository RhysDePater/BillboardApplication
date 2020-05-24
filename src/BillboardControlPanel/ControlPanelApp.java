package BillboardControlPanel;

import BillboardControlPanel.Controller.MainController;
import javax.swing.*;


public class ControlPanelApp {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        SwingUtilities.invokeLater(mainController.getMainView());
    }
}
