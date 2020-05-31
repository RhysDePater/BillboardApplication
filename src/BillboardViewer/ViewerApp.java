package BillboardViewer;

import BillboardViewer.View.mainView;

import javax.swing.*;

public class ViewerApp {
    public static void main(String[] args) {
        //start the application
        SwingUtilities.invokeLater(new mainView("Viewer"));
    }
}
