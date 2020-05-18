package BillboardControlPanel;

import BillboardControlPanel.Controller.MainController;
import BillboardControlPanel.Model.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class app {
    public static void main(String[] args) throws SQLException {
        MainController mainController = new MainController();
        SwingUtilities.invokeLater(mainController.getMainView());
    }
}
