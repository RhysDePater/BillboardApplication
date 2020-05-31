package BillboardViewer;

import BillboardViewer.View.mainView;

import javax.swing.*;

public class ViewerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new mainView("Viewer"));
    }
}
