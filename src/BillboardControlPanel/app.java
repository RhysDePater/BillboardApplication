package BillboardControlPanel;

import BillboardControlPanel.Controller.MainController;
import BillboardControlPanel.Model.DBConnection;

import javax.swing.*;
import java.sql.Connection;

public class app {
    public static void main(String[] args){
        MainController mainController = new MainController();
        SwingUtilities.invokeLater(mainController.getMainView());
    }
}
