package BillboardViewer;

import javax.swing.*;
import java.awt.*;

public class helper {
    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        return label;
    }
}
