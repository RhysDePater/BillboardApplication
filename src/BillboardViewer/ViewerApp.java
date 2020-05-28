package BillboardViewer;

import BillboardViewer.mainView;
import BillboardViewer.xmlParser;

import javax.swing.*;
import java.io.FileNotFoundException;

public class ViewerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new mainView("Viewer"));
    }
}
