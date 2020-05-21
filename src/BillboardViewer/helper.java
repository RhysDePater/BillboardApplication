package BillboardViewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class helper {
    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        return label;
    }

    public static JTextArea JMultilineLabel(String text, Font font, int Height, int Width){

        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setSize(Height,Width);
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(font);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

//        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
//        textArea.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        return textArea;
    }
}
